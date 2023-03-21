package com.lsecotaro.tenpo.svcchallenge.clients;

import com.lsecotaro.tenpo.svcchallenge.util.DateHelper;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MockPercentageClientTest {
    private static final int HUNDRED = 100;
    public static final int EXPIRATION_MINUTES = 30;
    private PercentageClient percentageClient;
    private Random random;
    private DateHelper dateHelper;

    @Before
    public void setUp() {
        random = mock(Random.class);
        dateHelper = mock(DateHelper.class);
        percentageClient = new MockPercentageClient(random, dateHelper);
    }

    @Test
    public void getPercentage() {
        Random realRandom= new Random();
        realRandom.ints();
        when(random.nextInt(eq(HUNDRED))).thenReturn(realRandom.nextInt(90));
        when(random.nextInt(eq(EXPIRATION_MINUTES))).thenReturn(realRandom.nextInt(EXPIRATION_MINUTES));
        BigDecimal percentage = percentageClient.getPercentage().getPercentage();
        assertTrue(percentage.compareTo(BigDecimal.ZERO) >= 0 && percentage.compareTo(BigDecimal.ONE) < 0);
    }

    @Test(expected = RuntimeException.class)
    public void getPercentageException() {
        Random realRandom= new Random();
        realRandom.ints();
        when(random.nextInt(eq(HUNDRED))).thenReturn(91);
        BigDecimal percentage = percentageClient.getPercentage().getPercentage();
        assertTrue(percentage.compareTo(BigDecimal.ZERO) >= 0 && percentage.compareTo(BigDecimal.ONE) < 0);
    }
}