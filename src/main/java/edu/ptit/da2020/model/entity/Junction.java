package edu.ptit.da2020.model.entity;

import edu.ptit.da2020.model.graph.GraphNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Junction implements GraphNode {
    private String id;
    private double latitude;
    private double longitude;
}