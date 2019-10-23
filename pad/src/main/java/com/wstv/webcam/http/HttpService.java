package com.wstv.webcam.http;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.libin.mylibrary.base.util.PreferenceUtil;
import com.libin.mylibrary.http.OkHttpUtils;
import com.libin.mylibrary.http.builder.OkHttpRequestBuilder;
import com.libin.mylibrary.http.builder.PostFormBuilder;
import com.libin.mylibrary.http.utils.GsonUtils;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.AppUrl;
import com.wstv.webcam.http.callback.BaseCallback;
import com.wstv.webcam.http.callback.BaseCamCallback;
import com.wstv.webcam.http.model.request.GetRoomListRequest;
import com.wstv.webcam.http.model.request.GetRoomRequest;
import com.wstv.webcam.util.LoadingInterface;

/**
 * <p>Description: </p>
 * HttpService
 *
 * @author lilibin
 * @createDate 2019/3/8 13:28
 */

@SuppressWarnings("unchecked")
public class HttpService {

    public static <T extends OkHttpRequestBuilder> T addHeader (OkHttpRequestBuilder<T> builder) {
//        return builder.addHeader(AppConstant.KEY_ACCESS_TOKEN, PreferenceUtil.readString(AppConstant.KEY_ACCESS_TOKEN));
        return (T) builder;
    }

    public static <T extends OkHttpRequestBuilder> T addReq (OkHttpRequestBuilder<T> builder, String url) {
//        return builder.addHeader(AppConstant.KEY_ACCESS_TOKEN, PreferenceUtil.readString(AppConstant.KEY_ACCESS_TOKEN));
        StringBuilder stringBuilder = new StringBuilder(url);
        addParam2Url(url, stringBuilder, AppConstant.KEY_PARAM_USER_ID, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID));
        addParam2Url(url, stringBuilder, AppConstant.KEY_PARAM_TOKEN, PreferenceUtil.readString(AppConstant.KEY_PARAM_TOKEN));
        return builder.url(stringBuilder.toString());
    }

    private static void addParam2Url(String url, StringBuilder stringBuilder, String key, String param) {
        if (null == stringBuilder || null == url || TextUtils.isEmpty(key) || TextUtils.isEmpty(param)) {
            return;
        }
        if (url.length() == stringBuilder.length()) {
            stringBuilder.append("?");
        } else if (stringBuilder.length() > url.length()) {
            stringBuilder.append("&");
        }
        stringBuilder.append(key).append("=").append(param);
    }

    // App登录
    public static void loginOrRegister(LoadingInterface tag, String userName, String password, BaseCallback callback){
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.LOGIN_OR_REGISTER)
                .addParams("userName", userName)
                .addParams("password", password)
                .build().execute(callback);
    }

    // 移动直播获取签名
    public static void getCamSign(LoadingInterface tag, BaseCamCallback callback){
        addReq(addHeader(OkHttpUtils.post(tag)), AppUrl.GET_CAM_SIGN)
                .build().execute(callback);
    }

    // 移动直播获取签名
    public static void getCamToken(LoadingInterface tag, String accountType, String sdkAppID, String userSig, BaseCamCallback callback){
        addReq(addHeader(OkHttpUtils.post(tag)), AppUrl.GET_CAM_TOKEN)
                .addParams("accountType", accountType)
                .addParams("sdkAppID", sdkAppID)
                .addParams("userSig", userSig)
                .build().execute(callback);
    }

    public static void getAccessToken(LoadingInterface tag, String code, BaseCallback callback){
        tag.showLoadingDialog();
        addReq(addHeader(OkHttpUtils.post(tag)), AppUrl.GET_ACCESS_TOKEN)
                .addParams("code", code)
                .build().execute(callback);
    }

    public static void loginW(LoadingInterface tag, String accessToken, String openid, BaseCallback callback){
        tag.showLoadingDialog();
        addReq(addHeader(OkHttpUtils.post(tag)), AppUrl.LOGIN_BY_W)
                .addParams("accessToken", accessToken)
                .addParams("openid", openid)
                .build().execute(callback);
    }

    public static void sendSMS(LoadingInterface tag, String phone, BaseCallback callback){
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.SEND_SMS)
                .addParams("phone", phone)
                .build().execute(callback);
    }

    public static void verifySMS(LoadingInterface tag, String phone, String code, BaseCallback callback){
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.VERIFY_SMS)
                .addParams("phone", phone)
                .addParams("code", code)
                .build().execute(callback);
    }

    public static void getRooms(LoadingInterface tag, int currentPage, int pageSize, BaseCamCallback callback){
        tag.showLoadingDialog();
        addReq(OkHttpUtils.postJson(tag), AppUrl.GET_ROOM_LIST)
                .content(GsonUtils.gsonString(new GetRoomListRequest(currentPage, pageSize)))
                .build().execute(callback);
    }

    public static void getRoomsBy(LoadingInterface tag, int currentPage, int pageSize, String type, BaseCamCallback callback){
        tag.showLoadingDialog();
        addReq(OkHttpUtils.postJson(tag), AppUrl.GET_ROOM_LIST_BY)
                .content(GsonUtils.gsonString(new GetRoomListRequest(currentPage, pageSize, type)))
                .build().execute(callback);
    }

    public static void getBanner(LoadingInterface tag, String roomType, BaseCallback callback){
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.GET_BANNER)
                .addParams("roomType", roomType)
                .build().execute(callback);
    }

    public static void createOrder(LoadingInterface tag, String ip, String price, String type, String userId, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.GET_PAY_INFO)
                .addParams("ip", ip)
                .addParams("pay", price)
                .addParams("payType", type)
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void hotWords(LoadingInterface tag, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.GET_HOT_WORDS)
                .build().execute(callback);
    }

    public static void search(LoadingInterface tag, String keywords, String page, String size, String userId, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.ON_SEARCH)
                .addParams("keywords", keywords)
                .addParams("page", page)
                .addParams("size", size)
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void follow(LoadingInterface tag, String performerId, String userId, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.FOLLOW)
                .addParams("idolId", performerId)
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void cancelFollow(LoadingInterface tag, String performerId, String userId, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.CANCEL_FOLLOW)
                .addParams("idolId", performerId)
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void followList(LoadingInterface tag, String page, String pageSize, String userId, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.FOLLOW_LIST)
                .addParams("page", page)
                .addParams("pageSize", pageSize)
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void watchRecord(LoadingInterface tag, String page, String pageSize, String userId, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.WATCH_RECORD)
                .addParams("page", page)
                .addParams("size", pageSize)
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void getRoomState(LoadingInterface tag, String roomId, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.GET_ROOM_STATE)
                .addParams("roomId", roomId)
                .build().execute(callback);
    }

    public static void getUserInfo(LoadingInterface tag, String userId, BaseCallback callback) {
        getUserInfo(tag, userId, callback, true);
    }

    public static void getUserInfo(LoadingInterface tag, String userId, BaseCallback callback, boolean showLoading) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.GET_USER_INFO)
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void getRoomTypes(LoadingInterface tag, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.GET_ROOM_TYPE)
                .build().execute(callback);
    }

    public static void getRanking(LoadingInterface tag, String page, String pageSize, String showId, String type, String userType, BaseCallback callback) {
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.GET_RANKING)
                .addParams("page", page)
                .addParams("size", pageSize)
                .addParams("showId", TextUtils.isEmpty(showId) ? "" : showId)
                .addParams("type", type)
                .addParams("userType", userType)
                .build().execute(callback);
    }

    public static void getRanking(LoadingInterface tag, String page, String pageSize, String type, String userType, BaseCallback callback) {
        getRanking(tag, page, pageSize, null, type, userType, callback);
    }

    public static void getGiftRanking(LoadingInterface tag, String page, String pageSize, String showId, String type, BaseCallback callback){
        getRanking(tag, page, pageSize, showId, type, "N", callback);
    }

    public static void getWithRanking(LoadingInterface tag, String page, String pageSize, String showId, BaseCallback callback){
        getRanking(tag, page, pageSize, showId, "ALL", "G", callback);
    }

    public static void getGiftList(LoadingInterface tag, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.GET_GIFT_LIST)
                .build().execute(callback);
    }

    public static void getRedPacket(LoadingInterface tag, String key, String userId, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.GET_RED_PACKET)
                .addParams("hongbaoKey", key)
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void updateUserInfo(LoadingInterface tag, String birthday, String headPortrait, @NonNull String userId, String nickname, String sex, String signature, BaseCallback callback) {
        tag.showLoadingDialog();
        PostFormBuilder builder = addHeader(OkHttpUtils.post(tag)).url(AppUrl.UPDATE_USER_INFO).addParams("id", userId);
        if (!TextUtils.isEmpty(birthday)) {
            builder.addParams("birthday", birthday);
        }
        if (!TextUtils.isEmpty(headPortrait)) {
            builder.addParams("headPortrait", headPortrait);
        }
        if (!TextUtils.isEmpty(nickname)) {
            builder.addParams("nickname", nickname);
        }
        if (!TextUtils.isEmpty(sex)) {
            builder.addParams("sex", sex);
        }
        if (!TextUtils.isEmpty(signature)) {
            builder.addParams("signature", signature);
        }
        builder.build().execute(callback);
    }

    public static void getMoments(LoadingInterface tag, String clientUserId, String userId, int currentPage, int pageSize, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.GET_MOMENTS)
                .addParams("clientUserId", clientUserId)
                .addParams("userId", userId)
                .addParams("page", String.valueOf(currentPage))
                .addParams("pageSize", String.valueOf(pageSize))
                .build().execute(callback);
    }

    public static void getVideos(LoadingInterface tag, String streamerId, String userId, int currentPage, int pageSize, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.GET_VIDEOS)
                .addParams("streamerId", streamerId)
                .addParams("userId", userId)
                .addParams("page", String.valueOf(currentPage))
                .addParams("pageSize", String.valueOf(pageSize))
                .build().execute(callback);
    }

    public static void getAccount(LoadingInterface tag, String userId, BaseCallback callback) {
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.GET_ACCOUNT)
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void getPerformerCard(LoadingInterface tag, String showId, String userId, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.GET_PERFORMER_CARD)
                .addParams("showId", showId)
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void getIsFollow(LoadingInterface tag, String showId, String userId, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.IS_FOLLOW)
                .addParams("showId", showId)
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void bindW(LoadingInterface tag, String openId, String userId, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.BIND_W)
                .addParams("openId", openId)
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void bindPhone(LoadingInterface tag, String phone, String userId, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.BIND_PHONE)
                .addParams("phone", phone)
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void getGuardGood(LoadingInterface tag, BaseCallback callback) {
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.GET_GUARD_GOOD)
                .build().execute(callback);
    }

    public static void buyGuard(LoadingInterface tag, String guardId, String userId, String showId, BaseCallback callback){
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.BUY_GUARD)
                .addParams("guardianId", guardId)
                .addParams("userId", userId)
                .addParams("showId", showId)
                .build().execute(callback);
    }

    public static void getComments(LoadingInterface tag, String actionId, int page, int pageSize, BaseCallback callback){
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.GET_COMMENTS)
                .addParams("id", actionId)
                .addParams("page", String.valueOf(page))
                .addParams("pageSize", String.valueOf(pageSize))
                .build().execute(callback);
    }

    public static void getVideoComments(LoadingInterface tag, String videoId, int page, int pageSize, BaseCallback callback){
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.GET_VIDEO_COMMENTS)
                .addParams("videoId", videoId)
                .addParams("page", String.valueOf(page))
                .addParams("pageSize", String.valueOf(pageSize))
                .build().execute(callback);
    }

    public static void likeMoment(LoadingInterface tag, String actionId, String userId, BaseCallback callback){
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.LIKE_MOMENT)
                .addParams("id", actionId)
                .addParams("userId", String.valueOf(userId))
                .build().execute(callback);
    }

    public static void postComment(LoadingInterface tag, String actionId, String userId, String comment, String commentId, BaseCallback callback){
        tag.showLoadingDialog();
        PostFormBuilder builder = addHeader(OkHttpUtils.post(tag)).url(AppUrl.POST_COMMENT)
                .addParams("id", actionId)
                .addParams("userId", String.valueOf(userId))
                .addParams("comment", comment);
        if (!TextUtils.isEmpty(commentId)) {
            builder.addParams("commentId", commentId);
        }
        builder.build().execute(callback);
    }

    public static void postVideoComment(LoadingInterface tag, String videoId, String userId, String comment, String commentId, BaseCallback callback){
        tag.showLoadingDialog();
        PostFormBuilder builder = addHeader(OkHttpUtils.post(tag)).url(AppUrl.POST_VIDEO_COMMENT)
                .addParams("videoId", videoId)
                .addParams("userId", String.valueOf(userId))
                .addParams("comment", comment);
        if (!TextUtils.isEmpty(commentId)) {
            builder.addParams("atUser", commentId);
        }
        builder.build().execute(callback);
    }

    public static void audienceState(LoadingInterface tag, String showId , String userId, BaseCallback callback){
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.AUDIENCE_STATE)
                .addParams("showId", showId )
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void accountLog(LoadingInterface tag, String userId, int page, int pageSize, BaseCallback callback){
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.ACCOUNT_LOG)
                .addParams("page", String.valueOf(page))
                .addParams("size", String.valueOf(pageSize))
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void buyGift(LoadingInterface tag, String userId, String showId, String giftId, String count, BaseCallback callback){
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.BUY_GIFT)
                .addParams("count", count)
                .addParams("giftId", giftId)
                .addParams("showId", showId)
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void getRoom(LoadingInterface tag, String roomId, BaseCamCallback callback){
        tag.showLoadingDialog();
        addReq(addHeader(OkHttpUtils.postJson(tag)), AppUrl.GET_ROOM)
                .content(GsonUtils.gsonString(new GetRoomRequest(roomId)))
                .build().execute(callback);
    }

    public static void watchVideo(LoadingInterface tag, String videoId){
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.WATCH_VIDEO)
                .addParams("videoId", videoId)
                .build().execute(null);
    }

    public static void likeVideo(LoadingInterface tag, String videoId, BaseCallback callback){
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.VIDEO_LIKE)
                .addParams("videoId", videoId)
                .build().execute(callback);
    }

    public static void cancelLikeVideo(LoadingInterface tag, String videoId, BaseCallback callback){
        tag.showLoadingDialog();
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.VIDEO_CANCEL_LIKE)
                .addParams("videoId", videoId)
                .build().execute(callback);
    }

    public static void requestUpdate(LoadingInterface tag, int version, BaseCallback callback){
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.VERSION_UPDATE)
                .addParams("versionNum", String.valueOf(version))
                .addParams("type", "0")
                .build().execute(callback);
    }

    public static void requestOrderCode(LoadingInterface tag, int count, String giftId, String userId, BaseCallback callback){
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.ORDER_CODE)
                .addParams("count", String.valueOf(count))
                .addParams("giftId", giftId)
                .addParams("userId", userId)
                .build().execute(callback);
    }

    public static void requestPayResult(LoadingInterface tag, int count, String giftId, String userId, String orderId, String showId, BaseCallback callback){
        addHeader(OkHttpUtils.post(tag)).url(AppUrl.CHECK_PAY_RESULT)
                .addParams("count", String.valueOf(count))
                .addParams("giftId", giftId)
                .addParams("userId", userId)
                .addParams("orderId", orderId)
                .addParams("showId", showId)
                .build().execute(callback);
    }
}
