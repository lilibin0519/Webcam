package com.wstv.webcam.http.model;

import com.google.gson.annotations.SerializedName;
import com.wstv.webcam.http.model.user.LevelInfo;
import com.wstv.webcam.http.model.user.UserBean;

/**
 * <p>Description: </p>
 * Performer
 *
 * @author lilibin
 * @createDate 2019/3/11 12:20
 */

public class Performer extends UserBean {
    @SerializedName("id")
    public String id;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("status")
    public int status;
    @SerializedName("approvalStatus")
    public int approvalStatus;
    @SerializedName("levelInfo")
    public LevelInfo levelInfo;
    @SerializedName("isfans")
    public boolean isfans;
    @SerializedName("streaming")
    public String streaming;
}
