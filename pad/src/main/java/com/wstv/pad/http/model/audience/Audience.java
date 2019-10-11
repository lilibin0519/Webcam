package com.wstv.pad.http.model.audience;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.libin.mylibrary.http.utils.GsonUtils;
import com.wstv.pad.http.model.Performer;

import java.io.Serializable;

/**
 * 观众
 */
public class Audience extends Performer {
    @SerializedName("userID")
    public String userID;
    @SerializedName("userInfo")
    public String userInfo;

    public UserInfo info;

    public UserInfo getInfo(){
        if (null == info && !TextUtils.isEmpty(userInfo)) {
            info = GsonUtils.gsonToBean(userInfo, UserInfo.class);
        }
        return info;
    }

    public static class UserInfo implements Serializable {
        @SerializedName("userAvatar")
        public String userAvatar;
        @SerializedName("userName")
        public String userName;
        @SerializedName("gender")
        public String gender;
        @SerializedName("level")
        public int level;
    }
}
