package com.wstv.pad.http.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * <p>Description: </p>
 * GetRoomListRequest
 *
 * @author lilibin
 * @createDate 2019/3/28 13:59
 */

public class GetRoomListRequest {

    public GetRoomListRequest(){

    }

    public GetRoomListRequest(int currentPage, int pageSize){
        this.currentPage = currentPage;
        this.pageSize = pageSize;
    }

    public GetRoomListRequest(int currentPage, int pageSize, String type){
        this(currentPage, pageSize);
        roomType = type;
    }

    @SerializedName("cnt")
    public int pageSize;

    @SerializedName("index")
    public int currentPage;

    @SerializedName("roomType")
    public String roomType;
}
