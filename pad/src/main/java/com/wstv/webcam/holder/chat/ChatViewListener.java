package com.wstv.webcam.holder.chat;

import android.content.Context;

import com.wstv.webcam.tencent.roomutil.misc.TextChatMsg;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;

/**
 * Created by Kindred on 2019/4/10.
 */

public class ChatViewListener extends DefaultAdapterViewListener<TextChatMsg> {

    @Override
    public CustomHolder getBodyHolder(Context context, List<TextChatMsg> lists, int itemID) {
        return new ChatHolder(context, lists, itemID);
    }
}
