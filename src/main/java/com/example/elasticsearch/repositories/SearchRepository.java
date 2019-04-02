package com.example.elasticsearch.repositories;


import com.example.elasticsearch.model.Search;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SearchRepository extends ElasticsearchRepository<Search, String> {
}
