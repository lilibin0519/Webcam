package com.wstv.webcam.entity;

import com.google.gson.annotations.SerializedName;

/**
 * <p>Description: </p>
 * CustomMsg
 *
 * @author lilibin
 * @createDate 2019/5/14 11:02
 */

public class CustomMsg {
    @SerializedName("type")
    public String type;
    @SerializedName("subType")
    public String subType;
    @SerializedName("data")
    public Data data;
    @SerializedName("user")
    public User user;

    public static class Data {
        @SerializedName("id")
        public String id;
        @SerializedName("amount")
        public String amount;
        @SerializedName("giftId")
        public String giftId;
        @SerializedName("giftUrl")
        public String giftUrl;
        @SerializedName("iconUrl")
        public String iconUrl;
        @SerializedName("giftCount")
        public String giftCount;
        @SerializedName("message")
        public String message;
        @SerializedName("giftAmount")
        public String giftAmount;
        @SerializedName("giftName")
        public String giftName;
        @SerializedName("time")
        public long time;
        @SerializedName("userId")
        public String userId;
    }

    public static class User {
        @SerializedName("level")
        public int level;
        @SerializedName("guard")
        public String guard;
        @SerializedName("sex")
        public String sex;
    }
}
