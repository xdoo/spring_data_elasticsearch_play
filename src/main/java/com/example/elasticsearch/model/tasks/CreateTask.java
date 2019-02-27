package com.example.elasticsearch.model.tasks;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class CreateTask extends Task {
    @Field( type = FieldType.Text)
    private String comment;
}
