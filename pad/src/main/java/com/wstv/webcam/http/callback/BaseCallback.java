package com.wstv.webcam.http.callback;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.libin.mylibrary.base.util.Trace;
import com.libin.mylibrary.http.callback.Callback;
import com.libin.mylibrary.util.ToastUtils;
import com.wstv.webcam.http.model.EmptyResult;
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

public abstract class BaseCallback<T extends EmptyResult> extends Callback<T> {

    protected Context context;

    private boolean autoHandle;

    protected LoadingInterface loadingInterface;

    public BaseCallback(Context context){
        this(context, true, null);
    }

    public BaseCallback(Context context, boolean autoHandle){
        this(context, autoHandle, null);
    }

    public BaseCallback(Context context, LoadingInterface loadingInterface){
        this(context, true, loadingInterface);
    }

    public BaseCallback(Context context, boolean autoHandle, LoadingInterface loadingInterface) {
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
        Trace.d("http response json " + " : " + json);
//        Trace.d("http response " + ((Class<T>)type).getSimpleName() + " : " + json);
        return new Gson().fromJson(json.replace(":\"\"", ":null").replace(":''", ":null"), type);
    }

    @Override
    public void onError(Call call, Exception e, int id) {
        e.printStackTrace();
        if (null != loadingInterface) {
            loadingInterface.dismissLoadingDialog();
        }
        if (autoHandle) {
            ToastUtils.showToastGravityCenter("网络请求失败");
        } else {
            onFailed("-1", "网络请求失败", null);
        }
    }

    @Override
    public void onResponse(T t, int id) {
        if (null != loadingInterface) {
            loadingInterface.dismissLoadingDialog();
        }
        if ("0".equals(t.result)) {
            onSuccess(t, id);
        } else {
            if (autoHandle) {
                ToastUtils.showToastGravityCenter(TextUtils.isEmpty(t.errmsg) ? ("请求错误(错误码：" + t.result + ")") : t.errmsg);
            } else {
                onFailed(t.result, t.errmsg, t);
            }
        }
    }

    protected void onFailed(String errCode, String errMsg, T t){

    }

    public abstract void onSuccess(T t, int id);

}
