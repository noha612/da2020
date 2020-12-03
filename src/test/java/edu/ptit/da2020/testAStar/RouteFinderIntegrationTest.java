package edu.ptit.da2020.testAStar;

import edu.ptit.da2020.model.graph.Graph;
import edu.ptit.da2020.util.algorithm.RouteFinder;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RouteFinderIntegrationTest {

    private Graph<Intersection> underground;

    private RouteFinder<Intersection> routeFinder;

    @Before
    public void setUp() {
        Set<Intersection> intersections = new HashSet<>();
        Map<String, Set<String>> connections = new HashMap<>();

        intersections.add(new Intersection("A"));
        intersections.add(new Intersection("B"));
        intersections.add(new Intersection("C"));
        intersections.add(new Intersection("D"));
        intersections.add(new Intersection("E"));
        intersections.add(new Intersection("F"));
        intersections.add(new Intersection("G"));
        intersections.add(new Intersection("H"));
        intersections.add(new Intersection("I"));
        intersections.add(new Intersection("J"));
        intersections.add(new Intersection("K"));

        connections.put("A", Stream.of("B", "H").collect(Collectors.toSet()));
        connections.put("B", Stream.of("A", "C").collect(Collectors.toSet()));
        connections.put("C", Stream.of("B", "D").collect(Collectors.toSet()));
        connections.put("D", Stream.of("C", "E").collect(Collectors.toSet()));
        connections.put("E", Stream.of("D", "F").collect(Collectors.toSet()));
        connections.put("F", Stream.of("E", "G", "K").collect(Collectors.toSet()));
        connections.put("G", Stream.of("H", "K", "F").collect(Collectors.toSet()));
        connections.put("H", Stream.of("A", "G", "I").collect(Collectors.toSet()));
        connections.put("I", Stream.of("H", "J").collect(Collectors.toSet()));
        connections.put("J", Stream.of("I", "K").collect(Collectors.toSet()));
        connections.put("K", Stream.of("G", "J", "F").collect(Collectors.toSet()));

        underground = new Graph<>(intersections, connections);
        routeFinder = new RouteFinder<>(underground, new GScorer(), new HScorer());
    }

    @Test
    public void findRoute() {
        List<Intersection> route = routeFinder.findRouteAStarAlgorithm(underground.getNode("A"), underground.getNode("K"));

        System.out.println(route.stream().map(Intersection::getId).collect(Collectors.toList()));
    }
}