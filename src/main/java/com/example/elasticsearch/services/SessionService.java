package com.example.elasticsearch.services;

import com.example.elasticsearch.dtos.ElasticsearchSessionDto;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SessionService {

    @Value(value = "${elasticsearch.password}")
    private String password;

    @Value(value = "${elasticsearch.user}")
    private String user;

    @Value(value = "${elasticsearch.host}")
    private String host;

    @Value(value = "${elasticsearch.port}")
    private int port;

    public ElasticsearchSessionDto getElasticSearchSession() {
        if(Strings.isNullOrEmpty(this.password) || Strings.isNullOrEmpty(this.user)) {
            return  new ElasticsearchSessionDto(this.host+":"+this.port);
        } else {
            return  new ElasticsearchSessionDto(this.user+":"+this.password+"@"+this.host+":"+this.port);
        }
    }

}
