package edu.ptit.da2020.service.impl;

import edu.ptit.da2020.config.DataLoader;
import edu.ptit.da2020.config.GraphBuilder;
import edu.ptit.da2020.model.GeoPoint;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.pathfinding.RouteFinder;
import edu.ptit.da2020.pathfinding.scorer.impl.EstimateTimeScorer;
import edu.ptit.da2020.pathfinding.scorer.impl.TimeScorer;
import edu.ptit.da2020.service.DirectionService;
import edu.ptit.da2020.util.CommonUtil;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DirectionServiceImpl implements DirectionService {

  @Autowired
  DataLoader dataLoader;

  @Autowired
  GraphBuilder graphBuilder;

  @Autowired
  TimeScorer timeScorer;

  @Autowired
  EstimateTimeScorer estimateTimeScorer;

  @Override
  public List<Junction> findRoute(String fromId, String toId) {
    GeoPoint from = new GeoPoint(dataLoader.getListV().get(fromId)[0],
        dataLoader.getListV().get(fromId)[1]);
    GeoPoint to = new GeoPoint(dataLoader.getListV().get(toId)[0],
        dataLoader.getListV().get(toId)[1]);
    if (CommonUtil.haversineFormula(from, to) > 11) {
      estimateTimeScorer.setX(1.7);
    } else if (CommonUtil.haversineFormula(from, to) > 7) {
      estimateTimeScorer.setX(1.8);
    } else if (CommonUtil.haversineFormula(from, to) > 3) {
      estimateTimeScorer.setX(1.6);
    } else {
      estimateTimeScorer.setX(1);
    }
    graphBuilder
        .setRouteFinder(new RouteFinder<>(graphBuilder.getGraph(), timeScorer, estimateTimeScorer));

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Callable<Object> task = () -> graphBuilder.getRouteFinder().findRoute(
        graphBuilder.getGraph().getNode(fromId),
        graphBuilder.getGraph().getNode(toId)
    );
    List<Junction> list = null;
    Future<Object> future = executor.submit(task);
    try {
      list = (List<Junction>) future.get(5, TimeUnit.SECONDS);
    } catch (TimeoutException ex) {
//      log.info("Timeout, use back up route finder");
      list = graphBuilder.getRouteFinder().findRouteBackUp(
          graphBuilder.getGraph().getNode(fromId),
          graphBuilder.getGraph().getNode(toId)
      );
    } catch (InterruptedException e) {
      e.printStackTrace();
      Thread.currentThread().interrupt();
    } catch (ExecutionException e) {
      e.printStackTrace();
    } finally {
      future.cancel(true);
      executor.shutdownNow();
    }
    return list;
  }

  @Override
  public List<Junction> findRouteExp(String fromId, String toId, double x) {
    estimateTimeScorer.setX(x);
    graphBuilder
        .setRouteFinder(new RouteFinder<>(graphBuilder.getGraph(), timeScorer, estimateTimeScorer));

    return graphBuilder.getRouteFinder().findRoute(
        graphBuilder.getGraph().getNode(fromId),
        graphBuilder.getGraph().getNode(toId)
    );
  }
}
