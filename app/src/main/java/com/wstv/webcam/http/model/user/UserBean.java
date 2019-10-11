package com.wstv.webcam.http.model.user;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * <p>Description: </p>
 * UserBean
 *
 * @author lilibin
 * @createDate 2019/3/8 13:28
 */

public class UserBean implements Serializable {
    @SerializedName("userId")
    public String userId;
    @SerializedName("part")
    public String part;
    @SerializedName("userName")
    public String userName;
    @SerializedName("nickname")
    public String nickname;
    @SerializedName("sex")
    public String sex;
    @SerializedName("age")
    public String age;
    @SerializedName("birthday")
    public String birthday;
    @SerializedName("signature")
    public String signature;
    @SerializedName("showId")
    public String showId;
    @SerializedName("level")
    public int level;
    @SerializedName("experience")
    public String experience;
    @SerializedName("headPortrait")
    public String headPortrait;
    @SerializedName("background")
    public String background;
    @SerializedName("fans")
    public String fans;
    @SerializedName("praise")
    public String praise;
    @SerializedName("lastShowTime")
    public String lastShowTime;
    @SerializedName("attentionCnt")
    public String attentionCnt;
    @SerializedName("openId")
    public String openId;
    @SerializedName("coins")
    public String coins;
}
