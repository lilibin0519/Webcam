package com.wstv.webcam.fragment;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.google.gson.Gson;
import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.widget.swiperecyclerview.SwipeRecyclerView;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.R;
import com.wstv.webcam.holder.hall.HallHeaderHolder;
import com.wstv.webcam.holder.hall.HallViewListener;
import com.wstv.webcam.http.HttpService;
import com.wstv.webcam.http.callback.BaseCallback;
import com.wstv.webcam.http.callback.BaseCamCallback;
import com.wstv.webcam.http.model.BannerBean;
import com.wstv.webcam.http.model.BannerResult;
import com.wstv.webcam.http.model.room.Room;
import com.wstv.webcam.http.model.room.RoomListResult;
import com.wstv.webcam.tencent.liveroom.IMLVBLiveRoomListener;
import com.wstv.webcam.tencent.liveroom.MLVBLiveRoom;
import com.wstv.webcam.tencent.roomutil.commondef.RoomInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import em.sang.com.allrecycleview.adapter.DefaultAdapter;

/**
 * Created by Kindred on 2019/3/12.
 */

public class HallFragment extends BaseFragment implements SwipeRecyclerView.OnLoadListener {

    public static final String KEY_BUNDLE_INDEX = "key.hall.index";

    private static final String KEY_BUNDLE_TYPE = "key.hall.type";

    private int index;

    @Bind(R.id.fragment_hall_content)
    SwipeRecyclerView content;

    DefaultAdapter<RoomInfo> adapter;

    List<RoomInfo> data;

    HallHeaderHolder header;

    private int currentPage;

    private List<BannerBean> bannerList;

    private String type;

    // add by easy
    private MLVBLiveRoom liveRoom;

    public static HallFragment newInstance(int position, String type) {
        HallFragment fragment = new HallFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_BUNDLE_INDEX, position);
        bundle.putString(KEY_BUNDLE_TYPE, type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        index = getArguments().getInt(KEY_BUNDLE_INDEX);
        type = getArguments().getString(KEY_BUNDLE_TYPE);

        // add  by easy
        liveRoom = MLVBLiveRoom.sharedInstance(getActivity());
    }

    private void initList(){
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2);
        content.getRecyclerView().setLayoutManager(manager);
        data = new ArrayList<>();
        bannerList = new ArrayList<>();
        adapter = new DefaultAdapter<>(getContext(), data, R.layout.item_hall, new HallViewListener(this));
//        if (index == 0) {
            View headerView = LayoutInflater.from(getContext()).inflate(R.layout.header_hall, content.getRecyclerView(), false);
            header = new HallHeaderHolder(getContext(), headerView, bannerList, index);
            adapter.addHead(header);
//        }
        content.setAdapter(adapter);
        content.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return /*index == 0 && */position == 0 ? 2 : 1;
            }
        });

        content.setRefreshEnable(true);
        content.setLoadMoreEnable(false);
        content.setHasBottom(false);
        content.setOnLoadListener(this);
    }

    @Override
    protected void initViewAndData() {
        initList();
//        if (index == 0) {
            requestBanner();
//        }
        requestData();
    }

    private void requestBanner() {
        HttpService.getBanner(this, type, new BaseCallback<BannerResult>(getContext(), this) {
            @Override
            public void onSuccess(BannerResult bannerResult, int id) {
                bannerList.clear();
                bannerList.addAll(bannerResult.detail);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void requestData() {
        liveRoom.getRoomList(currentPage, AppConstant.DEFAULT_PAGE_SIZE, new IMLVBLiveRoomListener.GetRoomListCallback() {
            @Override
            public void onError(int errCode, String errInfo) {

            }

            @Override
            public void onSuccess(ArrayList<RoomInfo> roomInfoList) {
                if (currentPage == 0) {
                    data.clear();
                } else if (null == roomInfoList || roomInfoList.size() == 0) {
                    currentPage--;
                }
                if (null == content) {
                    return;
                }
                content.setHasBottom(null != roomInfoList && roomInfoList.size() == AppConstant.DEFAULT_PAGE_SIZE);
                content.setLoadMoreEnable(null != roomInfoList && roomInfoList.size() == AppConstant.DEFAULT_PAGE_SIZE);
                content.complete();
                data.addAll(roomInfoList);
                adapter.notifyDataSetChanged();
            }
        });
        /* del by easy
        HttpService.getRoomsBy(this, currentPage, AppConstant.DEFAULT_PAGE_SIZE, type, new BaseCamCallback<RoomListResult>(getContext(), this) {
            @Override
            public void onSuccess(RoomListResult roomListResult, int id) {
                if (currentPage == 0) {
                    data.clear();
                } else if (null == roomListResult.rooms || roomListResult.rooms.size() == 0) {
                    currentPage--;
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
        */
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_hall;
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
