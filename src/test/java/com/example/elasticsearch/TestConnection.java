package com.example.elasticsearch;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.action.admin.indices.get.GetIndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.junit.Test;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.http.HttpHeaders;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class TestConnection {


    @Test
    public void testCreateTemplate () throws UnknownHostException {
        Settings elasticsearchSettings = Settings.builder()
                .put("client.transport.sniff", true)
                .build();
        TransportClient client = new PreBuiltTransportClient(elasticsearchSettings);
        client.threadPool().getThreadContext().putHeader(HttpHeaders.AUTHORIZATION, "Basic aDhhbWRkZ3NhbDp1dHoxa3V4dzk4");
        client.addTransportAddress(new TransportAddress(InetAddress.getByName("lhm-search-demo-4880040604.eu-west-1.bonsaisearch.net"), 443));

        ElasticsearchTemplate template = new ElasticsearchTemplate(client);
        boolean cases = template.indexExists("cases");
        log.info("da? " + cases);

    }

    @Test
    public void testCreateBasicClient () throws IOException {
        final CredentialsProvider credentialsProvider =
                new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("h8amddgsal", "utz1kuxw98"));

        RestClientBuilder builder = RestClient.builder(
                new HttpHost("lhm-search-demo-4880040604.eu-west-1.bonsaisearch.net", 9200))
                .setHttpClientConfigCallback(new RestClientBuilder.HttpClientConfigCallback() {

                    public HttpAsyncClientBuilder customizeHttpClient(
                            HttpAsyncClientBuilder httpClientBuilder) {
                        return httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider);
                    }
                });

        RestHighLevelClient client = new RestHighLevelClient(builder);

//        CreateIndexRequest createIndexRequest = new CreateIndexRequest("cases");
//        client.indices().create(createIndexRequest, RequestOptions.DEFAULT);

//        DeleteIndexRequest deleteIndexRequest = new DeleteIndexRequest("cases");
//        client.indices().delete(deleteIndexRequest, RequestOptions.DEFAULT);

        GetIndexRequest getIndexRequest = new GetIndexRequest();
//        getIndexRequest.indices("cases");
        getIndexRequest.indices("advisors");
        boolean exists = client.indices().exists(getIndexRequest, RequestOptions.DEFAULT);
        log.info("cases? " + exists);
    }

}
