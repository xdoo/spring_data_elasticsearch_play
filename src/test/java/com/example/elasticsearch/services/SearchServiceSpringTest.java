package com.example.elasticsearch.services;

import com.example.elasticsearch.model.Case;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class SearchServiceSpringTest {

    @Autowired
    SearchService searchService;

    @Test
    public void testFilteredSearch() {
        HashMap<String, Object> filters = new HashMap<>();
        filters.put(SearchService.MY_CASE_FILTER, "XFYBYFAEGQEBGMIBT5HWEVXVYHTJHCQHPWR");
        Page<Case> cases = this.searchService.search("Pat", 0, filters);
        log.info("cases -> " + cases);
    }
}
