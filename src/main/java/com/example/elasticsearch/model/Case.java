package com.example.elasticsearch.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.ArrayList;

@Document(indexName = "cases", type = "case")
@Data
public class Case {

    @Id
    String id;
    String title;
    String text;
    @Field(type = FieldType.Nested, includeInParent = true)
    ArrayList<Task> tasks = new ArrayList<>();
    @Field(type = FieldType.Nested, includeInParent = true)
    Address address;
    @Field(type = FieldType.Nested, includeInParent = true)
    Person owner;

}
