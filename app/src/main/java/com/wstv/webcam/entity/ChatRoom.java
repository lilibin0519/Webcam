package com.wstv.webcam.entity;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

/**
 * <p>Description: </p>
 * ChatRoom
 *
 * @author lilibin
 * @createDate 2019/5/21 13:08
 */

public class ChatRoom extends RealmObject {
    @PrimaryKey
    public long id;
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
}
