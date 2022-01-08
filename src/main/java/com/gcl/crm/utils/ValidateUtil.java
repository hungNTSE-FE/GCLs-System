package com.gcl.crm.utils;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ValidateUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidateUtil.class);

    public static boolean isNullOrEmpty(Object object) {
        boolean result = false;
        if (object == null) {
            return true;
        }

        if (object instanceof String) {
            if (((String) object).equalsIgnoreCase("")) {
                result = true;
            }
        }

        if (object instanceof List<?>) {
            if (((List<?>) object).size() == 0) {
                result = true;
            }
        }

        if (object instanceof Set<?>) {
            if (((Set<?>) object).size() == 0) {
                result = true;
            }
        }

        if (object instanceof Map<?, ?>) {
            if (((Map<?, ?>) object).size() == 0) {
                result = true;
            }
        }
        return result;
    }

    public static boolean isNotNullOrEmpty(Object object) {
        return !isNullOrEmpty(object);
    }

}
