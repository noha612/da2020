package edu.ptit.da2020.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Place {
    private String name;
    private String id;
    private double latitude;
    private double longitude;
}
