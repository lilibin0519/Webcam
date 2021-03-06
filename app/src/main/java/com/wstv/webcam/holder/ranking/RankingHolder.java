package com.wstv.webcam.holder.ranking;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.R;
import com.wstv.webcam.http.model.ranking.Ranking;
import com.wstv.webcam.util.NumberUtil;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;

/**
 * Created by Kindred on 2019/3/24.
 */

public class RankingHolder extends CustomHolder<Ranking> {

    public RankingHolder(Context context, List<Ranking> lists, int itemID) {
        super(context, lists, itemID);
    }

    @Override
    public void initView(int position, List<Ranking> datas, Context context) {
        Glide.with(context).load(datas.get(position).user.headPortrait).dontAnimate().placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).into((ImageView) holderHelper.getView(R.id.item_ranking_avatar));
        holderHelper.setText(R.id.item_ranking_index, String.valueOf(position + 4));
        holderHelper.setText(R.id.item_ranking_name, datas.get(position).user.nickname);
        holderHelper.setImageResource(R.id.item_ranking_level_logo, AppConstant.levelArr[(datas.get(position).user.levelInfo.sort - 1) < AppConstant.levelArr.length ? datas.get(position).user.levelInfo.sort - 1 : 0]);
        holderHelper.setText(R.id.item_ranking_cost, NumberUtil.format10_000(datas.get(position).coins) + " 秀币");
    }
}
