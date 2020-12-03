package edu.ptit.da2020.util.algorithm;

import edu.ptit.da2020.constant.Constants;
import edu.ptit.da2020.init.LoadFile;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.util.MathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TimeScorer implements Scorer<Junction> {
    @Autowired
    LoadFile loadFile;


    @Override
    public double computeCost(Junction from, Junction to) {
        String roadId = from.getId() + "_" + to.getId();
        int trafficLevel = loadFile.getListTraffic().get(roadId);
        double spd = Constants.TRAFFIC_TO_SPD.get(trafficLevel);
        return MathUtil.haversineFomular(from.getLat(), from.getLng(), to.getLat(), to.getLng()) / spd;
    }
}
