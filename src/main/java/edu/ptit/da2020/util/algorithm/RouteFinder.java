package edu.ptit.da2020.util.algorithm;

import edu.ptit.da2020.model.graph.Graph;
import edu.ptit.da2020.model.graph.GraphNode;
import edu.ptit.da2020.model.graph.RouteNode;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

@Slf4j
public class RouteFinder<T extends GraphNode> {
    private final Graph<T> graph;
    private final Scorer<T> nextNodeScorer;
    private final Scorer<T> targetScorer;

    Map<Integer, Double> trafficToSpd = new HashMap<Integer, Double>() {{
        put(1, 30.0 * 5 / 18);
        put(2, 10.0 * 5 / 18);
        put(3, 2.0 * 5 / 18);
    }};

    public RouteFinder(Graph<T> graph, Scorer<T> nextNodeScorer, Scorer<T> targetScorer) {
        this.graph = graph;
        this.nextNodeScorer = nextNodeScorer;
        this.targetScorer = targetScorer;
    }

    public List<T> findRouteAStarAlgorithm(T from, T to, Map<String, Integer> traffics) {
        Queue<RouteNode> openSet = new PriorityQueue<>();
        Map<T, RouteNode<T>> allNodes = new HashMap<>();

        RouteNode<T> start = new RouteNode<>(from, null, 0d, targetScorer.computeCost(from, to) / trafficToSpd.get(1));
        openSet.add(start);
        allNodes.put(from, start);

        while (!openSet.isEmpty()) {
            log.info("searching...");
            RouteNode<T> next = openSet.poll();
            if (next.getCurrent().getId().equals(to.getId())) {
                List<T> route = new ArrayList<>();
                RouteNode<T> current = next;
                do {
                    route.add(0, current.getCurrent());
                    current = allNodes.get(current.getPrevious());
                } while (current != null);
                return route;
            }

            graph.getConnections(next.getCurrent()).forEach(connection -> {
                RouteNode<T> nextNode = allNodes.getOrDefault(connection, new RouteNode<>(connection));
                allNodes.put(connection, nextNode);

//                String roadId = next.getCurrent().getId() + "_" + connection.getId();
//                log.info(roadId);
//                int trafficLevel = traffics.get(roadId);
//                double g = next.getRouteScore() / trafficToSpd.get(trafficLevel);
//                double h = nextNodeScorer.computeCost(next.getCurrent(), connection) / trafficToSpd.get(1);
//                double newScore = g + h;
                double newScore = next.getRouteScore() + nextNodeScorer.computeCost(next.getCurrent(), connection);

                if (newScore < nextNode.getRouteScore()) {
                    nextNode.setPrevious(next.getCurrent());
                    nextNode.setRouteScore(newScore);
                    nextNode.setEstimatedScore(newScore + targetScorer.computeCost(connection, to));
//                    nextNode.setEstimatedScore(newScore + targetScorer.computeCost(connection, to));
                    openSet.add(nextNode);
                }
            });
        }

        throw new IllegalStateException("No route found");
    }

}