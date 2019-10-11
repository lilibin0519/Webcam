package com.wstv.pad.holder.ranking;

import android.content.Context;
import android.widget.ImageView;

import com.libin.mylibrary.util.GlideLoadUtils;
import com.wstv.pad.AppConstant;
import com.wstv.pad.R;
import com.wstv.pad.http.model.ranking.Ranking;
import com.wstv.pad.util.NumberUtil;

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
//        Glide.with(context).load(datas.get(position).user.headPortrait).dontAnimate().placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).into((ImageView) holderHelper.getView(R.id.item_ranking_avatar));
        GlideLoadUtils.getInstance().glideLoad(context, datas.get(position).user.headPortrait,
                (ImageView) holderHelper.getView(R.id.item_ranking_avatar), R.drawable.default_avatar);
        holderHelper.setText(R.id.item_ranking_index, String.valueOf(position + 4));
        holderHelper.setText(R.id.item_ranking_name, datas.get(position).user.nickname);
        holderHelper.setImageResource(R.id.item_ranking_level_logo, AppConstant.levelArr[(datas.get(position).user.levelInfo.sort - 1) < AppConstant.levelArr.length ? datas.get(position).user.levelInfo.sort - 1 : 0]);
        holderHelper.setText(R.id.item_ranking_cost, NumberUtil.format10_000(datas.get(position).coins) + " 秀币");
    }
}
