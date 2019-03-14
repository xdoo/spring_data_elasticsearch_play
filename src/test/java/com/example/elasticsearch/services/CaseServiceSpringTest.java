package com.example.elasticsearch.services;

import com.example.elasticsearch.model.Case;
import com.example.elasticsearch.repositories.CaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CaseServiceSpringTest {

    @Autowired
    CaseService caseService;

    @Autowired
    CaseRepository caseRepository;

    @Test
    public void testSearch() {
        Page<Case> cases = this.caseService.search("Akademie", 0);

        log.info("Anzahl Seite -> {}", cases.getTotalPages());
        log.info("Anzahl Fälle -> {}", cases.getTotalElements());

        cases.get().forEach(c -> {
            log.info("Fall: {}", c.toString());
        });
    }
}
