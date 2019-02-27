package com.example.elasticsearch.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class CloseTask {
    @Field( type = FieldType.Text)
    private String comment;
}
