package com.wstv.pad.util;

import com.wstv.pad.activity.base.BaseActivity;
import com.wstv.pad.http.HttpService;
import com.wstv.pad.http.callback.CamSignCallback;
import com.wstv.pad.http.model.user.UserBean;

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
