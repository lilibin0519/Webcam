package com.wstv.webcam.tencent.roomutil.commondef;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jac on 2017/10/30.
 */

public class PusherInfo implements Parcelable {

    /**
     * 用户ID
     */
    public String   userID;

    /**
     * 用户昵称
     */
    public String   userName;

    /**
     * 用户头像地址
     */
    public String   userAvatar;

    /**
     * 低时延拉流地址（带防盗链key）
     */
    public String   accelerateURL;

    /**
     * 低时延拉流地址（带防盗链key）
     */
    public String   pushURL;


    public PusherInfo() {

    }

    public PusherInfo(String userID, String userName, String userAvatar, String accelerateURL) {
        this.userID = userID;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.accelerateURL = accelerateURL;
    }

    public PusherInfo(String userID, String userName, String userAvatar, String accelerateURL, String pushURL) {
        this.userID = userID;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.pushURL = pushURL;
    }

    protected PusherInfo(Parcel in) {
        this.userID = in.readString();
        this.userName = in.readString();
        this.accelerateURL = in.readString();
        this.userAvatar = in.readString();
        this.pushURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userID);
        dest.writeString(this.userName);
        dest.writeString(this.accelerateURL);
        dest.writeString(this.userAvatar);
        dest.writeString(this.pushURL);
    }

    public static final Creator<PusherInfo> CREATOR = new Creator<PusherInfo>() {
        @Override
        public PusherInfo createFromParcel(Parcel source) {
            return new PusherInfo(source);
        }

        @Override
        public PusherInfo[] newArray(int size) {
            return new PusherInfo[size];
        }
    };

    @Override
    public int hashCode() {
        return userID.hashCode();
    }

    @Override
    public String toString() {
        return "PusherInfo{" +
                "userID='" + userID + '\'' +
                ", userName='" + userName + '\'' +
                ", accelerateURL='" + accelerateURL + '\'' +
                ", userAvatar='" + userAvatar + '\'' +
                ", pushURL='" + pushURL + '\'' +
                '}';
    }
}
