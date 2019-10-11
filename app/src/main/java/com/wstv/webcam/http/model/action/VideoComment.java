package com.wstv.webcam.http.model.action;

import com.google.gson.annotations.SerializedName;
import com.wstv.webcam.http.model.Performer;

/**
 * <p>Description: </p>
 * VideoComment
 *
 * @author lilibin
 * @createDate 2019/6/5 16:06
 */

public class VideoComment {
    @SerializedName("id")
    public String id;
    @SerializedName("videoId")
    public String videoId;
    @SerializedName("comment")
    public String comment;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("createUser")
    public String createUser;
    @SerializedName("atUser")
    public String atUser;
    @SerializedName("praise")
    public int praise;
    @SerializedName("user")
    public Performer user;
}
