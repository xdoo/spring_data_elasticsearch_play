package com.example.elasticsearch.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LetterTask.class, name = "letter"),
        @JsonSubTypes.Type(value = VisitTask.class, name = "visit"),
        @JsonSubTypes.Type(value = CreateTask.class, name = "create")
})
public abstract class Task {
    @Field( type = FieldType.Date, format = DateFormat.date_time_no_millis)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssZZ")
    Date created;
    @Field( type = FieldType.Keyword)
    String advisorId;
    @Field( type = FieldType.Keyword)
    String advisorShorthandSymbol;
}
