package com.lsecotaro.tenpo.svcchallenge.filter;

import com.lsecotaro.tenpo.svcchallenge.exceptions.RateLimitException;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class RateLimitInterceptorTest {
    private RateLimitInterceptor rateLimitInterceptor;
    private Bucket bucket;

    @Before
    public void setUp() {
        bucket = mock(Bucket.class);
        rateLimitInterceptor = new RateLimitInterceptor(bucket);
    }

    @SneakyThrows
    @Test
    public void preHandle() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Object handler = mock(Object.class);

        when(request.getRequestURI()). thenReturn("/calc");
        when(bucket.tryConsume(eq(1L))).thenReturn(true);
        boolean result = rateLimitInterceptor.preHandle(request, response, handler);
        assertTrue(result);
    }

    @SneakyThrows
    @Test(expected = RateLimitException.class)
    public void preHandleException() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Object handler = mock(Object.class);

        when(request.getRequestURI()). thenReturn("/calc");
        when(bucket.tryConsume(eq(1L))).thenReturn(false);
        rateLimitInterceptor.preHandle(request, response, handler);
    }

    @SneakyThrows
    @Test
    public void preHandleSwagger() {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        Object handler = mock(Object.class);

        when(bucket.tryConsume(eq(1L))).thenReturn(true);
        boolean result = rateLimitInterceptor.preHandle(request, response, handler);
        assertTrue(result);
    }
}