package com.lsecotaro.tenpo.svcchallenge.filter;

import com.lsecotaro.tenpo.svcchallenge.db.repository.RequestsHistoricalRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class RequestCachingFilterTest {
    private RequestCachingFilter filter;
    private RequestsHistoricalRepository repository;

    @Before
    public void setUp() {
        repository = mock(RequestsHistoricalRepository.class);
        filter = new RequestCachingFilter(repository);
    }

    @SneakyThrows
    @Test
    public void doFilterInternal() {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);

        filter.doFilterInternal(request, response, filterChain);
        verify(repository).save(any());
    }

    @SneakyThrows
    @Test
    public void doFilterInternalNoSuccessfully() {
        HttpServletRequest request = new MockHttpServletRequest();
        HttpServletResponse response = new MockHttpServletResponse();
        FilterChain filterChain = mock(FilterChain.class);
        response.setStatus(400);

        filter.doFilterInternal(request, response, filterChain);
        verify(repository, never()).save(any());
    }
}