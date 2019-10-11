package com.wstv.pad.http.model.action;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Kindred on 2019/5/17.
 */

public class VideoBean implements Serializable {
    @SerializedName("id")
    public String id;
    @SerializedName("title")
    public String title;
    @SerializedName("url")
    public String url;
    @SerializedName("posterUrl")
    public String posterUrl;
    @SerializedName("praise")
    public int praise;
    @SerializedName("comment")
    public int comment;
    @SerializedName("share")
    public int share;
    @SerializedName("views")
    public int views;
    @SerializedName("status")
    public int status;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("createUser")
    public String createUser;
    @SerializedName("isMyLove")
    public String isMyLove;
}
