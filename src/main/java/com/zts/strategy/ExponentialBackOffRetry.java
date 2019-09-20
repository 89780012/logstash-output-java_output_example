package com.zts.strategy;

import java.util.Random;

/**
 * <p>Retry policy that retries a set number of times with increasing sleep time between retries</p>
 */
public class ExponentialBackOffRetry implements RetryPolicy {

    // 重试次数
    private Integer retry;
    // 重试时间
    private Long retrytime;

    public ExponentialBackOffRetry(Integer retry,Long retrytime){
        this.retry = retry;
        this.retrytime = retrytime;
    }

    @Override
    public boolean allowRetry(int retryCount) {
        if(retry == -1){
            return true;
        }
        if (retryCount < retry) {
            return true;
        }
        return false;
    }


    // 返回睡眠时间
    @Override
    public long getSleepTimeMs() {
        return retrytime;
    }
}