package com.wstv.pad.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.widget.swiperecyclerview.SwipeRecyclerView;
import com.wstv.pad.AppConstant;
import com.wstv.pad.R;
import com.wstv.pad.activity.LivePageActivity;
import com.wstv.pad.holder.live.LiveViewListener;
import com.wstv.pad.http.HttpService;
import com.wstv.pad.http.callback.BaseCamCallback;
import com.wstv.pad.http.model.room.Room;
import com.wstv.pad.http.model.room.RoomListResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import em.sang.com.allrecycleview.adapter.DefaultAdapter;

/**
 * Created by Kindred on 2019/3/12.
 */

public class LiveFragment extends BaseFragment implements SwipeRecyclerView.OnLoadListener {

    public static final String KEY_BUNDLE_INDEX = "key.live.index";

    private static final String KEY_BUNDLE_TYPE = "key.live.type";

    @Bind(R.id.fragment_live_content)
    SwipeRecyclerView content;

    DefaultAdapter<Room> adapter;

    List<Room> data;

    private int currentPage;

    private String type;

    private List<Room> adviceList;

    public static LiveFragment newInstance(int position, String type) {
        LiveFragment fragment = new LiveFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_BUNDLE_INDEX, position);
        bundle.putString(KEY_BUNDLE_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type = getArguments().getString(KEY_BUNDLE_TYPE);
    }

    private void initList(){
        GridLayoutManager manager = new GridLayoutManager(getContext(), 4);
        content.getRecyclerView().setLayoutManager(manager);
        data = new ArrayList<>();
        adapter = new DefaultAdapter<>(getContext(), data, R.layout.item_live, new LiveViewListener(this));
        content.setAdapter(adapter);

        content.setRefreshEnable(true);
        content.setLoadMoreEnable(false);
        content.setHasBottom(false);
        content.setOnLoadListener(this);
    }

    public void enterRoom(final Room room) {
        ((LivePageActivity) getActivity()).changeRoom(room);
    }

    @Override
    protected void initViewAndData() {
        initList();
        requestData();
    }

    private void requestData() {
        HttpService.getRoomsBy(this, currentPage, AppConstant.DEFAULT_PAGE_SIZE, type, new BaseCamCallback<RoomListResult>(getContext(), this) {
            @Override
            public void onSuccess(RoomListResult roomListResult, int id) {
                if (currentPage == 0) {
                    data.clear();
                }
                if (null == content) {
                    return;
                }
                content.setHasBottom(null != roomListResult.rooms && roomListResult.rooms.size() == AppConstant.DEFAULT_PAGE_SIZE);
                content.setLoadMoreEnable(null != roomListResult.rooms && roomListResult.rooms.size() == AppConstant.DEFAULT_PAGE_SIZE);
                content.complete();
                data.addAll(roomListResult.rooms);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_live;
    }

    @Override
    protected View getLoadingTargetView() {
        return content.getRecyclerView();
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
        currentPage = 0;
        requestData();
    }

    @Override
    public void onLoadMore() {
        currentPage++;
        requestData();
    }
}
