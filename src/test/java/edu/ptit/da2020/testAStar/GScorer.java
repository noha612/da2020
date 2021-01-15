package edu.ptit.da2020.testAStar;

import edu.ptit.da2020.model.graph.GraphNode;
import edu.ptit.da2020.pathfinding.scorer.Scorer;
import java.util.HashMap;
import java.util.Map;

public class GScorer implements Scorer {

  Map<String, Integer> g = new HashMap<String, Integer>() {{
    put("A_H", 15);
    put("A_B", 11);
    put("B_C", 9);
    put("C_D", 1);
    put("D_E", 2);
    put("E_F", 1);
    put("A_H", 15);
    put("H_I", 7);
    put("I_J", 29);
    put("J_K", 7);
    put("K_F", 5);
    put("H_G", 3);
    put("G_F", 16);
    put("G_K", 7);
  }};

  @Override
  public double computeCost(GraphNode from, GraphNode to) {
    return g.get(from.getId() + "_" + to.getId()) != null ? g.get(from.getId() + "_" + to.getId())
        : g.get(to.getId() + "_" + from.getId());
  }
}
