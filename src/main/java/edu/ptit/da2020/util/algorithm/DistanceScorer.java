package edu.ptit.da2020.util.algorithm;

import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.util.MathUtil;

public class DistanceScorer implements Scorer<Junction> {
    @Override
    public double computeCost(Junction from, Junction to) {
        return MathUtil.haversineFomular(from.getLat(), from.getLng(), to.getLat(), to.getLng());
    }
}
