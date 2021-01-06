package edu.ptit.da2020.model.dto;

import edu.ptit.da2020.model.GeoPoint;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.util.MathUtil;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Direction {
    private GeoPoint from;
    private GeoPoint to;
    private List<Junction> junctions;
    private Map<String, Integer> traffics;
    private double length; //km
    private double time; //h

    public double calLength() {
        double length = 0;
        for (int i = 0; i < junctions.size() - 1; i++) {
            GeoPoint from = new GeoPoint(junctions.get(i).getLat(), junctions.get(i).getLng());
            GeoPoint to = new GeoPoint(junctions.get(i + 1).getLat(),
                junctions.get(i + 1).getLng());
            length += MathUtil.haversineFomular(from, to);
        }
        return length;
    }

    public double calTime() {
        double time = 0;
        for (int i = 0; i < junctions.size() - 1; i++) {
            GeoPoint from = new GeoPoint(junctions.get(i).getLat(), junctions.get(i).getLng());
            GeoPoint to = new GeoPoint(junctions.get(i + 1).getLat(),
                junctions.get(i + 1).getLng());
            double v = 27.69;
            if (traffics.get(junctions.get(i).getId() + "_" + junctions.get(i + 1).getId())
                != null) {
                if (traffics.get(junctions.get(i).getId() + "_" + junctions.get(i + 1).getId())
                    == 2) {
                    v = 10.38;
                }
                if (traffics.get(junctions.get(i).getId() + "_" + junctions.get(i + 1).getId())
                    == 3) {
                    v = 3.46;
                }
            }
            time += MathUtil.haversineFomular(from, to) / v;
        }
        return time;
    }
}
