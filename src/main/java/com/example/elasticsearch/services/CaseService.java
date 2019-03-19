package com.example.elasticsearch.services;

import com.example.elasticsearch.model.Case;
import com.example.elasticsearch.repositories.CaseRepository;
import lombok.extern.slf4j.Slf4j;
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

import java.util.ArrayList;
import java.util.List;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
@Slf4j
public class CaseService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final CaseRepository caseRepository;

    public final static String CASE_SUGGEST = "case-suggest";

    public CaseService(ElasticsearchOperations elasticsearchOperations, CaseRepository caseRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.caseRepository = caseRepository;
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
        return cases;
    }

    /**
     * suggestions for case index
     *
     * @param query
     * @return
     */
    public List<String> suggest(String query) {
        CompletionSuggestionBuilder suggest = SuggestBuilders.completionSuggestion("suggest").prefix(query, Fuzziness.TWO).skipDuplicates(true);
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

    public String createWildcardQuery(String query) {
        StringBuilder queryBuilder = new StringBuilder();
        String[] words = query.split(" ");
        for(int i = 0; i < words.length; i++) {
            queryBuilder.append(words[i]).append("* ");
        }
        return queryBuilder.toString();
    }

}
