package com.lsecotaro.tenpo.svcchallenge.service;

import com.lsecotaro.tenpo.svcchallenge.clients.PercentageClient;
import com.lsecotaro.tenpo.svcchallenge.db.model.DBRequestsHistorical;
import com.lsecotaro.tenpo.svcchallenge.db.repository.RequestsHistoricalPageableRepository;
import com.lsecotaro.tenpo.svcchallenge.exceptions.PercentageNotFoundException;
import com.lsecotaro.tenpo.svcchallenge.model.PercentageResponse;
import com.lsecotaro.tenpo.svcchallenge.model.RequestHistoricalResponse;
import com.lsecotaro.tenpo.svcchallenge.model.SumResponse;
import com.lsecotaro.tenpo.svcchallenge.util.DateHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ChallengeService {
    public static final int TIME_TO_EXPIRE_PERCENTAGE = 30;
    private final int MAX_RETRIES = 3;
    private final RequestsHistoricalPageableRepository historicalPageableRepository;
    private final PercentageClient percentageClient;
    private final CacheService cacheService;
    private final DateHelper dateHelper;

    @Autowired
    public ChallengeService(
            RequestsHistoricalPageableRepository historicalPageableRepository,
            PercentageClient percentageClient,
            CacheService cacheService,
            DateHelper dateHelper) {
        this.historicalPageableRepository = historicalPageableRepository;
        this.percentageClient = percentageClient;
        this.cacheService = cacheService;
        this.dateHelper = dateHelper;
    }


    public SumResponse calc(List<BigDecimal> valuesToOperate) {
        BigDecimal sum = valuesToOperate.stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal percentage = sum.multiply(getPercentage());

        return SumResponse.builder()
                .result(sum.add(percentage))
                .build();
    }

    public BigDecimal getPercentage() {
        Optional<BigDecimal> percentage = cacheService.getPercentage();
        if (percentage.isPresent()) {
            log.info("get percentage from cache");
            return percentage.get();
        }

        percentage = getPercentageFromExternalService();
        if (percentage.isPresent()) {
            log.info("get percentage from external service");
            return percentage.get();
        }
        return getFallBackPercentage();
    }


    public RequestHistoricalResponse getHistorical(Integer page, Integer limit) {
        Page<DBRequestsHistorical> pageResult = historicalPageableRepository.findAll(
                PageRequest.of(page, limit));
        return RequestHistoricalResponse.builder()
                .historical(pageResult.stream().toList())
                .hasMore(pageResult.hasNext())
                .build();
    }

    private Optional<BigDecimal> getPercentageFromExternalService() {
        for (int i=0; i < MAX_RETRIES; i++) {
            try {
                PercentageResponse response = percentageClient.getPercentage();
                cacheService.setPercentage(response.getPercentage(), (int) resolveTtl(response.getLastUpdated()));
                return Optional.of(response.getPercentage());
            } catch (RuntimeException e) {
                log.warn("trying to get percentage from external service: {}", i+1);
            }
        }
        return Optional.empty();
    }

    private long resolveTtl(Date lastUpdated) {
        Date now = new Date();
        Date expireAt = dateHelper.addMinutesToDate(lastUpdated, TIME_TO_EXPIRE_PERCENTAGE);
        long ttl = dateHelper.getDifferenceInSeconds(expireAt, now);
        log.info("now: {}; expire at: {}; ttl {}", now, expireAt, ttl);
        return ttl > 0 ? ttl : 0;
    }


    private BigDecimal getFallBackPercentage() {
        log.info("Fallback percentage");
        Optional<BigDecimal> optionalPercentage = cacheService.getLastPercentage();
        if (optionalPercentage.isEmpty()) {
            throw new PercentageNotFoundException();
        }

        return cacheService.getLastPercentage().get();
    }
}
