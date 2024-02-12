package org.base.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(1)
@Slf4j
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String fullUri =
            request.getRequestURI() + (StringUtils.isEmpty(request.getQueryString()) ? "" :
                "?" + request.getQueryString());
        log.info("start request {} - [{}]", request.getMethod(), fullUri);

        // created request at
        filterChain.doFilter(request, response);
        //log method, status, body, url, headers, device id (optional), thời gian tạo (created at), trace id, span id
        log.info("done request [{}] with response status {}", fullUri,
            response.getStatus());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return "/".equals(request.getRequestURI()) || request.getRequestURI().contains("actuator");
    }

}
