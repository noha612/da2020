package edu.ptit.da2020.pathfinding.scorer;

import edu.ptit.da2020.config.AppConfig;
import edu.ptit.da2020.config.DataLoader;
import edu.ptit.da2020.constant.BaseConstant;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.util.MathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TimeScorer implements Scorer<Junction> {

    @Autowired
    DataLoader dataLoader;

    @Autowired
    AppConfig appConfig;

    @Override
    public double computeCost(Junction from, Junction to) {

        String roadId = from.getId() + "_" + to.getId();
        log.info(roadId);
        int trafficLevel = 1;
        if (dataLoader.getListCongestions().containsKey(roadId)) {
            trafficLevel = dataLoader.getListCongestions().get(roadId);
        }
        double spd;
        switch (trafficLevel) {
            case 1:
                spd = appConfig.getCongestionToSpeed().get(BaseConstant.SPEED_NORMAL);
                break;
            case 2:
                spd = appConfig.getCongestionToSpeed().get(BaseConstant.SPEED_SLOW);
                break;
            case 3:
                spd = appConfig.getCongestionToSpeed().get(BaseConstant.SPEED_JAM);
                break;
            default:
                spd = appConfig.getCongestionToSpeed().get(BaseConstant.SPEED_NORMAL);
                break;
        }
        return MathUtil.haversineFomular(from.getLat(), from.getLng(), to.getLat(), to.getLng()) / spd;
    }
}
