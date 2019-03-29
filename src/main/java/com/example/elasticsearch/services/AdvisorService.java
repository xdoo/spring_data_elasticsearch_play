package com.example.elasticsearch.services;

import com.example.elasticsearch.model.Advisor;
import com.example.elasticsearch.dtos.AdvisorNameIdDto;
import com.example.elasticsearch.repositories.AdvisorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AdvisorService {

    private final AdvisorRepository advisorRepository;

    public AdvisorService(AdvisorRepository advisorRepository) {
        this.advisorRepository = advisorRepository;
    }

    /**
     * Gibt eine Liste aller Sachbearbeiter Namen mit der
     * entsprechenden ID zurück.
     *
     * @return
     */
    public List<AdvisorNameIdDto> getAdvisorNames() {
        Iterable<Advisor> advisors = this.advisorRepository.findAll();
        List<AdvisorNameIdDto> names = new ArrayList<>();
        advisors.forEach(a -> {
            String name = a.getFirstname()+" "+a.getLastname()+" ("+a.getShorthandSymbol()+")";
            names.add(new AdvisorNameIdDto(
                    name,
                    a.getId()
            ));
        });
        return names;
    }
}
