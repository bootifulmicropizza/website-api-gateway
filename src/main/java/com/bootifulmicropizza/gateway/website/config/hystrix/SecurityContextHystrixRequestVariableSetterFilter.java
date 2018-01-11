package com.bootifulmicropizza.gateway.website.config.hystrix;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import java.io.IOException;

public class SecurityContextHystrixRequestVariableSetterFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityContextHystrixRequestVariableSetterFilter.class);

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {

        LOGGER.error("IANTEST-4 doFilter - Setting security context on Hystric request variable.");

        SecurityContext context = SecurityContextHolder.getContext();

        LOGGER.error("IANTEST-4 doFilter - SEcurity context {}.", context);
        LOGGER.error("IANTEST-4 doFilter - SEcurity context authenication {}.", context.getAuthentication());
        LOGGER.error("IANTEST-4 doFilter - SEcurity context authenication details {}.", context.getAuthentication().getDetails());

        SecurityContextHystrixRequestVariable.getInstance().set(SecurityContextHolder.getContext());

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
