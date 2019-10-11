package com.wstv.webcam.holder.chat;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wstv.webcam.R;
import com.wstv.webcam.entity.ChatRoom;
import com.wstv.webcam.util.click.ClickProxy;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import em.sang.com.allrecycleview.holder.CustomHolder;

/**
 * Created by Kindred on 2019/5/21.
 */

public class ChatRoomHolder extends CustomHolder<ChatRoom> {

    public ChatRoomHolder(Context context, List<ChatRoom> lists, int itemID) {
        super(context, lists, itemID);
    }

    @Override
    public void initView(final int position, final List<ChatRoom> datas, Context context) {
        itemView.setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClick(position, datas.get(position));
                }
            }
        }));
        Glide.with(context).load(datas.get(position).userAvatar).into((ImageView) holderHelper.getView(R.id.item_chat_list_avatar));
        holderHelper.setText(R.id.item_chat_list_time, new SimpleDateFormat("MM/dd HH:mm", Locale.SIMPLIFIED_CHINESE).format(datas.get(position).createTime));
        holderHelper.setText(R.id.item_chat_list_content, datas.get(position).message);
        holderHelper.setText(R.id.item_chat_list_name, datas.get(position).userName);
    }
}
