package com.lsecotaro.tenpo.svcchallenge.clients;

import com.lsecotaro.tenpo.svcchallenge.exceptions.ExternalServiceException;
import com.lsecotaro.tenpo.svcchallenge.model.PercentageResponse;
import com.lsecotaro.tenpo.svcchallenge.util.DateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

@Component
@Slf4j
public class MockPercentageClient implements PercentageClient{
    private static final BigDecimal EXCEPTION_THRESHOLD = BigDecimal.valueOf(0.70);
    private static final int HUNDRED = 100;
    public static final int EXPIRATION_MINUTES = 30;
    private final Random random;
    private final DateHelper dateHelper;

    @Autowired
    public MockPercentageClient(Random random, DateHelper dateHelper) {
        this.random = random;
        this.dateHelper = dateHelper;
    }

    @Override
    public PercentageResponse getPercentage() {
        BigDecimal percentage = BigDecimal.valueOf(random.nextInt(HUNDRED)).divide(BigDecimal.valueOf(HUNDRED));
        log.info("% {}", percentage);
        if (isGreaterThan(percentage, EXCEPTION_THRESHOLD)) {
            throw new ExternalServiceException();
        }
        return PercentageResponse.builder()
                .percentage(percentage)
                .lastUpdated(dateHelper.addMinutesToDate(new Date(), -random.nextInt(EXPIRATION_MINUTES)))
                .build();
    }

    private boolean isGreaterThan(BigDecimal first, BigDecimal second) {
        return first.compareTo(second) > 0;
    }
}
