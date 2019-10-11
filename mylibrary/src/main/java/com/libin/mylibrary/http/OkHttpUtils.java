package com.libin.mylibrary.http;

import com.libin.mylibrary.base.util.PreferenceUtil;
import com.libin.mylibrary.base.util.Trace;
import com.libin.mylibrary.http.builder.GetBuilder;
import com.libin.mylibrary.http.builder.HeadBuilder;
import com.libin.mylibrary.http.builder.OtherRequestBuilder;
import com.libin.mylibrary.http.builder.PostFileBuilder;
import com.libin.mylibrary.http.builder.PostFormBuilder;
import com.libin.mylibrary.http.builder.PostStringBuilder;
import com.libin.mylibrary.http.callback.Callback;
import com.libin.mylibrary.http.request.RequestCall;
import com.libin.mylibrary.http.utils.Platform;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by zhy on 15/8/17.
 */
@SuppressWarnings("unchecked")
public class OkHttpUtils
{
    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private volatile static OkHttpUtils mInstance;
    private OkHttpClient mOkHttpClient;
    private Platform mPlatform;

    public OkHttpUtils(OkHttpClient okHttpClient)
    {
        if (okHttpClient == null)
        {
            mOkHttpClient = new OkHttpClient();
        } else
        {
            mOkHttpClient = okHttpClient;
        }

        mPlatform = Platform.get();
    }


    public static OkHttpUtils initClient(OkHttpClient okHttpClient)
    {
        if (mInstance == null)
        {
            synchronized (OkHttpUtils.class)
            {
                if (mInstance == null)
                {
                    mInstance = new OkHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public static OkHttpUtils getInstance()
    {
        return initClient(null);
    }


    public Executor getDelivery()
    {
        return mPlatform.defaultCallbackExecutor();
    }

    public OkHttpClient getOkHttpClient()
    {
        return mOkHttpClient;
    }

    public static GetBuilder get(Object tag)
    {
        return new GetBuilder().tag(tag);
    }

    public static PostStringBuilder postString(Object tag)
    {
        return new PostStringBuilder().tag(tag);
    }

    public static PostStringBuilder postJson(Object tag)
    {
        return new PostStringBuilder().tag(tag).mediaType(MediaType.parse("application/json"));
    }

    public static PostFileBuilder postFile(Object tag)
    {
        return new PostFileBuilder().tag(tag);
    }

    public static PostFormBuilder post()
    {
        return new PostFormBuilder();
    }

    public static PostFormBuilder post(Object tag)
    {
        PostFormBuilder builder = new PostFormBuilder().tag(tag);
//        if (!TextUtils.isEmpty(getToken())) {
//            builder.addParams("token", getToken());
//            Trace.e("token : " + getToken());
//        }
        return builder;
    }

    public static OtherRequestBuilder put(Object tag)
    {
        return new OtherRequestBuilder(METHOD.PUT).tag(tag);
    }

    public static HeadBuilder head(Object tag)
    {
        return (HeadBuilder) new HeadBuilder().tag(tag);
    }

    public static OtherRequestBuilder delete(Object tag)
    {
        return new OtherRequestBuilder(METHOD.DELETE).tag(tag);
    }

    public static OtherRequestBuilder patch(Object tag)
    {
        return new OtherRequestBuilder(METHOD.PATCH).tag(tag);
    }

    public static OtherRequestBuilder options(Object tag)
    {
        return new OtherRequestBuilder(METHOD.OPTIONS).tag(tag);
    }

    public void execute(final RequestCall requestCall, Callback callback)
    {
        if (callback == null)
            callback = Callback.CALLBACK_DEFAULT;
        final Callback finalCallback = callback;
        final int id = requestCall.getOkHttpRequest().getId();

        requestCall.getCall().enqueue(new okhttp3.Callback()
        {
            @Override
            public void onFailure(Call call, final IOException e)
            {
                sendFailResultCallback(call, e, finalCallback, id);
            }

            @Override
            public void onResponse(final Call call, final Response response)
            {
                if (call.isCanceled())
                {
                    sendFailResultCallback(call, new IOException("Canceled!"), finalCallback, id);
                    return;
                }

                if (!finalCallback.validateReponse(response, id))
                {
                    sendFailResultCallback(call, new IOException("request failed , reponse's code is : " + response.code()), finalCallback, id);
                    return;
                }

                try
                {
                    String cookie = PreferenceUtil.readString("Cookie");
                    if (null != response.header("Set-Cookie") && response.header("Set-Cookie").equals(cookie)) {
                        Trace.e("cookie is same");
                    } else {
                        Trace.e("cookie is not same");
                    }
                    Object o = finalCallback.parseNetworkResponse(response, id);
                    sendSuccessResultCallback(o, finalCallback, id);
                } catch (Exception e)
                {
                    sendFailResultCallback(call, e, finalCallback, id);
                }

            }
        });
    }


    public void sendFailResultCallback(final Call call, final Exception e, final Callback callback, final int id)
    {
        if (callback == null) return;

        mPlatform.execute(new Runnable()
        {
            @Override
            public void run()
            {
                callback.onError(call, e, id);
                callback.onAfter(id);
            }
        });
    }

    public void sendSuccessResultCallback(final Object object, final Callback callback, final int id)
    {
        if (callback == null) return;
        mPlatform.execute(new Runnable()
        {
            @Override
            public void run()
            {
                callback.onResponse(object, id);
                callback.onAfter(id);
            }
        });
    }

    public void cancelTag(Object tag)
    {
        for (Call call : mOkHttpClient.dispatcher().queuedCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
        for (Call call : mOkHttpClient.dispatcher().runningCalls())
        {
            if (tag.equals(call.request().tag()))
            {
                call.cancel();
            }
        }
    }

    public static class METHOD
    {
        public static final String HEAD = "HEAD";
        public static final String DELETE = "DELETE";
        public static final String PUT = "PUT";
        public static final String PATCH = "PATCH";
        public static final String OPTIONS = "OPTIONS";
    }
}

