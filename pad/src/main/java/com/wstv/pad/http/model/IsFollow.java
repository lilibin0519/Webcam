package com.wstv.pad.http.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kindred on 2019/5/23.
 */

public class IsFollow {
    @SerializedName("ifAttention")
    public String ifAttention;
    @SerializedName("ifGuard")
    public String ifGuard;
    @SerializedName("guardType")
    public String guardType;
}
