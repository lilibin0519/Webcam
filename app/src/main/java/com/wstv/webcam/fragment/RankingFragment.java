package com.wstv.webcam.fragment;

import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.widget.swiperecyclerview.SwipeRecyclerView;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.PerformerActivity;
import com.wstv.webcam.holder.ranking.PerformerRankingHeader;
import com.wstv.webcam.holder.ranking.PerformerRankingViewListener;
import com.wstv.webcam.holder.ranking.RankingTop;
import com.wstv.webcam.http.HttpService;
import com.wstv.webcam.http.callback.BaseCallback;
import com.wstv.webcam.http.model.ranking.Ranking;
import com.wstv.webcam.http.model.ranking.RankingResult;
import com.wstv.webcam.util.click.ClickProxy;
import com.wstv.webcam.widget.RankingTypePop;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import em.sang.com.allrecycleview.adapter.DefaultAdapter;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Created by Kindred on 2019/3/15.
 */

public class RankingFragment extends BaseFragment implements OnToolsItemClickListener<Ranking>,SwipeRecyclerView.OnLoadListener {

    @Bind(R.id.title_bar_background)
    ViewGroup titleParent;

    @Bind(R.id.title_bar_parent)
    Toolbar titleParentLayout;

    @Bind(R.id.temp_status)
    View tempStatus;

    @Bind(R.id.fragment_ranking_type_layout)
    LinearLayout typeLayout;

    @Bind(R.id.fragment_ranking_type)
    TextView type;

    @Bind(R.id.fragment_ranking_content)
    public SwipeRecyclerView content;

    public DefaultAdapter<Ranking> adapter;

    public List<Ranking> data;

    private String showId;

    private List<Ranking> headerList;

    private int currentPage = 1;

    private RankingTop top;

    public String userType = "S";

    private AlertDialog typeCheck;

    RankingTypePop typePop;

    @Override
    protected void initViewAndData() {
        setTempStatus();
        type.setText("明星榜");
        initList();
        adapter.notifyDataSetChanged();
        typeLayout.setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (null == typeCheck) {
//                    typeCheck = new AlertDialog.Builder(getContext())
//                            .setTitle("类型选择")
//                            .setSingleChoiceItems(new String[]{"明星榜", "富豪榜"}, 0, new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    type.setText(0 == which ? "明星榜" : "富豪榜");
//                                    userType = 0 == which ? "S" : "R";
//                                    dialog.dismiss();
//                                    data.clear();
//                                    headerList.clear();
//                                    adapter.notifyDataSetChanged();
//                                    requestData();
//                                }
//                            })
//                            .show();
//                } else {
//                    typeCheck.show();
//                }
                if (null == typePop) {
                    typePop = new RankingTypePop(getActivity(), new RankingTypePop.TypeCheckListener() {
                        @Override
                        public void onCheck(int position) {
                            type.setText(0 == position ? "明星榜" : "富豪榜");
                            userType = 0 == position ? "S" : "R";
                            data.clear();
                            headerList.clear();
                            adapter.notifyDataSetChanged();
                            requestData();
                        }
                    });
                }

                typePop.setChecked(userType);
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                    typePop.showAsDropDown(typeLayout);
                } else {

                    Rect visibleFrame = new Rect();
                    typeLayout.getGlobalVisibleRect(visibleFrame);
                    int height = typeLayout.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
                    typePop.setHeight(height);
                    typePop.showAsDropDown(typeLayout, 0, 0);
                }
//                typePop.show(typeLayout);
            }
        }));
    }

    protected void initList() {
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        content.getRecyclerView().setLayoutManager(manager);
        data = new ArrayList<>();
        headerList = new ArrayList<>();
        content.setAdapter(adapter = new DefaultAdapter<>(getContext(), data, R.layout.item_ranking, new PerformerRankingViewListener(this)));
        View headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_ranking, content.getRecyclerView(), false);
        adapter.addHead(new PerformerRankingHeader(getContext(), headerView, headerList, this));
        View topView = LayoutInflater.from(getContext()).inflate(R.layout.top_ranking, content.getRecyclerView(), false);
        adapter.addTop(top = new RankingTop(getContext(), topView, new RankingTop.OnChangeListener() {
            @Override
            public void onChange() {
                onRefresh();
            }
        }));
        content.setRefreshEnable(false);
        content.setLoadMoreEnable(false);
        content.setHasBottom(false);
        content.setOnLoadListener(this);
        requestData();
    }

    private void requestData() {
        HttpService.getRanking(this, String.valueOf(currentPage), String.valueOf(AppConstant.DEFAULT_PAGE_SIZE), top.getType(), userType, new BaseCallback<RankingResult>(getContext(), this) {
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
    public void onItemClick(int position, Ranking item) {
        if ("S".equals(userType)) {
            Bundle bundle = new Bundle();
            bundle.putString("showId", item.user.showId);
            bundle.putString("performerId", item.user.userId);
            readyGo(PerformerActivity.class, bundle);
        }
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

    @Override
    protected void onEventComming(EventCenter eventCenter) {

    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_ranking;
    }

    @Override
    protected View getLoadingTargetView() {
        return content;
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
}
