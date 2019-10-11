package com.wstv.pad.activity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.wstv.pad.AppConstant;
import com.wstv.pad.R;
import com.wstv.pad.activity.base.BaseActivity;
import com.wstv.pad.http.HttpService;
import com.wstv.pad.http.callback.BaseCallback;
import com.wstv.pad.http.model.recharge.PayInfo;
import com.wstv.pad.http.model.recharge.PayInfoResult;
import com.wstv.pad.util.click.ClickProxy;
import com.wstv.pad.watcher.RMBInputTextWatcher;
import com.wstv.pad.watcher.WatcherCallback;
import com.wstv.pad.widget.CheckableLinearLayout;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * <p>Description: </p>
 * RechargeActivity
 *
 * @author lilibin
 * @createDate 2019/4/28 16:50
 */

public class RechargeActivity extends BaseActivity {

    @Bind(R.id.activity_recharge_price_label)
    TextView priceLabel;

    @Bind(R.id.activity_recharge_price_layout)
    LinearLayout priceLayout;

    @Bind(R.id.activity_recharge_price_1)
    CheckableLinearLayout price1;

    @Bind(R.id.activity_recharge_price_2)
    CheckableLinearLayout price2;

    @Bind(R.id.activity_recharge_price_3)
    CheckableLinearLayout price3;

    @Bind(R.id.activity_recharge_price_4)
    CheckableLinearLayout price4;

    private CheckableLinearLayout[] priceArr;

    @Bind(R.id.activity_recharge_price_5)
    CheckableLinearLayout price5;

    String payPrice = "10";

    @Bind(R.id.activity_recharge_price_other)
    EditText otherPrice;

    @Bind(R.id.activity_recharge_type_1)
    CheckableLinearLayout type1;

    @Bind(R.id.activity_recharge_type_2)
    CheckableLinearLayout type2;

    @Bind(R.id.activity_recharge_type_3)
    CheckableLinearLayout type3;

    String payType = "2";

    private CheckableLinearLayout[] typeArr;

    public static String prepayId;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_recharge;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void initViewAndData() {
        setTitleStr("秀币充值");
        price1.setSelected(true);
        type2.setSelected(true);
        priceLabel.setText("10");
        priceArr = new CheckableLinearLayout[]{price1, price2, price3, price4, price5};
        typeArr = new CheckableLinearLayout[]{type1, type2, type3};
        initEditText();
        View.OnClickListener priceListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.activity_recharge_price_5) {
                    otherPrice.requestFocus();
                    priceLabel.setText("");
                } else {
                    priceLayout.requestFocus();
                    otherPrice.setText("");
                    switch (v.getId()) {
                        case R.id.activity_recharge_price_1:
                            priceLabel.setText("10");
                            payPrice = "10";
                            break;
                        case R.id.activity_recharge_price_2:
                            priceLabel.setText("50");
                            payPrice = "50";
                            break;
                        case R.id.activity_recharge_price_3:
                            priceLabel.setText("300");
                            payPrice = "300";
                            break;
                        case R.id.activity_recharge_price_4:
                            priceLabel.setText("1000");
                            payPrice = "1000";
                            break;
                    }
                }
                for (CheckableLinearLayout price : priceArr) {
                    price.setSelected(price.getId() == v.getId());
                }
            }
        };
        price1.setOnClickListener(new ClickProxy(priceListener));
        price2.setOnClickListener(new ClickProxy(priceListener));
        price3.setOnClickListener(new ClickProxy(priceListener));
        price4.setOnClickListener(new ClickProxy(priceListener));
        price5.setOnClickListener(new ClickProxy(priceListener));
        View.OnClickListener typeListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CheckableLinearLayout type : typeArr) {
                    type.setSelected(type.getId() == v.getId());
                    if (v.getId() == R.id.activity_recharge_type_1) {
                        payType = "3";
                    } else if (v.getId() == R.id.activity_recharge_type_2) {
                        payType = "2";
                    } else if (v.getId() == R.id.activity_recharge_type_3) {
                        payType = "1";
                    }
                }
            }
        };
        type1.setOnClickListener(new ClickProxy(typeListener));
        type2.setOnClickListener(new ClickProxy(typeListener));
        type3.setOnClickListener(new ClickProxy(typeListener));
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComming(EventCenter center) {
        if (center.getEventCode() == 2047) {
            finish();
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    private void initEditText(){
        otherPrice.setShowSoftInputOnFocus(true);
        otherPrice.setFocusableInTouchMode(true);
        otherPrice.addTextChangedListener(new RMBInputTextWatcher(new WatcherCallback() {
            @Override
            public void afterTextChange(String text) {
//                String tempValue = TextUtils.isEmpty(text) ? "0" : text;
//                String value;
//                if (PatternUtils.isPositiveTwoDecimals(tempValue)) {
//                    value = tempValue;
//                } else if (tempValue.lastIndexOf(".") == tempValue.length() - 1) {
//                    value = tempValue.subSequence(0, tempValue.length() - 1).toString();
//                } else if (tempValue.lastIndexOf(".0") == tempValue.length() - 2) {
//                    value = tempValue.subSequence(0, tempValue.length() - 2).toString();
//                } else {
//                    value = "0";
//                }
//                costCalculate(value);
                priceLabel.setText(TextUtils.isEmpty(text) ? "0" : text);
                payPrice = TextUtils.isEmpty(text) ? "0" : text;
            }
        }));
        otherPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    priceLabel.setText("");
                    otherPrice.setHintTextColor(ContextCompat.getColor(RechargeActivity.this, android.R.color.white));
                    otherPrice.setSelection(otherPrice.getText().toString().length());
                    for (CheckableLinearLayout price : priceArr) {
                        price.setSelected(price.getId() == R.id.activity_recharge_price_5);
                    }
                } else {
                    otherPrice.setHintTextColor(ContextCompat.getColor(RechargeActivity.this, R.color.label_gray));
                }
            }
        });
    }

    @OnClick(R.id.activity_recharge_pay)
    public void pay() {
        getPayInfo();
    }

    private void getPayInfo(){
        if (!"2".equals(payType)) {
            showToastCenter("暂不支持该支付方式");
            return;
        }
        if (TextUtils.isEmpty(payPrice) || "0".equals(payPrice)) {
            showToastCenter("充值金额不能低于0");
            return;
        }
        HttpService.createOrder(this, getIPAddress(this), "0.01", payType, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), new BaseCallback<PayInfoResult>(this, this) {
            @Override
            public void onSuccess(PayInfoResult result, int id) {
                requestPay(result.detail.wxRechargeDto);
            }
        });
    }

    private void requestPay(PayInfo data) {
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
        // 将该app注册到微信
        msgApi.registerApp(AppConstant.W_X_APP_ID);

        PayReq request = new PayReq();
        request.appId = AppConstant.W_X_APP_ID;
        request.partnerId = data.partnerid;
        request.prepayId = data.prepayid;
        prepayId = data.prepayid;
        request.packageValue = data.packagevalue;
        request.nonceStr = data.noncestr;
        request.timeStamp = data.timestamp;
        request.sign = data.sign;
        msgApi.sendReq(request);
    }

    public static String getIPAddress(Context context) {
        NetworkInfo info = ((ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            if (info.getType() == ConnectivityManager.TYPE_MOBILE) {//当前使用2G/3G/4G网络
                try {
                    //Enumeration<NetworkInterface> en=NetworkInterface.getNetworkInterfaces();
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException e) {
                    e.printStackTrace();
                }


            } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {//当前使用无线网络
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
                return ipAddress;
            }
        } else {
            //当前无网络连接,请在设置中打开网络
        }
        return null;
    }

    /**
      * 将得到的int类型的IP转换为String类型
      *
      * @param ip
      * @return
      */
    public static String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }
}
