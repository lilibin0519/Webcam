package com.wstv.webcam.http.model.gift;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <p>Description: </p>
 * GiftType
 *
 * @author lilibin
 * @createDate 2019/6/18 10:16
 */

public class GiftType {
    @SerializedName("type")
    public String type;
    @SerializedName("giftDTOList")
    public List<GiftBean> giftDTOList;
}
