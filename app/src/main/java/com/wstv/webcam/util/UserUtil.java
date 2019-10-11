package com.wstv.webcam.util;

import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.http.HttpService;
import com.wstv.webcam.http.callback.CamSignCallback;
import com.wstv.webcam.http.model.user.UserBean;

/**
 * <p>Description: </p>
 * UserUtil
 *
 * @author lilibin
 * @createDate 2019/4/10 13:36
 */

public class UserUtil {

    public static void loginCam(BaseActivity activity){
        UserBean userBean = activity.getApp().getUserBean();
        HttpService.getCamSign(activity, new CamSignCallback(activity, userBean.nickname, userBean.headPortrait));
    }
}
