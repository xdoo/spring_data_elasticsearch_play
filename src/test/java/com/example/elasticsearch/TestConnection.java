package com.example.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.transport.TransportAddress;
import org.junit.Test;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

@Slf4j
public class TestConnection {

    @Test
    public void createConnection () {
        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("http://h8amddgsal:utz1kuxw98@lhm-search-demo-4880040604.eu-west-1.bonsaisearch.net"))
        );
        log.info(client.toString());
        GetRequest getRequest = new GetRequest();

    }

    @Test
    public void testCreateTemplate () throws UnknownHostException {
        String url = "http://h8amddgsal:utz1kuxw98@lhm-search-demo-4880040604.eu-west-1.bonsaisearch.net";
        InetAddress address = InetAddress.getByAddress(url.getBytes());
        Settings elasticsearchSettings = Settings.builder()
                .put("client.transport.sniff", true)
                .build();
        TransportClient client = new PreBuiltTransportClient(elasticsearchSettings);
        client.addTransportAddress(new TransportAddress(address, 9300));

    }

}
