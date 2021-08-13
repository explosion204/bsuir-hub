package com.karnyshov.bsuirhub.util;

import java.util.HashSet;
import java.util.Set;

public class UniqueValuesCache {
    private static final UniqueValuesCache instance = new UniqueValuesCache();
    private Set<Object> uniqueValues;

    private UniqueValuesCache() {
        uniqueValues = new HashSet<>();
    }

    public static UniqueValuesCache getInstance() {
        return instance;
    }

    public boolean add(Object value) {
        return uniqueValues.add(value);
    }

    public void remove(Object value) {
        uniqueValues.remove(value);
    }
}