package com.fluentest;

import com.fluentest.util.Logger;

import java.util.Hashtable;
import java.util.Map;

public class TestContext implements ITestContext {

    private final Map<String, Object> attributes = new Hashtable<>();

    @Override
    public void logI(String message, Object... args) {
        Logger.info(getInvokerClass(), message, args);
    }

    @Override
    public void logE(String message, Object... args) {
        Logger.error(getInvokerClass(), message, args);
    }

    @Override
    public void logW(String message, Object... args) {
        Logger.warn(getInvokerClass(), message, args);
    }

    @Override
    public void put(String key, Object value) {
        this.attributes.put(key, value);
    }

    @Override
    public <T> T get(String key) {
        return (T) this.attributes.get(key);
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
