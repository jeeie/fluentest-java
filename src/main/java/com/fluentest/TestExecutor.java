package com.fluentest;

public abstract class TestExecutor implements Runnable {

    protected ExecutionStatus executionStatus = ExecutionStatus.Init;

    private Thread thread;
    private Throwable cause;

    protected synchronized void start(String threadName) {
        if (thread == null) {
            thread = new Thread(this, threadName);
            thread.setUncaughtExceptionHandler((t, e) -> setException(e));
            thread.start();
        }
    }


    public void join() {
        if (thread != null && thread.isAlive()) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                TestFailureException.throwException(e);
            }
        }
    }

    public void stop() {
        if (thread != null && thread.isAlive()) {
            thread.interrupt();
        }
    }

    public synchronized boolean isStarted() {
        return !executionStatus.equals(ExecutionStatus.Init);
    }

    public synchronized boolean isRunning() {
        return executionStatus == ExecutionStatus.Running;
    }

    public synchronized boolean isFinished() {
        return executionStatus == ExecutionStatus.Fail ||
                executionStatus == ExecutionStatus.Success ||
                executionStatus == ExecutionStatus.Incon;
    }

    public synchronized boolean isSuccess() {
        return executionStatus == ExecutionStatus.Success;
    }

    public synchronized ExecutionStatus getStatus() {
        return executionStatus;
    }

    public Throwable getException() {
        return cause;
    }

    protected void setException(Throwable cause) {
        this.cause = cause;
    }

}
