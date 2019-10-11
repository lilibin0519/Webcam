package com.wstv.pad.holder.message;

import android.content.Context;

import com.wstv.pad.http.model.MessageBean;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;

/**
 * Created by Kindred on 2019/3/25.
 */

public class MessageHolder extends CustomHolder<MessageBean> {

    public MessageHolder(Context context, List<MessageBean> lists, int itemID) {
        super(context, lists, itemID);
    }

    @Override
    public void initView(int position, List<MessageBean> datas, Context context) {

    }
}
