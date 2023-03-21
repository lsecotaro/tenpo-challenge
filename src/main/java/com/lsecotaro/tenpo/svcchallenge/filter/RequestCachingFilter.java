package com.lsecotaro.tenpo.svcchallenge.filter;

import com.lsecotaro.tenpo.svcchallenge.db.model.DBRequestsHistorical;
import com.lsecotaro.tenpo.svcchallenge.db.repository.RequestsHistoricalRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.lsecotaro.tenpo.svcchallenge.filter.SwaggerHelper.isSwaggerPath;

@Order(value = Ordered.HIGHEST_PRECEDENCE)
@Component
@WebFilter(filterName = "RequestCachingFilter", urlPatterns = "/*")
@Slf4j
public class RequestCachingFilter extends OncePerRequestFilter {
    private static final int PATH_LIMIT = 40;
    private final int FIELD_LIMIT = 60;
    private final RequestsHistoricalRepository repository;

    @Autowired
    public RequestCachingFilter(RequestsHistoricalRepository repository) {
        this.repository = repository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        CachedHttpServletRequest cachedHttpServletRequest = new CachedHttpServletRequest(request);
        String requestStr = IOUtils.toString(cachedHttpServletRequest.getInputStream(), StandardCharsets.UTF_8);

        ContentCachingResponseWrapper responseCacheWrapperObject = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(cachedHttpServletRequest, responseCacheWrapperObject);

        byte[] responseArray = responseCacheWrapperObject.getContentAsByteArray();
        String responseStr = new String(responseArray, responseCacheWrapperObject.getCharacterEncoding());

        if(!isSwaggerPath(request.getRequestURI()) && HttpStatus.valueOf(response.getStatus()).is2xxSuccessful()) {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            executorService.execute(PersistHistoricalTask.builder()
                    .requestsHistorical(DBRequestsHistorical.builder()
                            .date(new Date())
                            .params(truncateString(requestStr, FIELD_LIMIT))
                            .endpoint(truncateString(request.getRequestURI(), PATH_LIMIT))
                            .response(truncateString(responseStr, FIELD_LIMIT))
                            .build())
                    .historicalRepository(repository)
                    .build());
        }

        responseCacheWrapperObject.copyBodyToResponse();
    }

    private String truncateString(String str, int limit) {
        if(str.length() < limit) {
            return str;
        }
        return str.substring(0, limit);
    }
}
