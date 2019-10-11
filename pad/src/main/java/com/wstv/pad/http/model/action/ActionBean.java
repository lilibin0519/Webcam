package com.wstv.pad.http.model.action;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Kindred on 2019/5/17.
 */

public class ActionBean implements Serializable {
    @SerializedName("id")
    public String id;
    @SerializedName("content")
    public String content;
    @SerializedName("images")
    public String images;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("likeCnt")
    public String likeCnt;
    @SerializedName("commentCnt")
    public String commentCnt;
    @SerializedName("ifLike")
    public boolean ifLike;
}
