package com.wstv.webcam.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.AboutUsActivity;
import com.wstv.webcam.activity.BindPhoneActivity;
import com.wstv.webcam.activity.CashActivity;
import com.wstv.webcam.activity.ChatListActivity;
import com.wstv.webcam.activity.StartActivity;
import com.wstv.webcam.activity.UserInfoActivity;
import com.wstv.webcam.http.HttpService;
import com.wstv.webcam.http.callback.BaseCallback;
import com.wstv.webcam.http.model.user.UserBean;
import com.wstv.webcam.http.model.user.UserResult;
import com.wstv.webcam.util.click.ClickProxy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineFragment extends BaseFragment {

    @Bind(R.id.title_bar_background)
    ViewGroup titleParent;

    @Bind(R.id.title_bar_parent)
    Toolbar titleParentLayout;

    @Bind(R.id.temp_status)
    View tempStatus;

    @Bind(R.id.fragment_mine_message)
    LinearLayout messageLayout;

    @Bind(R.id.layout_user_info_sign)
    TextView sign;

    @Bind(R.id.layout_user_info_name)
    TextView userInfoName;

    @Bind(R.id.layout_user_info_num)
    TextView userInfoNum;

    @Bind(R.id.layout_user_info_fun_count)
    TextView fansCount;

    @Bind(R.id.layout_user_info_old)
    TextView age;

    @Bind(R.id.layout_user_info_gender)
    ImageView gender;

    @Bind(R.id.layout_user_info_avatar)
    ImageView avatar;

    @Bind(R.id.layout_user_info_level_logo)
    ImageView levelLogo;

    @Bind(R.id.layout_user_info_follow_count)
    TextView followCount;

    @Bind(R.id.layout_user_info_good_count)
    TextView goodCount;

    @Bind(R.id.fragment_mine_w_state)
    TextView stateW;

    @Bind(R.id.fragment_mine_phone_state)
    TextView statePhone;

    private IWXAPI api;

    @Bind(R.id.fragment_mine_cash_value)
    TextView account;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_mine;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.findById(view, R.id.title_bar_left).setVisibility(View.GONE);
    }

    @Override
    protected void initViewAndData() {
        setTempStatus();
        messageLayout.setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                readyGo(ChatListActivity.class);
            }
        }));
        setUserData(getApp().getUserBean());
        requestUserInfo();
    }

    private void requestUserInfo(){
        HttpService.getUserInfo(this, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), new BaseCallback<UserResult>(getContext(), this) {
            @Override
            public void onSuccess(UserResult userResult, int id) {
                getApp().setUserBean(userResult.detail);
                setUserData(userResult.detail);
            }
        });
    }

    private void setUserData(UserBean userBean) {
        Glide.with(getContext()).load(userBean.headPortrait).into(avatar);
        sign.setText(userBean.signature);
        userInfoName.setText(userBean.nickname);
        userInfoNum.setText(userBean.showId);
        fansCount.setText(TextUtils.isEmpty(userBean.fans) ? "0" : userBean.fans);
        age.setText(TextUtils.isEmpty(userBean.age) ? "0" : userBean.age);
        followCount.setText(TextUtils.isEmpty(userBean.attentionCnt) ? "0" : userBean.attentionCnt);
        goodCount.setText(TextUtils.isEmpty(userBean.praise) ? "0" : userBean.praise);
        levelLogo.setImageResource(AppConstant.levelArr[(userBean.level - 1) < AppConstant.levelArr.length ? userBean.level - 1 : 0]);
        gender.setImageResource("0".equals(userBean.sex) ? R.drawable.icon_gender_female : R.drawable.icon_gender_male);
        account.setText(userBean.coins + "秀币");
        statePhone.setText(TextUtils.isEmpty(userBean.userName) ? "未绑定" : "已绑定");
        stateW.setText(TextUtils.isEmpty(userBean.openId) ? "未绑定" : "已绑定");
    }

    private void setTempStatus() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tempStatus.getLayoutParams();
        params.height = getStatusHeight(getContext());
        tempStatus.setLayoutParams(params);
        titleParent.post(new Runnable() {
            @Override
            public void run() {
                if (titleParent.getHeight() == titleParentLayout.getHeight()) {
                    tempStatus.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick({R.id.fragment_mine_logout, R.id.fragment_mine_cash, R.id.fragment_mine_info, R.id.fragment_mine_about_us,
            R.id.fragment_mine_w_bind})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.fragment_mine_logout:
                getApp().setUserBean(null);
                PreferenceUtil.write(AppConstant.KEY_PARAM_USER_ID, "");
                PreferenceUtil.write(AppConstant.KEY_PARAM_TOKEN, "");
                readyGoThenKill(StartActivity.class);
                break;
            case R.id.fragment_mine_cash:
                readyGo(CashActivity.class);
                break;
            case R.id.fragment_mine_info:
                readyGo(UserInfoActivity.class);
                break;
            case R.id.fragment_mine_about_us:
                readyGo(AboutUsActivity.class);
                break;
            case R.id.fragment_mine_w_bind:
                if (!TextUtils.isEmpty(getApp().getUserBean().openId)) {
                    return;
                }
                AuthReq();
                break;
            case R.id.fragment_mine_phone_bind:
                if (!TextUtils.isEmpty(getApp().getUserBean().userName)) {
                    return;
                }
                readyGo(BindPhoneActivity.class);
                break;
        }
    }

    private void AuthReq(){
        PreferenceUtil.write(AppConstant.KEY_W_BIND, true);
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(getContext(), AppConstant.W_X_APP_ID, true);

        // 将应用的appId注册到微信
        api.registerApp(AppConstant.W_X_APP_ID);
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";//
//                req.scope = "snsapi_login";//提示 scope参数错误，或者没有scope权限
        req.state = "wechat_sdk_微信登录";
        api.sendReq(req);
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void onFirstUserInVisible() {

    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        if (eventCenter.getEventCode() == 2050) {
            setUserData(getApp().getUserBean());
        }
        if (eventCenter.getEventCode() == 2051) {
            stateW.setText("已绑定");
            requestUserInfo();
        }
        if (eventCenter.getEventCode() == 2052) {
            statePhone.setText("已绑定");
            requestUserInfo();
        }
        if (eventCenter.getEventCode() == 2047) {
            requestUserInfo();
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }
    /**
     * 将文件生成位图
     * @param path
     * @return
     * @throws IOException
     */
    @SuppressWarnings("deprecation")
    public BitmapDrawable getImageDrawable(String path) {
        //打开文件
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] bt = new byte[1024];

        //得到文件的输入流
        InputStream in = null;
        try {
            in = new FileInputStream(file);

            //将文件读出到输出流中
            int readLength = in.read(bt);
            while (readLength != -1) {
                outStream.write(bt, 0, readLength);
                readLength = in.read(bt);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //转换成byte 后 再格式化成位图
        byte[] data = outStream.toByteArray();
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);// 生成位图
        BitmapDrawable bd = new BitmapDrawable(bitmap);

        return bd;
    }
}
