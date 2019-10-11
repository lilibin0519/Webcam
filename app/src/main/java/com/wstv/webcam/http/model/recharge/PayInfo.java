package com.wstv.webcam.http.model.recharge;

import com.google.gson.annotations.SerializedName;

/**
 * <p>Description: </p>
 * PayInfo
 *
 * @author lilibin
 * @createDate 2019/4/29 15:39
 */

public class PayInfo {
    @SerializedName("partnerid")
    public String partnerid;
    @SerializedName("prepayid")
    public String prepayid;
    @SerializedName("noncestr")
    public String noncestr;
    @SerializedName("timestamp")
    public String timestamp;
    @SerializedName("sign")
    public String sign;
    @SerializedName("packagevalue")
    public String packagevalue;
}
