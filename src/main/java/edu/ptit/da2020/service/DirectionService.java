package edu.ptit.da2020.service;

import edu.ptit.da2020.init.LoadFile;
import edu.ptit.da2020.init.MapGraph;
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
    LoadFile loadFile;

    @Autowired
    MapGraph mapGraph;

    @Autowired
    TimeScorer timeScorer;

    @Autowired
    EstTimeScorer estTimeScorer;

    public List<Junction> findRoute(String startId, String finishId) {
        mapGraph.setRouteFinder(new RouteFinder<>(mapGraph.getGraph(), timeScorer, estTimeScorer));
        return mapGraph.getRouteFinder().findRoute(
                mapGraph.getGraph().getNode(startId),
                mapGraph.getGraph().getNode(finishId)
        );
    }
}
