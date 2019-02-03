package com.fluentest.util;

import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.HashMap;

public class Logger {

    private static HashMap<Class<?>, org.slf4j.Logger> loggerTable = new HashMap<>();

    public static void info(Class<?> clazz, String message, Object... args) {
        getLogger(clazz).info(String.format(message, args));
    }

    public static void info(String message, Object... args) {
        info(getInvokerClass(), message, args);
    }

    public static void warn(Class<?> clazz, String message, Object... args) {
        getLogger(clazz).warn(String.format(message, args));
    }

    public static void warn(String message, Object... args) {
        warn(getInvokerClass(), message, args);
    }

    public static void error(Class<?> clazz, String message, Object... args) {
        getLogger(clazz).error(String.format(message, args));
    }

    public static void error(String message, Object... args) {
        error(getInvokerClass(), message, args);
    }

    private static org.slf4j.Logger getLogger(Class<?> clazz) {
        if (loggerTable.containsKey(clazz)) {
            return loggerTable.get(clazz);
        } else {
            org.slf4j.Logger logger = LoggerFactory.getLogger(clazz);
            loggerTable.put(clazz, logger);
            return logger;
        }
    }

    private static Class<?> getInvokerClass() {
        Class clazz = Logger.class;
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        if (elements.length > 4) {
            String className = elements[3].getClassName();
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return clazz;
    }

}
