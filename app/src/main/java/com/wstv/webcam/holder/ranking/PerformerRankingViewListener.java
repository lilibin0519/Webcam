package com.wstv.webcam.holder.ranking;

import android.content.Context;

import com.wstv.webcam.http.model.Performer;
import com.wstv.webcam.http.model.ranking.Ranking;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Created by Kindred on 2019/3/24.
 */

public class PerformerRankingViewListener extends DefaultAdapterViewListener<Ranking> {

    private OnToolsItemClickListener<Ranking> listener;

    public PerformerRankingViewListener(OnToolsItemClickListener<Ranking> listener){
        this.listener = listener;
    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<Ranking> lists, int itemID) {
        PerformerRankingHolder holder = new PerformerRankingHolder(context, lists, itemID);
        holder.setOnTOnToolsItemClickListener(listener);
        return holder;
    }
}
