package com.wstv.pad.http.model.account;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kindred on 2019/3/25.
 */

public class AccountLog {
    @SerializedName("coins")
    public String coins;
    @SerializedName("type")
    public String type;
    @SerializedName("createTime")
    public String createTime;
}
