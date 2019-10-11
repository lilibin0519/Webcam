package com.wstv.webcam.gift.bean;

/**
 * <p>Description: </p>
 * SendGiftBean
 *
 * @author lilibin
 * @createDate 2019/4/12 14:10
 */

public class SendGiftBean extends BaseGiftBean {

    /**
     * 用户id
     */
    private String userId;
    /**
     * 礼物id
     */
    private String giftId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 礼物名称
     */
    private String giftName;
    /**
     * 礼物本地图片也可以定义为远程url
     */
    private int giftImg;
    /**
     * 礼物持续时间
     */
    private long giftStayTime;

    /**
     * 单次礼物数目
     */
    private int giftSendSize = 1;

//    /**
//     * 礼物计数
//     */
//    private int giftCount;
//    /**
//     * 礼物刷新时间
//     */
//    private long latestRefreshTime;
//    /**
//     * 当前index
//     */
//    private int currentIndex;

    public SendGiftBean() {
    }

    public SendGiftBean(String userId, String giftId, String userName,String giftName,int giftImg,long time) {
        this.userId = userId;
        this.giftId = giftId;
        this.userName = userName;
        this.giftName = giftName;
        this.giftImg = giftImg;
        this.giftStayTime = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getGiftImg() {
        return giftImg;
    }

    public void setGiftImg(int giftImg) {
        this.giftImg = giftImg;
    }

    @Override
    public String getTheGiftId() {
        return giftId;
    }

    @Override
    public void setTheGiftId(String gid) {
        this.giftId = gid;
    }

    @Override
    public String getTheUserId() {
        return userId;
    }

    @Override
    public void setTheUserId(String uid) {
        this.userId = uid;
    }

    @Override
    public int getTheSendGiftSize() {
        return giftSendSize;
    }

    @Override
    public void setTheSendGiftSize(int size) {
        giftSendSize = size;
    }

    @Override
    public long getTheGiftStay() {
        return giftStayTime;
    }

    @Override
    public void setTheGiftStay(long stay) {
        giftStayTime = stay;
    }

}