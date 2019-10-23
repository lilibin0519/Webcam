package com.wstv.webcam.http.model.follow;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kindred on 2019/5/3.
 */

public class Follow {
    @SerializedName("idol")
    public String idol;
    @SerializedName("nickname")
    public String nickname;
    @SerializedName("headPortrait")
    public String headPortrait;
    @SerializedName("level")
    public String level;
    @SerializedName("signature")
    public String signature;
    @SerializedName("sex")
    public String sex;
    @SerializedName("liveStatus")
    public String liveStatus;
    @SerializedName("showId")
    public String showId;
    @SerializedName("lastShowTime")
    public String lastShowTime;
}
