package com.wstv.webcam.util.counter;

import android.os.CountDownTimer;

/**
 * Created by admin on 2016/5/30.
 */
public class MyCountDownTimer extends CountDownTimer {
    
    public static final long millisInFuture = 60 * 1000;
    
    public static final long countDownInterval = 1 * 1000;
    
    private CountDownTimerListener downTimerListener;

    public void setDownTimerListener(CountDownTimerListener downTimerListener) {
        this.downTimerListener = downTimerListener;
    }

    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    public MyCountDownTimer(long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {
        if(downTimerListener != null)
            downTimerListener.onTick(millisUntilFinished);
    }

    @Override
    public void onFinish() {
        cancel();
        if(downTimerListener != null)
            downTimerListener.onFinish();
    }
    
    
    
}
