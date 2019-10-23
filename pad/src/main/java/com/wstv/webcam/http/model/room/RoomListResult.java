package com.wstv.webcam.http.model.room;

import com.google.gson.annotations.SerializedName;
import com.wstv.webcam.http.model.EmptyCamResult;
import com.wstv.webcam.tencent.roomutil.commondef.RoomInfo;

import java.util.List;

/**
 * Created by Kindred on 2019/3/15.
 */

public class RoomListResult extends EmptyCamResult {

    @SerializedName("rooms")
    public List<RoomInfo> rooms;
}
