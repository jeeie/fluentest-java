package com.fluentest;

public interface ITestContext {

    void logI(String message, Object... args);

    void logE(String message, Object... args);

    void logW(String message, Object... args);

    void put(String key, Object value);

    <T>T get(String key);

}
