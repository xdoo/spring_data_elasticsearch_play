package com.example.elasticsearch.model.tasks;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
public class LetterTask extends Task {

    @Field( type = FieldType.Text)
    String urlToCopy;
    @Field( type = FieldType.Text)
    String comment;
}
