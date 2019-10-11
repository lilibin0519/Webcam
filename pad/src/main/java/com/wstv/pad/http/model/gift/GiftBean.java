package com.wstv.pad.http.model.gift;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.wstv.pad.gift.bean.BaseGiftBean;

import java.util.List;

/**
 * Created by Kindred on 2019/5/13.
 */

public class GiftBean extends BaseGiftBean {
    @SerializedName("id")
    public String id;
    @SerializedName("iconUrl")
    public String iconUrl;
    @SerializedName("name")
    public String name;
    @SerializedName("showCoin")
    public String showCoin;
    @SerializedName("giftComboDTOS")
    public List<GiftAnim> giftComboDTOS;
    @SerializedName("ifMoreThanOnce")
    public String ifMoreThanOnce;
    @SerializedName("animationUrl")
    public String animationUrl;
    @SerializedName("userAvatar")
    public String userAvatar;

    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 礼物持续时间
     */
    private long giftStayTime = 2700;

    /**
     * 单次礼物数目
     */
    private int giftSendSize = 1;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGiftName() {
        return name;
    }

    public void setGiftName(String giftName) {
        this.name = giftName;
    }

    public String getGiftImg() {
        return iconUrl;
    }

    public void setGiftImg(String giftImg) {
        this.iconUrl = giftImg;
    }

    @Override
    public String getTheGiftId() {
        return TextUtils.isEmpty(id) ? "0" : id;
    }

    @Override
    public void setTheGiftId(String gid) {
        this.id = gid;
    }

    @Override
    public String getTheUserId() {
        return userId;
    }

    @Override
    public void setTheUserId(String uid) {
        this.userId = uid;
    }

    @Override
    public int getTheSendGiftSize() {
        return giftSendSize;
    }

    @Override
    public void setTheSendGiftSize(int size) {
        giftSendSize = size;
    }

    @Override
    public long getTheGiftStay() {
        return giftStayTime;
    }

    @Override
    public void setTheGiftStay(long stay) {
        giftStayTime = stay;
    }
}
