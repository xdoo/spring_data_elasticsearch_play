package com.example.elasticsearch.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.elasticsearch.search.DocValueFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = BarTask.class, name = "bar"),
        @JsonSubTypes.Type(value = FooTask.class, name = "foo"),
        @JsonSubTypes.Type(value = CreateTask.class, name = "create")
})
public abstract class Task {

    String name;
    String datum;
}
