package com.wstv.webcam.holder.live;

import android.content.Context;

import com.wstv.webcam.fragment.LiveFragment;
import com.wstv.webcam.tencent.roomutil.commondef.RoomInfo;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;

/**
 * <p>Description: </p>
 * LiveViewListener
 *
 * @author lilibin
 * @createDate 2019/3/12 17:04
 */

public class LiveViewListener extends DefaultAdapterViewListener<RoomInfo> {

    private LiveFragment liveFragment;

    public LiveViewListener(LiveFragment liveFragment) {
        this.liveFragment = liveFragment;
    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<RoomInfo> lists, int itemID) {
        return new LiveHolder(context, lists, itemID, liveFragment);
    }
}
