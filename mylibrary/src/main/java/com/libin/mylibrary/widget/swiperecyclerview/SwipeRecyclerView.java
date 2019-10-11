package com.libin.mylibrary.widget.swiperecyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.GridLayoutManager.SpanSizeLookup;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.LayoutManager;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.libin.mylibrary.R;
import com.libin.mylibrary.widget.swiperecyclerview.adapter.WrapperAdapter;
import com.libin.mylibrary.widget.swiperecyclerview.footerView.BaseFooterView;
import com.libin.mylibrary.widget.swiperecyclerview.footerView.FooterViewListener;
import com.libin.mylibrary.widget.swiperecyclerview.footerView.SimpleFooterView;

import em.sang.com.allrecycleview.adapter.DefaultAdapter;


/**
 * @auther deadline
 * @time   2016/10/22
 * SwipeRefreshLayout + recyclerView
 */
public class SwipeRecyclerView extends FrameLayout
                implements SwipeRefreshLayout.OnRefreshListener{

    private View mEmptyView;
    private BaseFooterView mFootView;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mRefreshLayout;

    private LayoutManager mLayoutManager;
    private OnLoadListener mListener;
    private DataObserver mDataObserver;
    private WrapperAdapter mWrapperAdapter;

    private boolean isEmptyViewShowing;
    private boolean isLoadingMore;
    private boolean isLoadMoreEnable;
    private boolean isRefreshEnable;

    private int lastVisiablePosition = 0;

    public SwipeRecyclerView(Context context) {
        this(context, null);
    }

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setupSwipeRecyclerView();
    }

    public void setHasBottom(boolean hasBottom){
        mWrapperAdapter.setHasBottom(hasBottom);
    }

    private void setupSwipeRecyclerView() {

        isEmptyViewShowing = false;
        isRefreshEnable = true;
        isLoadingMore = false;
        isLoadMoreEnable = true;

        mFootView = new SimpleFooterView(getContext());

        View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_swipe_recyclerview, this);
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.SwipeRefreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.RecyclerView);
        mLayoutManager = recyclerView.getLayoutManager();

        mRefreshLayout.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // do nothing if load more is not enable or refreshing or loading more
                if(!isLoadMoreEnable || isRefreshing() || isLoadingMore){
                    return;
                }

                //get the lastVisiablePosition
                mLayoutManager = recyclerView.getLayoutManager();
                if(mLayoutManager instanceof LinearLayoutManager){
                    lastVisiablePosition = ((LinearLayoutManager)mLayoutManager).findLastVisibleItemPosition();
                }else if(mLayoutManager instanceof GridLayoutManager){
                    lastVisiablePosition = ((GridLayoutManager)mLayoutManager).findLastCompletelyVisibleItemPosition();
                }else if(mLayoutManager instanceof StaggeredGridLayoutManager){
                    int[] into = new int[((StaggeredGridLayoutManager) mLayoutManager).getSpanCount()];
                    ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(into);
                    lastVisiablePosition = findMax(into);
                }

                int childCount = mWrapperAdapter == null ? 0 : mWrapperAdapter.getItemCount();
                if(childCount > 1 && lastVisiablePosition == childCount - 1){

                    if(mListener != null){
                        isLoadingMore = true;
                        mListener.onLoadMore();
                    }
                }
            }
        });
//        recyclerView.setOnScrollListener(new OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                // do nothing if load more is not enable or refreshing or loading more
//                if(!isLoadMoreEnable || isRefreshing() || isLoadingMore){
//                    return;
//                }
//
//                //get the lastVisiablePosition
//                mLayoutManager = recyclerView.getLayoutManager();
//                if(mLayoutManager instanceof LinearLayoutManager){
//                    lastVisiablePosition = ((LinearLayoutManager)mLayoutManager).findLastVisibleItemPosition();
//                }else if(mLayoutManager instanceof GridLayoutManager){
//                    lastVisiablePosition = ((GridLayoutManager)mLayoutManager).findLastCompletelyVisibleItemPosition();
//                }else if(mLayoutManager instanceof StaggeredGridLayoutManager){
//                    int[] into = new int[((StaggeredGridLayoutManager) mLayoutManager).getSpanCount()];
//                    ((StaggeredGridLayoutManager) mLayoutManager).findLastVisibleItemPositions(into);
//                    lastVisiablePosition = findMax(into);
//                }
//
//                int childCount = mWrapperAdapter == null ? 0 : mWrapperAdapter.getItemCount();
//                if(childCount > 1 && lastVisiablePosition == childCount - 1){
//
//                    if(mListener != null){
//                        isLoadingMore = true;
//                        mListener.onLoadMore();
//                    }
//                }
//            }
//        });
    }

    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * set is enable pull to refresh
     * @param refreshEnable
     */
    public void setRefreshEnable(boolean refreshEnable){
        isRefreshEnable = refreshEnable;
        mRefreshLayout.setEnabled(isRefreshEnable);
    }

    public boolean getRefreshEnable(){
        return isRefreshEnable;
    }

    /**
     * set is loading more enable
     * @param loadMoreEnable
     *              if true when recyclerView scroll to bottom load more action will be trigger
     */
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        if(!loadMoreEnable){
            stopLoadingMore();
        }
        isLoadMoreEnable = loadMoreEnable;
    }

    public boolean getLoadMoreEnable(){
        return isLoadMoreEnable;
    }

    /**
     * get is refreshing
     * @return
     */
    public boolean isRefreshing(){
        return mRefreshLayout.isRefreshing();
    }

    /**
     * get is loading more
     * @return
     */
    public boolean isLoadingMore(){
        return isLoadingMore;
    }

    /**
     * is empty view showing
     * @return
     */
    public boolean isEmptyViewShowing(){
        return isEmptyViewShowing;
    }

    /**
     * you may need set some other attributes of swipeRefreshLayout
     * @return
     *     swipeRefreshLayout
     */
    public SwipeRefreshLayout getSwipeRefreshLayout(){
        return mRefreshLayout;
    }

    /**
     * you may need set some other attributes of RecyclerView
     * @return
     *     RecyclerView
     */
    public RecyclerView getRecyclerView(){
        return recyclerView;
    }

    /**
     * set load more listener
     * @param listener
     */
    public void setOnLoadListener(OnLoadListener listener){
        mListener = listener;
    }

    /**
     * support for GridLayoutManager
     * @param spanSizeLookup
     */
    public void setSpanSizeLookup(SpanSizeLookup spanSizeLookup){
        mWrapperAdapter.setmSpanSizeLookup(spanSizeLookup);
    }

    /**
     * set the footer view
     * @param footerView
     *        the view to be showing when pull up
     */
    public void setFooterView(BaseFooterView footerView){
        if(footerView != null) {
            this.mFootView = footerView;
        }
    }

    /**
     * set a empty view like listview
     * @param emptyView
     *        the view to be showing when the data set size is zero
     */
    public void setEmptyView(View emptyView){
        if(mEmptyView != null){
            removeView(mEmptyView);
        }
        this.mEmptyView = emptyView;

        if(mDataObserver != null) {
            mDataObserver.onChanged();
        }
    }

    /**
     * set adapter to recyclerView
     * @param adapter
     */
    public <T extends DefaultAdapter> void setAdapter(T adapter){
        if(adapter != null) {
            if(mDataObserver == null){
                mDataObserver = new DataObserver();
            }
            mWrapperAdapter = new WrapperAdapter(adapter, mFootView);
            recyclerView.setAdapter(mWrapperAdapter);
            adapter.registerAdapterDataObserver(mDataObserver);
            mDataObserver.onChanged();
        }
    }

    /**
     * refresh or load more completed
     */
    public void complete(){
        mRefreshLayout.setRefreshing(false);
        stopLoadingMore();
    }

    /**
     * set refreshing
     * if you want load data when first in, you can setRefreshing(true)
     * after {@link #setOnLoadListener(OnLoadListener)}
     * @param refreshing
     */
    public void setRefreshing(boolean refreshing){
        mRefreshLayout.setRefreshing(refreshing);
        if(refreshing && !isLoadingMore && mListener != null){
            mListener.onRefresh();
        }
    }

    /**
     * stop loading more without animation
     */
    public void stopLoadingMore(){
        isLoadingMore = false;
        if(mWrapperAdapter != null) {
            mWrapperAdapter.notifyItemRemoved(mWrapperAdapter.getItemCount());
        }
    }
    
    /**
     * call method {@link OnLoadListener#onRefresh()}
     */
    @Override
    public void onRefresh() {
        if(mListener != null){
            //reset footer view status loading
            if(mFootView != null){
                mFootView.onLoadingMore();
            }
            mListener.onRefresh();
        }
    }

    /**
     * {@link FooterViewListener#onNetChange(boolean isAvailable)}
     * call when network is available or not available
     */
    public void onNetChange(boolean isAvailable) {
        if(mFootView != null){
            mFootView.onNetChange(isAvailable);
        }
    }

    /**
     * {@link FooterViewListener#onLoadingMore()}
     * call when you need change footer view to loading status
     */
    public void onLoadingMore() {
        if(mFootView != null){
            mFootView.onLoadingMore();
        }
    }

    /**
     * {@link FooterViewListener#onNoMore(CharSequence message)}
     * call when no more data add to list
     */
    public void onNoMore(CharSequence message) {
        if(mFootView != null){
            mFootView.onNoMore(message);
        }
    }

    /**
     * {@link FooterViewListener#onNoMore(CharSequence message)}
     * call when no more data add to list
     */
    public void onNoMore() {
        onNoMore(null);
    }

    /**
     * {@link FooterViewListener#onError(CharSequence message)}
     * call when you need show error message
     */
    public void onError(CharSequence message) {
        if(mFootView != null){
            mFootView.onError(message);
        }
    }

    /**
     * {@link FooterViewListener#onError(CharSequence message)}
     * call when you need show error message
     */
    public void onError() {
        onError(null);
    }

    /**
     * ViewHolder of footerView
     */
    private class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * a inner class used to monitor the dataSet change
     * <p>
     * because wrapperAdapter do not know when wrapperAdapter.mInnerAdapter
     * <p>
     * dataSet changed, these method are final
     */
    class DataObserver extends RecyclerView.AdapterDataObserver{

        @Override
        public void onChanged() {
            super.onChanged();
            RecyclerView.Adapter adapter = recyclerView.getAdapter();
            if(adapter != null && mEmptyView != null){

                int count = 0;
                if(isLoadMoreEnable && adapter.getItemCount() != 0){
                    count ++;
                }
                if(adapter.getItemCount() == count){
                    isEmptyViewShowing = true;
                    if(mEmptyView.getParent() == null){
                        LayoutParams params = new LayoutParams(
                                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        params.gravity = Gravity.CENTER;

                        addView(mEmptyView, params);
                    }

                    recyclerView.setVisibility(GONE);
                    mEmptyView.setVisibility(VISIBLE);
                }else{
                    isEmptyViewShowing = false;
                    mEmptyView.setVisibility(GONE);
                    recyclerView.setVisibility(VISIBLE);
                }
            }
            mWrapperAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            super.onItemRangeChanged(positionStart, itemCount);
            mWrapperAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            super.onItemRangeChanged(positionStart, itemCount, payload);
            mWrapperAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            super.onItemRangeInserted(positionStart, itemCount);
            mWrapperAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            super.onItemRangeMoved(fromPosition, toPosition, itemCount);
           mWrapperAdapter.notifyItemRangeRemoved(fromPosition, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            super.onItemRangeRemoved(positionStart, itemCount);
            mWrapperAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

    }

    public interface OnLoadListener {

        void onRefresh();

        void onLoadMore();
    }
}
