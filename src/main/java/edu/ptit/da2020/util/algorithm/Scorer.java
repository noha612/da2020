package edu.ptit.da2020.util.algorithm;

import edu.ptit.da2020.model.graphmodel.GraphNode;

public interface Scorer<T extends GraphNode> {
    double computeCost(T from, T to);
}
