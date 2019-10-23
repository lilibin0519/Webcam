package com.wstv.webcam.activity.base;

import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.libin.mylibrary.widget.swiperecyclerview.SwipeRecyclerView;
import com.wstv.webcam.R;

import java.util.ArrayList;
import java.util.List;

import em.sang.com.allrecycleview.adapter.DefaultAdapter;
import em.sang.com.allrecycleview.cutline.RecycleViewDivider;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;

/**
 * <p>Description: </p>
 * BaseRecyclerActivity
 *
 * @author lilibin
 * @createDate 2018/12/18 19:39
 */

public abstract class BaseRecyclerActivity<T> extends BaseActivity implements SwipeRecyclerView.OnLoadListener {

    public SwipeRecyclerView content;

    public DefaultAdapter<T> adapter;

    public List<T> data;

    @Override
    protected void initViewAndData() {
        setTitleStr(getTitleStr());
        initList();
        initView();
    }

    protected abstract String getTitleStr();

    public abstract DefaultAdapterViewListener<T> getViewListener();

    public abstract int getItemLayoutId();

    public abstract void initView();

    protected void initList() {
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        content.getRecyclerView().setLayoutManager(manager);
        data = new ArrayList<>();
        content.setAdapter(adapter = getAdapter());
        if (addItemDecoration()) {
            content.getRecyclerView().addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));
        }
        content.setRefreshEnable(false);
        content.setLoadMoreEnable(false);
        content.setHasBottom(false);
        content.setOnLoadListener(this);
    }

    protected DefaultAdapter<T> getAdapter(){
        return new DefaultAdapter<>(this, data, getItemLayoutId(), getViewListener());
    }

    protected boolean addItemDecoration() {
        return true;
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.layout_recycler;
    }

    @Override
    protected View getLoadingTargetView() {
        content = findViewById(R.id.layout_recycler_content);
        return content.getRecyclerView();
    }
}
