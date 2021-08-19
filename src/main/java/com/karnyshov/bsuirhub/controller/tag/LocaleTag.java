package com.karnyshov.bsuirhub.controller.tag;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * {@code LocaleTag} class is a custom JSP tag used for JSP internationalization.
 * @author Dmitry Karnyshov
 */
public class LocaleTag extends TagSupport {
    private static final String BUNDLE_NAME = "lang";
    private static final String LOCALE_COOKIE_NAME = "localeCode";
    private String key;

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int doStartTag() throws JspException {
        HttpServletRequest httpRequest = (HttpServletRequest) pageContext.getRequest();
        Cookie[] cookies = httpRequest.getCookies();

        if (cookies != null) {
            Optional<Cookie> localeCookie = Arrays.stream(cookies)
                    .filter(cookie -> cookie.getName().equals(LOCALE_COOKIE_NAME))
                    .findFirst();

            if (localeCookie.isPresent()) {
                String localeCode = localeCookie.get().getValue();
                ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME, new Locale(localeCode));
                String localizedValue = bundle.getString(key);

                try {
                    JspWriter out = pageContext.getOut();
                    out.write(localizedValue);
                } catch (IOException e) {
                    throw new JspException("An error occurred processing tag: ", e);
                }
            }
        }

        return SKIP_BODY;
    }
}
