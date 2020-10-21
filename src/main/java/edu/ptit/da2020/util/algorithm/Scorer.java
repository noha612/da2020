package edu.ptit.da2020.util.algorithm;

import edu.ptit.da2020.model.graph.GraphNode;
import org.springframework.stereotype.Service;

@Service
public interface Scorer<T extends GraphNode> {
    double computeCost(T from, T to);
}
