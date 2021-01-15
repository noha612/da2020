package edu.ptit.da2020.pathfinding;

import edu.ptit.da2020.model.graph.Graph;
import edu.ptit.da2020.model.graph.GraphNode;
import edu.ptit.da2020.model.graph.RouteNode;
import edu.ptit.da2020.pathfinding.scorer.Scorer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RouteFinder<T extends GraphNode> {

  private final Graph<T> graph;
  private final Scorer<T> nextNodeScorer;
  private final Scorer<T> targetScorer;

  public RouteFinder(Graph<T> graph, Scorer<T> nextNodeScorer, Scorer<T> targetScorer) {
    this.graph = graph;
    this.nextNodeScorer = nextNodeScorer;
    this.targetScorer = targetScorer;
  }

  public List<T> findRoute(T from, T to) {
    return aStar(from, to);
  }

  public List<T> findRouteBackUp(T from, T to) {
    return greedy(from, to);
  }

  private List<T> aStar(T from, T to) {
//        log.info("searching...");
    Queue<RouteNode> openSet = new PriorityQueue<>();
    Map<T, RouteNode<T>> allNodes = new HashMap<>();

    RouteNode<T> start = new RouteNode<>(from, null, 0d, targetScorer.computeCost(from, to));
    openSet.add(start);
    allNodes.put(from, start);

    while (!openSet.isEmpty()) {
      RouteNode<T> next = openSet.poll();
      if (next.getCurrent().equals(to)) {
//                log.info(next.getEstimatedScore() + "");
//                log.info("Done!");
        List<T> route = new ArrayList<>();
        RouteNode<T> current = next;
        do {
          route.add(0, current.getCurrent());
          current = allNodes.get(current.getPrevious());
        } while (current != null);
        return route;
      }
      try {
        graph.getConnections(next.getCurrent()).forEach(connection -> {
//                    log.info(connection.getId());
          RouteNode<T> nextNode = allNodes
              .getOrDefault(connection, new RouteNode<>(connection));
          allNodes.put(connection, nextNode);
          double newScore = next.getRouteScore() + nextNodeScorer
              .computeCost(next.getCurrent(), connection);
          if (newScore < nextNode.getRouteScore()) {
            nextNode.setPrevious(next.getCurrent());
            nextNode.setRouteScore(newScore);
            nextNode
                .setEstimatedScore(newScore + targetScorer.computeCost(connection, to));
            openSet.add(nextNode);
          }
        });
      } catch (Exception e) {
//                log.error(" e " + next.getCurrent().toString());
        RouteNode<T> nextNode = allNodes
            .getOrDefault(next.getPrevious(), new RouteNode<>(next.getPrevious()));
        allNodes.put(next.getPrevious(), nextNode);
        double newScore = next.getRouteScore() + nextNodeScorer
            .computeCost(next.getCurrent(), next.getPrevious());
        if (newScore < nextNode.getRouteScore()) {
          nextNode.setPrevious(next.getCurrent());
          nextNode.setRouteScore(newScore);
          nextNode
              .setEstimatedScore(
                  newScore + targetScorer.computeCost(next.getPrevious(), to));
          openSet.add(nextNode);
        }
      }
    }

    throw new IllegalStateException("No route found");
  }

  private List<T> greedy(T from, T to) {
    Set<T> set = new HashSet<>();
    Map<T, RouteNode<T>> allNodes = new HashMap<>();
    Queue<RouteNode> openSet = new PriorityQueue<>();

    RouteNode<T> start = new RouteNode<>(from, null, 0d, targetScorer.computeCost(from, to));
    allNodes.put(from, start);
    openSet.add(start);

    while (!openSet.isEmpty()) {
      RouteNode<T> next = openSet.poll();
      set.add(next.getCurrent());
      if (next.getCurrent().equals(to)) {

        List<T> route = new ArrayList<>();
        RouteNode<T> current = next;
        do {
          route.add(0, current.getCurrent());
          current = allNodes.get(current.getPrevious());
        } while (current != null);
        return route;
      }
      try {
        graph.getConnections(next.getCurrent()).forEach(connection -> {
          if (!set.contains(connection)) {
            RouteNode<T> nextNode = allNodes
                .getOrDefault(connection, new RouteNode<>(connection));
            allNodes.put(connection, nextNode);
            nextNode.setPrevious(next.getCurrent());
            nextNode
                .setEstimatedScore(targetScorer.computeCost(connection, to));
            openSet.add(nextNode);
          }
        });
      } catch (Exception ignored) {
      }
    }
    throw new IllegalStateException("No route found");
  }

}