package com.example.elasticsearch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com/example/elasticsearch/repositories")
@Slf4j
public class ElasticSearchConfig {

}
