package com.example.elasticsearch.repositories;

import com.example.elasticsearch.model.Case;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CaseRepository extends ElasticsearchRepository<Case, String> {

}
