package com.wstv.pad.fragment;

import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.libin.mylibrary.util.BaseAppManager;
import com.wstv.pad.AppConstant;
import com.wstv.pad.R;
import com.wstv.pad.activity.LoginPhoneActivity;
import com.wstv.pad.adapter.HallPagerAdapter;
import com.wstv.pad.http.HttpService;
import com.wstv.pad.http.callback.BaseCallback;
import com.wstv.pad.http.model.room.RoomType;
import com.wstv.pad.http.model.room.RoomTypeResult;
import com.wstv.pad.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * <p>Description: </p>
 * MainFragment
 *
 * @author lilibin
 * @createDate 2019/3/11 16:32
 */

public class MainFragment extends BaseFragment {

    @Bind(R.id.title_bar_background)
    ViewGroup titleParent;

    @Bind(R.id.title_bar_parent)
    Toolbar titleParentLayout;

    @Bind(R.id.temp_status)
    View tempStatus;

    @Bind(R.id.fragment_main_tab)
    TabLayout tab;

    @Bind(R.id.fragment_main_pager)
    public NoScrollViewPager pager;

    private HallPagerAdapter pagerAdapter;

    List<RoomType> types;

    @Override
    protected void initViewAndData() {
        types = new ArrayList<>();
        initPager();
        requestRoomType();
//        setTempStatus();
    }

    private void requestRoomType() {
        HttpService.getRoomTypes(this, new BaseCallback<RoomTypeResult>(getContext(), this) {
            @Override
            public void onSuccess(RoomTypeResult roomTypeResult, int id) {
                types.addAll(roomTypeResult.detail);
                initPager();
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

    private void initPager(){
        pagerAdapter = new HallPagerAdapter(getChildFragmentManager(), types);
        pager.setAdapter(pagerAdapter);
        tab.setupWithViewPager(pager);
    }

    @OnClick({R.id.fragment_main_record})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.fragment_main_record:
//                readyGo(SearchActivity.class);
                getApp().setUserBean(null);
                PreferenceUtil.write(AppConstant.KEY_PARAM_USER_ID, "");
                PreferenceUtil.write(AppConstant.KEY_PARAM_TOKEN, "");
                BaseAppManager.getInstance().clearToTop();
                readyGoThenKill(LoginPhoneActivity.class);
                break;
        }
    }

//    private void initTab(final List<NewTypeModel> newType) {
//        tab.removeAllTabs();
//        for(int i = 0; i < newType.size(); i++){
//            tab.addTab(tab.newTab().setText(newType.get(i).name));
//        }
//        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                currentId = newType.get(tab.getPosition()).id;
//                onRefresh();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_main;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
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
}
