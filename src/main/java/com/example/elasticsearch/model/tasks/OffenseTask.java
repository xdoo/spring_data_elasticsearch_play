package com.example.elasticsearch.model.tasks;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class OffenseTask extends Task {
    @Field(type = FieldType.Double)
    private Double amount;
    @Field(type = FieldType.Text)
    private String reason;
}
