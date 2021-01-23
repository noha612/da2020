package edu.ptit.da2020.controller.impl;

import edu.ptit.da2020.config.AppConfig;
import edu.ptit.da2020.config.DataLoader;
import edu.ptit.da2020.config.GraphBuilder;
import edu.ptit.da2020.constant.BaseConstant;
import edu.ptit.da2020.controller.ApiInterface;
import edu.ptit.da2020.model.GeoPoint;
import edu.ptit.da2020.model.Junction;
import edu.ptit.da2020.model.Place;
import edu.ptit.da2020.model.dto.AlertDTO;
import edu.ptit.da2020.model.dto.Direction;
import edu.ptit.da2020.model.dto.Location;
import edu.ptit.da2020.model.dto.Road;
import edu.ptit.da2020.service.DirectionService;
import edu.ptit.da2020.service.LocatingService;
import edu.ptit.da2020.service.TrafficService;
import edu.ptit.da2020.util.CommonUtil;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ApiController implements ApiInterface {

    @Autowired
    DirectionService directionService;

    @Autowired
    LocatingService locatingService;

    @Autowired
    TrafficService trafficService;

    @Autowired
    DataLoader dataLoader;

    @Autowired
    GraphBuilder graphBuilder;

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    AppConfig appConfig;

    @Override
    public List<Place> getListPlaceByName(String name) {
        return locatingService.findIdByName(name);
    }

    @Override
    public Location getLocationByPoint(double lat, double lng) {
        Location l = locatingService.findLocationByPoint(lat, lng);
        if (!l.getH().getLng().equals(l.getMarker().getLng()) && !l.getH().getLat()
                .equals(l.getMarker().getLat())) {
            l.getPlace().setName("gần " + l.getPlace().getName());
        }
        log.info(l.toString());
        return l;
    }

    @Override
    public Road getRoadByPoint(double lat, double lng) {
        return locatingService.findRoadByPoint(lat, lng);
    }

    @Override
    public Direction getDirection(String fromId, String toId) {
        if (fromId.equalsIgnoreCase(toId)) return null;
        if (StringUtils.isNotEmpty(fromId) && StringUtils.isNotEmpty(toId)) {
            Direction direction = new Direction();
            LocalDateTime start = LocalDateTime.now();
            List<Junction> lsIts = directionService.findRoute(fromId, toId);
            LocalDateTime finish = LocalDateTime.now();
            direction.setFrom(new GeoPoint(lsIts.get(0).getLat(), lsIts.get(0).getLng()));
            direction.setTo(new GeoPoint(lsIts.get(lsIts.size() - 1).getLat(),
                    lsIts.get(lsIts.size() - 1).getLng()));
            direction.setJunctions(lsIts);
            Map<String, String> traffics = new LinkedHashMap<>();
            for (int i = 0; i < lsIts.size() - 1; i++) {
                String roadId = lsIts.get(i).getId() + "_" + lsIts.get(i + 1).getId();
                String roadId2 = lsIts.get(i + 1).getId() + "_" + lsIts.get(i).getId();
                String trafficLevel = BaseConstant.SPEED_VERY_SMOOTH;
                if (dataLoader.getListCongestions().containsKey(roadId)) {
                    trafficLevel = dataLoader.getListCongestions().get(roadId);
                }
                if (dataLoader.getListCongestions().containsKey(roadId2)) {
                    trafficLevel = dataLoader.getListCongestions().get(roadId2);
                }
                traffics.put(lsIts.get(i).getId() + "_" + lsIts.get(i + 1).getId(), trafficLevel);
            }
            direction.setTraffics(traffics);
            direction.setLength(direction.calLength());
            direction.setTime(calTime(lsIts, traffics));
            log.info(direction.toString());
            log.info(direction.getJunctions().size() + "");
            log.info(direction.calLength() + "");
            log.info(calTime(lsIts, traffics) * 60 + "");
            log.info(CommonUtil.getTime(start, finish));
            return direction;
        }
        return null;
    }

    //For Testing
//    @Override
//    public Direction getDirection(String fromId, String toId) {
//        Direction direction = new Direction();
//        if (StringUtils.isNotEmpty(fromId) && StringUtils.isNotEmpty(toId)) {
//            double x = 1;
//            for (int c = 0; c <= 10; c++) {
//                direction = new Direction();
//                LocalDateTime start = LocalDateTime.now();
//                List<Junction> lsIts = directionService.findRouteExp(fromId, toId, x);
//                LocalDateTime finish = LocalDateTime.now();
//                direction.setFrom(new GeoPoint(lsIts.get(0).getLat(), lsIts.get(0).getLng()));
//                direction.setTo(new GeoPoint(lsIts.get(lsIts.size() - 1).getLat(),
//                    lsIts.get(lsIts.size() - 1).getLng()));
//                direction.setJunctions(lsIts);
//                Map<String, Integer> traffics = new LinkedHashMap<>();
//                for (int i = 0; i < lsIts.size() - 1; i++) {
//                    traffics.put(lsIts.get(i).getId() + "_" + lsIts.get(i + 1).getId(),
//                        dataLoader.getListCongestions()
//                            .get(lsIts.get(i).getId() + "_" + lsIts.get(i + 1).getId()));
//                }
//                direction.setTraffics(traffics);
//                direction.setLength(direction.calLength());
//                direction.setTime(direction.calTime());
//                log.info(direction.toString());
//                log.info(direction.getJunctions().size() + "");
//                log.info(direction.calLength() + "");
//                log.info(direction.calTime() * 60 + "");
//                System.out.println(
//                    "x: " + x + " " + direction.calLength() + " " + direction.getJunctions().size()
//                        + " " + getTime(start, finish));
//                x += 0.1;
//            }
//            return direction;
//        }
//        return null;
//    }

    @Override
    public String getTraffic(String id) {
        return trafficService.getTrafficStatusByRoadId(id);
    }

    @Override
    public double getDistance(String fromId, String toId, Double fromLat, Double fromLng,
                              Double toLat, Double toLng) {
        if (StringUtils.isNotEmpty(fromId) && StringUtils.isNotEmpty(toId)) {
            GeoPoint from = new GeoPoint(dataLoader.getListV().get(fromId)[0],
                    dataLoader.getListV().get(fromId)[1]);
            GeoPoint to = new GeoPoint(dataLoader.getListV().get(toId)[0],
                    dataLoader.getListV().get(toId)[1]);
            return CommonUtil.haversineFormula(from, to);
        } else {
            return CommonUtil.haversineFormula(fromLat, fromLng, toLat, toLng);
        }
    }

    @Override
    public void updateCongestion(AlertDTO alertDTO) {
        trafficService.update(alertDTO);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void test() {
//        redisTemplate.opsForHash().put("CONGEST", "6610656034_5716482151", "SMOOTH");
//        redisTemplate.opsForHash().put("CONGEST", "5707271700_5707271694", "SMOOTH");
//        redisTemplate.opsForHash().put("CONGEST", "6684837024_6666466358", "SMOOTH");
//        redisTemplate.opsForHash().put("CONGEST", "6666466359_4106979199", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "5707271702_6666466361", "SMOOTH");
//
//        redisTemplate.opsForHash().put("CONGEST", "4539674888_1692612450", "SMOOTH");
//        redisTemplate.opsForHash().put("CONGEST", "5709923611_6637795370", "SMOOTH");
//        redisTemplate.opsForHash().put("CONGEST", "6628713645_6628713643", "SMOOTH");
// 72 1
//        redisTemplate.opsForHash().put("CONGEST", "5710056505_6661487945", "SMOOTH");
//        redisTemplate.opsForHash().put("CONGEST", "7404045734_4540453404", "SMOOTH");
//        redisTemplate.opsForHash().put("CONGEST", "4540453404_6668144440", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "1897734810_6628713645", "SMOOTH");
//        redisTemplate.opsForHash().put("CONGEST", "6621426601_1314185658", "SMOOTH");
// 72 2
//        redisTemplate.opsForHash().put("CONGEST", "6666466359_4106979199", "HEAVY");
//        redisTemplate.opsForHash().put("CONGEST", "5707271702_6666466361", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6684837045_4106979203", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6610656034_5716482151", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "1692612449_1692612448", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "1692612450_5709923611", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "4539674879_6621426595", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "1314191436_6621426601", "HEAVY");
//        redisTemplate.opsForHash().put("CONGEST", "1897734810_6628713645", "MILD");
//long bien 1
//        redisTemplate.opsForHash().put("CONGEST", "5721494367_4870728332", "SMOOTH");
//        redisTemplate.opsForHash().put("CONGEST", "81920098_5721494366", "SMOOTH");
//        redisTemplate.opsForHash().put("CONGEST", "84796874_4491335810", "SMOOTH");
//        redisTemplate.opsForHash().put("CONGEST", "84796877_4491335809", "SMOOTH");
//        redisTemplate.opsForHash().put("CONGEST", "5716537694_6677561266", "SMOOTH");
        //long bien 2
//        redisTemplate.opsForHash().put("CONGEST", "5831399049_6661708577", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "4538405061_4538404998", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "444394806_5716537703", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6691165214_1221161730", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "444394827_444394823", "HEAVY");
//        redisTemplate.opsForHash().put("CONGEST", "444394834_6677561267", "HEAVY");
//        redisTemplate.opsForHash().put("CONGEST", "6603014205_4530855117", "HEAVY");
//        redisTemplate.opsForHash().put("CONGEST", "5831399053_6603014206", "HEAVY");
//        redisTemplate.opsForHash().put("CONGEST", "6413709264_5831399053", "HEAVY");
//        redisTemplate.opsForHash().put("CONGEST", "1884757284_6413709264", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6603014204_6603014203", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "317968992_6406219805", "MILD");
//
//        redisTemplate.opsForHash().put("CONGEST", "317968992_6406219805", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6406219805_6685187328", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6685187328_6685187327", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6685187327_6685187325", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6685187325_5704011544", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "5704011544_6685187323", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6685187323_1497544826", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "1497544826_6685187345", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6685187345_6685187343", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6685187343_322505294", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "322505294_6685187319", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6685187319_2592290239", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "2592290239_6406176270", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6406176270_317968694", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "317968694_6685568158", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6685568158_5703585010", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "5703585010_6688377251", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6688377251_81804025", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "81804025_6688377254", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6688377254_6657729907", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6657729907_103061231", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "103061231_4145112302", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "4145112302_7202796992", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "7202796992_6657726570", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6657726570_6657726558", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6657726558_2371534393", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "2371534393_6657726566", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6657726566_6657839337", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6657839337_309814374", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "309814374_6657839335", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6657839335_6657839330", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6657839330_309639029", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "309639029_5701443833", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "5701443833_6657839327", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6657839327_6657839326", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6657839326_317968475", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "317968475_6657839325", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6657839325_317968483", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "317968483_6651394974", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6651394974_308712571", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "308712571_6651394973", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6651394973_104771041", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "84798026_84798025", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "84798025_1497603121", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "1497603121_84798024", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "84798024_84798023", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "84798023_84796872", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "84796872_84796873", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "84796873_4491335812", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "4491335812_84796874", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "84796874_4491335810", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "4491335810_4491335811", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "4491335811_84796875", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "84796875_4491335808", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "4491335808_84796877", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "84796877_4491335809", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "4491335809_438049258", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "438049258_84796315", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "84796315_1497570508", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "1497570508_84796316", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "84796316_84792594", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "84792594_438049260", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "6641013629_5721494367", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "5721494367_4870728332", "MILD");
//        redisTemplate.opsForHash().put("CONGEST", "4870728332_426896152", "MILD");

//        LinkedHashSet<String> keySet = (LinkedHashSet<String>) redisTemplate.keys("*roadId*");
//        List<Integer> level = redisTemplate.opsForValue().multiGet(keySet);
//        int i = 0;
//        for (String k : keySet) {
//            log.info(k + " " + level.get(i));
//            i++;
//        }

//    Map<String, Integer> v = new HashMap<>();
//    for (String i : dataLoader.getListV().keySet()) {
//      v.put(i, 0);
//    }
//    Map<String, Set<String>> e = graphBuilder.getNeighbourhoods();
//        v.put("1893253381", 1);
//        int d = 1;
//        boolean yet;
//        do {
//            yet = false;
//            for (String i : e.keySet()) {
//                if (v.get(i) == 1) {
//                    v.put(i, 2);
//                    for (String j : e.keySet()) {
//                        if (v.get(j) == 0 && e.get(i).contains(j)) {
//                            v.put(j, 1);
//                            log.info(j);
//                            log.info(d + "");
//                            d++;
//                            yet = true;
//                        }
//                    }
//                }
//            }
//        } while (yet);
//        System.out.println(d + "");
//    int d = 0;
//    for (String i : v.keySet()) {
//      d += e.get(i).size();
//    }
//    System.out.println(d);
        redisTemplate.opsForValue().set("61302ad40af136f5", 100.0);

    }


    public double calTime(List<Junction> junctions, Map<String, String> traffics) {
        double time = 0;
        for (int i = 0; i < junctions.size() - 1; i++) {
            GeoPoint from = new GeoPoint(junctions.get(i).getLat(), junctions.get(i).getLng());
            GeoPoint to = new GeoPoint(junctions.get(i + 1).getLat(),
                    junctions.get(i + 1).getLng());
            String trafficLevel = traffics.get(junctions.get(i).getId() + "_" + junctions.get(i + 1).getId());
            double spd = appConfig.getTrafficToSpeedMapping().get(BaseConstant.SPEED_VERY_SMOOTH);
            if (trafficLevel != null) {
                switch (trafficLevel) {
                    case BaseConstant.SPEED_VERY_SMOOTH:
                        spd = appConfig.getTrafficToSpeedMapping().get(BaseConstant.SPEED_VERY_SMOOTH);
                        break;
                    case BaseConstant.SPEED_SMOOTH:
                        spd = appConfig.getTrafficToSpeedMapping().get(BaseConstant.SPEED_SMOOTH);
                        break;
                    case BaseConstant.SPEED_MILD:
                        spd = appConfig.getTrafficToSpeedMapping().get(BaseConstant.SPEED_MILD);
                        break;
                    case BaseConstant.SPEED_HEAVY:
                        spd = appConfig.getTrafficToSpeedMapping().get(BaseConstant.SPEED_HEAVY);
                        break;
                    default:
                        spd = appConfig.getTrafficToSpeedMapping().get(BaseConstant.SPEED_VERY_SMOOTH);
                        break;
                }
            }
            time += CommonUtil.haversineFormula(from, to) / spd;
        }
        return time;
    }

    public static void main(String[] args) {
        double kmMin = 6D / 13D;
        double v1 = 27.69 / 60;
        double v2 = 10.38 / 60;
        double v3 = 3.46 / 60;
        double normal =
                5.749232431302711 - 0.1155258337341999 - 0.07173869631117338 - 0.1389920990389998;
        double traff = 0.1155258337341999 + 0.07173869631117338 + 0.1389920990389998;
        System.out.println(normal / v1 + traff / v2);
        System.out.println((normal + traff) / v1);
        System.out.println(kmMin + " " + v1);
        System.out.println(0.22759675442593622 * 60);
    }
}
