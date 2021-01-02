package edu.ptit.da2020.model.dto;

import edu.ptit.da2020.model.GeoPoint;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.util.MathUtil;
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
    private Map<String, Integer> traffics;

    public double length() {
        double length = 0;
        for (int i = 0; i < junctions.size() - 1; i++) {
            GeoPoint from = new GeoPoint(junctions.get(i).getLat(), junctions.get(i).getLng());
            GeoPoint to = new GeoPoint(junctions.get(i + 1).getLat(), junctions.get(i + 1).getLng());
            length += MathUtil.haversineFomular(from, to);
        }
        return length;
    }
}
