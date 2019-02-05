package com.example.elasticsearch.config;

import com.google.maps.GeoApiContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GoogleGeocodingConfig {

    @Bean
    GeoApiContext createGecodingApi () {
        return new GeoApiContext.Builder().apiKey("AIzaSyBf-Xkij1WnB8m8Pv2KQoW1y19vboNBb6c").build();
    }
}
