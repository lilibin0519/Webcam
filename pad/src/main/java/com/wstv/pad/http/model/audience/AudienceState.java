package com.wstv.pad.http.model.audience;

import com.google.gson.annotations.SerializedName;

/**
 * 观众
 */
public class AudienceState {
    @SerializedName("manager")
    public int manager; // 是否是管理员 1：是；2：不是
    @SerializedName("state")
    public int state; // 是否是管理员 0：黑名单；1：禁言；-1：正常
    @SerializedName("endTime")
    public long endTime;
}
