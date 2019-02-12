package com.example.elasticsearch.repositories;

import com.example.elasticsearch.model.Citizen;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CitizenRepository extends ElasticsearchRepository<Citizen, String> {
}
