package com.example.elasticsearch.services;

import com.example.elasticsearch.model.Address;
import com.example.elasticsearch.model.Case;
import com.example.elasticsearch.repositories.CaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

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
        Case case01 = new Case();
        case01.setId("CASE000001");
        case01.setTitle("qwertitle");
        case01.setDescription("Some description...");
        Address address01 = new Address();
        address01.setStreet("Foostraße");
        address01.setSublocality("Moosach");
        case01.setAddress(address01);
        this.caseRepository.save(case01);

        Case case02 = new Case();
        case02.setId("CASE000002");
        case02.setTitle("yxcvtitle");
        case02.setDescription("Some description...");
        Address address02 = new Address();
        address02.setStreet("Fooweg");
        address02.setSublocality("Bogenhausen");
        case02.setAddress(address02);
        this.caseRepository.save(case02);

        Case case03 = new Case();
        case03.setId("CASE000003");
        case03.setTitle("poiutitle");
        case03.setDescription("Some description...");
        Address address03 = new Address();
        address03.setStreet("Barstraße");
        address03.setSublocality("Bogenhausen");
        case03.setAddress(address03);
        this.caseRepository.save(case03);


        Page<Case> cases01 = this.caseService.search("Fooweg", 0);

        assertThat(cases01.getTotalElements(), is(equalTo(1L)));

        log.info("Anzahl Seite -> {}", cases01.getTotalPages());
        log.info("Anzahl Fälle -> {}", cases01.getTotalElements());

        cases01.get().forEach(c -> {
            log.info("Fall Titel: {}", c.toString());
        });

        Optional<Case> case000002 = this.caseRepository.findById("CASE000002");
        log.info("case000002 -> {}", case000002.get().toString());
    }
}
