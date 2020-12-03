package edu.ptit.da2020.constant;

import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static final double R = 6372.8; // km

    public static final Map<Integer, Double> TRAFFIC_TO_SPD = new HashMap<Integer, Double>() {{
        put(1, 30.0);
        put(2, 10.0);
        put(3, 2.0);
    }};
}
