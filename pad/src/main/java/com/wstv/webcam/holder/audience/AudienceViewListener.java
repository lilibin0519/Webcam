package com.wstv.webcam.holder.audience;

import android.content.Context;

import com.wstv.webcam.http.model.audience.Audience;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;

public class AudienceViewListener extends DefaultAdapterViewListener<Audience> {
    @Override
    public CustomHolder getBodyHolder(Context context, List<Audience> lists, int itemID) {
        return new AudienceHolder(context, lists);
    }
}
