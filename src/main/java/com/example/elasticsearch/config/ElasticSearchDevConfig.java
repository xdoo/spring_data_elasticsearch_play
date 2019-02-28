package com.example.elasticsearch.config;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Profile("dev")
@Configuration
@EnableElasticsearchRepositories(basePackages = "com/example/elasticsearch/repositories")
@Slf4j
public class ElasticSearchDevConfig {

    @Value(value = "${elasticsearch.password}")
    private String password;

    @Value(value = "${elasticsearch.user}")
    private String user;

    @Value(value = "${elasticsearch.url}")
    private String url;

    @Value(value = "${elasticsearch.port}")
    private int port;

    @Bean
    RestHighLevelClient client() {

        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(this.url+":"+this.port)
                .build();

        return RestClients.create(clientConfiguration).rest();
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate(RestHighLevelClient client) {
        return new ElasticsearchRestTemplate(client);
    }
}
