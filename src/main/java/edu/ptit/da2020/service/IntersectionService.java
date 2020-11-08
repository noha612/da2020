package edu.ptit.da2020.service;

import edu.ptit.da2020.config.GraphConfig;
import edu.ptit.da2020.model.entity.Intersection;
import edu.ptit.da2020.repository.IntersectionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
public class IntersectionService {
    @Autowired
    GraphConfig graphConfig;

    @Autowired
    IntersectionRepository intersectionRepository;

    public void insertDBFromXML() {
        intersectionRepository.saveAll(graphConfig.getIntersections());
    }

    public void insertTXTFromXML() {
        try {
            File f = new File("hn.txt");
            if (f.createNewFile()) {
                log.info("File created: " + f.getName());
            } else {
                log.info("File already exists.");
            }
        } catch (IOException e) {
            log.error("An error occurred.");
            e.printStackTrace();
        }


        try (
                FileWriter fw = new FileWriter("hn.txt");
        ) {
            List<Intersection> set = graphConfig.getAl();
            log.info("begin insert V, size: " + set.size());
            for (Intersection i : set) {
                fw.write(i.getId() + " " + i.getLatitude() + " " + i.getLongitude());
            }
            log.info("done");
        } catch (IOException e) {
            log.error("An error occurred.");
            e.printStackTrace();
        }

    }
}
