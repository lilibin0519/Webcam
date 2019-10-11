package com.wstv.webcam.holder.ranking;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.R;
import com.wstv.webcam.http.model.ranking.Ranking;
import com.wstv.webcam.util.NumberUtil;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomPeakHolder;

/**
 * Created by Kindred on 2019/3/24.
 */

public class RankingHeader extends CustomPeakHolder {

    private List<Ranking> data;

    public RankingHeader(Context context, View itemView, List<Ranking> data) {
        super(itemView);
        this.context = context;
        this.data = data;
    }

    @Override
    public void initView(int position, final Context context) {
        if (null != data) {
            if (data.size() > 0) {
                Glide.with(context).load(data.get(0).user.headPortrait).dontAnimate().placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar)
                        .into((ImageView) holderHelper.getView(R.id.header_ranking_avatar_1));
                holderHelper.setText(R.id.header_ranking_king_cost_1, NumberUtil.format10_000(data.get(0).coins));
                holderHelper.setText(R.id.header_ranking_king_name_1, data.get(0).user.nickname);
                holderHelper.setImageResource(R.id.header_ranking_king_level_logo_1, AppConstant.levelArr[(data.get(0).user.levelInfo.sort - 1) < AppConstant.levelArr.length ? data.get(0).user.levelInfo.sort - 1 : 0]);
            } else {
                holderHelper.setImageResource(R.id.header_ranking_avatar_1, R.drawable.default_avatar);
                holderHelper.setText(R.id.header_ranking_king_cost_1, "0");
                holderHelper.setText(R.id.header_ranking_king_name_1, "暂无上榜");
                holderHelper.setImageResource(R.id.header_ranking_king_level_logo_1, AppConstant.levelArr[0]);
            }
            if (data.size() > 1) {
                Glide.with(context).load(data.get(1).user.headPortrait).dontAnimate().placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar)
                        .into((ImageView) holderHelper.getView(R.id.header_ranking_avatar_2));
                holderHelper.setText(R.id.header_ranking_king_cost_2, NumberUtil.format10_000(data.get(1).coins));
                holderHelper.setText(R.id.header_ranking_king_name_2, data.get(1).user.nickname);
                holderHelper.setImageResource(R.id.header_ranking_king_level_logo_2, AppConstant.levelArr[(data.get(1).user.levelInfo.sort - 1) < AppConstant.levelArr.length ? data.get(1).user.levelInfo.sort - 1 : 0]);
            } else {
                holderHelper.setImageResource(R.id.header_ranking_avatar_2, R.drawable.default_avatar);
                holderHelper.setText(R.id.header_ranking_king_cost_2, "0");
                holderHelper.setText(R.id.header_ranking_king_name_2, "暂无上榜");
                holderHelper.setImageResource(R.id.header_ranking_king_level_logo_2, AppConstant.levelArr[0]);
            }
            if (data.size() > 2) {
                Glide.with(context).load(data.get(2).user.headPortrait).dontAnimate().placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar)
                        .into((ImageView) holderHelper.getView(R.id.header_ranking_avatar_3));
                holderHelper.setText(R.id.header_ranking_king_cost_3, NumberUtil.format10_000(data.get(2).coins));
                holderHelper.setText(R.id.header_ranking_king_name_3, data.get(2).user.nickname);
                holderHelper.setImageResource(R.id.header_ranking_king_level_logo_3, AppConstant.levelArr[(data.get(2).user.levelInfo.sort - 1) < AppConstant.levelArr.length ? data.get(2).user.levelInfo.sort - 1 : 0]);
            } else {
                holderHelper.setImageResource(R.id.header_ranking_avatar_3, R.drawable.default_avatar);
                holderHelper.setText(R.id.header_ranking_king_cost_3, "0");
                holderHelper.setText(R.id.header_ranking_king_name_3, "暂无上榜");
                holderHelper.setImageResource(R.id.header_ranking_king_level_logo_3, AppConstant.levelArr[0]);
            }
        }
    }
}
