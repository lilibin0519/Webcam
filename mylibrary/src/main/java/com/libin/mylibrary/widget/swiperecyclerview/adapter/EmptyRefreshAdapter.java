package com.libin.mylibrary.widget.swiperecyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.libin.mylibrary.widget.swiperecyclerview.holder.EmptyHolder;

import java.util.ArrayList;
import java.util.List;

import em.sang.com.allrecycleview.adapter.RefrushAdapter;
import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.holder.CustomPeakHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;

/**
 * Create date: 2017/9/17.
 * Create time: 15:21
 * EmptyRefreshAdapter
 *
 * @author lilibin
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class EmptyRefreshAdapter<T> extends RefrushAdapter<T> {

    public static final int EMPTY = 100003;

    protected List<EmptyHolder> empty = new ArrayList<>();

    public EmptyRefreshAdapter(Context context, List<T> lists, int itemID, DefaultAdapterViewListener<T> listener) {
        super(context, lists, itemID, listener);
    }

    @Override
    public int getItemCount() {
        return getBodySize() + getEmptySize() + heards.size() + foots.size() + tops.size() + booms.size();
    }

    public void addEmpty(EmptyHolder emptyHolder) {
        empty.clear();
        empty.add(emptyHolder);
    }

    @Override
    public int getBodySize() {
        return lists == null ? 0 : lists.size();
    }

    int getEmptySize() {
        return getBodySize() > 0 ? 0 : empty.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == refrushPosition && tops.size() > 0) {
            return TOP;
        } else if (position < heards.size() + tops.size()) {
            if (position < refrushPosition) {
                return position;
            } else if (position > refrushPosition && tops.size() > 0) {
                return position;
            } else {
                return position;
            }
        } else if (position < heards.size() + getBodySize() + tops.size()) {
            return BODY;
        } else if (position < heards.size() + getEmptySize() + tops.size()) {
            return EMPTY;
        } else if (position < heards.size() + getBodySize() + getEmptySize() + tops.size() + foots.size()) {
            return position;
        } else {
            return FOOT;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder holder = null;

        if (viewType == TOP) {
            holder = tops.get(0);
        } else if (viewType < heards.size() + tops.size()) {
            if (viewType > refrushPosition && tops.size() > 0) {
                holder = heards.get(viewType - tops.size());
            } else {
                holder = heards.get(viewType);
            }
        } else if (viewType == BODY) {
            holder = ((DefaultAdapterViewListener<T>) listener).getBodyHolder(context, lists, itemID);
        } else if (viewType == EMPTY) {
            holder = empty.get(0);
        } else if (viewType < heards.size() + getBodySize() + getEmptySize() + tops.size() + foots.size()) {
            holder = foots.get(viewType - heards.size() - getBodySize() - getEmptySize() - tops.size());
        } else if (viewType == FOOT) {
            holder = booms.get(0);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int id;
        if (position == refrushPosition && tops.size() > 0) {
            id = position;
            ((CustomPeakHolder) holder).initView(id, context);
        } else if (position < heards.size() + tops.size()) {
            if (position < refrushPosition) {
                id = position;
            } else if (position > refrushPosition && tops.size() > 0) {
                id = position - tops.size();
            } else {
                id = position - tops.size();
            }
            ((CustomPeakHolder) holder).initView(id, context);
        } else if (position < heards.size() + getBodySize() + tops.size()) {
            id = position - heards.size() - tops.size();
            ((CustomHolder) holder).initView(id, lists, context);
        } else if (position < heards.size() + getEmptySize() + tops.size()) {
            id = position - heards.size() - tops.size();
            ((CustomHolder) holder).initView(id, lists, context);
        } else if (position < heards.size() + getBodySize() + getEmptySize() + tops.size() + foots.size()) {
            id = position - heards.size() - tops.size() - getBodySize() - getEmptySize();
            ((CustomPeakHolder) holder).initView(id, context);
        } else {
            id = position - (heards.size() + getBodySize() + getEmptySize() + tops.size() + foots.size());
            ((CustomPeakHolder) holder).initView(id, context);
        }
    }
}
