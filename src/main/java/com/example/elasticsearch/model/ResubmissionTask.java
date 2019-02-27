package com.example.elasticsearch.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
public class ResubmissionTask extends Task {
    @Field( type = FieldType.Date, format = DateFormat.date_time_no_millis)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZZ")
    private Date resubmissionDate;
    @Field( type = FieldType.Text )
    private String comment;
    @Field (type = FieldType.Integer)
    private int duration;
}
