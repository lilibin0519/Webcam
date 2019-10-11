package com.wstv.pad.http.model.guard;

import com.google.gson.annotations.SerializedName;

/**
 * <p>Description: </p>
 * GuardGood
 *
 * @author lilibin
 * @createDate 2019/5/23 10:46
 */

public class GuardGood {
    @SerializedName("id")
    public String id;
    @SerializedName("name")
    public String name;
    @SerializedName("showCoin")
    public String showCoin;
    @SerializedName("imageUrl")
    public String imageUrl;
    @SerializedName("describe")
    public String describe;
}
