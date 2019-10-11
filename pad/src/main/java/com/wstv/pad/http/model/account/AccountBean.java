package com.wstv.pad.http.model.account;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kindred on 2019/5/22.
 */

public class AccountBean {
    @SerializedName("id")
    public String id;
    @SerializedName("userId")
    public String userId;
    @SerializedName("coins")
    public int coins;
    @SerializedName("fee")
    public int fee;
    @SerializedName("withdrawAmount")
    public int withdrawAmount;
    @SerializedName("giftAmount")
    public int giftAmount;
    @SerializedName("incomeAmount")
    public int incomeAmount;
    @SerializedName("expendAmount")
    public int expendAmount;
    @SerializedName("createTime")
    public String createTime;
    @SerializedName("updatTime")
    public String updatTime;
    @SerializedName("remark")
    public String remark;
}
