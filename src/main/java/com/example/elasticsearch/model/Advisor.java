package com.example.elasticsearch.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "advisors", type = "advisor")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Advisor {

    @Id
    String id;
    @Field(type = FieldType.Text)
    @NonNull
    String firstname;
    @Field(type = FieldType.Text)
    @NonNull
    String lastname;
    @Field(type = FieldType.Keyword)
    @NonNull
    String shorthandSymbol;
//    @Field(type = FieldType.Keyword)
    List<String> referencedFrom;
}
