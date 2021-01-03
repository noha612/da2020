package edu.ptit.da2020.service;

import edu.ptit.da2020.config.DataLoader;
import edu.ptit.da2020.config.MapBuilder;
import edu.ptit.da2020.model.GeoPoint;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.pathfinding.RouteFinder;
import edu.ptit.da2020.pathfinding.scorer.EstTimeScorer;
import edu.ptit.da2020.pathfinding.scorer.TimeScorer;
import edu.ptit.da2020.util.MathUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DirectionService {
    @Autowired
    DataLoader dataLoader;

    @Autowired
    MapBuilder mapBuilder;

    @Autowired
    TimeScorer timeScorer;

    @Autowired
    EstTimeScorer estTimeScorer;

    public List<Junction> findRoute(String fromId, String toId) {
        GeoPoint from = new GeoPoint(dataLoader.getListV().get(fromId)[0], dataLoader.getListV().get(fromId)[1]);
        GeoPoint to = new GeoPoint(dataLoader.getListV().get(toId)[0], dataLoader.getListV().get(toId)[1]);
        if (MathUtil.haversineFomular(from, to) > 3) estTimeScorer.setX(10);
        else estTimeScorer.setX(1);
        mapBuilder.setRouteFinder(new RouteFinder<>(mapBuilder.getGraph(), timeScorer, estTimeScorer));
        return mapBuilder.getRouteFinder().findRoute(
                mapBuilder.getGraph().getNode(fromId),
                mapBuilder.getGraph().getNode(toId)
        );
    }
}
