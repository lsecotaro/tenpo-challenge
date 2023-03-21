package com.lsecotaro.tenpo.svcchallenge.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Objects;
import java.util.Optional;

@Service
public class JedisCacheService implements CacheService{
    private static final String CURRENT_PERCENTAGE_KEY = "CURRENT_PERCENTAGE";
    private static final String LAST_PERCENTAGE_KEY = "CURRENT_PERCENTAGE";
    private static final int SECOND_30 = 30 * 1000;
    private final JedisPool jedisPool;

    @Autowired
    public JedisCacheService(JedisPool jedisPool) {
        this.jedisPool = jedisPool;
    }

    @Override
    public Optional<BigDecimal> getPercentage() {
        return getValue(CURRENT_PERCENTAGE_KEY);
    }

    @Override
    public Optional<BigDecimal> getLastPercentage() {
        return getValue(LAST_PERCENTAGE_KEY);
    }

    @Override
    public void setPercentage(BigDecimal value, int ttl) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.setex(CURRENT_PERCENTAGE_KEY, ttl, value.toString());
            jedis.setex(LAST_PERCENTAGE_KEY, SECOND_30, value.toString());
        }
    }

    private Optional<BigDecimal> getValue(String key) {
        try (Jedis jedis = jedisPool.getResource()) {
            String value = jedis.get(key);
            if (!Objects.isNull(value)) {
                return Optional.of(new BigDecimal(value));
            }
        }
        return Optional.empty();
    }
}
