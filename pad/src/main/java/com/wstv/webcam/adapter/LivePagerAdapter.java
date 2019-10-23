package com.wstv.webcam.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wstv.webcam.fragment.LiveFragment;
import com.wstv.webcam.http.model.room.RoomType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kindred on 2019/1/20.
 */

public class LivePagerAdapter extends FragmentPagerAdapter {

    protected List<LiveFragment> fragments;

    private List<RoomType> types;

    public LivePagerAdapter(FragmentManager fm, List<RoomType> types) {
        super(fm);
        this.types = types;
        fragments = new ArrayList<>();
    }

    @Override
    public Fragment getItem(int position) {
        LiveFragment fragment = LiveFragment.newInstance(position, types.get(position).id);
        fragments.add(position, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return null == types ? 0 : types.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return types.get(position).name;
    }

    public List<LiveFragment> getFragments(){
        return fragments;
    }
}
