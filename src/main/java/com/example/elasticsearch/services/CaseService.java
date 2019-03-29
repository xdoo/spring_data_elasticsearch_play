package com.example.elasticsearch.services;

import ch.qos.logback.classic.Logger;
import com.example.elasticsearch.model.Advisor;
import com.example.elasticsearch.model.Case;
import com.example.elasticsearch.model.tasks.BookmarkTask;
import com.example.elasticsearch.model.tasks.Task;
import com.example.elasticsearch.repositories.AdvisorRepository;
import com.example.elasticsearch.repositories.CaseRepository;
import com.google.common.collect.Lists;
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

import static org.elasticsearch.index.query.QueryBuilders.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CaseService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final CaseRepository caseRepository;
    private final AdvisorRepository advisorRepository;

    public final static String CASE_SUGGEST = "case-suggest";

    public CaseService(ElasticsearchOperations elasticsearchOperations, CaseRepository caseRepository, AdvisorRepository advisorRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.caseRepository = caseRepository;
        this.advisorRepository = advisorRepository;
    }

    /**
     * Erstellt ein Bookmark für den Fall.
     *
     * @param caseId
     * @param advisorId
     */
    public void bookmark(String caseId, String advisorId) {
        Optional<Case> optionalCase = this.caseRepository.findById(caseId);
        Optional<Advisor> optionalAdvisor = this.advisorRepository.findById(advisorId);
        if(optionalCase.isPresent() && optionalAdvisor.isPresent()) {
            Case aCase = optionalCase.get();
            Advisor advisor = optionalAdvisor.get();

            BookmarkTask bookmarkTask = new BookmarkTask();
            bookmarkTask.setAdvisorId(advisorId);
            bookmarkTask.setAdvisorShorthandSymbol(advisor.getShorthandSymbol());
            bookmarkTask.setCreated(new Date());

            aCase.getTasks().add(bookmarkTask);
            this.caseRepository.save(aCase);
        } else {
            log.error("Cannot find case ({}) or advisor ({}).", caseId, advisorId);
        }
    }

    /**
     * Entfernt das Bookmark für den Fall
     *
     * @param caseId
     * @param advisorId
     */
    public void removeBookmark(String caseId, String advisorId) {
        Optional<Case> optionalCase = this.caseRepository.findById(caseId);
        if(optionalCase.isPresent()) {
            Case aCase = optionalCase.get();

            ArrayList<Task> tasks = aCase.getTasks();
            List<Task> bookmarks = tasks.stream()
                    .filter(c -> c instanceof BookmarkTask && c.getAdvisorId().equals(advisorId))
                    .collect(Collectors.toList());
            bookmarks.forEach(b -> tasks.remove(b));

            this.caseRepository.save(aCase);
        } else {
            log.error("Cannot find case ({}) or advisor ({}).", caseId, advisorId);
        }
    }

    /**
     *
     * @param advisorId
     * @param page
     * @return
     */
    public Page<Case> findBookmarkedCasesForAdvisor(String advisorId, int page) {

        String q = "Bookmark* " + advisorId.trim() + "*";
        log.info("query for {}", q);
        QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(q);
        queryStringQueryBuilder.field("tasks.type");
        queryStringQueryBuilder.field("tasks.advisorId");
        queryStringQueryBuilder.defaultOperator(Operator.AND);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("cases")
                .withQuery(queryStringQueryBuilder)
                .withPageable(PageRequest.of(page, 10))
                .build();

        Page<Case> page1 = this.caseRepository.search(searchQuery);
        log.info("results: {}", page1.getTotalElements());
        return page1;
    }

}
