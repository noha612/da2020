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
        String roadId2 = to.getId() + "_" + from.getId();
        int trafficLevel = 1;
        if (dataLoader.getListCongestions().containsKey(roadId)) {
            trafficLevel = dataLoader.getListCongestions().get(roadId);
        }
        if (dataLoader.getListCongestions().containsKey(roadId2)) {
            trafficLevel = dataLoader.getListCongestions().get(roadId2);
        }
        if(trafficLevel != 1){
            log.info("oho");
        }
        double spd;
        switch (trafficLevel) {
            case 1:
                spd = appConfig.getCongestionToSpeed().get(BaseConstant.SPEED_NORMAL);
                spd = 27.69;
                break;
            case 2:
                spd = appConfig.getCongestionToSpeed().get(BaseConstant.SPEED_SLOW);
                spd = 17.31;
                break;
            case 3:
                spd = appConfig.getCongestionToSpeed().get(BaseConstant.SPEED_JAM);
                spd = 10.38;
                break;
            case 4:
                spd = appConfig.getCongestionToSpeed().get(BaseConstant.SPEED_JAM);
                spd = 3.46;
                break;
            default:
                spd = appConfig.getCongestionToSpeed().get(BaseConstant.SPEED_NORMAL);
                spd = 27.69;
                break;
        }
        return MathUtil.haversineFomular(from.getLat(), from.getLng(), to.getLat(), to.getLng()) / spd;
//        return MathUtil.haversineFomular(from.getLat(), from.getLng(), to.getLat(), to.getLng());
    }
}
