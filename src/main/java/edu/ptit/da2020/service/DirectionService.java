package edu.ptit.da2020.service;

import edu.ptit.da2020.init.DataInit;
import edu.ptit.da2020.init.MapGraphInit;
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
    DataInit dataInit;

    @Autowired
    MapGraphInit mapGraphInit;

    @Autowired
    TimeScorer timeScorer;

    @Autowired
    EstTimeScorer estTimeScorer;

    public List<Junction> findRoute(String startId, String finishId) {
        mapGraphInit.setRouteFinder(new RouteFinder<>(mapGraphInit.getGraph(), timeScorer, estTimeScorer));
        return mapGraphInit.getRouteFinder().findRoute(
                mapGraphInit.getGraph().getNode(startId),
                mapGraphInit.getGraph().getNode(finishId)
        );
    }
}
