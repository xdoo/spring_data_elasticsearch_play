package com.example.elasticsearch.services;

import com.example.elasticsearch.model.Case;
import com.example.elasticsearch.repositories.CaseRepository;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

@Service
public class CaseService {

    private final CaseRepository caseRepository;

    public CaseService(CaseRepository caseRepository) {
        this.caseRepository = caseRepository;
    }

    public Case saveSomething(String id, String name) {
        Case aCase = new Case();
        aCase.setId(id);
        aCase.setTitle(name);
        return this.caseRepository.save(aCase);
    }

    public Case addGeoPoint(GeoPoint geoPoint, Case aCase) {
        return this.caseRepository.save(aCase);
    }

    public Case saveOne(String name) {
        Case aCase = new Case();
        aCase.setTitle(name);
        Case save = this.caseRepository.save(aCase);
        // hack um die id zu speichern
        return this.caseRepository.save(save);
    }

    public Case updateIt(Case c) {
        return this.caseRepository.save(c);
    }

}
