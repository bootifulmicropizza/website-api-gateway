package com.bootifulmicropizza.gateway.website.rest.v1;

import com.bootifulmicropizza.gateway.website.model.Customer;
import com.bootifulmicropizza.gateway.website.rest.v1.request.LoginRequest;
import com.bootifulmicropizza.gateway.website.service.CustomerService;
import com.bootifulmicropizza.gateway.website.session.UserContext;
import com.bootifulmicropizza.gateway.website.util.SessionUtils;
import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.hateoas.Resource;
import org.springframework.http.*;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.net.URI;
import java.nio.charset.Charset;

@RestController
@RequestMapping(value = "/api/v1/auth", consumes = "application/json", produces = "application/json")
public class AuthRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthRestController.class);

    private static final String LOGIN_URL = "//account-service/oauth/token?grant_type=password&username=%s&password=%s";

    private static final String JOT_ACCESS_TOKEN = "access_token";

    private final RestTemplate restTemplate;

    private OAuth2RestTemplate oAuth2RestTemplate;

    private CustomerService customerService;

    public AuthRestController(@Qualifier("loadBalancedRestTemplate") @LoadBalanced RestTemplate restTemplate,
                              final OAuth2RestTemplate oAuth2RestTemplate,
                              final CustomerService customerService) {
        this.restTemplate = restTemplate;
        this.oAuth2RestTemplate = oAuth2RestTemplate;
        this.customerService = customerService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<UserContext> register(@RequestBody final Customer customer,
                                                final HttpSession session, final HttpServletRequest request) {

        final Resource<Customer> customerResource = customerService.createCustomer(customer);

        if (customerResource == null) {
            return ResponseEntity.badRequest().build();
        }

//        final ResponseEntity<User> registeredUser = oAuth2RestTemplate.postForEntity(CREATE_CUSTOMER_URL, customer, User.class);
//        if (!registeredUser.getStatusCode().equals(HttpStatus.CREATED)) {
//            LOGGER.error("Failed to register new user. HTTP code=[{}].", registeredUser.getStatusCodeValue());
//            return ResponseEntity.badRequest().build();
//        }

        // Now log the new user in
        final LoginRequest loginRequest = new LoginRequest(customer.getUsername(), customer.getPassword());
        return login(loginRequest, session, request);
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public ResponseEntity isLoggedIn(final HttpSession session, final HttpServletRequest request) {
        final UserContext userContext = SessionUtils.getUserContext(session);

        return ResponseEntity.ok(userContext);
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<UserContext> login(@RequestBody final LoginRequest loginRequest, final HttpSession session,
                                        final HttpServletRequest request) {

        final UserContext userContext = SessionUtils.getUserContext(session);

        URI uri = URI.create(String.format(LOGIN_URL, loginRequest.getUsername(), loginRequest.getPassword()));

        final RequestEntity<JsonNode> requestEntity = createRequestEntity(uri, HttpMethod.POST);

        try {
            final ResponseEntity<JsonNode> response = restTemplate.exchange(requestEntity, JsonNode.class);

            if (response.getStatusCode() == HttpStatus.OK) {
                final JsonNode accessToken = response.getBody().get(JOT_ACCESS_TOKEN);
                userContext.setToken(accessToken.asText());

                SessionUtils.setUserContext(userContext, session);

                return ResponseEntity.ok(userContext);
            }
        } catch (Exception ex) {
            LOGGER.debug("Unable to log in user.", ex);
            return ResponseEntity.badRequest().build();
        }

        // Access denied
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(userContext);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ResponseEntity<Void> logout(final HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok().build();
    }

    private RequestEntity<JsonNode> createRequestEntity(final URI uri, final HttpMethod method) {
        final HttpHeaders headers = createHeaders("website-client", "website-client-secret");

        return new RequestEntity<>(headers, method, uri);
    }

    private HttpHeaders createHeaders(String username, String password){
        return new HttpHeaders() {{
            String auth = username + ":" + password;
            byte[] encodedAuth = Base64.encodeBase64(
                auth.getBytes(Charset.forName("US-ASCII")));
            String authHeader = "Basic " + new String( encodedAuth );
            set( "Authorization", authHeader );
        }};
    }
}
