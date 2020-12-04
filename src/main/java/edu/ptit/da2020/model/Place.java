package edu.ptit.da2020.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Place {
    private String name;
    private String id;
    private double latitude;
    private double longitude;
}