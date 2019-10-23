package com.wstv.webcam.http.callback;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.libin.mylibrary.base.util.Trace;
import com.libin.mylibrary.http.callback.Callback;
import com.libin.mylibrary.util.BaseAppManager;
import com.libin.mylibrary.util.ToastUtils;
import com.wstv.webcam.activity.LoginPhoneActivity;
import com.wstv.webcam.http.model.EmptyCamResult;
import com.wstv.webcam.util.LoadingInterface;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * <p>Description: </p>
 * BaseCallback
 *
 * @author lilibin
 * @createDate 2019/1/29 15:29
 */

public abstract class BaseCamCallback<T extends EmptyCamResult> extends Callback<T> {

    protected Context context;

    private boolean autoHandle;

    protected LoadingInterface loadingInterface;

    public BaseCamCallback(Context context){
        this(context, true, null);
    }

    public BaseCamCallback(Context context, boolean autoHandle){
        this(context, autoHandle, null);
    }

    public BaseCamCallback(Context context, LoadingInterface loadingInterface){
        this(context, true, loadingInterface);
    }

    public BaseCamCallback(Context context, boolean autoHandle, LoadingInterface loadingInterface) {
        this.context = context;
        this.autoHandle = autoHandle;
        this.loadingInterface = loadingInterface;
    }

    @Override
    public void onBefore(Request request, int id) {
        if (null != loadingInterface) {
            loadingInterface.showLoadingDialog();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        Type type = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        String json = response.body().string();
        Trace.d("http response " + ((Class<T>)type).getSimpleName() + " : " + json);
        String newJson = json.replaceAll("\"\"", "null");
        return new Gson().fromJson(newJson, type);
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        e.printStackTrace();
        if (null != loadingInterface) {
            loadingInterface.dismissLoadingDialog();
        }
        if (autoHandle) {
            ToastUtils.showToastGravityCenter("网络请求失败");
        }
        onFailed("-1", "网络请求失败", null);
    }

    @Override
    public void onResponse(T t, int id) {
        if (null != loadingInterface) {
            loadingInterface.dismissLoadingDialog();
        }
        if (0 == t.code) {
            onSuccess(t, id);
        } else if (7 == t.code) {
            ToastUtils.showToastGravityCenter("登录超时，请重新登录");
            context.startActivity(new Intent(context, LoginPhoneActivity.class));
            BaseAppManager.getInstance().clear();
        } else {
            if (autoHandle) {
                ToastUtils.showToastGravityCenter(TextUtils.isEmpty(t.message) ? ("请求错误(错误码：" + t.code + ")") : t.message);
            }
            onFailed(String.valueOf(t.code), t.message, t);
        }
    }

    protected void onFailed(String errCode, String errMsg, T t){

    }

    public abstract void onSuccess(T t, int id);

}
