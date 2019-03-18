package com.example.elasticsearch.api;

import com.example.elasticsearch.model.Case;
import com.example.elasticsearch.services.CaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

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

}
