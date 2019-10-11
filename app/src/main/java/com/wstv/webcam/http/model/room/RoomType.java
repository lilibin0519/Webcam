package com.wstv.webcam.http.model.room;

import com.google.gson.annotations.SerializedName;

/**
 * <p>Description: </p>
 * RoomType
 *
 * @author lilibin
 * @createDate 2019/5/10 09:59
 */

public class RoomType {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("order")
    public String order;
}
