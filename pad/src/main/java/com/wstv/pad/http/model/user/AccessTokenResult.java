package com.wstv.pad.http.model.user;

import com.google.gson.annotations.SerializedName;
import com.wstv.pad.http.model.EmptyCamResult;

/**
 * <p>Description: </p>
 * AccessTokenResult
 *
 * @author lilibin
 * @createDate 2019/3/14 16:40
 */

public class AccessTokenResult extends EmptyCamResult {
    @SerializedName("token")
    public String token;
    @SerializedName("userID")
    public String userID;
}
