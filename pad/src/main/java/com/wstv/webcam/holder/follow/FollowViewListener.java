package com.wstv.webcam.holder.follow;

import android.content.Context;

import com.wstv.webcam.http.model.follow.Follow;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;

/**
 * Created by Kindred on 2019/3/12.
 */

public class FollowViewListener extends DefaultAdapterViewListener<Follow> {
    @Override
    public CustomHolder getBodyHolder(Context context, List<Follow> lists, int itemID) {
        return new FollowHolder(context, lists, itemID);
    }
}
