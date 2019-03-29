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

    @PutMapping("/bookmark/{case_id}/{advisor_id}")
    public void bookmark(@PathVariable(value = "case_id") String caseId, @PathVariable(value = "advisor_id") String advisorId) {
        log.info("bookmarking case {} for advisor {}", caseId, advisorId);
        // an dieser Stelle würde die Nutzter ID mitkommen. Dann könnte der
        // entsprechende Sachbearbeiter zum Bookmark gespeichert werden. Da
        // wir hier ohne Security arbeiten, wird sie über die URL übertragen.
        this.caseService.bookmark(caseId, advisorId);
    }

    @DeleteMapping("/bookmark/{case_id}/{advisor_id}")
    public void removeBookmark(@PathVariable(value = "case_id") String caseId, @PathVariable(value = "advisor_id") String advisorId) {
        this.caseService.removeBookmark(caseId, advisorId);
    }

    @GetMapping("/bookmark/{advisor_id}/{page}")
    public Page<Case> getBookmarks(@PathVariable(value = "advisor_id") String advisorId, @PathVariable(value = "page") int page) {
        log.info("loading bookmarks for {}", advisorId);
        Page<Case> bookmarks = this.caseService.findBookmarkedCasesForAdvisor(advisorId, page);
        return bookmarks;
    }

}
