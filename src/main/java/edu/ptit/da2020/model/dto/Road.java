package edu.ptit.da2020.model.dto;

import edu.ptit.da2020.model.GeoPoint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Road implements Serializable {
    String id;
    GeoPoint gp1;
    GeoPoint gp2;
}
