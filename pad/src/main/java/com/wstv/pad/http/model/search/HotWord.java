package com.wstv.pad.http.model.search;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kindred on 2019/5/3.
 */

public class HotWord {
    @SerializedName("id")
    public String id;
    @SerializedName("content")
    public String content;
    @SerializedName("count")
    public int count;
    @SerializedName("isAdmin")
    public int isAdmin;
    @SerializedName("status")
    public int status;
}
