package com.example.elasticsearch.model;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.GeoPointField;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

@Data
public class Address {

    @GeoPointField
    GeoPoint location;
    String street;
    String streetNumber;
    String sublocality;
    String city;
    String country;
    String postalCode;
}
