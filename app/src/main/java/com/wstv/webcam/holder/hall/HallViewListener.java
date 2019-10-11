package com.wstv.webcam.holder.hall;

import android.content.Context;

import com.wstv.webcam.fragment.HallFragment;
import com.wstv.webcam.http.model.room.Room;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;

/**
 * <p>Description: </p>
 * HallViewListener
 *
 * @author lilibin
 * @createDate 2019/3/12 17:04
 */

public class HallViewListener extends DefaultAdapterViewListener<Room> {

    private HallFragment hallFragment;

    public HallViewListener(HallFragment hallFragment) {
        this.hallFragment = hallFragment;
    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<Room> lists, int itemID) {
        return new HallHolder(context, lists, itemID, hallFragment);
    }
}
