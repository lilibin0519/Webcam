package com.wstv.webcam.http.model.room;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <p>Description: </p>
 * RoomState
 *
 * @author lilibin
 * @createDate 2019/5/7 15:28
 */

public class RoomState {
    // 20601 表示从来都没有直播过，0表示正在直播，其他表示未直播
    @SerializedName("ret")
    public int ret;
    @SerializedName("retcode")
    public int retcode;
    @SerializedName("errmsg")
    public String errmsg;
    @SerializedName("message")
    public String message;
    @SerializedName("output")
    public List<Output> output;

    public static class Output {
    }
}
