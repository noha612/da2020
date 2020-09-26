package edu.ptit.da2020.model;

import edu.ptit.da2020.model.entity.Intersection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Location {
    private String name;
    private Intersection intersection;
}
