package com.wstv.pad.holder.performer;

import android.content.Context;

import com.wstv.pad.http.model.Performer;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * <p>Description: </p>
 * PerformerViewListener
 *
 * @author lilibin
 * @createDate 2019/3/11 13:55
 */

public class PerformerViewListener extends DefaultAdapterViewListener<Performer> {

    private OnToolsItemClickListener<Performer> listener;

    public PerformerViewListener(OnToolsItemClickListener<Performer> listener){
        this.listener = listener;
    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<Performer> lists, int itemID) {
        PerformerHolder holder = new PerformerHolder(context, lists, itemID);
        holder.setOnTOnToolsItemClickListener(listener);
        return holder;
    }
}
