package com.lsecotaro.tenpo.svcchallenge.service;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class JedisCacheServiceTest {
    private static final String CURRENT_PERCENTAGE_KEY = "CURRENT_PERCENTAGE";
    private static final String LAST_PERCENTAGE_KEY = "CURRENT_PERCENTAGE";
    private static final double DELTA = 0.0001;
    private JedisCacheService jedisCacheService;
    private JedisPool jedisPool;
    private Jedis jedis;

    @Before
    public void setUp() {
        jedisPool = mock(JedisPool.class);
        jedis = mock(Jedis.class);
        jedisCacheService = new JedisCacheService(jedisPool);
        when(jedisPool.getResource()).thenReturn(jedis);
    }

    @Test
    public void getPercentage() {
        String value = "0.2";
        when(jedis.get(anyString())).thenReturn(value);
        Optional<BigDecimal> optionalPercentage = jedisCacheService.getPercentage();
        verify(jedisPool).getResource();
        verify(jedis).get(eq(CURRENT_PERCENTAGE_KEY));
        assertEquals(new BigDecimal(value).doubleValue(), optionalPercentage.get().doubleValue(), DELTA);
    }

    @Test
    public void getPercentageNotFound() {
        when(jedis.get(anyString())).thenReturn(null);
        Optional<BigDecimal> optionalPercentage = jedisCacheService.getPercentage();
        verify(jedisPool).getResource();
        verify(jedis).get(eq(CURRENT_PERCENTAGE_KEY));
        assertTrue(optionalPercentage.isEmpty());
    }

    @Test
    public void getLastPercentage() {
        String value = "0.4";
        when(jedis.get(anyString())).thenReturn(value);
        Optional<BigDecimal> optionalPercentage = jedisCacheService.getLastPercentage();
        verify(jedisPool).getResource();
        verify(jedis).get(eq(LAST_PERCENTAGE_KEY));
        assertEquals(new BigDecimal(value).doubleValue(), optionalPercentage.get().doubleValue(), DELTA);
    }

    @Test
    public void getLastPercentageNotFound() {
        when(jedis.get(anyString())).thenReturn(null);
        Optional<BigDecimal> optionalPercentage = jedisCacheService.getPercentage();
        verify(jedisPool).getResource();
        verify(jedis).get(eq(LAST_PERCENTAGE_KEY));
        assertTrue(optionalPercentage.isEmpty());
    }

    @Test
    public void setPercentage() {
        jedisCacheService.setPercentage(BigDecimal.ONE, 1800);
        verify(jedis, times(2)).setex(anyString(), anyInt(), anyString());
    }
}