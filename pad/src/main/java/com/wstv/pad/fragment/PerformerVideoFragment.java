package com.wstv.pad.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.wstv.pad.R;
import com.wstv.pad.activity.PerformerActivity;
import com.wstv.pad.activity.VideoActivity;
import com.wstv.pad.holder.action.VideoViewListener;
import com.wstv.pad.http.HttpService;
import com.wstv.pad.http.callback.BaseCallback;
import com.wstv.pad.http.model.action.VideoBean;
import com.wstv.pad.http.model.action.VideoResult;

import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

public class PerformerVideoFragment extends BaseRecyclerFragment<VideoBean> implements OnToolsItemClickListener<VideoBean> {

    private String performerId;

    private int currentPage = 1;

    private int currentPosition;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            performerId = getArguments().getString("performerId");
        }
    }

    @Override
    public DefaultAdapterViewListener<VideoBean> getViewListener() {
        return new VideoViewListener(this);
    }

    @Override
    protected RecyclerView.LayoutManager getLayoutManager() {
        GridLayoutManager manager = new GridLayoutManager(getContext(), 3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        return manager;
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_video;
    }

    @Override
    public void initView() {
        content.setRefreshEnable(true);
//        adapter.addFoots(new BlankFooterHolder(getContext()));
        requestData();
    }

    private void requestData() {
        HttpService.getVideos(this, performerId, getApp().getUserBean().userId, currentPage, 20, new BaseCallback<VideoResult>(getContext(), this){
            @Override
            public void onSuccess(VideoResult videoResult, int id) {
                content.setHasBottom(null != videoResult.detail && videoResult.detail.size() == 20);
                content.setLoadMoreEnable(null != videoResult.detail && videoResult.detail.size() == 20);
                if (null != videoResult.detail) {
                    data.addAll(videoResult.detail);
                }
//                if (adapter.getHeards().size() == 0) {
//                    View headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_video, content.getRecyclerView(), false);
//                    adapter.addHead(new VideoHeaderHolder(headerView, getContext()));
//                }
                content.complete();
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(int position, VideoBean item) {
        currentPosition = position;
        Bundle bundle = new Bundle();
        bundle.putSerializable(VideoActivity.KEY_VIDEO_MODEL, item);
        bundle.putString("avatar", ((PerformerActivity) getActivity()).avatarUrl);
        bundle.putBoolean("isFans", ((PerformerActivity) getActivity()).isFans);
        bundle.putString("nicknameStr", ((PerformerActivity) getActivity()).userInfoName.getText().toString());
        readyGo(VideoActivity.class, bundle);
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
        if (eventCenter.getEventCode() == 2045) {
            data.get(currentPosition).isMyLove = "0";
        } else if (eventCenter.getEventCode() == 2046) {
            data.get(currentPosition).isMyLove = "1";
        }
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
