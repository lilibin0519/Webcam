package com.wstv.webcam.activity;

import android.os.Bundle;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.base.BaseRecyclerActivity;
import com.wstv.webcam.holder.message.MessageViewListener;
import com.wstv.webcam.http.model.MessageBean;

import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Created by Kindred on 2019/3/25.
 */

public class MessageListActivity extends BaseRecyclerActivity<MessageBean> implements OnToolsItemClickListener<MessageBean> {

    @Override
    protected String getTitleStr() {
        return "消息列表";
    }

    @Override
    public DefaultAdapterViewListener<MessageBean> getViewListener() {
        return new MessageViewListener(this);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_message;
    }

    @Override
    public void initView() {
        data.add(null);
        data.add(null);
        data.add(null);
        data.add(null);
        data.add(null);
        data.add(null);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(int position, MessageBean item) {

    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void onEventComming(EventCenter center) {

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }
}
