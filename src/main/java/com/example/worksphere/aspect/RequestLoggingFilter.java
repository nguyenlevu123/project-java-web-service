//Ghi log thông tin cơ bản của mỗi HTTP request.
package com.example.worksphere.aspect;

import com.example.worksphere.util.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//Ghi log method, URI, status và thời gian xử lý request.
@Slf4j
@Component
public class RequestLoggingFilter extends OncePerRequestFilter {

    //Ghi log request sau khi chuỗi filter xử lý xong.
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        long startTime = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long durationMs = System.currentTimeMillis() - startTime;
            logHttpRequest(request, response, durationMs);
        }
    }

    //Ghi log HTTP mà không đọc header, body hoặc file upload.
    private void logHttpRequest(HttpServletRequest request, HttpServletResponse response, long durationMs) {
        log.info(
                "[HTTP_REQUEST] method={} uri={} status={} user={} durationMs={}",
                request.getMethod(),
                request.getRequestURI(),
                response.getStatus(),
                SecurityUtils.getCurrentUsernameOrAnonymous(),
                durationMs
        );
    }
}
