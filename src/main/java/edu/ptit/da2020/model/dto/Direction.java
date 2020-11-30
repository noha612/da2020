package edu.ptit.da2020.model.dto;

import edu.ptit.da2020.model.GeoPoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Direction {
    private GeoPoint from;
    private GeoPoint to;
    private List<GeoPoint> route;
}
