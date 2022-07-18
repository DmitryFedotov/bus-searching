package ru.belov.bussearching.utils;

import java.util.Collection;
import java.util.Map;

public final class EmptinessUtils {

    private EmptinessUtils() {
        //Utility
    }

    /**
     * Is argument empty?.
     * @param value value
     * @return is empty?
     */
    public static boolean isEmpty(Object value) {
        if (value == null) {
            return true;
        } else if (value instanceof String) {
            return ((String) value).isEmpty();
        } else if (value instanceof Collection) {
            return ((Collection) value).isEmpty();
        } else if (value instanceof Map) {
            return ((Map) value).isEmpty();
        } else {
            return false;
        }
    }

    /**
     * Is argument not empty?.
     * @param value value
     * @return is not empty?
     */
    public static boolean isNotEmpty(Object value){
        return !isEmpty(value);
    }
}
