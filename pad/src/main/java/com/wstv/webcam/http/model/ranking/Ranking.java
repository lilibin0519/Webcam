package com.wstv.webcam.http.model.ranking;

import com.google.gson.annotations.SerializedName;
import com.wstv.webcam.http.model.Performer;

/**
 * Created by Kindred on 2019/5/12.
 */

public class Ranking {
    @SerializedName("id")
    public String id;
    @SerializedName("userId")
    public String userId;
    @SerializedName("showId")
    public int showId;
    @SerializedName("coins")
    public int coins;
    @SerializedName("cycleId")
    public String cycleId;
    @SerializedName("type")
    public String type;
    @SerializedName("userType")
    public String userType;
    @SerializedName("user")
    public Performer user;
}
