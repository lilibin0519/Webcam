package com.libin.mylibrary.widget.swiperecyclerview.adapter;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;

import com.libin.mylibrary.widget.swiperecyclerview.footerView.BaseFooterView;

import em.sang.com.allrecycleview.adapter.DefaultAdapter;

/**
 * Create date: 2017/10/11.
 * Create time: 20:24
 * WrapperAdapter
 *
 * @author lilibin
 * @version 1.0
 */
@SuppressWarnings("unchecked")
public class WrapperAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int TYPE_FOOTER = 0x100;

    DefaultAdapter mInnerAdapter;

    private BaseFooterView mFootView;

    private boolean hasBottom = true;

    private GridLayoutManager.SpanSizeLookup mSpanSizeLookup;

    public WrapperAdapter(DefaultAdapter adapter, BaseFooterView mFootView){
        this.mInnerAdapter = adapter;
        this.mFootView = mFootView;
    }

    public DefaultAdapter getInnerAdapter() {
        return mInnerAdapter;
    }

    public void setInnerAdapter(EmptyRefreshAdapter mInnerAdapter) {
        this.mInnerAdapter = mInnerAdapter;
    }

    public boolean isLoadMoreItem(int position){
        return hasBottom && mInnerAdapter.getBodySize() > 0 && position == getItemCount() - 1;
    }

    public boolean isLoadMoreEnable() {
        return hasBottom;
    }

    public void setHasBottom(boolean hasBottom) {
        this.hasBottom = hasBottom;
        notifyDataSetChanged();
    }

    public void setmSpanSizeLookup(GridLayoutManager.SpanSizeLookup mSpanSizeLookup) {
        this.mSpanSizeLookup = mSpanSizeLookup;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(TYPE_FOOTER == viewType){
            return new FooterViewHolder(mFootView);
        }
        return mInnerAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(isLoadMoreItem(position)){
            return;
        }
        mInnerAdapter.onBindViewHolder(holder, position);
    }


    @Override
    public int getItemViewType(int position) {
        if(isLoadMoreItem(position)){
            return TYPE_FOOTER;
        }else{
            return mInnerAdapter.getItemViewType(position);
        }
    }

    @Override
    public int getItemCount() {
        int count = mInnerAdapter == null ? 0 : mInnerAdapter.getItemCount();

        //without loadingMore when adapter size is zero
        if(count == 0){
            return 0;
        }
        return hasBottom && mInnerAdapter.getBodySize() > 0 ? count + 1 : count;
    }

    @Override
    public long getItemId(int position) {
        return mInnerAdapter.getItemId(position);
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && isLoadMoreItem(holder.getLayoutPosition()))
        {
            StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
        mInnerAdapter.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewDetachedFromWindow(holder);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    boolean isLoadMore = isLoadMoreItem(position);
                    if(mSpanSizeLookup != null && !isLoadMore){
                        return mSpanSizeLookup.getSpanSize(position);
                    }
                    return isLoadMore ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        mInnerAdapter.onDetachedFromRecyclerView(recyclerView);
    }

    @Override
    public boolean onFailedToRecycleView(RecyclerView.ViewHolder holder) {
        return mInnerAdapter.onFailedToRecycleView(holder);
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        mInnerAdapter.registerAdapterDataObserver(observer);
    }

    @Override
    public void unregisterAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        mInnerAdapter.unregisterAdapterDataObserver(observer);
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        mInnerAdapter.onViewRecycled(holder);
    }

    /**
     * ViewHolder of footerView
     */
    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }
}