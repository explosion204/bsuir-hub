package com.karnyshov.bsuirhub.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class UrlStringBuilder {
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
            url.append("?");
        }

        Iterator<Map.Entry<String, Object>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            url.append(key).append("=").append(value);

            if (iterator.hasNext()) {
                url.append("&");
            }
        }

        return url.toString();
    }
}
