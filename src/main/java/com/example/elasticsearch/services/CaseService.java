package com.example.elasticsearch.services;

import com.example.elasticsearch.model.Case;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.ElasticsearchClient;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.script.mustache.SearchTemplateRequestBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.elasticsearch.index.query.QueryBuilders.*;

@Service
@Slf4j
public class CaseService {

    private final ElasticsearchOperations elasticsearchOperations;

    public CaseService(ElasticsearchOperations elasticsearchOperations) {
        this.elasticsearchOperations = elasticsearchOperations;
    }

    public Page<Case> search(String query, int page) {
        log.info("start query");
        String q = this.createWildcardQuery(query);

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withIndices("cases")
//                .withFields("owner.firstname^2", "owner.lastname^3", "address.street^2", "address.postalCode", "address.sublocality", "advisor.shorthandSymbol^5")
                .withFields("address.street", "address.sublocality")
                .withQuery(queryStringQuery(q))
                .withPageable(PageRequest.of(page, 15))
                .build();

        Page<Case> cases = this.elasticsearchOperations.queryForPage(searchQuery, Case.class);
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
