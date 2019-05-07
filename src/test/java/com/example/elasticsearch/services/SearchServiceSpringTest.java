package com.example.elasticsearch.services;

import com.example.elasticsearch.model.Case;
import com.example.elasticsearch.repositories.CaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "demo")
@Slf4j
public class SearchServiceSpringTest {

    @Autowired
    SearchService searchService;

    @Autowired
    CaseRepository caseRepository;

    @Test
    public void testSearch() {
        HashMap<String, Object> filters = new HashMap<>();
        Page<Case> cases = this.searchService.search("West", 0, filters);
        log.info("cases -> " + cases);
    }

    @Test
    public void testRemoteConnection() {
        long count = this.caseRepository.count();
        log.info("cases: {}", count);
    }
}
