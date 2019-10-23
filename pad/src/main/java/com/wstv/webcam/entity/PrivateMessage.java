package com.wstv.webcam.entity;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * <p>Description: </p>
 * PrivateMessage
 *
 * @author lilibin
 * @createDate 2019/5/21 13:15
 */

public class PrivateMessage extends RealmObject {
    @PrimaryKey
    public int id;
    @SerializedName("type")
    public int type;
    @SerializedName("userID")
    public String userID;
    @SerializedName("userName")
    public String userName;
    @SerializedName("userAvatar")
    public String userAvatar;
    @SerializedName("message")
    public String message;
    @SerializedName("createTime")
    public long createTime;
    @SerializedName("with")
    public String with = "";
    @SerializedName("withName")
    public String withName = "";
    @SerializedName("withAvatar")
    public String withAvatar = "";
    @SerializedName("success")
    public boolean success = false;

    @Override
    public boolean equals(Object obj) {
        if(this == obj)
            return true;
        if(obj == null)
            return false;
        if(getClass() != obj.getClass())
            return false;
        final PrivateMessage other = (PrivateMessage)obj;
        if(createTime != other.createTime){
            return false;
        }
        return true;
    }
}
