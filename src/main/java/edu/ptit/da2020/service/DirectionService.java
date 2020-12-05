package edu.ptit.da2020.service;

import edu.ptit.da2020.config.DataLoader;
import edu.ptit.da2020.config.MapBuilder;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.pathfinding.RouteFinder;
import edu.ptit.da2020.pathfinding.scorer.EstTimeScorer;
import edu.ptit.da2020.pathfinding.scorer.TimeScorer;
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

    public List<Junction> findRoute(String startId, String finishId) {
        mapBuilder.setRouteFinder(new RouteFinder<>(mapBuilder.getGraph(), timeScorer, estTimeScorer));
        return mapBuilder.getRouteFinder().findRoute(
                mapBuilder.getGraph().getNode(startId),
                mapBuilder.getGraph().getNode(finishId)
        );
    }
}
