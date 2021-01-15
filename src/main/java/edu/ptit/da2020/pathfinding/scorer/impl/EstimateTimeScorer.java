package edu.ptit.da2020.pathfinding.scorer.impl;

import edu.ptit.da2020.config.AppConfig;
import edu.ptit.da2020.constant.BaseConstant;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.pathfinding.scorer.Scorer;
import edu.ptit.da2020.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EstimateTimeScorer implements Scorer<Junction> {

    @Autowired
    AppConfig appConfig;

    private double x;

    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double computeCost(Junction from, Junction to) {
        double spd = appConfig.getTrafficToSpeedMapping().get(BaseConstant.SPEED_VERY_SMOOTH);
        return CommonUtil.haversineFormula(from.getLat(), from.getLng(), to.getLat(), to.getLng()) * x
            / spd;
    }
}
