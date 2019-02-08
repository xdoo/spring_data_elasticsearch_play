package com.example.elasticsearch.repositories;

import com.example.elasticsearch.model.Advisor;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AdvisorRepository extends ElasticsearchRepository<Advisor, String> {
}
