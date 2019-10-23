package com.wstv.webcam.http.model.recharge;

import com.google.gson.annotations.SerializedName;

/**
 * <p>Description: </p>
 * PayInfoDto
 *
 * @author lilibin
 * @createDate 2019/4/29 15:37
 */

public class PayInfoDto {
    @SerializedName("aliPayRecharge")
    public String aliPayRecharge;
    @SerializedName("wxRechargeDto")
    public PayInfo wxRechargeDto;
}
