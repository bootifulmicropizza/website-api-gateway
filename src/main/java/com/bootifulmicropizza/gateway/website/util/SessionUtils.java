package com.bootifulmicropizza.gateway.website.util;

import com.bootifulmicropizza.gateway.website.session.BasketSessionStore;
import com.bootifulmicropizza.gateway.website.session.UserContext;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpSession;

public final class SessionUtils {

    private static final String USER_CONTEXT_SESSION_ATTRIBUTE = "USER_CONTEXT";

    private static final String BASKET_SESSION_ATTRIBUTE = "USER_BASKET";

    public static UserContext getUserContext(final HttpSession session) {
        UserContext userContext = (UserContext) session.getAttribute(USER_CONTEXT_SESSION_ATTRIBUTE);

        if (userContext == null) {
            userContext = new UserContext();
            session.setAttribute(USER_CONTEXT_SESSION_ATTRIBUTE, userContext);
        }

        return userContext;
    }

    public static String getUserToken(final HttpSession session) {
        final UserContext userContext = getUserContext(session);

        if (StringUtils.isEmpty(userContext.getToken())) {
            return "";
        }

        return userContext.getToken();
    }

    public static void setUserContext(final UserContext userContext, final HttpSession session) {
        session.setAttribute(USER_CONTEXT_SESSION_ATTRIBUTE, userContext);
    }

    public static BasketSessionStore getBasket(final HttpSession session) {
        BasketSessionStore basket = (BasketSessionStore) session.getAttribute(BASKET_SESSION_ATTRIBUTE);

        if (basket == null) {
            basket = new BasketSessionStore();
            session.setAttribute(BASKET_SESSION_ATTRIBUTE, basket);
        }

        return basket;
    }

    public static void updateBasket(final BasketSessionStore basket, final HttpSession session) {
        session.setAttribute(BASKET_SESSION_ATTRIBUTE, basket);
    }
}
