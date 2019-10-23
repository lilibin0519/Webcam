package com.wstv.webcam.http.model;

import com.google.gson.annotations.SerializedName;

/**
 * <p>Description: </p>
 * BannerBean
 *
 * @author lilibin
 * @createDate 2019/3/26 13:09
 */

public class BannerBean {
    @SerializedName("id")
    public String id;
    @SerializedName("flag")
    public String flag;
    @SerializedName("title")
    public String title;
    @SerializedName("url")
    public String url;
    @SerializedName("webEditor")
    public String webEditor;
    @SerializedName("image")
    public String image;
}
