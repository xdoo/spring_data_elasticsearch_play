package com.example.elasticsearch.services;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CaseServiceTest {

    @Test
    public void testCreateWildcardQuery() {
        CaseService caseService = new CaseService(null, null);
        String query = caseService.createWildcardQuery("Max Alt");
        assertThat(query, is(equalTo("Max* Alt* ")));
    }
}
