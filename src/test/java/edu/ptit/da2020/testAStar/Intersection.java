package edu.ptit.da2020.testAStar;

import edu.ptit.da2020.model.graph.GraphNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class Intersection implements GraphNode {
    private String id;
}
