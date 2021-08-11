package com.karnyshov.bsuirhub.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UrlStringBuilder {
    private static final String PARAMS_START = "?";
    private static final String PARAMS_ASSIGNMENT = "=";
    private static final String PARAMS_DELIMITER = "&";

    private Map<String, Object> params = new HashMap<>();
    private StringBuilder url;

    public UrlStringBuilder(String url) {
        this.url = new StringBuilder(url);
    }

    public UrlStringBuilder addParam(String key, Object value) {
        params.put(key, value);
        return this;
    }

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

    public String build(String terminalString) {
        return build() + (params.isEmpty() ? PARAMS_START : PARAMS_DELIMITER) + terminalString;
    }
}
