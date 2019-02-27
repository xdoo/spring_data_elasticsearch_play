package com.example.elasticsearch.model;

import com.example.elasticsearch.model.tasks.Task;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.ArrayList;

@Document(indexName = "cases", type = "case")
@Data
public class Case {

    @Id
    String id;
    @Field(type = FieldType.Keyword)
    String state;
    @Field(type = FieldType.Text)
    String title;
    @Field(type = FieldType.Text)
    String description;
    @Field(type = FieldType.Nested, includeInParent = true)
    ArrayList<Task> tasks = new ArrayList<>();
    @Field(type = FieldType.Nested, includeInParent = true)
    Address address;
    @Field(type = FieldType.Nested, includeInParent = true)
    Citizen owner;
    @Field(type = FieldType.Nested, includeInParent = true)
    Advisor advisor;
}
