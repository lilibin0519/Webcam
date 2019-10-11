package com.libin.mylibrary.http.builder;

import com.libin.mylibrary.http.OkHttpUtils;
import com.libin.mylibrary.http.request.OtherRequest;
import com.libin.mylibrary.http.request.RequestCall;

/**
 * Created by zhy on 16/3/2.
 */
public class HeadBuilder extends GetBuilder
{
    @Override
    public RequestCall build()
    {
        return new OtherRequest(null, null, OkHttpUtils.METHOD.HEAD, url, tag, params, headers,id).build();
    }
}
