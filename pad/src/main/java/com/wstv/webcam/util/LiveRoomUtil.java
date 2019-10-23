package com.wstv.webcam.util;

import com.tencent.imsdk.TIMSdkConfig;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.tencent.liveroom.MLVBLiveRoom;
import com.wstv.webcam.tencent.liveroom.MLVBLiveRoomImpl;

/**
 * <p>Description: </p>
 * LiveRoomUtil
 *
 * @author lilibin
 * @createDate 2019/4/10 11:16
 */

public class LiveRoomUtil {

    private static LiveRoomUtil mInstance;

    private MLVBLiveRoomImpl liveRoom;

    private TIMSdkConfig mTIMSdkConfig;

    private LiveRoomUtil(BaseActivity activity){
        liveRoom = MLVBLiveRoom.sharedInstance(activity);
    }

    public static LiveRoomUtil getInstance(BaseActivity activity){
        if (null == mInstance) {
            mInstance = new LiveRoomUtil(activity);
        }
        return mInstance;
    }

    public void setToken(String token){
        liveRoom.setToken(token);
    }

//    public void setSelfAccountInfo(String userID, String userName, String headPicUrl, String userSig, String accType, long sdkAppID){
//        Trace.e("setSelfAccountInfo");
//        liveRoom.setSelfAccountInfo(userID, userName, headPicUrl, userSig, accType, sdkAppID);
//        liveRoom.loginIm(new LiveRoom.LoginCallback() {
//            @Override
//            public void onError(int errCode, String errInfo) {
//                Trace.e("loginIm errorCode : " + errCode + ", errInfo : " + errInfo);
//            }
//
//            @Override
//            public void onSuccess(String userId) {
//                Trace.e("loginIm onSuccess : " + userId);
//            }
//        });
//    }

    public MLVBLiveRoomImpl getLiveRoom(){
        return liveRoom;
    }
}
