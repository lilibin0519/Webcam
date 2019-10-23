package com.wstv.webcam;

import android.net.http.HttpResponseCache;
import android.text.TextUtils;
import android.util.Base64;

import com.libin.mylibrary.base.MyApplication;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.libin.mylibrary.base.util.Trace;
import com.libin.mylibrary.http.OkHttpUtils;
import com.libin.mylibrary.http.utils.GsonUtils;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.rtmp.TXLiveBase;
import com.wstv.webcam.http.model.user.UserBean;
import com.wstv.webcam.util.upload.UploadUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

//import io.realm.Realm;
import io.realm.Realm;
import okhttp3.OkHttpClient;

//import com.mob.MobSDK;

/**
 * Created by Kindred on 2019/1/18.
 */

@SuppressWarnings("unchecked")
public class WsApp extends MyApplication {

    public UserBean userBean;

//    private BaseRoom.SelfAccountInfo accountInfo;

    @Override
    public void onCreate() {
        super.onCreate();
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .connectTimeout(10, TimeUnit.SECONDS) //连接超时
                .readTimeout(10, TimeUnit.SECONDS) //读取超时
                .writeTimeout(10, TimeUnit.SECONDS) //写超时
//                .addInterceptor(getHeaderInterceptor())
//                .addInterceptor(new CacheInterceptor())
//                .addInterceptor(new HttpLoggerInterceptor()
//                        .setLevel(BuildConfig.DEBUG ? Level.BODY : Level.NONE)
//                        .setTag(HTTP_LOG_TAG))
                .build();
        OkHttpUtils.initClient(httpClient);
//        MobSDK.init(this);
        closeAndroidPDialog();
        String base64 = PreferenceUtil.readString(AppConstant.KEY_USER_BEAN);
        if (!TextUtils.isEmpty(base64)) {
            try {
                userBean = GsonUtils.gsonToBean(new String(Base64.decode(base64.getBytes(), Base64.DEFAULT)), UserBean.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

//        CrashHandler crashHandler = CrashHandler.getInstance();
//        crashHandler.init(this);

        File cacheDir = new File(getApplicationContext().getCacheDir(), "http");
        try {
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        CrashReport.initCrashReport(getApplicationContext(), "6adf49bbbe", true);
        Bugly.init(getApplicationContext(), "6adf49bbbe", false);
        Realm.init(this);

        UploadUtil.init(this);

        initTXLive();
    }

    private void initTXLive() {
        String licenceURL = "http://license.vod2.myqcloud.com/license/v1/58dc5c0d046c053c588ca03606f11627/TXLiveSDK.licence";
        String licenceKey = "62c37ef982c6e05a8e9550e835ea9f24";
        Trace.e("licence ----------------------------------------");
        TXLiveBase.getInstance().setLicence(this, licenceURL, licenceKey);
        Trace.e("licence ----------------------------------------");
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        PreferenceUtil.write(AppConstant.KEY_USER_BEAN, Base64.encodeToString(GsonUtils.gsonString(userBean).getBytes(), Base64.DEFAULT));
        this.userBean = userBean;
    }

    public boolean isLogin(){
        return null != userBean;
    }

    private void closeAndroidPDialog(){
        try {
            Class aClass = Class.forName("android.content.pm.PackageParser$Package");
            Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class);
            declaredConstructor.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Class cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread");
            declaredMethod.setAccessible(true);
            Object activityThread = declaredMethod.invoke(null);
            Field mHiddenApiWarningShown = cls.getDeclaredField("mHiddenApiWarningShown");
            mHiddenApiWarningShown.setAccessible(true);
            mHiddenApiWarningShown.setBoolean(activityThread, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void setAccountInfo(BaseRoom.SelfAccountInfo accountInfo) {
//        this.accountInfo = accountInfo;
//    }
//
//    public BaseRoom.SelfAccountInfo getAccountInfo() {
//        return accountInfo;
//    }
}
