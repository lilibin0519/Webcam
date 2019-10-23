package com.wstv.webcam.holder.record;

import android.content.Context;

import com.wstv.webcam.http.model.record.WatchRecord;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;

/**
 * Created by Kindred on 2019/3/12.
 */

public class WatchRecordViewListener extends DefaultAdapterViewListener<WatchRecord> {
    @Override
    public CustomHolder getBodyHolder(Context context, List<WatchRecord> lists, int itemID) {
        return new WatchRecordHolder(context, lists, itemID);
    }
}
