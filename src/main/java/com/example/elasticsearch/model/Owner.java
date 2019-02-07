package com.example.elasticsearch.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Document(indexName = "owners", type = "owner")
@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class Owner {

    @Id
    String id;
    @Field(type = FieldType.Text)
    @NonNull
    String firstname;
    @Field(type = FieldType.Text)
    @NonNull
    String lastname;
    @Field(type = FieldType.Object)
    Address address;

}
