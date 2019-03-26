package com.example.elasticsearch.api;

import com.example.elasticsearch.services.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = {"http://localhost:8080"})
@RequestMapping("/session")
@Slf4j
public class SessionController {

    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/elasticsearch")
    public ResponseEntity getElasticSearchSession() {
        log.info("requesting session info for elastic search");
        return ResponseEntity.ok().body(this.sessionService.getElasticSearchSession());
    }

    @GetMapping("/ping")
    public ResponseEntity ping() {
        log.info("ping received...");
        return ResponseEntity.ok(true);
    }

}
