package com.wstv.webcam.holder.action;

import android.content.Context;

import com.wstv.webcam.http.model.action.Comment;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * <p>Description: </p>
 * CommentViewListener
 *
 * @author lilibin
 * @createDate 2019/5/24 11:11
 */

public class CommentViewListener extends DefaultAdapterViewListener<Comment> {

    private final OnToolsItemClickListener<Comment> listener;

    public CommentViewListener(OnToolsItemClickListener<Comment> listener){
        this.listener = listener;
    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<Comment> lists, int itemID) {
        CommentHolder holder = new CommentHolder(context, lists, itemID);
        holder.setOnTOnToolsItemClickListener(listener);
        return holder;
    }
}
