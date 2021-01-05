package edu.ptit.da2020.pathfinding.scorer;

import edu.ptit.da2020.config.AppConfig;
import edu.ptit.da2020.constant.BaseConstant;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.util.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EstTimeScorer implements Scorer<Junction> {

    @Autowired
    AppConfig appConfig;

    private double x;

    public void setX(double x) {
        this.x = x;
    }

    @Override
    public double computeCost(Junction from, Junction to) {
        double spd = appConfig.getCongestionToSpeed().get(BaseConstant.SPEED_NORMAL);
        return MathUtil.haversineFomular(from.getLat(), from.getLng(), to.getLat(), to.getLng()) * x
            / spd;
//        return MathUtil.haversineFomular(from.getLat(), from.getLng(), to.getLat(), to.getLng())
//            * x;
    }
}
