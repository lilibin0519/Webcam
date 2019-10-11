package com.wstv.pad.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.libin.mylibrary.util.GlideLoadUtils;
import com.libin.mylibrary.widget.swiperecyclerview.SwipeRecyclerView;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wstv.pad.AppConstant;
import com.wstv.pad.R;
import com.wstv.pad.activity.LivePageActivity;
import com.wstv.pad.activity.base.BaseActivity;
import com.wstv.pad.holder.hall.HallViewListener;
import com.wstv.pad.http.HttpService;
import com.wstv.pad.http.callback.BaseCallback;
import com.wstv.pad.http.callback.BaseCamCallback;
import com.wstv.pad.http.model.BannerResult;
import com.wstv.pad.http.model.audience.AudienceStateResult;
import com.wstv.pad.http.model.room.Room;
import com.wstv.pad.http.model.room.RoomListResult;
import com.wstv.pad.util.LiveRoomUtil;
import com.wstv.pad.util.click.ClickProxy;
import com.wstv.pad.widget.util.BannerImageLoader;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import em.sang.com.allrecycleview.adapter.DefaultAdapter;

/**
 * Created by Kindred on 2019/3/12.
 */

public class HallFragment extends BaseFragment implements SwipeRecyclerView.OnLoadListener {

    public static final String KEY_BUNDLE_INDEX = "key.hall.index";

    private static final String KEY_BUNDLE_TYPE = "key.hall.type";

    private int index;

    @Bind(R.id.fragment_hall_swipe)
    SwipeRefreshLayout swipe;

    @Bind(R.id.fragment_hall_content)
    SwipeRecyclerView content;

    @Bind(R.id.fragment_hall_banner)
    Banner banner;

    DefaultAdapter<Room> adapter;

    List<Room> data;

    private int currentPage;

    private String type;

    private List<Room> adviceList;

    private List<AdviceRoom> viewList;

//    @Bind(R.id.fragment_hall_advice_1)
//    RoundedImageView advice1;
//
//    @Bind(R.id.fragment_hall_advice_2)
//    RoundedImageView advice2;
//
//    @Bind(R.id.fragment_hall_advice_3)
//    RoundedImageView advice3;
//
//    @Bind(R.id.fragment_hall_advice_1_name)
//    TextView name1;
//
//    @Bind(R.id.fragment_hall_advice_2_name)
//    TextView name2;
//
//    @Bind(R.id.fragment_hall_advice_3_name)
//    TextView name3;
//
//    @Bind(R.id.fragment_hall_advice_1_views)
//    TextView times1;
//
//    @Bind(R.id.fragment_hall_advice_2_views)
//    TextView times2;
//
//    @Bind(R.id.fragment_hall_advice_3_views)
//    TextView times3;

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
    }

    private void initList(){
        GridLayoutManager manager = new GridLayoutManager(getContext(), 1);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        content.getRecyclerView().setLayoutManager(manager);
        data = new ArrayList<>();
        banner.setImageLoader(new BannerImageLoader()).start();
        adapter = new DefaultAdapter<>(getContext(), data, R.layout.item_hall, new HallViewListener(this));
        content.setAdapter(adapter);

        content.setRefreshEnable(true);
        content.setLoadMoreEnable(false);
        content.setHasBottom(false);
        content.setOnLoadListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewList = new ArrayList<>();
        final int[] avatarIds = {R.id.fragment_hall_advice_1, R.id.fragment_hall_advice_2, R.id.fragment_hall_advice_3};
        int[] nameIds = {R.id.fragment_hall_advice_1_name, R.id.fragment_hall_advice_2_name, R.id.fragment_hall_advice_3_name};
        int[] timeIds = {R.id.fragment_hall_advice_1_views, R.id.fragment_hall_advice_2_views, R.id.fragment_hall_advice_3_views};
        for (int i = 0; i < 3; i++) {
            final AdviceRoom adviceRoom = new AdviceRoom();
            adviceRoom.avatar = ButterKnife.findById(view, avatarIds[i]);
            adviceRoom.name = ButterKnife.findById(view, nameIds[i]);
            adviceRoom.times = ButterKnife.findById(view, timeIds[i]);
            viewList.add(adviceRoom);
            adviceRoom.avatar.setOnClickListener(new ClickProxy(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = 0;
                    for (int i = 0; i < avatarIds.length; i++) {
                        if (v.getId() == avatarIds[i]) {
                            position = i;
                            break;
                        }
                    }
                    if (position < adviceList.size()) {
                        enterRoom(adviceList.get(position));
                    }
                }
            }));
        }
    }

    private void enterRoom(final Room room) {
        HttpService.audienceState(this, room.roomID, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), new BaseCallback<AudienceStateResult>(getContext(), this) {
            @Override
            public void onSuccess(AudienceStateResult audienceStateResult, int id) {
                if (audienceStateResult.detail.state != 0) {
                    Bundle bundle = new Bundle();
                    bundle.putString(LivePageActivity.BUNDLE_KEY_FLV_URL, room.mixedPlayURL);
                    bundle.putString(LivePageActivity.BUNDLE_KEY_LINK_URL, room.pushersMap.get(room.roomCreator).accelerateURL);
                    bundle.putString(LivePageActivity.BUNDLE_KEY_ROOM_ID, room.roomID);
                    bundle.putString(LivePageActivity.BUNDLE_KEY_PERFORMER_ID, room.roomCreator);
                    bundle.putSerializable(LivePageActivity.BUNDLE_KEY_PERFORMER, room.pushersMap.get(room.roomCreator));
                    bundle.putSerializable(LivePageActivity.BUNDLE_KEY_AUDIENCE, room.audiences);
                    bundle.putInt(LivePageActivity.BUNDLE_KEY_MANAGE, audienceStateResult.detail.manager);
                    bundle.putLong(LivePageActivity.BUNDLE_KEY_SHUT_UP, audienceStateResult.detail.endTime);
                    LiveRoomUtil.getInstance((BaseActivity) context).getLiveRoom().addRoom(room);
                    Intent intent = new Intent(context, LivePageActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);

//                    Bundle bundle = new Bundle();
//                    bundle.putString(CamDetailActivity.BUNDLE_KEY_FLV_URL, room.mixedPlayURL);
//                    bundle.putString(CamDetailActivity.BUNDLE_KEY_LINK_URL, room.pushersMap.get(room.roomCreator).accelerateURL);
//                    bundle.putString(CamDetailActivity.BUNDLE_KEY_ROOM_ID, room.roomID);
//                    bundle.putString(CamDetailActivity.BUNDLE_KEY_PERFORMER_ID, room.roomCreator);
//                    bundle.putSerializable(CamDetailActivity.BUNDLE_KEY_PERFORMER, room.pushersMap.get(room.roomCreator));
//                    bundle.putSerializable(CamDetailActivity.BUNDLE_KEY_AUDIENCE, room.audiences);
//                    bundle.putInt(CamDetailActivity.BUNDLE_KEY_MANAGE, audienceStateResult.detail.manager);
//                    bundle.putLong(CamDetailActivity.BUNDLE_KEY_SHUT_UP, audienceStateResult.detail.endTime);
//                    LiveRoomUtil.getInstance((BaseActivity) context).getLiveRoom().addRoom(room);
//                    readyGo(CamDetailActivity.class, bundle);
                }
            }
        });
    }

    @Override
    protected void initViewAndData() {
        initList();
//        if (index == 0) {
            requestBanner();
//        }
        adviceList = new ArrayList<>();
        swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestBanner();
                currentPage = 0;
                requestData();
            }
        });
        requestData();
    }

    private void requestBanner() {
        HttpService.getBanner(this, type, new BaseCallback<BannerResult>(getContext(), this) {
            @Override
            public void onSuccess(BannerResult bannerResult, int id) {
                banner.setImages(bannerResult.detail).setImageLoader(new BannerImageLoader()).start();
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void requestData() {
        HttpService.getRoomsBy(this, currentPage, AppConstant.DEFAULT_PAGE_SIZE, type, new BaseCamCallback<RoomListResult>(getContext(), this) {
            @Override
            public void onSuccess(RoomListResult roomListResult, int id) {
                if (currentPage == 0) {
                    data.clear();
                    int size = null == roomListResult.rooms ? 0 : roomListResult.rooms.size();
                    for (int i = 0; i < (size > 3 ? 3 : size); i++) {
                        adviceList.add(roomListResult.rooms.remove(0));
                    }
                    for (int i = 0; i < 3; i++) {
                        if (i < adviceList.size()) {
//                            Glide.with(getContext()).load(adviceList.get(i).pushersMap.get(adviceList.get(i).roomCreator).userAvatar).dontAnimate().into(viewList.get(i).avatar);
                            GlideLoadUtils.getInstance().glideLoad(getContext(), adviceList.get(i).pushersMap.get(adviceList.get(i).roomCreator).userAvatar,
                                    viewList.get(i).avatar);
                            viewList.get(i).name.setText(adviceList.get(i).roomInfo);
                            viewList.get(i).times.setText(String.valueOf(adviceList.get(i).audiencesCnt));
                        } else {
//                            Glide.with(getContext()).load(R.drawable.default_avatar).fitCenter().dontAnimate().into(viewList.get(i).avatar);
                            GlideLoadUtils.getInstance().glideLoad(getContext(), R.drawable.default_avatar,
                                    viewList.get(i).avatar);
                            viewList.get(i).name.setVisibility(View.GONE);
                            viewList.get(i).times.setVisibility(View.GONE);
                        }
                    }
                } else if (null == roomListResult.rooms || roomListResult.rooms.size() == 0) {
                    currentPage--;
                }
                if (null == content) {
                    return;
                }
                content.setHasBottom(null != roomListResult.rooms && roomListResult.rooms.size() == AppConstant.DEFAULT_PAGE_SIZE);
                content.setLoadMoreEnable(null != roomListResult.rooms && roomListResult.rooms.size() == AppConstant.DEFAULT_PAGE_SIZE);
                content.complete();
                swipe.setRefreshing(false);
                data.addAll(roomListResult.rooms);
                adapter.notifyDataSetChanged();
            }

            @Override
            protected void onFailed(String errCode, String errMsg, RoomListResult roomListResult) {
                content.complete();
                swipe.setRefreshing(false);
            }
        });
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

    public class AdviceRoom {
        public RoundedImageView avatar;

        public TextView name;

        public TextView times;
    }
}
