package edu.ptit.da2020.model.dto;

import edu.ptit.da2020.model.GeoPoint;
import edu.ptit.da2020.model.Junction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Direction {
    private GeoPoint from;
    private GeoPoint to;
    private List<Junction> junctions;
    private Map<String,Integer> traffics;
}
