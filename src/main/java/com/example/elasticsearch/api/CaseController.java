package com.example.elasticsearch.api;

import com.example.elasticsearch.model.Case;
import com.example.elasticsearch.services.CaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/case")
@Slf4j
public class CaseController {

    private final CaseService caseService;

    public CaseController(CaseService caseService) {
        this.caseService = caseService;
    }

    @GetMapping("/search/{query}/{page}")
    public Page<Case> search(@PathVariable(value = "query") String query, @PathVariable(value = "page") int page) {
        Page<Case> cases = this.caseService.search(query, page);
        return cases;
    }

    @GetMapping("/search/suggest/{query}")
    public List<String> suggest(@PathVariable(value = "query") String query) {
        log.info("suggest for {}", query);
        return this.caseService.suggest(query);
    }

    @PutMapping("/bookmark/{case_id}/{advisor_id}")
    public void bookmark(@PathVariable(value = "case_id") String caseId, @PathVariable(value = "advisor_id") String advisorId) {
        // an dieser Stelle würde die Nutzter ID mitkommen. Dann könnte der
        // entsprechende Sachbearbeiter zum Bookmark gespeichert werden. Da
        // wir hier ohne Security arbeiten, wird sie über die URL übertragen.

    }

}
