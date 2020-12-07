package edu.ptit.da2020.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Place extends GeoPoint {
    private String name;
    private String id;

    public Place(String name, String id, Double lat, Double lng) {
        super(lat, lng);
        this.name = name;
        this.id = id;
    }
}
