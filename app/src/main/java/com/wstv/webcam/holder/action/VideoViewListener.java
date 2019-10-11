package com.wstv.webcam.holder.action;

import android.content.Context;

import com.wstv.webcam.http.model.action.VideoBean;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Created by Kindred on 2019/5/17.
 */

public class VideoViewListener extends DefaultAdapterViewListener<VideoBean> {

    private final OnToolsItemClickListener<VideoBean> listener;

    public VideoViewListener(OnToolsItemClickListener<VideoBean> listener){
        this.listener = listener;
    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<VideoBean> lists, int itemID) {
        VideoHolder holder = new VideoHolder(context, lists, itemID);
        holder.setOnTOnToolsItemClickListener(listener);
        return holder;
    }
}
