package com.bootifulmicropizza.gateway.website;

import com.bootifulmicropizza.gateway.website.session.UserContext;
import com.bootifulmicropizza.gateway.website.util.SessionUtils;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

import javax.servlet.http.HttpSession;

public class MyRequestInterceptor implements RequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyRequestInterceptor.class);

    private HttpSession session;

    private OAuth2RestTemplate oAuth2RestTemplate;

    public MyRequestInterceptor(final HttpSession session, final OAuth2RestTemplate oAuth2RestTemplate) {
        this.session = session;
        this.oAuth2RestTemplate = oAuth2RestTemplate;
        LOGGER.info("IANTEST-1: Init MyRequestInterceptor with session=[{}], oAuth2RestTemplate=[{}].", session,
                    oAuth2RestTemplate);
    }

    @Override
    public void apply(RequestTemplate template) {
        LOGGER.error("IANTEST-1 requestTokenBearerInterceptor init for SESSION=[{}].", session.getId());

        final UserContext userContext = SessionUtils.getUserContext(session);

        if (!StringUtils.isEmpty(userContext.getToken())) {
            LOGGER.error("IANTEST-1 token=[{}].", userContext.getToken());
            template.header("Authorization", "bearer " + userContext.getToken());
        } else {
            // User token is empty so create token for website-gateway-service
            OAuth2AccessToken accessToken = oAuth2RestTemplate.getAccessToken();

            if (accessToken != null) {
                LOGGER.info("IANTEST-1 - access token: {}", accessToken.getValue());
                template.header("Authorization", "Bearer " + accessToken.getValue());
            } else {
                LOGGER.info("IANTEST-1 - no access token available.");
            }
        }
    }
}
