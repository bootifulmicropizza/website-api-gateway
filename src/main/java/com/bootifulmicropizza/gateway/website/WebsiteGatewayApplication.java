package com.bootifulmicropizza.gateway.website;

import com.bootifulmicropizza.gateway.website.config.hystrix.SecurityContextRegistratorCommandHook;
import com.netflix.hystrix.strategy.HystrixPlugins;
import feign.RequestInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import javax.servlet.http.HttpSession;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCircuitBreaker
@EnableFeignClients
@EnableCaching(proxyTargetClass = true)
@EnableScheduling
public class WebsiteGatewayApplication {

    public static void main(String[] args) {
        HystrixPlugins.getInstance().registerCommandExecutionHook(new SecurityContextRegistratorCommandHook());
        SpringApplication.run(WebsiteGatewayApplication.class, args);
    }

    @Bean
    public RequestInterceptor requestTokenBearerInterceptor(final HttpSession session,
                                                            final OAuth2RestTemplate oAuth2RestTemplate) {
        return new MyRequestInterceptor(session, oAuth2RestTemplate);
    }
}
