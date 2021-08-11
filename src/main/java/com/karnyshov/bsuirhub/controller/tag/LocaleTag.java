package com.karnyshov.bsuirhub.controller.tag;

import jakarta.servlet.http.HttpSession;
import jakarta.servlet.jsp.JspException;
import jakarta.servlet.jsp.JspWriter;
import jakarta.servlet.jsp.tagext.TagSupport;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static com.karnyshov.bsuirhub.controller.command.SessionAttribute.LOCALE;

public class LocaleTag extends TagSupport {
    private static final String BUNDLE_NAME = "locale";
    private static final String DEFAULT_LOCALE = "en";
    private String key;

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int doStartTag() throws JspException {
        HttpSession session = pageContext.getSession();
        String localeCode = (String) session.getAttribute(LOCALE);
        ResourceBundle bundle = ResourceBundle.getBundle(BUNDLE_NAME,
                new Locale(localeCode != null ? localeCode : DEFAULT_LOCALE));
        String localizedValue = bundle.getString(key);

        try {
            JspWriter out = pageContext.getOut();
            out.write(localizedValue);
        } catch (IOException e) {
            throw new JspException("An error occurred processing tag: ", e);
        }

        return SKIP_BODY;
    }

    @Override
    public int doEndTag() {
        return EVAL_PAGE;
    }
}
