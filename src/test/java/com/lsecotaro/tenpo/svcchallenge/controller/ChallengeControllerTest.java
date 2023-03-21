package com.lsecotaro.tenpo.svcchallenge.controller;

import com.lsecotaro.tenpo.svcchallenge.model.SumRequest;
import com.lsecotaro.tenpo.svcchallenge.service.ChallengeService;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class ChallengeControllerTest {
    private ChallengeController challengeController;
    private ChallengeService challengeService;

    @Before
    public void setUp() {
        challengeService = mock(ChallengeService.class);
        challengeController = new ChallengeController(challengeService);
    }

    @Test
    public void calc() {
        challengeController.calc(SumRequest.builder()
                        .valuesToOperate(Arrays.asList(BigDecimal.valueOf(5.0), BigDecimal.valueOf(5.0)))
                        .build());
        verify(challengeService).calc(anyList());
    }

    @Test
    public void historical() {
        challengeController.historical(0, 10);
        verify(challengeService).getHistorical(eq(0), eq(10));
    }
}