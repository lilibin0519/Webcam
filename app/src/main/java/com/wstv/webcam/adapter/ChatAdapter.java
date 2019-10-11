package com.wstv.webcam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.libin.mylibrary.base.util.PreferenceUtil;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.entity.PrivateMessage;
import com.wstv.webcam.holder.chat.PrivateLeftListener;
import com.wstv.webcam.holder.chat.PrivateRightListener;

import java.util.List;

import em.sang.com.allrecycleview.adapter.DefaultAdapter;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;

/**
 * <p>Description: </p>
 * ActionAdapter
 *
 * @author lilibin
 * @createDate 2019/5/17 16:31
 */

@SuppressWarnings("unchecked")
public class ChatAdapter extends DefaultAdapter<PrivateMessage> {

    private int CHAT_LEFT = 9991;

    private int CHAT_RIGHT = 9992;

    private PrivateLeftListener leftListener;

    private PrivateRightListener rightListener;

    public ChatAdapter(Context context, List<PrivateMessage> lists, int itemID, DefaultAdapterViewListener<PrivateMessage> listener) {
        super(context, lists, itemID, listener);
    }

    @Override
    public int getItemViewType(int position) {
        if (position < tops.size()) {
            return TOP;
        } else if (position < heards.size()+tops.size()) {
            return position;
        } else if (position < heards.size() + lists.size() + tops.size()) {
            return lists.get(position).userID.equals( PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID)) ? CHAT_RIGHT : CHAT_LEFT;
        } else if (position<heards.size() + lists.size() + tops.size()+foots.size()){
            return position;
        }else {
            return FOOT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        if (viewType == TOP) {
            holder = (RecyclerView.ViewHolder) tops.get(0);
        } else if (viewType < heards.size()+tops.size()) {
            holder= (RecyclerView.ViewHolder) heards.get(viewType-tops.size());
        } else if (viewType == CHAT_LEFT) {
            holder= leftListener.getBodyHolder(context, lists, itemID);
        } else if (viewType == CHAT_RIGHT) {
            holder= rightListener.getBodyHolder(context, lists, itemID);
        } else if (viewType<heards.size() + lists.size() + tops.size()+foots.size()){
            holder= (RecyclerView.ViewHolder) foots.get(viewType - heards.size() - lists.size()-tops.size());
        }else if (viewType==FOOT){
            holder = (RecyclerView.ViewHolder) booms.get(0);
        }
        return holder;
    }

    public void setLeftListener(PrivateLeftListener leftListener) {
        this.leftListener = leftListener;
    }

    public void setRightListener(PrivateRightListener rightListener) {
        this.rightListener = rightListener;
    }
}
