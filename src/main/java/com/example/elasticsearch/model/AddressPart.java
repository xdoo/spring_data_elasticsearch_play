package com.example.elasticsearch.model;

import lombok.Data;

import java.util.List;

@Data
public class AddressPart {
    private String longName;
    private String shortName;
    private List<String> types;
}
