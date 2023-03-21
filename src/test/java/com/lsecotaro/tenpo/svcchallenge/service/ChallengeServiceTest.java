package com.lsecotaro.tenpo.svcchallenge.service;

import com.lsecotaro.tenpo.svcchallenge.clients.PercentageClient;
import com.lsecotaro.tenpo.svcchallenge.db.model.DBRequestsHistorical;
import com.lsecotaro.tenpo.svcchallenge.db.repository.RequestsHistoricalPageableRepository;
import com.lsecotaro.tenpo.svcchallenge.exceptions.ExternalServiceException;
import com.lsecotaro.tenpo.svcchallenge.exceptions.PercentageNotFoundException;
import com.lsecotaro.tenpo.svcchallenge.model.PercentageResponse;
import com.lsecotaro.tenpo.svcchallenge.model.RequestHistoricalResponse;
import com.lsecotaro.tenpo.svcchallenge.model.SumResponse;
import com.lsecotaro.tenpo.svcchallenge.util.DateHelper;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ChallengeServiceTest {
    private static final double DELTA = 0.0001;
    private ChallengeService challengeService;
    private RequestsHistoricalPageableRepository repository;
    private PercentageClient percentageClient;
    private CacheService cacheService;

    @Before
    public void setUp()  {
        cacheService = mock(CacheService.class);
        percentageClient = mock(PercentageClient.class);
        repository = mock(RequestsHistoricalPageableRepository.class);
        challengeService = new ChallengeService(repository, percentageClient, cacheService, new DateHelper());
    }

    @Test
    public void calcFromCache() {
        when(cacheService.getPercentage()).thenReturn(Optional.of(BigDecimal.valueOf(0.1)));
        SumResponse response = challengeService.calc(Arrays.asList(BigDecimal.valueOf(5.0), BigDecimal.valueOf(5.0)));
        assertEquals(11.0, response.getResult().doubleValue(), DELTA);
    }

    @Test
    public void calcFromExternalService() {
        when(cacheService.getPercentage()).thenReturn(Optional.empty());
        when(percentageClient.getPercentage())
                .thenReturn(PercentageResponse.builder()
                        .percentage(BigDecimal.valueOf(0.1))
                        .lastUpdated(new Date())
                        .build());
        SumResponse response = challengeService.calc(Arrays.asList(BigDecimal.valueOf(5.0), BigDecimal.valueOf(5.0)));
        assertEquals(11.0, response.getResult().doubleValue(), DELTA);
    }

    @Test
    public void calcFromFallback() {
        when(cacheService.getPercentage()).thenReturn(Optional.empty());
        when(percentageClient.getPercentage())
                .thenThrow(new ExternalServiceException())
                .thenThrow(new ExternalServiceException())
                .thenThrow(new ExternalServiceException());
        when(cacheService.getLastPercentage()).thenReturn(Optional.of(BigDecimal.valueOf(0.1)));
        SumResponse response = challengeService.calc(Arrays.asList(BigDecimal.valueOf(5.0), BigDecimal.valueOf(5.0)));
        assertEquals(11.0, response.getResult().doubleValue(), DELTA);
    }

    @Test(expected = PercentageNotFoundException.class)
    public void calcException() {
        when(cacheService.getPercentage()).thenReturn(Optional.empty());
        when(percentageClient.getPercentage())
                .thenThrow(new ExternalServiceException())
                .thenThrow(new ExternalServiceException())
                .thenThrow(new ExternalServiceException());
        when(cacheService.getLastPercentage()).thenReturn(Optional.empty());
        challengeService.calc(Arrays.asList(BigDecimal.valueOf(5.0), BigDecimal.valueOf(5.0)));
    }

    @Test
    public void getHistorical() {
        final Page<DBRequestsHistorical> page = new PageImpl<>(Arrays.asList(DBRequestsHistorical.builder()
                .id(1)
                .date(new Date())
                .params("{\"valuesToOperate\": [20.0, 10.0]}")
                .endpoint("api/api-rest/calc")
                .response("{\"result\": 45.600}")
                .build()));
        when(repository.findAll((Pageable) any())).thenReturn(page);
        when(page.hasNext()).thenReturn(true);
        RequestHistoricalResponse response = challengeService.getHistorical(0, 10);
        assertEquals(1, response.getHistorical().size());
        assertTrue(response.hasMore());
    }
}