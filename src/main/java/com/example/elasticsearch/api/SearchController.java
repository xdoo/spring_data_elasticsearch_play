package com.example.elasticsearch.api;

import com.example.elasticsearch.dtos.ComplexSuggestDto;
import com.example.elasticsearch.model.Case;
import com.example.elasticsearch.services.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/search")
@Slf4j
public class SearchController {

    private final SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping("/{query}/{page}/{my_case_filter}")
    public Page<Case> search(
            @PathVariable(value = "query") String query, 
            @PathVariable(value = "page") int page,
            @PathVariable(value = "my_case_filter") String myCaseFilter) {
        
        // Filter Attribute aufarbeiten
        HashMap<String, Object> filters = new HashMap<>();
        filters.put(SearchService.MY_CASE_FILTER, myCaseFilter);

        Page<Case> cases = this.searchService.search(query, page, filters);
        return cases;
    }

    @GetMapping("/suggestionterm/{query}/{advisor_id}/{page}")
    public Page<Case> searchWithSuggestionTerm(
            @PathVariable(value = "query") String query,
            @PathVariable(value = "advisor_id") String advisorId,
            @PathVariable(value = "page") int page) {
        return this.searchService.searchWithSuggestionTerm(query, advisorId, page);
    }

    @GetMapping("/simplesuggest/{query}")
    public List<String> suggest(@PathVariable(value = "query") String query) {
        log.info("suggest for {}", query);
        return this.searchService.suggest(query,5);
    }

    @GetMapping("/complexsuggest/{query}/{advisor_id}")
    public List<ComplexSuggestDto> complexSuggest(@PathVariable(value = "query") String query, @PathVariable(value = "advisor_id") String advisorId) {
        log.info("complex suggest for {}", query);
        return this.searchService.complexSuggest(query, advisorId);
    }
}
