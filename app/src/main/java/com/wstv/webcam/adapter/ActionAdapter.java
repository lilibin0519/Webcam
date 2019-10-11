package com.wstv.webcam.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.wstv.webcam.holder.action.ActionBottomListener;
import com.wstv.webcam.holder.action.ActionCenterListener;
import com.wstv.webcam.holder.action.ActionTopListener;
import com.wstv.webcam.http.model.action.ActionBean;

import java.util.List;

import em.sang.com.allrecycleview.adapter.DefaultAdapter;
import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.holder.CustomPeakHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;

/**
 * <p>Description: </p>
 * ActionAdapter
 *
 * @author lilibin
 * @createDate 2019/5/17 16:31
 */

@SuppressWarnings("unchecked")
public class ActionAdapter extends DefaultAdapter<ActionBean> {

    private int A_TOP = 9991;

    private int A_CENTER = 9992;

    private int A_BOTTOM = 9993;

    private int[] type = {A_TOP, A_CENTER, A_BOTTOM};

    private ActionTopListener topListener;

    private ActionCenterListener centerListener;

    private ActionBottomListener bottomListener;

    public ActionAdapter(Context context, List<ActionBean> lists, int itemID, DefaultAdapterViewListener<ActionBean> listener) {
        super(context, lists, itemID, listener);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + lists.size() * 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < tops.size()) {
            return TOP;
        } else if (position < heards.size()+tops.size()) {
            return position;
        } else if (position < heards.size() + lists.size() * 3 + tops.size()) {
            return type[position % 3];
        } else if (position<heards.size() + lists.size() * 3 + tops.size()+foots.size()){
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
        } else if (viewType == A_TOP) {
            holder= topListener.getBodyHolder(context, lists, itemID);
        } else if (viewType == A_CENTER) {
            holder= centerListener.getBodyHolder(context, lists, itemID);
        } else if (viewType == A_BOTTOM) {
            holder= bottomListener.getBodyHolder(context, lists, itemID);
        } else if (viewType<heards.size() + lists.size() * 3 + tops.size()+foots.size()){
            holder= (RecyclerView.ViewHolder) foots.get(viewType - heards.size() - lists.size() * 3-tops.size());
        }else if (viewType==FOOT){
            holder = (RecyclerView.ViewHolder) booms.get(0);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int id ;
        if (position < tops.size()) {
            id=position;
            ((CustomPeakHolder) holder).initView(id, context);
        } else if (position < heards.size()+tops.size()) {
            id= position-tops.size();
            ((CustomPeakHolder) holder).initView(id, context);
        } else if (position < heards.size() + lists.size() * 3 + tops.size()) {
            id=position-heards.size()-tops.size();
            ((CustomHolder) holder).initView(id,lists, context);
        } else if (position<heards.size() + lists.size() * 3 + tops.size()+foots.size()){
            id = position-heards.size()-tops.size()-lists.size() * 3;
            ((CustomPeakHolder) holder).initView(id, context);
        }else {
            id = position-(heards.size() + lists.size() * 3 + tops.size()+foots.size());
            ((CustomPeakHolder) holder).initView(id, context);
        }
    }

    public void setTopListener(ActionTopListener topListener) {
        this.topListener = topListener;
    }

    public void setCenterListener(ActionCenterListener centerListener) {
        this.centerListener = centerListener;
    }

    public void setBottomListener(ActionBottomListener bottomListener) {
        this.bottomListener = bottomListener;
    }
}
