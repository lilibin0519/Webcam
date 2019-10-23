package com.wstv.webcam.http.model.audience;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.libin.mylibrary.http.utils.GsonUtils;
import com.wstv.webcam.http.model.Performer;

import java.io.Serializable;

/**
 * 观众
 */
public class Audience extends Performer {
    @SerializedName("userID")
    public String userID;
    @SerializedName("userInfo")
    public String userInfo;
    @SerializedName("userAvatar")
    public String userAvatar;

    public UserInfo info;

    public UserInfo getInfo(){
        if (null == info && !TextUtils.isEmpty(userInfo)) {
            info = GsonUtils.gsonToBean(userInfo, UserInfo.class);
        }
        return info;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        final Audience other = (Audience)obj;
        if(TextUtils.isEmpty(userID) || !userID.equals(other.userID)){
            return false;
        }
        return true;
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
