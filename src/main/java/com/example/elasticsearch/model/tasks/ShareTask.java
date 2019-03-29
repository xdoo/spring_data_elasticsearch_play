package com.example.elasticsearch.model.tasks;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class ShareTask extends Task {
    @Field( type = FieldType.Text)
    String message;
}
