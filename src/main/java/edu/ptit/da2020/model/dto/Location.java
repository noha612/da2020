package edu.ptit.da2020.model.dto;

import edu.ptit.da2020.model.GeoPoint;
import edu.ptit.da2020.model.Place;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    GeoPoint marker;
    GeoPoint h;
    Place place;
}
