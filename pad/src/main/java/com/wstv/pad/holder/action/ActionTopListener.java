package com.wstv.pad.holder.action;

import android.content.Context;

import com.wstv.pad.http.model.action.ActionBean;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;

/**
 * Created by Kindred on 2019/5/17.
 */

public class ActionTopListener extends DefaultAdapterViewListener<ActionBean> {

//    private final OnToolsItemClickListener<ActionBean> listener;
//
//    public ActionTopListener(OnToolsItemClickListener<ActionBean> listener){
//        this.listener = listener;
//    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<ActionBean> lists, int itemID) {
        ActionTopHolder holder = new ActionTopHolder(context, lists);
//        holder.setOnTOnToolsItemClickListener(listener);
        return holder;
    }
}