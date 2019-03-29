package com.example.elasticsearch.api;

import com.example.elasticsearch.dtos.AdvisorNameIdDto;
import com.example.elasticsearch.services.AdvisorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/advisor")
@Slf4j
public class AdvisorController {

    private final AdvisorService advisorService;

    public AdvisorController(AdvisorService advisorService) {
        this.advisorService = advisorService;
    }

    @GetMapping("/names")
    public List<AdvisorNameIdDto> getAdvisorNames() {
        log.info("loading advisor names and ids");
        return this.advisorService.getAdvisorNames();
    }
}
