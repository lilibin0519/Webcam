package com.wstv.webcam.holder.chat;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wstv.webcam.R;
import com.wstv.webcam.entity.PrivateMessage;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;

/**
 * <p>Description: </p>
 * PrivateChatLeftHolder
 *
 * @author lilibin
 * @createDate 2019/5/20 11:11
 */

public class PrivateChatLeftHolder extends CustomHolder<PrivateMessage> {

    public PrivateChatLeftHolder(Context context, List<PrivateMessage> lists) {
        super(context, lists, R.layout.item_chat_room_left);
    }

    @Override
    public void initView(int position, List<PrivateMessage> datas, Context context) {
        holderHelper.setVisibility(R.id.item_chat_room_left_img, datas.get(position).type == 0 ? View.VISIBLE : View.GONE);
        holderHelper.setVisibility(R.id.item_chat_room_left_text, datas.get(position).type == 1 ? View.VISIBLE : View.GONE);
        Glide.with(context).load(datas.get(position).userAvatar).dontAnimate().placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar).into((ImageView) holderHelper.getView(R.id.item_chat_room_left_avatar));
        if (datas.get(position).type == 0) {
            Glide.with(context).load(datas.get(position).userAvatar).dontAnimate().placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar).into((ImageView) holderHelper.getView(R.id.item_chat_room_left_img));
        }
        holderHelper.setText(R.id.item_chat_room_left_text, datas.get(position).message);
    }
}
