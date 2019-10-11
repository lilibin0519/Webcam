package com.wstv.pad.holder.live;

import android.content.Context;

import com.wstv.pad.fragment.HallFragment;
import com.wstv.pad.fragment.LiveFragment;
import com.wstv.pad.holder.hall.HallHolder;
import com.wstv.pad.http.model.room.Room;

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

public class LiveViewListener extends DefaultAdapterViewListener<Room> {

    private LiveFragment liveFragment;

    public LiveViewListener(LiveFragment liveFragment) {
        this.liveFragment = liveFragment;
    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<Room> lists, int itemID) {
        return new LiveHolder(context, lists, itemID, liveFragment);
    }
}
