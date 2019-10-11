package com.wstv.webcam.holder.chat;

import android.content.Context;

import com.wstv.webcam.entity.ChatRoom;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Created by Kindred on 2019/5/21.
 */

public class ChatRoomListener extends DefaultAdapterViewListener<ChatRoom> {

    private final OnToolsItemClickListener<ChatRoom> listener;

    public ChatRoomListener(OnToolsItemClickListener<ChatRoom> listener){
        this.listener = listener;
    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<ChatRoom> lists, int itemID) {
        ChatRoomHolder holder = new ChatRoomHolder(context, lists, itemID);
        holder.setOnTOnToolsItemClickListener(listener);
        return holder;
    }
}
