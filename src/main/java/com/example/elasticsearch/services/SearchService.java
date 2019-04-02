package com.example.elasticsearch.services;

import com.example.elasticsearch.dtos.ComplexSuggestDto;
import com.example.elasticsearch.model.Case;
import com.example.elasticsearch.model.Search;
import com.example.elasticsearch.repositories.AdvisorRepository;
import com.example.elasticsearch.repositories.CaseRepository;
import com.example.elasticsearch.repositories.SearchRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import static org.elasticsearch.index.query.QueryBuilders.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class SearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final CaseRepository caseRepository;
    private final AdvisorRepository advisorRepository;
    private final SearchRepository searchRepository;

    public final static String CASE_SUGGEST = "case-suggest";

    public SearchService(ElasticsearchOperations elasticsearchOperations, CaseRepository caseRepository, AdvisorRepository advisorRepository, SearchRepository searchRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.caseRepository = caseRepository;
        this.advisorRepository = advisorRepository;
        this.searchRepository = searchRepository;
    }

    /**
     * pageable wildcard search
     *
     * @param query
     * @param page
     * @return
     */
    public Page<Case> search(String query, int page) {
        String q = this.createWildcardQuery(query);
        log.info("start query '{}'", q);

        QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(q);
        queryStringQueryBuilder.field("owner.firstname", 2);
        queryStringQueryBuilder.field("owner.lastname", 3);
        queryStringQueryBuilder.field("address.street", 2);
        queryStringQueryBuilder.field("address.postalcode");
        queryStringQueryBuilder.field("address.sublocality");
        queryStringQueryBuilder.field("advisor.shorthandSymbol");
        queryStringQueryBuilder.defaultOperator(Operator.AND);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("cases")
                .withQuery(queryStringQueryBuilder)
                .withPageable(PageRequest.of(page, 10))
                .build();

        Page<Case> cases = this.caseRepository.search(searchQuery);
        log.info("found {}", cases.getTotalElements());
        return cases;
    }

    /**
     * Sucht mit dem vorgeschlagenen Wörtern. Diese sind so 1:1 im Index gespeichert.
     * Deshalb wird hier eine Term Query auf der Suggestion ausgeführt.
     *
     * @param query
     * @param page
     * @return
     */
    public Page<Case> searchWithSuggestionTerm(String query, String advisorId, int page) {

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("cases")
                .withQuery(termQuery("suggestionTerms", query))
                .withPageable(PageRequest.of(page, 10))
                .build();

        Page<Case> cases = this.caseRepository.search(searchQuery);

        // Suchanfrage speichern
        this.saveSearch(query, advisorId);

        return cases;
    }

    /**
     * speichert eine Suchanfrage
     *
     * @param query
     * @param advisorId
     */
    public void saveSearch(String query, String advisorId) {
        Search search = null;
        if(this.searchRepository.existsById(query)) {
            search = this.searchRepository.findById(query).get();
            search.setSearches(search.getSearches() + 1);
        } else {
            search = new Search();
            search.setId(query);
            search.setQuery(query);
            search.setAdvisorId(advisorId);
            search.setSearches(1);
        }
        this.searchRepository.save(search);
    }

    /**
     * suggestions for case index
     *
     * @param query
     * @return
     */
    public List<String> suggest(String query, int size) {
        CompletionSuggestionBuilder suggest = SuggestBuilders.completionSuggestion("suggest").prefix(query, Fuzziness.ONE).skipDuplicates(true).size(size);
        ElasticsearchRestTemplate template = (ElasticsearchRestTemplate)this.elasticsearchOperations;
        SearchResponse searchResponse = template.suggest(new SuggestBuilder().addSuggestion(CASE_SUGGEST, suggest), Case.class);

        // extract text
        List<? extends Suggest.Suggestion.Entry.Option> options = searchResponse.getSuggest().getSuggestion(CASE_SUGGEST).getEntries().get(0).getOptions();
        List<String> result = new ArrayList<>();
        options.forEach(o -> {
            result.add(o.getText().string());
        });

        return result;
    }

    /**
     * Erstellt eine Vorschlagsliste aus den Suggestions in Kombination mit den Bookmarks.
     *
     * @param query
     * @param advisorId
     * @return
     */
    public List<ComplexSuggestDto> complexSuggest(String query, String advisorId) {
        List<String> suggest = this.suggest(query, 4);
        Page<Case> bookmarks = this.searchBookmarkedCases(query, advisorId);
        Page<Search> searches = this.searchSearchPhrases(query, advisorId);

        List<ComplexSuggestDto> results = new ArrayList<>();

        // ganz vorne kommt die Suchanfrage selbst
        ComplexSuggestDto wildcardSuggestDto = new ComplexSuggestDto();
        wildcardSuggestDto.setSuggestion(query);
        wildcardSuggestDto.setType("wildcard");
        results.add(wildcardSuggestDto);

        // erst die suggests
        suggest.forEach(s -> {
            ComplexSuggestDto complexSuggestDto = new ComplexSuggestDto();
            complexSuggestDto.setSuggestion(s);
            complexSuggestDto.setType("search");
            results.add(complexSuggestDto);
        });

        // dann die Suchanfragen
        searches.forEach(s -> {
            ComplexSuggestDto complexSuggestDto = new ComplexSuggestDto();
            complexSuggestDto.setSuggestion(s.getQuery());
            complexSuggestDto.setType("timelapse");
            results.add(complexSuggestDto);
        });

        // zuletzt die Bookmarks
        if(!bookmarks.isEmpty()) {
            bookmarks.get().forEach(c -> {
                ComplexSuggestDto complexSuggestDto = new ComplexSuggestDto();
                complexSuggestDto.setId(c.getId());
                complexSuggestDto.setSuggestion(c.getOwner().getFirstname() + " " + c.getOwner().getLastname() + " " + c.getAddress().getStreet() + " " + c.getAddress().getSublocality());
                complexSuggestDto.setType("bookmark");
                results.add(complexSuggestDto);
            });
        }

        return results;
    }

    /**
     * Durchsucht die gestellten Suchanfragen eines Sachbearbeiters
     *
     * @param query
     * @param advisorId
     * @return
     */
    public Page<Search> searchSearchPhrases(String query, String advisorId) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("searches")
                .withQuery(matchPhrasePrefixQuery("query", query))
                .withFilter(termQuery("advisorId", advisorId))
                .withPageable(PageRequest.of(0, 2))
                .build();

        Page<Search> searches = this.searchRepository.search(searchQuery);
        return searches;
    }

    /**
     * Durchsucht die Bookmarks eines Sachbearbeiters.
     *
     * @param query
     * @param advisorId
     * @return
     */
    public Page<Case> searchBookmarkedCases(String query, String advisorId) {
        String q = this.createWildcardQuery(query);
        log.info("start query '{}'", q);

        QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(q);
        queryStringQueryBuilder.field("owner.firstname", 2);
        queryStringQueryBuilder.field("owner.lastname", 3);
        queryStringQueryBuilder.field("address.street", 2);
        queryStringQueryBuilder.field("address.postalcode");
        queryStringQueryBuilder.field("address.sublocality");
        queryStringQueryBuilder.field("advisor.shorthandSymbol");
        queryStringQueryBuilder.defaultOperator(Operator.AND);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("cases")
                .withQuery(queryStringQueryBuilder)
                .withFilter(termQuery("tasks.advisorId", advisorId))
                // Es ist wichtig 'bookmark' klein zu schreiben, da der Standard
                // Analyzer bei Text Feldern alle Wörte klein vergleicht.
                .withFilter(termQuery("tasks.type", "bookmark"))
                .withPageable(PageRequest.of(0, 2))
                .build();

        Page<Case> cases = this.caseRepository.search(searchQuery);
        log.info("found {}", cases.getTotalElements());
        return cases;
    }

    public String createWildcardQuery(String query) {
        StringBuilder queryBuilder = new StringBuilder();
        String[] words = query.split(" ");
        for(int i = 0; i < words.length; i++) {
            queryBuilder.append(words[i]).append("* ");
        }
        return queryBuilder.toString();
    }
}
