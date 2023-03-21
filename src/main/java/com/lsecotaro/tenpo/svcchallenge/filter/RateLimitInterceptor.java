package com.lsecotaro.tenpo.svcchallenge.filter;

import com.lsecotaro.tenpo.svcchallenge.exceptions.RateLimitException;
import io.github.bucket4j.Bucket;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import static com.lsecotaro.tenpo.svcchallenge.filter.SwaggerHelper.isSwaggerPath;

@Component
@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {
    private static final int ONE_TOKEN = 1;
    private final Bucket bucket;

    @Autowired
    public RateLimitInterceptor(Bucket bucket) {
        this.bucket = bucket;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {

        log.info("[preHandle]" + "[" + request.getMethod()
                + "]" + request.getRequestURI() + " from " + getRemoteAddr(request));

        if (!isSwaggerPath(request.getRequestURI()) && !bucket.tryConsume(ONE_TOKEN)) {
            log.error("Rate Limit Exception!");
            throw new RateLimitException();
        }

        return true;
    }

    @Override
    public void afterCompletion(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler, Exception ex) throws Exception {
        if (ex != null){
            ex.printStackTrace();
        }
        log.info("[afterCompletion]" + "[exception: " + ex + "]");
    }

    private String getRemoteAddr(HttpServletRequest request) {
        String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            log.debug("ip from proxy - X-FORWARDED-FOR : " + ipFromHeader);
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }
}
