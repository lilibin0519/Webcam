package com.wstv.pad.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.libin.mylibrary.widget.swiperecyclerview.SwipeRecyclerView;
import com.wstv.pad.R;

import java.util.ArrayList;
import java.util.List;

import em.sang.com.allrecycleview.adapter.DefaultAdapter;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;

/**
 * Created by Kindred on 2019/1/6.
 */

public abstract class BaseRecyclerFragment<T> extends BaseFragment implements SwipeRecyclerView.OnLoadListener {

    public SwipeRecyclerView content;

    public DefaultAdapter<T> adapter;

    public List<T> data;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_recycler;
    }

    @Override
    protected View getLoadingTargetView() {
        return content/*.getRecyclerView()*/;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        content = view.findViewById(R.id.fragment_recycler_content);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected void initViewAndData() {
        initList();
        initView();
    }

    protected void initList() {
        content.getRecyclerView().setLayoutManager(getLayoutManager());
        data = new ArrayList<>();
        content.setAdapter(adapter = getAdapter());
        content.setRefreshEnable(false);
        content.setLoadMoreEnable(false);
        content.setHasBottom(false);
        content.setOnLoadListener(this);
        content.getRecyclerView().setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
    }

    protected RecyclerView.LayoutManager getLayoutManager() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        return manager;
    }

    public DefaultAdapter<T> getAdapter(){
        return new DefaultAdapter<>(getContext(), data, getItemLayoutId(), getViewListener());
    }

    public abstract DefaultAdapterViewListener<T> getViewListener();

    public abstract int getItemLayoutId();

    public abstract void initView();
}
