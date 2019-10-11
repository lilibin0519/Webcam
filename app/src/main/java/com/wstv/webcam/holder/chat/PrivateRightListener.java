package com.wstv.webcam.holder.chat;

import android.content.Context;

import com.wstv.webcam.entity.PrivateMessage;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * <p>Description: </p>
 * PrivateRightListener
 *
 * @author lilibin
 * @createDate 2019/5/20 11:20
 */

public class PrivateRightListener extends DefaultAdapterViewListener<PrivateMessage> {

    private final OnToolsItemClickListener<PrivateMessage> listener;

    public PrivateRightListener(OnToolsItemClickListener<PrivateMessage> listener){
        this.listener = listener;
    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<PrivateMessage> lists, int itemID) {
        PrivateChatRightHolder holder = new PrivateChatRightHolder(context, lists);
        holder.setOnTOnToolsItemClickListener(listener);
        return holder;
    }
}
