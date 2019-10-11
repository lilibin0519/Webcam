package com.wstv.pad.http.model.redpacket;

import com.google.gson.annotations.SerializedName;

/**
 * <p>Description: </p>
 * RedPacket
 *
 * @author lilibin
 * @createDate 2019/5/16 11:15
 */

public class RedPacket {
    @SerializedName("amount")
    public String amount;
    @SerializedName("status")
    public String status;
}
