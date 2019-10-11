package com.wstv.pad.http.model.record;

import com.google.gson.annotations.SerializedName;
import com.wstv.pad.http.model.Performer;

/**
 * <p>Description: </p>
 * WatchRecord
 *
 * @author lilibin
 * @createDate 2019/5/5 14:59
 */

public class WatchRecord {
    @SerializedName("id")
    public String id;
    @SerializedName("showId")
    public int showId;
    @SerializedName("streamer")
    public Performer streamer;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("createUser")
    public String createUser;
}
