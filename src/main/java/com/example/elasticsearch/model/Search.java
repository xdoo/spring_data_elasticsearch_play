package com.example.elasticsearch.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "searches", type = "search")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Search {

    @Id
    String id;
    @Field( type = FieldType.Keyword)
    String advisorId;
    @Field( type = FieldType.Text)
    String query;
    @Field( type = FieldType.Integer)
    int searches;
}
