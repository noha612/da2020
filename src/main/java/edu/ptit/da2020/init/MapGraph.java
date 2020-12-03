package edu.ptit.da2020.init;

import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.model.graph.Graph;
import edu.ptit.da2020.util.algorithm.RouteFinder;
import lombok.Data;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.annotation.PostConstruct;
import java.util.*;

@Configuration
@Data
@Slf4j
@Order(2)
public class MapGraph {
    @Autowired
    LoadFile loadFile;

    private Graph<Junction> graph;
    private RouteFinder<Junction> routeFinder;
    private Set<Junction> nodes;
    private ArrayList<Junction> al;
    private Map<String, Set<String>> neighbourhoods;


    @SneakyThrows
    @PostConstruct
    public void init() {
        log.info("init graph...");
        initLinks();
        initNodes();
        graph = new Graph<>(nodes, neighbourhoods);
    }


    private void initNodes() {
        nodes = new LinkedHashSet<>();

        for (Map.Entry<String, Double[]> entry : loadFile.getListV().entrySet()) {
            nodes.add(new Junction(entry.getKey(), entry.getValue()[0], entry.getValue()[1]));
        }
        log.info("total V: " + nodes.size());
    }


    private void initLinks() {
        neighbourhoods = new HashMap<>();

        for (Map.Entry<String, String[]> entry : loadFile.getListE().entrySet()) {
            if (neighbourhoods.containsKey(entry.getValue()[0])) {
                neighbourhoods.get(entry.getValue()[0]).add(entry.getValue()[1]);
            } else {
                Set<String> tempSet = new HashSet<>();
                tempSet.add(entry.getValue()[1]);
                neighbourhoods.put(entry.getValue()[0], tempSet);
            }
        }
        log.info("total star: " + neighbourhoods.size());
    }
}
