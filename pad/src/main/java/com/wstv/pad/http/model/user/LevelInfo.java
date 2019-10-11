package com.wstv.pad.http.model.user;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kindred on 2019/5/3.
 */

public class LevelInfo {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("minEx")
    public int minEx;
    @SerializedName("maxEx")
    public int maxEx;
    @SerializedName("sort")
    public int sort;
    @SerializedName("imageUrl")
    public String imageUrl;
}
