package edu.ptit.da2020.model.dto;

import edu.ptit.da2020.model.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class Moving {
    protected String type;
    private List<Coordinate> route;

    public void setRoute(List<Coordinate> route) {
        this.route = route;
    }
}
