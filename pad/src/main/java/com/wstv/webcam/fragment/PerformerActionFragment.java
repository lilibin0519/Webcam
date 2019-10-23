package com.wstv.webcam.fragment;

import android.os.Bundle;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.R;
import com.wstv.webcam.adapter.ActionAdapter;
import com.wstv.webcam.holder.action.ActionBottomListener;
import com.wstv.webcam.holder.action.ActionCenterListener;
import com.wstv.webcam.holder.action.ActionTopListener;
import com.wstv.webcam.http.HttpService;
import com.wstv.webcam.http.callback.BaseCallback;
import com.wstv.webcam.http.model.EmptyResult;
import com.wstv.webcam.http.model.action.ActionBean;
import com.wstv.webcam.http.model.action.ActionResult;

import em.sang.com.allrecycleview.adapter.DefaultAdapter;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Created by Kindred on 2019/4/10.
 */

public class PerformerActionFragment extends BaseRecyclerFragment<ActionBean> {

    private String performerId;

    private ActionTopListener topListener;

    private int currentPage = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            performerId = getArguments().getString("performerId");
        }
    }

    @Override
    public DefaultAdapterViewListener<ActionBean> getViewListener() {
        return topListener = new ActionTopListener();
    }

    @Override
    public int getItemLayoutId() {
        return 0;
    }

    @Override
    public void initView() {
        ((ActionAdapter) adapter).setTopListener(topListener);
        ((ActionAdapter) adapter).setCenterListener(new ActionCenterListener());
        ((ActionAdapter) adapter).setBottomListener(new ActionBottomListener(new OnToolsItemClickListener<ActionBean>() {
            @Override
            public void onItemClick(int position, ActionBean item) {
                onLikeClick(position, item);
            }
        }));
        content.setRefreshEnable(true);

        content.setPadding(getResources().getDimensionPixelOffset(R.dimen.qb_px_40), 0, getResources().getDimensionPixelOffset(R.dimen.qb_px_40), 0);
        requestData();
    }

    private void requestData() {
        HttpService.getMoments(this, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), performerId, currentPage, AppConstant.DEFAULT_PAGE_SIZE, new BaseCallback<ActionResult>(getContext(), this) {
            @Override
            public void onSuccess(ActionResult actionResult, int id) {
                content.setHasBottom(null != actionResult.detail && actionResult.detail.size() == AppConstant.DEFAULT_PAGE_SIZE);
                content.setLoadMoreEnable(null != actionResult.detail && actionResult.detail.size() == AppConstant.DEFAULT_PAGE_SIZE);
                if (null != actionResult.detail) {
                    data.addAll(actionResult.detail);
                }
                content.complete();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void onLikeClick(final int position, ActionBean item) {
        HttpService.likeMoment(this, item.id, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), new BaseCallback<EmptyResult>(getContext(), this) {
            @Override
            public void onSuccess(EmptyResult emptyResult, int id) {
                data.get(position).ifLike = true;
                data.get(position).likeCnt = String.valueOf(Integer.parseInt(data.get(position).likeCnt) + 1);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public DefaultAdapter<ActionBean> getAdapter() {
        return new ActionAdapter(getContext(), data, getItemLayoutId(), getViewListener());
    }

    @Override
    protected void onUserVisible() {

    }

    @Override
    protected void onUserInvisible() {

    }

    @Override
    protected void onFirstUserInVisible() {

    }

    @Override
    protected void onFirstUserVisible() {

    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    public void onRefresh() {
        data.clear();
        adapter.notifyDataSetChanged();
        currentPage = 1;
        requestData();
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        requestData();
    }
}
