package com.example.elasticsearch.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.HttpHeaders;

@Configuration
//@Profile("bonsai")
public class ElasticSearchBonsaiConfig {

    @Bean
    RestHighLevelClient client() {

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBasicAuth("h8amddgsal", "utz1kuxw98");

        ClientConfiguration.builder()
                .connectedTo("lhm-search-demo-4880040604.eu-west-1.bonsaisearch.net:443")
                .usingSsl()
                .withDefaultHeaders(httpHeaders)
                .build();

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient
                .builder(new HttpHost("localhost", 9200)));
        return client;
    }

    @Bean
    ElasticsearchRestTemplate elasticsearchTemplate(RestHighLevelClient client) {
        return new ElasticsearchRestTemplate(client);
    }
}
