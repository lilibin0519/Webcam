package com.wstv.pad.holder.ranking;

import android.content.Context;

import com.wstv.pad.http.model.ranking.Ranking;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Created by Kindred on 2019/3/24.
 */

public class RankingViewListener extends DefaultAdapterViewListener<Ranking> {

    private OnToolsItemClickListener<Ranking> listener;

    public RankingViewListener(OnToolsItemClickListener<Ranking> listener){
        this.listener = listener;
    }

    @Override
    public CustomHolder getBodyHolder(Context context, List<Ranking> lists, int itemID) {
        RankingHolder holder = new RankingHolder(context, lists, itemID);
        holder.setOnTOnToolsItemClickListener(listener);
        return holder;
    }
}
