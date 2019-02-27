package com.example.elasticsearch.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
public class PhoneTask extends Task {
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time_no_millis)
    private Date start;
    @Field(type = FieldType.Date, format = DateFormat.basic_date_time_no_millis)
    private Date end;
    @Field( type = FieldType.Text)
    private String comment;
}
