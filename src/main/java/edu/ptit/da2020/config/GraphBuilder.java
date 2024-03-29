package edu.ptit.da2020.config;

import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.model.graph.Graph;
import edu.ptit.da2020.pathfinding.RouteFinder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import javax.annotation.PostConstruct;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

@Configuration
@Data
@Slf4j
@Order(2)
public class GraphBuilder {

  @Autowired
  DataLoader dataLoader;

  private Graph<Junction> graph;
  private RouteFinder<Junction> routeFinder;
  private Set<Junction> nodes;
  private ArrayList<Junction> al;
  private Map<String, Set<String>> neighbourhoods;


  @SneakyThrows
  @PostConstruct
  public void init() {
    log.info("Init graph...");
    initLinks();
    initNodes();
    graph = new Graph<>(nodes, neighbourhoods);
  }


  private void initNodes() {
    nodes = new LinkedHashSet<>();

    for (Map.Entry<String, Double[]> entry : dataLoader.getListV().entrySet()) {
      nodes.add(new Junction(entry.getKey(), entry.getValue()[0], entry.getValue()[1]));
    }
    log.info("Graph Vertex: " + nodes.size());
  }


  private void initLinks() {
    neighbourhoods = new HashMap<>();

    for (Map.Entry<String, String[]> entry : dataLoader.getListE().entrySet()) {
      if (neighbourhoods.containsKey(entry.getValue()[0])) {
        neighbourhoods.get(entry.getValue()[0]).add(entry.getValue()[1]);
      } else {
        Set<String> tempSet = new HashSet<>();
        tempSet.add(entry.getValue()[1]);
        neighbourhoods.put(entry.getValue()[0], tempSet);
      }
    }
    log.info("Graph Junctions: " + neighbourhoods.size());
  }
}
