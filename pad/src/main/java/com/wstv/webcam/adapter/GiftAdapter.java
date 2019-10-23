package com.wstv.webcam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.wstv.webcam.http.model.gift.GiftBean;

import java.util.List;

import em.sang.com.allrecycleview.adapter.DefaultAdapter;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;

/**
 * Created by Kindred on 2019/5/13.
 */

public class GiftAdapter extends DefaultAdapter<GiftBean> {

    private int spanColumn = 4;

    private int spanRow;

    private int itemWidth;

    public GiftAdapter(Context context, List<GiftBean> lists, int itemID, DefaultAdapterViewListener<GiftBean> listener) {
        super(context, lists, itemID, listener);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = super.onCreateViewHolder(parent, viewType);
//        if (itemWidth <= 0) {
//            // 计算Item的宽度
//            itemWidth = (parent.getWidth()/* - pageMargin * 2*/) / spanColumn;
//        }
//        Trace.e("itemWidth : " + itemWidth);
////        holder.itemView.measure(0, 0);
//        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
//        if (null == layoutParams) {
//            layoutParams = new RecyclerView.LayoutParams(itemWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
//        } else {
//            layoutParams.width = itemWidth;
//            layoutParams.height = holder.itemView.getMeasuredHeight();
//        }
//        Trace.e("layoutParams.height ：" + layoutParams.height);
//        holder.itemView.setLayoutParams(layoutParams);
        return holder;
    }
}
