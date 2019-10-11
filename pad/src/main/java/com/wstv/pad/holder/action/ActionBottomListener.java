package com.wstv.pad.holder.action;

import android.content.Context;

import com.wstv.pad.http.model.action.ActionBean;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Created by Kindred on 2019/5/17.
 */

public class ActionBottomListener extends DefaultAdapterViewListener<ActionBean> {

    private final OnToolsItemClickListener<ActionBean> listener;

    public ActionBottomListener(OnToolsItemClickListener<ActionBean> listener){
        this.listener = listener;
    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<ActionBean> lists, int itemID) {
        ActionBottomHolder holder = new ActionBottomHolder(context, lists);
        holder.setOnTOnToolsItemClickListener(listener);
        return holder;
    }
}
