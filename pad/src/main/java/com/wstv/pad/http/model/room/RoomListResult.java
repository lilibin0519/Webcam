package com.wstv.pad.http.model.room;

import com.google.gson.annotations.SerializedName;
import com.wstv.pad.http.model.EmptyCamResult;

import java.util.List;

/**
 * Created by Kindred on 2019/3/15.
 */

public class RoomListResult extends EmptyCamResult {

    @SerializedName("rooms")
    public List<Room> rooms;
}
