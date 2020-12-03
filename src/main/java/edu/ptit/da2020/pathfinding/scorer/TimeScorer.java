package edu.ptit.da2020.pathfinding.scorer;

import edu.ptit.da2020.config.AppConfig;
import edu.ptit.da2020.constant.Constants;
import edu.ptit.da2020.init.DataInit;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.util.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TimeScorer implements Scorer<Junction> {
    @Autowired
    DataInit dataInit;

    @Autowired
    AppConfig appConfig;

    @Override
    public double computeCost(Junction from, Junction to) {

        String roadId = from.getId() + "_" + to.getId();
        int trafficLevel = dataInit.getListCongestions().get(roadId);
        double spd;
        switch (trafficLevel) {
            case 1:
                spd = appConfig.getCongestionToSpeed().get(Constants.SPEED_NORMAL);
                break;
            case 2:
                spd = appConfig.getCongestionToSpeed().get(Constants.SPEED_SLOW);
                break;
            case 3:
                spd = appConfig.getCongestionToSpeed().get(Constants.SPEED_JAM);
                break;
            default:
                spd = appConfig.getCongestionToSpeed().get(Constants.SPEED_NORMAL);
                break;
        }
        return MathUtil.haversineFomular(from.getLat(), from.getLng(), to.getLat(), to.getLng()) / spd;
    }
}
