package com.wstv.webcam.http.model.request;

import com.google.gson.annotations.SerializedName;

/**
 * <p>Description: </p>
 * GetRoomRequest
 *
 * @author lilibin
 * @createDate 2019/5/25 11:29
 */

public class GetRoomRequest {

    @SerializedName("roomID")
    public String roomID;

    public GetRoomRequest(){
    }

    public GetRoomRequest(String roomID){
        this.roomID = roomID;
    }
}
