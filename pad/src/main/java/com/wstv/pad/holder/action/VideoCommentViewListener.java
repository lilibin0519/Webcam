package com.wstv.pad.holder.action;

import android.content.Context;

import com.wstv.pad.http.model.action.VideoComment;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * <p>Description: </p>
 * VideoCommentViewListener
 *
 * @author lilibin
 * @createDate 2019/5/24 11:11
 */

public class VideoCommentViewListener extends DefaultAdapterViewListener<VideoComment> {

    private final OnToolsItemClickListener<VideoComment> listener;

    public VideoCommentViewListener(OnToolsItemClickListener<VideoComment> listener){
        this.listener = listener;
    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<VideoComment> lists, int itemID) {
        VideoCommentHolder holder = new VideoCommentHolder(context, lists, itemID);
        holder.setOnTOnToolsItemClickListener(listener);
        return holder;
    }
}
