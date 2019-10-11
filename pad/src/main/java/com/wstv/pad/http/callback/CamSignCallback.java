package com.wstv.pad.http.callback;

import com.libin.mylibrary.base.util.PreferenceUtil;
import com.libin.mylibrary.util.BaseAppManager;
import com.wstv.pad.AppConstant;
import com.wstv.pad.activity.MainActivity;
import com.wstv.pad.activity.base.BaseActivity;
import com.wstv.pad.http.HttpService;
import com.wstv.pad.http.model.user.UserSignResult;
import com.wstv.pad.http.model.user.UserTokenResult;
import com.wstv.pad.tencent.liveroom.LiveRoom;
import com.wstv.pad.tencent.roomutil.commondef.BaseRoom;
import com.wstv.pad.tencent.roomutil.commondef.LoginInfo;
import com.wstv.pad.util.LiveRoomUtil;

/**
 * <p>Description: </p>
 * CamSignCallback
 *
 * @author lilibin
 * @createDate 2019/4/10 13:25
 */

public class CamSignCallback extends BaseCamCallback<UserSignResult> {

    private String userName;

    private String avatar;

    public CamSignCallback(BaseActivity activity, String userName, String avatar) {
        super(activity, activity);
        this.userName = userName;
        this.avatar = avatar;
    }

    @Override
    public void onSuccess(UserSignResult userSignResult, int id) {
//        LiveRoomUtil.getInstance((BaseActivity) context).setSelfAccountInfo(userSignResult.userID, userName, avatar, userSignResult.userSig, userSignResult.accType, userSignResult.sdkAppID);
//        getCamToken(userSignResult);
        loadingInterface.showLoadingDialog();
        internalInitializeLiveRoom(userSignResult);
    }

    private void getCamToken(UserSignResult userSignResult) {
        HttpService.getCamToken(loadingInterface, userSignResult.accType, String.valueOf(userSignResult.sdkAppID), userSignResult.userSig,
                new BaseCamCallback<UserTokenResult>(context, loadingInterface) {
                    @Override
                    public void onSuccess(UserTokenResult userTokenResult, int id) {
                        PreferenceUtil.write(AppConstant.KEY_PARAM_TOKEN, userTokenResult.token);
                        LiveRoomUtil.getInstance((BaseActivity) context).setToken(userTokenResult.token);
                        BaseAppManager.getInstance().clearToTop();
                        ((BaseActivity) context).readyGoThenKill(MainActivity.class);
                    }
                });
    }

    private void internalInitializeLiveRoom(UserSignResult resp) {
        LoginInfo loginInfo       = new LoginInfo();
        loginInfo.sdkAppID       = resp.sdkAppID;
        loginInfo.userID         = resp.userID;
        loginInfo.userSig        = resp.userSig;
        loginInfo.accType        = resp.accType;
        loginInfo.userName       = userName;
        loginInfo.userAvatar     = avatar;

        LiveRoomUtil.getInstance((BaseActivity) context).getLiveRoom()
                .login(BaseRoom.ROOM_SERVICE_DOMAIN + "live_room", loginInfo, new LiveRoom.LoginCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
//                setTitle(errInfo);
                ((BaseActivity) context).showToastCenter("[Activity]LiveRoom初始化失败：{%s}");
//                printGlobalLog(String.format("[Activity]LiveRoom初始化失败：{%s}", errInfo));
//                retryInitRoomRunnable = new Runnable() {
//                    @Override
//                    public void run() {
//                        ((BaseActivity) context).showToastCenter("重试...");
//                        UserUtil.loginCam((BaseActivity) context);
//                    }
//                };
            }

            @Override
            public void onSuccess(String userId) {
//                setTitle("美女直播");
//                LiveRoomActivity.this.userId = userId;
//                printGlobalLog("[Activity]LiveRoom初始化成功,userID{%s}", userId);
//                Fragment fragment = getFragmentManager().findFragmentById(R.id.rtmproom_fragment_container);
//                if (!(fragment instanceof LiveRoomChatFragment)) {
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    fragment = LiveRoomListFragment.newInstance(userId);
//                    ft.replace(R.id.rtmproom_fragment_container, fragment);
//                    ft.commit();
//                }
                PreferenceUtil.write(AppConstant.KEY_PARAM_TOKEN, LiveRoomUtil.getInstance((BaseActivity) context).getLiveRoom().getToken());
                ((BaseActivity) context).getApp().setAccountInfo(LiveRoomUtil.getInstance((BaseActivity) context).getLiveRoom().getSelfAccountInfo());
                loadingInterface.dismissLoadingDialog();
                ((BaseActivity) context).readyGoThenKill(MainActivity.class);
            }
        });
    }
}
