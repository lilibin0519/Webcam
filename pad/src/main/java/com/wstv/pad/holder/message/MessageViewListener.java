package com.wstv.pad.holder.message;

import android.content.Context;

import com.wstv.pad.http.model.MessageBean;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Created by Kindred on 2019/3/25.
 */

public class MessageViewListener extends DefaultAdapterViewListener<MessageBean> {

    private OnToolsItemClickListener<MessageBean> listener;

    public MessageViewListener(OnToolsItemClickListener<MessageBean> listener){
        this.listener = listener;
    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<MessageBean> lists, int itemID) {
        MessageHolder holder = new MessageHolder(context, lists, itemID);
        holder.setOnTOnToolsItemClickListener(listener);
        return holder;
    }
}
