package com.wstv.webcam.holder.cash;

import android.content.Context;

import com.wstv.webcam.http.model.account.AccountLog;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Created by Kindred on 2019/3/25.
 */

public class CashViewListener extends DefaultAdapterViewListener<AccountLog> {

    private OnToolsItemClickListener<AccountLog> listener;

    public CashViewListener(OnToolsItemClickListener<AccountLog> listener){
        this.listener = listener;
    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<AccountLog> lists, int itemID) {
        CashHolder holder = new CashHolder(context, lists, itemID);
        holder.setOnTOnToolsItemClickListener(listener);
        return holder;
    }
}
