package com.wstv.pad;

/**
 * <p>Description: </p>
 * AppUrl
 *
 * @author lilibin
 * @createDate 2019/3/14 10:10
 */

public interface AppUrl {

    String LOGIN_OR_REGISTER = AppConstant.ROOT_URL + "loginByBaofang";

    String GET_CAM_SIGN = AppConstant.ROOT_URL + "weapp/utils/get_login_info";

    String GET_CAM_TOKEN = AppConstant.ROOT_URL + "weapp/live_room/login";

    String SEND_SMS = AppConstant.ROOT_URL + "sendSMS";

    String GET_ACCESS_TOKEN = AppConstant.ROOT_URL + "getAccessToken";

    String LOGIN_BY_W = AppConstant.ROOT_URL + "loginByWechat";

    String VERIFY_SMS = AppConstant.ROOT_URL + "verifySMS";

    String GET_ROOM_LIST = AppConstant.ROOT_URL + "weapp/live_room/get_room_list";

    String GET_ROOM_LIST_BY = AppConstant.ROOT_URL + "weapp/live_room/get_room_list_roomtype";

    String GET_BANNER = AppConstant.ROOT_URL + "banner";

    String GET_PAY_INFO = AppConstant.ROOT_URL + "pay";

    String GET_HOT_WORDS = AppConstant.ROOT_URL + "api/search/hotwords";

    String ON_SEARCH = AppConstant.ROOT_URL + "api/search/page";

    String FOLLOW = AppConstant.ROOT_URL + "attention";

    String CANCEL_FOLLOW = AppConstant.ROOT_URL + "cancelAttention";

    String FOLLOW_LIST = AppConstant.ROOT_URL + "attentionList";

    String WATCH_RECORD = AppConstant.ROOT_URL + "api/view/log/page";

    String GET_ROOM_STATE = AppConstant.ROOT_URL + "getStatus";

    String GET_USER_INFO = AppConstant.ROOT_URL + "getUserInfo";

    String GET_ROOM_TYPE = AppConstant.ROOT_URL + "getRoomTypeList";

    String GET_RANKING = AppConstant.ROOT_URL + "api/gift/rank";

    String GET_GIFT_LIST = AppConstant.ROOT_URL + "getGiftListByType";

    String GET_RED_PACKET = AppConstant.ROOT_URL + "lootHongBao";

    String UPDATE_USER_INFO = AppConstant.ROOT_URL + "api/user/update";

    String GET_MOMENTS = AppConstant.ROOT_URL + "momentsList";

    String GET_VIDEOS = AppConstant.ROOT_URL + "api/video/page";

    String GET_ACCOUNT = AppConstant.ROOT_URL + "api/account";

    String GET_PERFORMER_CARD = AppConstant.ROOT_URL + "userCard";

    String IS_FOLLOW = AppConstant.ROOT_URL + "ifGuardAndAttention";

    String BIND_W = AppConstant.ROOT_URL + "bindWechat";

    String BIND_PHONE = AppConstant.ROOT_URL + "bindPhone";

    String GET_GUARD_GOOD = AppConstant.ROOT_URL + "getGuardianList";

    String BUY_GUARD = AppConstant.ROOT_URL + "buyGuardian";

    String GET_COMMENTS = AppConstant.ROOT_URL + "commentList2";

    String GET_VIDEO_COMMENTS = AppConstant.ROOT_URL + "api/video/comment/page";

    String LIKE_MOMENT = AppConstant.ROOT_URL + "likes";

    String POST_COMMENT = AppConstant.ROOT_URL + "comment";

    String POST_VIDEO_COMMENT = AppConstant.ROOT_URL + "api/video/comment/save";

    String AUDIENCE_STATE = AppConstant.ROOT_URL + "api/manager/is";

    String ACCOUNT_LOG = AppConstant.ROOT_URL + "api/account/clientCoinsLog";

    String BUY_GIFT = AppConstant.ROOT_URL + "api/gift/buy";

    String GET_ROOM = AppConstant.ROOT_URL + "weapp/live_room/get_pushers";

    String WATCH_VIDEO = AppConstant.ROOT_URL + "api/video/view";

    String VIDEO_LIKE = AppConstant.ROOT_URL + "api/video/love";

    String VIDEO_CANCEL_LIKE = AppConstant.ROOT_URL + "api/video/hate";

    String VERSION_UPDATE = AppConstant.ROOT_URL + "checkNewVersion";

    String ORDER_CODE = AppConstant.ROOT_URL + "createNative";

    String CHECK_PAY_RESULT = AppConstant.ROOT_URL + "api/gift/buyGiftPrivateRoom";
}
