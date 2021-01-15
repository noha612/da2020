package edu.ptit.da2020.model;

import edu.ptit.da2020.model.graph.GraphNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
@EqualsAndHashCode(callSuper = true)
public class Junction extends GeoPoint implements GraphNode {

  private String id;

  public Junction(String id, Double lat, Double lng) {
    super(lat, lng);
    this.id = id;
  }
}