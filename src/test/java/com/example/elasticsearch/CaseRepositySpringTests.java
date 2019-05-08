package com.example.elasticsearch;


import com.example.elasticsearch.model.Case;
import com.example.elasticsearch.repositories.CaseRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class CaseRepositySpringTests {

    @Autowired
    private CaseRepository caseRepository;

    @Test
    public void testSomeRepoFunctions() {
        // Einträge zählen
        caseRepository.count();

        // speichern
        caseRepository.save(new Case());

        // laden mit id
        caseRepository.findById("xyz");

        // löschen mit id
        caseRepository.deleteById("xyz");

        // ...
    }

}
