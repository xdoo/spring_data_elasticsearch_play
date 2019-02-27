package com.example.elasticsearch.model.tasks;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
public class PhoneTask extends Task {
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time_no_millis)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZZ")
    private Date start;
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time_no_millis)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZZ")
    private Date end;
    @Field( type = FieldType.Text)
    private String comment;
    @Field( type = FieldType.Text)
    private String number;
}
