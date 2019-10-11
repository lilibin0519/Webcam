package com.wstv.webcam.util.counter;

/**
 * Created by admin on 2016/5/30.
 */
public interface CountDownTimerListener {
    
    void onTick(long millisUntilFinished);
    
    void onFinish();
    
}
