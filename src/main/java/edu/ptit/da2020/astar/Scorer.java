package edu.ptit.da2020.astar;

public interface Scorer<T extends GraphNode> {
    double computeCost(T from, T to);
}
