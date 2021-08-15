package com.karnyshov.bsuirhub.util;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * {@code UniqueValuesCache} singleton class is a simple cache implementation with manual adding and removing.
 * Internally based on {@link HashSet} object.
 * @author Dmitry Karnyshov
 */
public class UniqueValuesCache {
    private static final UniqueValuesCache instance = new UniqueValuesCache();
    private Set<Object> uniqueValues;

    private UniqueValuesCache() {
        uniqueValues = ConcurrentHashMap.newKeySet();
    }

    /**
     * Get instance of {@code UniqueValuesCache} class.
     *
     * @return {@code UniqueValuesCache} instance.
     */
    public static UniqueValuesCache getInstance() {
        return instance;
    }

    /**
     * Add value to the cache.
     *
     * @param value the value.
     * @return {@code true} if the value is added, {@code false} otherwise.
     */
    public boolean add(Object value) {
        return uniqueValues.add(value);
    }

    /**
     * Remove value from the cache.
     *
     * @param value the value.
     * @return {@code true} if the value is removed, {@code false} otherwise.
     */
    public boolean remove(Object value) {
        return uniqueValues.remove(value);
    }
}