package com.wstv.pad.http.model.user;

import com.google.gson.annotations.SerializedName;
import com.wstv.pad.http.model.EmptyCamResult;

/**
 * <p>Description: </p>
 * CamUserInfo
 *
 * @author lilibin
 * @createDate 2019/3/26 13:50
 */

public class CamUserInfo extends EmptyCamResult {
    @SerializedName("sdkAppID")
    public int sdkAppID;
    @SerializedName("accType")
    public String accType;
    @SerializedName("userID")
    public String userID;
    @SerializedName("userSig")
    public String userSig;
}
