package com.wstv.pad.holder.gift;

import android.content.Context;

import com.wstv.pad.http.model.gift.GiftBean;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * <p>Description: </p>
 * GiftViewListener
 *
 * @author lilibin
 * @createDate 2019/5/14 10:03
 */

public class GiftViewListener extends DefaultAdapterViewListener<GiftBean> {

    private OnToolsItemClickListener<GiftBean> listener;

    private GiftHolder.OnGiftLongClick longClickListener;

    public GiftViewListener(OnToolsItemClickListener<GiftBean> listener, GiftHolder.OnGiftLongClick longClickListener) {
        this.listener = listener;
        this.longClickListener = longClickListener;
    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<GiftBean> lists, int itemID) {
        GiftHolder holder = new GiftHolder(context, lists, itemID);
        holder.setOnTOnToolsItemClickListener(listener);
        holder.setLongClickListener(longClickListener);
        return holder;
    }
}
