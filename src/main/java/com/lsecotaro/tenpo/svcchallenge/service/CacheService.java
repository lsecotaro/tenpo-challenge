package com.lsecotaro.tenpo.svcchallenge.service;


import java.math.BigDecimal;
import java.time.Duration;
import java.util.Optional;

public interface CacheService {
    Optional<BigDecimal> getPercentage();
    Optional<BigDecimal> getLastPercentage();
    void setPercentage(BigDecimal value, int ttl);
}
