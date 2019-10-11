package com.wstv.webcam.holder.audience;

import android.content.Context;

import com.wstv.webcam.http.model.audience.Audience;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

public class AudienceViewListener extends DefaultAdapterViewListener<Audience> {

    private OnToolsItemClickListener<Audience> listener;

    public AudienceViewListener(OnToolsItemClickListener<Audience> listener){
        this.listener = listener;
    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<Audience> lists, int itemID) {
        AudienceHolder holder = new AudienceHolder(context, lists);
        holder.setOnTOnToolsItemClickListener(listener);
        return holder;
    }
}
