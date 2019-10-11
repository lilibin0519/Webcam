package com.wstv.pad.http.model;

import com.google.gson.annotations.SerializedName;

/**
 * <p>Description: </p>
 * EmptyResult
 *
 * @author lilibin
 * @createDate 2019/3/14 10:14
 */

public class EmptyResult<T> {
    @SerializedName("result")
    public String result;
    @SerializedName("errmsg")
    public String errmsg;
    @SerializedName("detail")
    public T detail;
}
