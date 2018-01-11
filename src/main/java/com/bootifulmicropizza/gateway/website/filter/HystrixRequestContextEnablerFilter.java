package com.bootifulmicropizza.gateway.website.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.io.IOException;

/**
 * ServletFilter for initializing HystrixRequestContext at the beginning of an HTTP request and shutting down at the
 * end:
 * <p>
 * The filter shuts down the HystrixRequestContext at the end of the request to avoid
 * leakage into subsequent requests.
 */
public class HystrixRequestContextEnablerFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(HystrixRequestContextEnablerFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        LOGGER.error("IANTEST-2 doFilter, init context");

        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            chain.doFilter(request, response);
        } finally {
            LOGGER.error("IANTEST-3 doFilter, shutdown context");
            context.shutdown();
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}