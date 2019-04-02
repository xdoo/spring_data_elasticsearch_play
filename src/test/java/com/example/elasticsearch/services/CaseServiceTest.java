package com.example.elasticsearch.services;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CaseServiceTest {

    @Test
    public void testCreateWildcardQuery() {
        SearchService searchService = new SearchService(null, null, null, null);
        String query = searchService.createWildcardQuery("Max Alt");
        assertThat(query, is(equalTo("Max* Alt* ")));
    }
}
