package com.wstv.webcam.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.Trace;
import com.libin.mylibrary.widget.MainNavigateTabBar;
import com.tencent.rtmp.TXLiveBase;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.fragment.MainFragment;
import com.wstv.webcam.util.AppUtil;

import butterknife.Bind;

public class MainActivity extends BaseActivity {

    private int[] colors = {android.R.color.transparent, android.R.color.white, android.R.color.white, android.R.color.transparent, android.R.color.transparent};

    private boolean[] statusBarDarkAble = {true, true, true, false, false};

    @Bind(R.id.activity_main_parent)
    RelativeLayout parent;

    @Bind(R.id.activity_main_container)
    FrameLayout mainContainer;

    @Bind(R.id.activity_main_tab)
    MainNavigateTabBar mainTabBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (AppUtil.isApkInDebug(this)) {
            String sdkver = TXLiveBase.getSDKVersionStr();
            Trace.e("liteavsdk", "liteav sdk version is : " + sdkver);
        }
    }

    @Override
    protected void initViewAndData() {
        mainTabBar.setParentGroup(parent);
        mainTabBar.addTab(MainFragment.class, new MainNavigateTabBar.TabParam(
                R.drawable.icon_main_tab_1_normal, R.drawable.icon_main_tab_1, "大厅", View.VISIBLE, ContextCompat.getColor(this, R.color.pink)));
//        mainTabBar.addTab(RankingFragment.class, new MainNavigateTabBar.TabParam(
//                R.drawable.icon_main_tab_2_normal, R.drawable.icon_main_tab_2, "排行榜", View.VISIBLE, ContextCompat.getColor(this, R.color.pink)));
//        mainTabBar.addTab(FollowFragment.class, new MainNavigateTabBar.TabParam(
//                R.drawable.icon_main_tab_3_normal, R.drawable.icon_main_tab_3, "关注", View.VISIBLE, ContextCompat.getColor(this, R.color.pink)));
//        mainTabBar.addTab(MineFragment.class, new MainNavigateTabBar.TabParam(
//                R.drawable.icon_main_tab_4_normal, R.drawable.icon_main_tab_4, "我的", View.VISIBLE, ContextCompat.getColor(this, R.color.pink)));
//        mainTabBar.setTabSelectListener(new MainNavigateTabBar.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(MainNavigateTabBar.ViewHolder holder) {
//                mainTabBar.showFragment(holder, null);
//                if (isImmersionBarEnabled()) {
//                    ImmersionBar.with(MainActivity.this).statusBarDarkFont(statusBarDarkAble[holder.tabIndex], 0.2f).statusBarColor(colors[holder.tabIndex]).init();
//                }
//            }
//        });
    }

    @Override
    protected int getStatusBarColor() {
        return android.R.color.transparent;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_main;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComming(EventCenter center) {

    }
}
