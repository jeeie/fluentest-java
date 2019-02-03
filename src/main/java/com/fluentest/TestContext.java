package com.fluentest;

public class TestContext<T> implements ITestContext<T> {

    private int repeatTimes = 1;
    private int numberOfRepeats = 1;

    @Override
    public void logI(String... message) {

    }

    @Override
    public void logE(String... message) {

    }

    @Override
    public void logW(String... message) {

    }

    @Override
    public int getRepeatTimes() {
        return repeatTimes;
    }

    public void setRepeatTimes(int repeatTimes) {
        this.repeatTimes = repeatTimes;
    }

    @Override
    public int getNumberOfRepeats() {
        return numberOfRepeats;
    }

    public void setNumberOfRepeats(int n) {
        this.numberOfRepeats = n;
    }

}
