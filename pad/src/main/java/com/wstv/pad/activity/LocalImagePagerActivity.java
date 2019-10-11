package com.wstv.pad.activity;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bumptech.glide.Glide;
import com.libin.mylibrary.base.eventbus.EventCenter;
import com.wstv.pad.R;
import com.wstv.pad.activity.base.BaseActivity;
import com.wstv.pad.adapter.LocalImagePagerAdapter;

import java.util.ArrayList;

import butterknife.Bind;

public class LocalImagePagerActivity extends BaseActivity {

    private int mCurrentPageIndex = 0;

    private ArrayList<String> mPaths;
    private LocalImagePagerAdapter photoPagerAdapter;

    @Bind(R.id.vp_photos)
    ViewPager vpPhotos;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_localimagepager;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        mCurrentPageIndex = extras.getInt("index");
        mPaths = extras.getStringArrayList("paths");
    }

    @Override
    protected void initViewAndData() {
        setTitleStr((mCurrentPageIndex + 1) + "/" + mPaths.size());
        vpPhotos.setOffscreenPageLimit(3);
        photoPagerAdapter = new LocalImagePagerAdapter(Glide.with(this), mPaths, this);
        vpPhotos.setAdapter(photoPagerAdapter);
        vpPhotos.setCurrentItem(mCurrentPageIndex);
        vpPhotos.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPageIndex = position;
                setTitleStr((mCurrentPageIndex + 1) + "/" + mPaths.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComming(EventCenter center) {

    }
}
