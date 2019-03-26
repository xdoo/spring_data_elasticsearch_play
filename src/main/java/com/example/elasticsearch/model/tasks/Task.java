package com.example.elasticsearch.model.tasks;

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
        @JsonSubTypes.Type(value = LetterTask.class, name = "Brief"),
        @JsonSubTypes.Type(value = VisitTask.class, name = "Besuch"),
        @JsonSubTypes.Type(value = CreateTask.class, name = "erstellt"),
        @JsonSubTypes.Type(value = CloseTask.class, name = "geschlossen"),
        @JsonSubTypes.Type(value = NoteTask.class, name = "Notiz"),
        @JsonSubTypes.Type(value = OffenseTask.class, name = "Ordnungswidrigkeit"),
        @JsonSubTypes.Type(value = PhoneTask.class, name = "Telefonat"),
        @JsonSubTypes.Type(value = ResubmissionTask.class, name = "Wiedervorlage"),
        @JsonSubTypes.Type(value = BookmarkTask.class, name = "Bookmark")
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
