package edu.ptit.da2020.config;

import edu.ptit.da2020.constant.BaseConstant;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "")
@Getter
@Setter
@Slf4j
public class AppConfig {

  private double baseSpeed;

  private Map<String, Double> trafficToSpeedMapping;

  @PostConstruct
  public void initSpeed() {
    trafficToSpeedMapping = new HashMap<>();
    trafficToSpeedMapping.put(BaseConstant.SPEED_HEAVY, baseSpeed * 0.125);
    trafficToSpeedMapping.put(BaseConstant.SPEED_MILD, baseSpeed * 0.375);
    trafficToSpeedMapping.put(BaseConstant.SPEED_SMOOTH, baseSpeed * 0.625);
    trafficToSpeedMapping.put(BaseConstant.SPEED_VERY_SMOOTH, baseSpeed);
  }

  @Bean
  public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(factory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    return redisTemplate;
  }
}
