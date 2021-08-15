package com.karnyshov.bsuirhub.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * {@code UrlStringBuilder} class is used for constructing URLs with parameters.
 * @author Dmitry Karnyshov
 */
public class UrlStringBuilder {
    private static final String PARAMS_START = "?";
    private static final String PARAMS_ASSIGNMENT = "=";
    private static final String PARAMS_DELIMITER = "&";

    private Map<String, Object> params = new HashMap<>();
    private StringBuilder url;

    /**
     * Instantiates a {@code UrlStringBuilder} class.
     *
     * @param url parameterless URL
     */
    public UrlStringBuilder(String url) {
        this.url = new StringBuilder(url);
    }

    /**
     * Add url parameter
     *
     * @param key the key.
     * @param value the value.
     * @return {@code UrlStringBuilder} instance.
     */
    public UrlStringBuilder addParam(String key, Object value) {
        params.put(key, value);
        return this;
    }

    /**
     * Build URL with parameters.
     *
     * @return ready URL.
     */
    public String build() {
        if (!params.isEmpty()) {
            url.append(PARAMS_START);
        }

        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            url.append(key).append(PARAMS_ASSIGNMENT).append(value);

            if (iterator.hasNext()) {
                url.append(PARAMS_DELIMITER);
            }
        }

        return url.toString();
    }

    /**
     * Build URL with parameters.
     *
     * @param terminalString query string that will be added to the URL end.
     * @return ready URL.
     */
    public String build(String terminalString) {
        return build() + (params.isEmpty() ? PARAMS_START : PARAMS_DELIMITER) + terminalString;
    }
}
