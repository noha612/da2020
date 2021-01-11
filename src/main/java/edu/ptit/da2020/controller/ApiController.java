package edu.ptit.da2020.controller;

import edu.ptit.da2020.config.DataLoader;
import edu.ptit.da2020.config.MapBuilder;
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
import edu.ptit.da2020.util.MathUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
    RedisTemplate redisTemplate;

    @Autowired
    MapBuilder mapBuilder;

    @Override
    public List<Place> getListPlaceByName(String name) {
        return locatingService.findIdByName(name);
    }

    @Override
    public Location getLocationByPoint(double lat, double lng) {
        Location l = locatingService.findLocationByPoint(lat, lng);
        if (!l.getH().getLng().equals(l.getMarker().getLng()) && !l.getH().getLat().equals(l.getMarker().getLat())) {
            l.getPlace().setName("gần " + l.getPlace().getName());
        }
        return l;
    }

    @Override
    public Road getRoadByPoint(double lat, double lng) {
        return locatingService.findRoadByPoint(lat, lng);
    }

    @Override
    public Direction getDirection(String fromId, String toId) {
        if (StringUtils.isNotEmpty(fromId) && StringUtils.isNotEmpty(toId)) {
            Direction direction = new Direction();
//            LocalDateTime start = LocalDateTime.now();
//            List<Junction> lsIts = directionService.findRoute(fromId, toId);
            double x = 1.7;
//            for (int c = 0; c <= 10; c++) {
            direction = new Direction();
            LocalDateTime start = LocalDateTime.now();
            List<Junction> lsIts = directionService.findRouteExp(fromId, toId, x);
            LocalDateTime finish = LocalDateTime.now();
            direction.setFrom(new GeoPoint(lsIts.get(0).getLat(), lsIts.get(0).getLng()));
            direction.setTo(new GeoPoint(lsIts.get(lsIts.size() - 1).getLat(),
                    lsIts.get(lsIts.size() - 1).getLng()));
            direction.setJunctions(lsIts);
            Map<String, Integer> traffics = new LinkedHashMap<>();
            for (int i = 0; i < lsIts.size() - 1; i++) {
                traffics.put(lsIts.get(i).getId() + "_" + lsIts.get(i + 1).getId(),
                        dataLoader.getListCongestions()
                                .get(lsIts.get(i).getId() + "_" + lsIts.get(i + 1).getId()));
            }
            direction.setTraffics(traffics);
            direction.setLength(direction.calLength());
            direction.setTime(direction.calTime());
//            log.info(direction.toString());
//            log.info(direction.getJunctions().size() + "");
//            log.info(direction.calLength() + "");
//            log.info(direction.calTime() * 60 + "");
            System.out.println("x: " + x + " " + direction.calLength() + " " + direction.getJunctions().size() + " " + getTime(start, finish));
//                x += 0.1;
//            }
            return direction;
        }
        return null;
    }

    @Override
    public Integer getTraffic(String id) {
        return trafficService.getTrafficStatusByRoadId(id);
    }

    @Override
    public double getDistance(String fromId, String toId, Double fromLat, Double fromLng, Double toLat, Double toLng) {
        if (StringUtils.isNotEmpty(fromId) && StringUtils.isNotEmpty(toId)) {
            GeoPoint from = new GeoPoint(dataLoader.getListV().get(fromId)[0], dataLoader.getListV().get(fromId)[1]);
            GeoPoint to = new GeoPoint(dataLoader.getListV().get(toId)[0], dataLoader.getListV().get(toId)[1]);
            return MathUtil.haversineFomular(from, to);
        } else return MathUtil.haversineFomular(fromLat, fromLng, toLat, toLng);
    }

    @Override
    public void updateCongestion(AlertDTO alertDTO) {
        trafficService.update(alertDTO);
    }

    @Override
    public void test() {
//        redisTemplate.opsForHash().put("CONGEST", "6610656034_5716482151", 2);
//        redisTemplate.opsForHash().put("CONGEST", "5707271700_5707271694", 2);
//        redisTemplate.opsForHash().put("CONGEST", "6684837024_6666466358", 2);
//        redisTemplate.opsForHash().put("CONGEST", "6666466359_4106979199", 3);
//        redisTemplate.opsForHash().put("CONGEST", "5707271702_6666466361", 2);
//
//        redisTemplate.opsForHash().put("CONGEST", "4539674888_1692612450", 2);
//        redisTemplate.opsForHash().put("CONGEST", "5709923611_6637795370", 2);
//        redisTemplate.opsForHash().put("CONGEST", "6628713645_6628713643", 2);
// 72 1
//        redisTemplate.opsForHash().put("CONGEST", "5710056505_6661487945", 2);
//        redisTemplate.opsForHash().put("CONGEST", "7404045734_4540453404", 2);
//        redisTemplate.opsForHash().put("CONGEST", "4540453404_6668144440", 3);
//        redisTemplate.opsForHash().put("CONGEST", "1897734810_6628713645", 2);
//        redisTemplate.opsForHash().put("CONGEST", "6621426601_1314185658", 2);
// 72 2
//        redisTemplate.opsForHash().put("CONGEST", "6666466359_4106979199", 4);
//        redisTemplate.opsForHash().put("CONGEST", "5707271702_6666466361", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6684837045_4106979203", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6610656034_5716482151", 3);
//        redisTemplate.opsForHash().put("CONGEST", "1692612449_1692612448", 3);
//        redisTemplate.opsForHash().put("CONGEST", "1692612450_5709923611", 3);
//        redisTemplate.opsForHash().put("CONGEST", "4539674879_6621426595", 3);
//        redisTemplate.opsForHash().put("CONGEST", "1314191436_6621426601", 4);
//        redisTemplate.opsForHash().put("CONGEST", "1897734810_6628713645", 3);
//long bien 1
//        redisTemplate.opsForHash().put("CONGEST", "5721494367_4870728332", 2);
//        redisTemplate.opsForHash().put("CONGEST", "81920098_5721494366", 2);
//        redisTemplate.opsForHash().put("CONGEST", "84796874_4491335810", 2);
//        redisTemplate.opsForHash().put("CONGEST", "84796877_4491335809", 2);
//        redisTemplate.opsForHash().put("CONGEST", "5716537694_6677561266", 2);
        //long bien 2
//        redisTemplate.opsForHash().put("CONGEST", "5831399049_6661708577", 3);
//        redisTemplate.opsForHash().put("CONGEST", "4538405061_4538404998", 3);
//        redisTemplate.opsForHash().put("CONGEST", "444394806_5716537703", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6691165214_1221161730", 3);
//        redisTemplate.opsForHash().put("CONGEST", "444394827_444394823", 4);
//        redisTemplate.opsForHash().put("CONGEST", "444394834_6677561267", 4);
//        redisTemplate.opsForHash().put("CONGEST", "6603014205_4530855117", 4);
//        redisTemplate.opsForHash().put("CONGEST", "5831399053_6603014206", 4);
//        redisTemplate.opsForHash().put("CONGEST", "6413709264_5831399053", 4);
//        redisTemplate.opsForHash().put("CONGEST", "1884757284_6413709264", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6603014204_6603014203", 3);
//        redisTemplate.opsForHash().put("CONGEST", "317968992_6406219805", 3);
//
//        redisTemplate.opsForHash().put("CONGEST", "317968992_6406219805", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6406219805_6685187328", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6685187328_6685187327", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6685187327_6685187325", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6685187325_5704011544", 3);
//        redisTemplate.opsForHash().put("CONGEST", "5704011544_6685187323", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6685187323_1497544826", 3);
//        redisTemplate.opsForHash().put("CONGEST", "1497544826_6685187345", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6685187345_6685187343", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6685187343_322505294", 3);
//        redisTemplate.opsForHash().put("CONGEST", "322505294_6685187319", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6685187319_2592290239", 3);
//        redisTemplate.opsForHash().put("CONGEST", "2592290239_6406176270", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6406176270_317968694", 3);
//        redisTemplate.opsForHash().put("CONGEST", "317968694_6685568158", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6685568158_5703585010", 3);
//        redisTemplate.opsForHash().put("CONGEST", "5703585010_6688377251", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6688377251_81804025", 3);
//        redisTemplate.opsForHash().put("CONGEST", "81804025_6688377254", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6688377254_6657729907", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6657729907_103061231", 3);
//        redisTemplate.opsForHash().put("CONGEST", "103061231_4145112302", 3);
//        redisTemplate.opsForHash().put("CONGEST", "4145112302_7202796992", 3);
//        redisTemplate.opsForHash().put("CONGEST", "7202796992_6657726570", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6657726570_6657726558", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6657726558_2371534393", 3);
//        redisTemplate.opsForHash().put("CONGEST", "2371534393_6657726566", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6657726566_6657839337", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6657839337_309814374", 3);
//        redisTemplate.opsForHash().put("CONGEST", "309814374_6657839335", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6657839335_6657839330", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6657839330_309639029", 3);
//        redisTemplate.opsForHash().put("CONGEST", "309639029_5701443833", 3);
//        redisTemplate.opsForHash().put("CONGEST", "5701443833_6657839327", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6657839327_6657839326", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6657839326_317968475", 3);
//        redisTemplate.opsForHash().put("CONGEST", "317968475_6657839325", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6657839325_317968483", 3);
//        redisTemplate.opsForHash().put("CONGEST", "317968483_6651394974", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6651394974_308712571", 3);
//        redisTemplate.opsForHash().put("CONGEST", "308712571_6651394973", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6651394973_104771041", 3);
//        redisTemplate.opsForHash().put("CONGEST", "84798026_84798025", 3);
//        redisTemplate.opsForHash().put("CONGEST", "84798025_1497603121", 3);
//        redisTemplate.opsForHash().put("CONGEST", "1497603121_84798024", 3);
//        redisTemplate.opsForHash().put("CONGEST", "84798024_84798023", 3);
//        redisTemplate.opsForHash().put("CONGEST", "84798023_84796872", 3);
//        redisTemplate.opsForHash().put("CONGEST", "84796872_84796873", 3);
//        redisTemplate.opsForHash().put("CONGEST", "84796873_4491335812", 3);
//        redisTemplate.opsForHash().put("CONGEST", "4491335812_84796874", 3);
//        redisTemplate.opsForHash().put("CONGEST", "84796874_4491335810", 3);
//        redisTemplate.opsForHash().put("CONGEST", "4491335810_4491335811", 3);
//        redisTemplate.opsForHash().put("CONGEST", "4491335811_84796875", 3);
//        redisTemplate.opsForHash().put("CONGEST", "84796875_4491335808", 3);
//        redisTemplate.opsForHash().put("CONGEST", "4491335808_84796877", 3);
//        redisTemplate.opsForHash().put("CONGEST", "84796877_4491335809", 3);
//        redisTemplate.opsForHash().put("CONGEST", "4491335809_438049258", 3);
//        redisTemplate.opsForHash().put("CONGEST", "438049258_84796315", 3);
//        redisTemplate.opsForHash().put("CONGEST", "84796315_1497570508", 3);
//        redisTemplate.opsForHash().put("CONGEST", "1497570508_84796316", 3);
//        redisTemplate.opsForHash().put("CONGEST", "84796316_84792594", 3);
//        redisTemplate.opsForHash().put("CONGEST", "84792594_438049260", 3);
//        redisTemplate.opsForHash().put("CONGEST", "6641013629_5721494367", 3);
//        redisTemplate.opsForHash().put("CONGEST", "5721494367_4870728332", 3);
//        redisTemplate.opsForHash().put("CONGEST", "4870728332_426896152", 3);

//        LinkedHashSet<String> keySet = (LinkedHashSet<String>) redisTemplate.keys("*roadId*");
//        List<Integer> level = redisTemplate.opsForValue().multiGet(keySet);
//        int i = 0;
//        for (String k : keySet) {
//            log.info(k + " " + level.get(i));
//            i++;
//        }
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

    private String getTime(LocalDateTime fromDateTime, LocalDateTime toDateTime) {

        LocalDateTime tempDateTime = LocalDateTime.from(fromDateTime);

        long years = tempDateTime.until(toDateTime, ChronoUnit.YEARS);
        tempDateTime = tempDateTime.plusYears(years);

        long months = tempDateTime.until(toDateTime, ChronoUnit.MONTHS);
        tempDateTime = tempDateTime.plusMonths(months);

        long days = tempDateTime.until(toDateTime, ChronoUnit.DAYS);
        tempDateTime = tempDateTime.plusDays(days);


        long hours = tempDateTime.until(toDateTime, ChronoUnit.HOURS);
        tempDateTime = tempDateTime.plusHours(hours);

//        long minutes = tempDateTime.until(toDateTime, ChronoUnit.MINUTES);
//        tempDateTime = tempDateTime.plusMinutes(minutes);

        long seconds = tempDateTime.until(toDateTime, ChronoUnit.SECONDS);
        tempDateTime = tempDateTime.plusSeconds(seconds);

        long milisecond = tempDateTime.until(toDateTime, ChronoUnit.MILLIS);

        return
                seconds + "." +
                        milisecond;
    }
}
