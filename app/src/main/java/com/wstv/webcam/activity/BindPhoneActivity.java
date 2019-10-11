package com.wstv.webcam.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.http.HttpService;
import com.wstv.webcam.http.callback.BaseCallback;
import com.wstv.webcam.http.callback.LoginCallback;
import com.wstv.webcam.http.model.EmptyResult;
import com.wstv.webcam.util.click.ClickProxy;
import com.wstv.webcam.util.counter.CountDownTimerListener;
import com.wstv.webcam.util.counter.MyCountDownTimer;

import org.greenrobot.eventbus.EventBus;

import butterknife.Bind;

public class BindPhoneActivity extends BaseActivity implements View.OnClickListener, CountDownTimerListener {

    @Bind(R.id.activity_bind_phone_submit)
    Button login;

    @Bind(R.id.activity_bind_phone_number)
    EditText phoneNumber;

    @Bind(R.id.activity_bind_phone_send)
    Button send;

    @Bind(R.id.activity_bind_phone_code)
    EditText code;

    private MyCountDownTimer countDownTimer;

    @Override
    protected void initViewAndData() {
        countDownTimer = new MyCountDownTimer(MyCountDownTimer.millisInFuture, MyCountDownTimer
                .countDownInterval);
        countDownTimer.setDownTimerListener(this);
        login.setOnClickListener(new ClickProxy(this));
        send.setOnClickListener(new ClickProxy(this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_bind_phone_submit:
                toBind();
                break;
            case R.id.activity_bind_phone_send:
                sendSMS();
                break;
        }
    }

    private void sendSMS() {
        if (TextUtils.isEmpty(phoneNumber.getText().toString()) || !phoneNumber.getText().toString().startsWith("1") || phoneNumber.getText().toString().length() != 11) {
            showToastCenter("手机号不正确");
            return;
        }
        String timeStamp = PreferenceUtil.readString(AppConstant.SEND_SMS_TIME_STAMP);
        long time = TextUtils.isEmpty(timeStamp) ? 0 : Long.parseLong(timeStamp);
        if (System.currentTimeMillis() - time < 60 * 1000) {
            showToastCenter("请勿频繁发送验证码，剩余" + (time / 1000) + "s");
            return;
        }
        HttpService.sendSMS(this, phoneNumber.getText().toString(), new BaseCallback<EmptyResult>(this, false, this) {
            @Override
            public void onSuccess(EmptyResult emptyResult, int id) {
                showToastCenter("验证码已发送");
                PreferenceUtil.write(AppConstant.SEND_SMS_TIME_STAMP, String.valueOf(System.currentTimeMillis()));
            }

            @Override
            protected void onFailed(String errCode, String errMsg, EmptyResult emptyResult) {
                showToastCenter(TextUtils.isEmpty(errMsg) ? "验证码发送失败" : errMsg);
                countDownTimer.onFinish();
            }
        });
        countDownTimer.start();
    }

    private void toBind() {
        if (TextUtils.isEmpty(phoneNumber.getText().toString()) || !phoneNumber.getText().toString().startsWith("1") || phoneNumber.getText().toString().length() != 11) {
            showToastCenter("手机号不正确");
            return;
        }
        if (TextUtils.isEmpty(code.getText().toString())) {
            showToastCenter("请填写验证码");
            return;
        }
        HttpService.verifySMS(this, phoneNumber.getText().toString(), code.getText().toString(), new BaseCallback<EmptyResult>(this, this) {
            @Override
            public void onSuccess(EmptyResult emptyResult, int id) {
                HttpService.bindPhone(BindPhoneActivity.this, phoneNumber.getText().toString(), PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), new BaseCallback<EmptyResult>(BindPhoneActivity.this, BindPhoneActivity.this) {
                    @Override
                    public void onSuccess(EmptyResult result, int id) {
                        showToastCenter("手机号绑定成功");
                        EventBus.getDefault().post(new EventCenter(2052));
                        finish();
                    }
                });
            }
        });
    }

    @Override
    public void onTick(long millisUntilFinished) {
        send.setText(millisUntilFinished / MyCountDownTimer.countDownInterval + "秒");
    }

    @Override
    public void onFinish() {
        send.setText("重新获取");
        send.setClickable(true);
    }

    @Override
    protected void onDestroy() {
        countDownTimer.onFinish();
        super.onDestroy();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComming(EventCenter center) {

    }
}
