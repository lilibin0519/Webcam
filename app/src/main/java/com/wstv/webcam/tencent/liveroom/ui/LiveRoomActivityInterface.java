package com.wstv.webcam.tencent.liveroom.ui;


import com.wstv.webcam.tencent.liveroom.MLVBLiveRoomImpl;

/**
 * Created by dennyfeng on 2017/11/22.
 */

public interface LiveRoomActivityInterface {
    MLVBLiveRoomImpl getLiveRoom();
    String   getSelfUserID();
    String   getSelfUserName();
    void     showGlobalLog(boolean enable);
    void     printGlobalLog(String format, Object... args);
    void     setTitle(String s);
}
