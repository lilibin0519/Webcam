package com.wstv.webcam.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.wstv.webcam.fragment.PerformerActionFragment;
import com.wstv.webcam.fragment.PerformerVideoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kindred on 2019/1/20.
 */

public class PerformerPagerAdapter extends FragmentPagerAdapter {

    protected List<Fragment> fragments;

    private List<String> types;

    private String performerId;

    public PerformerPagerAdapter(FragmentManager fm, List<String> types, String performerId) {
        super(fm);
        this.types = types;
        fragments = new ArrayList<>();
        this.performerId = performerId;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment;
        if (position == 1) {
            fragment = new PerformerVideoFragment();
        } else {
            fragment = new PerformerActionFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString("performerId", performerId);
        fragment.setArguments(bundle);
        fragments.add(position, fragment);
        return fragment;
    }

    @Override
    public int getCount() {
        return null == types ? 0 : types.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return types.get(position);
    }

    public List<Fragment> getFragments(){
        return fragments;
    }
}
