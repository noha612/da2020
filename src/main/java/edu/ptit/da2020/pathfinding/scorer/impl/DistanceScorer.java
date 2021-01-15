package edu.ptit.da2020.pathfinding.scorer.impl;

import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.pathfinding.scorer.Scorer;
import edu.ptit.da2020.util.CommonUtil;

public class DistanceScorer implements Scorer<Junction> {

  @Override
  public double computeCost(Junction from, Junction to) {
    return CommonUtil.haversineFormula(from.getLat(), from.getLng(), to.getLat(), to.getLng());
  }
}
