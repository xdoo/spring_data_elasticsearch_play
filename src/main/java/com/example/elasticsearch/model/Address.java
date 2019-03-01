package com.example.elasticsearch.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Data
public class Address {

    @GeoPointField
    GeoPoint location;
    @Field(type = FieldType.Text)
    String street;
    @Field(type = FieldType.Keyword)
    String streetNumber;
    @Field(type = FieldType.Text)
    String sublocality;
    @Field(type = FieldType.Text)
    String city;
    @Field(type = FieldType.Text)
    String country;
    @Field(type = FieldType.Keyword)
    String postalCode;
}
