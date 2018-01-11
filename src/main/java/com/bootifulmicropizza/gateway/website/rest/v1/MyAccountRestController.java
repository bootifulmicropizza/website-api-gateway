package com.bootifulmicropizza.gateway.website.rest.v1;

import com.bootifulmicropizza.gateway.website.model.Catalogue;
import com.bootifulmicropizza.gateway.website.session.UserContext;
import com.bootifulmicropizza.gateway.website.util.SessionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "/api/v1/myaccount", consumes = "application/json", produces = "application/json")
public class MyAccountRestController {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyAccountRestController.class);

    @Autowired
    private HttpSession session;

    @RequestMapping(value = "/summary", method = RequestMethod.GET)
    public ResponseEntity<UserContext> createCatalogue(@RequestBody final Catalogue catalogue) {

        final UserContext userContext = SessionUtils.getUserContext(session);

        return ResponseEntity.ok(userContext);
    }
}
