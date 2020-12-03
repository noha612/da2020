package edu.ptit.da2020.util.algorithm;

import edu.ptit.da2020.constant.Constants;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.util.MathUtil;

public class EstTimeScorer implements Scorer<Junction> {

    @Override
    public double computeCost(Junction from, Junction to) {
        double spd = Constants.TRAFFIC_TO_SPD.get(1);
        return MathUtil.haversineFomular(from.getLat(), from.getLng(), to.getLat(), to.getLng()) / spd;
    }
}
