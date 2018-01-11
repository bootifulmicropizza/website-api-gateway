package com.bootifulmicropizza.gateway.website.config.hystrix;

import com.netflix.hystrix.HystrixInvokable;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import com.netflix.hystrix.strategy.executionhook.HystrixCommandExecutionHook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * A HystrixCommandExecutionHook that makes the Spring SecurityContext available during the execution of
 * HystrixCommands.
 * <p>
 * It extracts the SecurityContext from the SecurityContextHystrixRequestVariable and sets it on the
 * SecurityContextHolder.
 */
public class SecurityContextRegistratorCommandHook extends HystrixCommandExecutionHook {

    private static final Logger
        LOGGER = LoggerFactory.getLogger(SecurityContextRegistratorCommandHook.class);

    @Override
    public <T> void onExecutionStart(HystrixInvokable<T> commandInstance) {

        LOGGER.error("IANTEST-5 commandInstance tostring={}", commandInstance.toString());
        LOGGER.error("IANTEST-5 commandInstance class name={}", commandInstance.getClass().getName());

        SecurityContext context = SecurityContextHystrixRequestVariable.getInstance().get();

        LOGGER.error("IANTEST-5 onExecutionStart - Security context {}.", context);
        LOGGER.error("IANTEST-5 onExecutionStart - Security context authenication {}.", context.getAuthentication());
        LOGGER.error("IANTEST-5 onExecutionStart - Security context authenication details {}.", context.getAuthentication().getDetails());

        SecurityContextHolder.setContext(SecurityContextHystrixRequestVariable.getInstance().get());
    }

    @Override
    public <T> T onEmit(HystrixInvokable<T> commandInstance, T response) {
        LOGGER.error("IANTEST-5 command hook was a success. Clearing the context.");
//        SecurityContextHolder.clearContext();
        return response;
    }

    @Override
    public <T> Exception onError(HystrixInvokable<T> commandInstance, HystrixRuntimeException.FailureType failureType,
                                 Exception e) {
        LOGGER.error("IANTEST-5 An error has occurred during command hook.", e);
//        SecurityContextHolder.clearContext();
        return e;
    }
}