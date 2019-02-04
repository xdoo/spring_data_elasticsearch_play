package com.example.elasticsearch.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public abstract class Task {
    String name;
    LocalDate date;
}
