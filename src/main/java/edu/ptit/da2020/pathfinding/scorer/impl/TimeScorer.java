package edu.ptit.da2020.pathfinding.scorer.impl;

import edu.ptit.da2020.config.AppConfig;
import edu.ptit.da2020.config.DataLoader;
import edu.ptit.da2020.constant.BaseConstant;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.pathfinding.scorer.Scorer;
import edu.ptit.da2020.util.CommonUtil;
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
    String trafficLevel = BaseConstant.SPEED_VERY_SMOOTH;
    if (dataLoader.getListCongestions().containsKey(roadId)) {
      trafficLevel = dataLoader.getListCongestions().get(roadId);
    }
    if (dataLoader.getListCongestions().containsKey(roadId2)) {
      trafficLevel = dataLoader.getListCongestions().get(roadId2);
    }
    double spd;
    switch (trafficLevel) {
      case BaseConstant.SPEED_VERY_SMOOTH:
        spd = appConfig.getTrafficToSpeedMapping().get(BaseConstant.SPEED_VERY_SMOOTH);
        break;
      case BaseConstant.SPEED_SMOOTH:
        spd = appConfig.getTrafficToSpeedMapping().get(BaseConstant.SPEED_SMOOTH);
        break;
      case BaseConstant.SPEED_MILD:
        spd = appConfig.getTrafficToSpeedMapping().get(BaseConstant.SPEED_MILD);
        break;
      case BaseConstant.SPEED_HEAVY:
        spd = appConfig.getTrafficToSpeedMapping().get(BaseConstant.SPEED_HEAVY);
        break;
      default:
        spd = appConfig.getTrafficToSpeedMapping().get(BaseConstant.SPEED_VERY_SMOOTH);
        break;
    }
    return CommonUtil.haversineFormula(from.getLat(), from.getLng(), to.getLat(), to.getLng())
        / spd;
  }
}
