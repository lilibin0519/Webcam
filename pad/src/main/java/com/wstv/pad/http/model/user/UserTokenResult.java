package com.wstv.pad.http.model.user;

import com.google.gson.annotations.SerializedName;
import com.wstv.pad.http.model.EmptyCamResult;

/**
 * <p>Description: </p>
 * UserTokenResult
 *
 * @author lilibin
 * @createDate 2019/3/28 13:44
 */

public class UserTokenResult extends EmptyCamResult {
    @SerializedName("token")
    public String token;
    @SerializedName("userID")
    public String userID;
}
