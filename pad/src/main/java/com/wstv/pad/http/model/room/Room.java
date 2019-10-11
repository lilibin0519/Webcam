package com.wstv.pad.http.model.room;

import com.google.gson.annotations.SerializedName;
import com.wstv.pad.http.model.EmptyCamResult;
import com.wstv.pad.http.model.Pusher;
import com.wstv.pad.http.model.audience.Audience;
import com.wstv.pad.tencent.roomutil.commondef.PusherInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Kindred on 2019/3/15.
 */

public class Room extends EmptyCamResult {
    @SerializedName("actived")
    public boolean actived;
    @SerializedName("audiences")
    public ArrayList<Audience> audiences;
    @SerializedName("audiencesCnt")
    public int audiencesCnt;
    @SerializedName("customInfo")
    public String customInfo;
    @SerializedName("mixedPlayURL")
    public String mixedPlayURL;
    @SerializedName("pushers")
    public List<PusherInfo> pushers;
    @SerializedName("pushersCnt")
    public int pushersCnt;
    @SerializedName("pushersMap")
    public Map<String, Pusher> pushersMap;
    @SerializedName("roomCreator")
    public String roomCreator;
    @SerializedName("roomID")
    public String roomID;
    @SerializedName("roomInfo")
    public String roomInfo;
    @SerializedName("timestamp")
    public int timestamp;
    @SerializedName("roomName")
    public String roomName;
}
