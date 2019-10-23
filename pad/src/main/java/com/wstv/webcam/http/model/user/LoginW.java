package com.wstv.webcam.http.model.user;

import com.google.gson.annotations.SerializedName;

/**
 * <p>Description: </p>
 * LoginW
 *
 * @author lilibin
 * @createDate 2019/4/23 14:49
 */

public class LoginW {
    @SerializedName("access_token")
    public String access_token;
    @SerializedName("expires_in")
    public String expires_in;
    @SerializedName("refresh_token")
    public String refresh_token;
    @SerializedName("openid")
    public String openid;
    @SerializedName("scope")
    public String scope;
    @SerializedName("uniond")
    public String uniond;
    @SerializedName("errcode")
    public String errcode;
    @SerializedName("errmsg")
    public String errmsg;
}
