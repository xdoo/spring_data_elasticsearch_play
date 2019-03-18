package com.example.elasticsearch.services;

import com.example.elasticsearch.model.Case;
import com.example.elasticsearch.repositories.CaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
@Slf4j
public class CaseService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final CaseRepository caseRepository;

    public CaseService(ElasticsearchOperations elasticsearchOperations, CaseRepository caseRepository) {
        this.elasticsearchOperations = elasticsearchOperations;
        this.caseRepository = caseRepository;
    }

    public Page<Case> search(String query, int page) {
        log.info("start query");
        String q = this.createWildcardQuery(query);

        QueryStringQueryBuilder queryStringQueryBuilder = new QueryStringQueryBuilder(q);
        queryStringQueryBuilder.field("owner.firstname", 2);
        queryStringQueryBuilder.field("owner.lastname", 3);
        queryStringQueryBuilder.field("address.street", 2);
        queryStringQueryBuilder.field("address.postalcode");
        queryStringQueryBuilder.field("address.sublocality");
        queryStringQueryBuilder.field("advisor.shorthandSymbol");

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("cases")
                .withQuery(queryStringQueryBuilder)
                .withPageable(PageRequest.of(page, 15))
                .build();

        Page<Case> cases = this.caseRepository.search(searchQuery);
        log.info("end query");
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
