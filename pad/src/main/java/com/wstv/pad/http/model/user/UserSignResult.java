package com.wstv.pad.http.model.user;

import com.google.gson.annotations.SerializedName;
import com.wstv.pad.http.model.EmptyCamResult;

/**
 * <p>Description: </p>
 * UserSignResult
 *
 * @author lilibin
 * @createDate 2019/3/28 13:24
 */

public class UserSignResult extends EmptyCamResult {
    @SerializedName("sdkAppID")
    public int sdkAppID;
    @SerializedName("accType")
    public String accType;
    @SerializedName("userID")
    public String userID;
    @SerializedName("userSig")
    public String userSig;
}
