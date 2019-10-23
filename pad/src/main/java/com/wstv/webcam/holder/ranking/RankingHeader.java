package com.wstv.webcam.holder.ranking;

import android.content.Context;
import android.view.View;

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
    public void initView(int position, Context context) {
        if (null != data) {
            if (data.size() > 0) {
                holderHelper.setText(R.id.header_ranking_king_cost_1, NumberUtil.format10_000(data.get(0).coins));
                holderHelper.setText(R.id.header_ranking_king_name_1, data.get(0).user.nickname);
                holderHelper.setImageResource(R.id.header_ranking_king_level_logo_1, AppConstant.levelArr[(data.get(0).user.levelInfo.sort - 1) < AppConstant.levelArr.length ? data.get(0).user.levelInfo.sort - 1 : 0]);
            } else {
                holderHelper.setText(R.id.header_ranking_king_cost_1, "0");
                holderHelper.setText(R.id.header_ranking_king_name_1, "暂无上榜");
                holderHelper.setImageResource(R.id.header_ranking_king_level_logo_1, AppConstant.levelArr[0]);
            }
            if (data.size() > 1) {
                holderHelper.setText(R.id.header_ranking_king_cost_2, NumberUtil.format10_000(data.get(1).coins));
                holderHelper.setText(R.id.header_ranking_king_name_2, data.get(1).user.nickname);
                holderHelper.setImageResource(R.id.header_ranking_king_level_logo_2, AppConstant.levelArr[(data.get(1).user.levelInfo.sort - 1) < AppConstant.levelArr.length ? data.get(1).user.levelInfo.sort - 1 : 0]);
            } else {
                holderHelper.setText(R.id.header_ranking_king_cost_2, "0");
                holderHelper.setText(R.id.header_ranking_king_name_2, "暂无上榜");
                holderHelper.setImageResource(R.id.header_ranking_king_level_logo_2, AppConstant.levelArr[0]);
            }
            if (data.size() > 2) {
                holderHelper.setText(R.id.header_ranking_king_cost_3, NumberUtil.format10_000(data.get(2).coins));
                holderHelper.setText(R.id.header_ranking_king_name_3, data.get(2).user.nickname);
                holderHelper.setImageResource(R.id.header_ranking_king_level_logo_3, AppConstant.levelArr[(data.get(2).user.levelInfo.sort - 1) < AppConstant.levelArr.length ? data.get(2).user.levelInfo.sort - 1 : 0]);
            } else {
                holderHelper.setText(R.id.header_ranking_king_cost_3, "0");
                holderHelper.setText(R.id.header_ranking_king_name_3, "暂无上榜");
                holderHelper.setImageResource(R.id.header_ranking_king_level_logo_3, AppConstant.levelArr[0]);
            }
        }
    }
}
