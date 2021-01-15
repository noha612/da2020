package edu.ptit.da2020.testAStar;

import edu.ptit.da2020.model.graph.GraphNode;
import edu.ptit.da2020.pathfinding.scorer.Scorer;
import java.util.HashMap;
import java.util.Map;

public class HScorer implements Scorer {

  Map<String, Integer> h = new HashMap<String, Integer>() {{
    put("A", 60);
    put("B", 53);
    put("C", 36);
    put("D", 35);
    put("E", 35);
    put("F", 19);
    put("G", 16);
    put("H", 38);
    put("I", 23);
    put("J", 0);
    put("K", 7);
  }};

  @Override
  public double computeCost(GraphNode from, GraphNode to) {
    return h.get(from.getId());
  }
}
