package com.example.elasticsearch.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplexSuggestDto {

    String id;
    String suggestion;
    String type;
}
