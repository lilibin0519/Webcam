package com.wstv.webcam.activity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.base.BaseRecyclerActivity;
import com.wstv.webcam.holder.ranking.RankingHeader;
import com.wstv.webcam.holder.ranking.RankingTop;
import com.wstv.webcam.holder.ranking.RankingViewListener;
import com.wstv.webcam.http.HttpService;
import com.wstv.webcam.http.callback.BaseCallback;
import com.wstv.webcam.http.model.ranking.Ranking;
import com.wstv.webcam.http.model.ranking.RankingResult;

import java.util.ArrayList;
import java.util.List;

import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Created by Kindred on 2019/3/25.
 */

public class WithRankingActivity extends BaseRecyclerActivity<Ranking> implements OnToolsItemClickListener<Ranking> {

    private RankingHeader header;

    private RankingTop top;

    private String showId;

    private List<Ranking> headerList;

    private int currentPage = 1;

    @Override
    protected String getTitleStr() {
        return "守护";
    }

    @Override
    public DefaultAdapterViewListener<Ranking> getViewListener() {
        return new RankingViewListener(this);
    }

    @Override
    public int getItemLayoutId() {
        return R.layout.item_ranking;
    }

    @Override
    public void initView() {
        content.getRecyclerView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        headerList = new ArrayList<>();
        View headerView = LayoutInflater.from(this).inflate(R.layout.header_ranking, content.getRecyclerView(), false);
        adapter.addHead(header = new RankingHeader(this, headerView, headerList));
//        View topView = LayoutInflater.from(this).inflate(R.layout.top_ranking, content.getRecyclerView(), false);
//        adapter.addTop(top = new RankingTop(this, topView, new RankingTop.OnChangeListener() {
//            @Override
//            public void onChange() {
//                onRefresh();
//            }
//        }));
        requestData();
    }

    private void requestData(){
        HttpService.getWithRanking(this, String.valueOf(currentPage), String.valueOf(AppConstant.DEFAULT_PAGE_SIZE), showId, new BaseCallback<RankingResult>(this, this) {
            @Override
            public void onSuccess(RankingResult rankingResult, int id) {
                content.setLoadMoreEnable(rankingResult.detail != null && rankingResult.detail.size() == AppConstant.DEFAULT_PAGE_SIZE);
                content.setHasBottom(rankingResult.detail != null && rankingResult.detail.size() == AppConstant.DEFAULT_PAGE_SIZE);
                if (currentPage == 1) {
                    int count = rankingResult.detail.size() < 3 ? rankingResult.detail.size() : 3;
                    for (int i = 0; i < count; i++) {
                        headerList.add(rankingResult.detail.remove(0));
                    }
                }
                data.addAll(rankingResult.detail);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onItemClick(int position, Ranking item) {

    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        showId = extras.getString("showId");
    }

    @Override
    protected boolean addItemDecoration() {
        return false;
    }

    @Override
    protected void onEventComming(EventCenter center) {

    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        data.clear();
        headerList.clear();
        requestData();
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        requestData();
    }
}
