package me.untaini.sharingmemo.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;

@Slf4j
@Order(0)
@Component
public class LoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        filterChain.doFilter(request, response);

        log.info("|[REQUEST] {} {}", request.getMethod(), request.getRequestURI());

        @Builder
        record RequestLogMessage (
                ContentCachingRequestWrapper requestWrapper
        ) {

            public String getLogMessage() {
                String jsonBody = new String(requestWrapper.getContentAsByteArray());

                return new StringBuilder()
                        .append(String.format("\n|[REQUEST] %s %s\n", requestWrapper.getMethod(), requestWrapper.getRequestURI()))
                        .append(String.format("|REQUEST_PARAM: %s\n", requestWrapper.getParameterMap()))
                        .append(String.format("|REQUEST_BODY: %s\n", jsonBody))
                        .toString();
            }
        }

        @Builder
        record ResponseLogMessage (
                ContentCachingResponseWrapper responseWrapper
        ) {

            public String getLogMessage() throws IOException {
                String jsonBody = new String(responseWrapper.getContentAsByteArray());
                responseWrapper.copyBodyToResponse();

                return new StringBuilder()
                        .append(String.format("|[RESPONSE] %d %s\n", responseWrapper.getStatus(), HttpStatus.valueOf(responseWrapper.getStatus())))
                        .append(String.format("|RESPONSE_BODY: %s\n", jsonBody))
                        .toString();
            }
        }
        log.info("|[RESPONSE] {}", HttpStatus.valueOf(response.getStatus()));
    }
 }
