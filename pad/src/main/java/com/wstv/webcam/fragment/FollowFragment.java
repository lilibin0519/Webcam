package com.wstv.webcam.fragment;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.R;
import com.wstv.webcam.holder.follow.FollowViewListener;
import com.wstv.webcam.http.HttpService;
import com.wstv.webcam.http.callback.BaseCallback;
import com.wstv.webcam.http.model.follow.Follow;
import com.wstv.webcam.http.model.follow.FollowResult;

import butterknife.Bind;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;

/**
 * Created by Kindred on 2019/3/15.
 */

public class FollowFragment extends BaseRecyclerFragment<Follow> {

    @Bind(R.id.fragment_recycler_title)
    LinearLayout titleLayout;

    @Bind(R.id.title_bar_background)
    ViewGroup titleParent;

    @Bind(R.id.title_bar_parent)
    Toolbar titleParentLayout;

    @Bind(R.id.temp_status)
    View tempStatus;

    private int currentPage = 1;

    @Override
    public DefaultAdapterViewListener<Follow> getViewListener() {
        return new FollowViewListener();
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_follow;
    }

    @Override
    public void initView() {
        initTitle();
        content.setRefreshEnable(true);
//        adapter.notifyDataSetChanged();
        requestData();
    }

    private void requestData() {
        HttpService.followList(this, String.valueOf(currentPage), String.valueOf(AppConstant.DEFAULT_PAGE_SIZE), PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID),
                new BaseCallback<FollowResult>(getContext(), this) {
            @Override
            public void onSuccess(FollowResult followResult, int id) {
                if (null != followResult.detail && followResult.detail.size() > 0) {
                    data.addAll(followResult.detail);
                }
                content.setHasBottom(null != followResult.detail && followResult.detail.size() == AppConstant.DEFAULT_PAGE_SIZE);
                content.setLoadMoreEnable(null != followResult.detail && followResult.detail.size() == AppConstant.DEFAULT_PAGE_SIZE);
                content.complete();
                adapter.notifyDataSetChanged();
            }

                    @Override
                    protected void onFailed(String errCode, String errMsg, FollowResult followResult) {
                        content.complete();
                    }
                });
    }

    private void initTitle() {
        titleLayout.setVisibility(View.VISIBLE);
        setTempStatus();
        if (null != title) {
            title.setText("关注");
        }
    }

    private void setTempStatus() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tempStatus.getLayoutParams();
        params.height = getStatusHeight(getContext());
        tempStatus.setLayoutParams(params);
        titleParent.post(new Runnable() {
            @Override
            public void run() {
                if (titleParent.getHeight() == titleParentLayout.getHeight()) {
                    tempStatus.setVisibility(View.VISIBLE);
                }
            }
        });
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
        currentPage = 1;
        data.clear();
        adapter.notifyDataSetChanged();
        requestData();
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        requestData();
    }
}
