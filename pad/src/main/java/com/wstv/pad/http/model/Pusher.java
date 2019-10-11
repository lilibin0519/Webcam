package com.wstv.pad.http.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 推流者
 */

public class Pusher implements Serializable {
    @SerializedName("accelerateURL")
    public String accelerateURL;
    @SerializedName("pushURL")
    public String pushURL;
    @SerializedName("streamID")
    public String streamID;
    @SerializedName("timestamp")
    public int timestamp;
    @SerializedName("userAvatar")
    public String userAvatar;
    @SerializedName("userID")
    public String userID;
    @SerializedName("userName")
    public String userName;
}
