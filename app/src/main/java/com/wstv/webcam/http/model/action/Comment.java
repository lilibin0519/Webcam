package com.wstv.webcam.http.model.action;

import com.google.gson.annotations.SerializedName;
import com.wstv.webcam.http.model.Performer;

/**
 * <p>Description: </p>
 * Comment
 *
 * @author lilibin
 * @createDate 2019/5/24 10:28
 */

public class Comment {
    @SerializedName("id")
    public String id;
    @SerializedName("comment")
    public String comment;
    @SerializedName("createUser")
    public String createUser;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("callUser")
    public String callUser;
    @SerializedName("momentId")
    public String momentId;
    @SerializedName("status")
    public int status;
    @SerializedName("user")
    public Performer user;
}
