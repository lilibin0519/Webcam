package com.wstv.webcam.http.callback;

import com.libin.mylibrary.base.util.PreferenceUtil;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.http.model.user.UserResult;
import com.wstv.webcam.util.UserUtil;

/**
 * <p>Description: </p>
 * LoginCallback
 *
 * @author lilibin
 * @createDate 2019/3/14 15:28
 */

public class LoginCallback extends BaseCallback<UserResult> {

    public LoginCallback(BaseActivity activity) {
        super(activity, activity);
    }

    @Override
    public void onSuccess(UserResult userResult, int id) {
        ((BaseActivity) context).getApp().setUserBean(userResult.detail);
        PreferenceUtil.write(AppConstant.KEY_PARAM_USER_ID, userResult.detail.userId);
        UserUtil.loginCam((BaseActivity) context);
    }
}
