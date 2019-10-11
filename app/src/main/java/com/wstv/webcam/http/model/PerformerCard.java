package com.wstv.webcam.http.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Kindred on 2019/5/22.
 */

public class PerformerCard {
    @SerializedName("headPortrait")
    public String headPortrait;
    @SerializedName("nickname")
    public String nickname;
    @SerializedName("showId")
    public String showId;
    @SerializedName("level")
    public String level;
    @SerializedName("sex")
    public String sex;
    @SerializedName("leftHeadPortrait")
    public String leftHeadPortrait;
    @SerializedName("rightHeadPortrait")
    public String rightHeadPortrait;
    @SerializedName("signature")
    public String signature;
    @SerializedName("ifAttention")
    public String ifAttention;
    @SerializedName("attentionCnt")
    public String attentionCnt;
    @SerializedName("fansCnt")
    public String fansCnt;
    @SerializedName("receiveCoinsSum")
    public String receiveCoinsSum;
    @SerializedName("giveCoinsSum")
    public String giveCoinsSum;
}
