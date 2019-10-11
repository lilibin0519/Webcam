package com.wstv.webcam.holder.ranking;

import android.content.Context;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.wstv.webcam.R;

import em.sang.com.allrecycleview.holder.CustomPeakHolder;

/**
 * Created by Kindred on 2019/3/25.
 */

public class RankingTop extends CustomPeakHolder {

    private int position;

    private int[] radioIds = {/*R.id.top_ranking_day, */R.id.top_ranking_week, R.id.top_ranking_month, R.id.top_ranking_all};

    private String[] typeArr = {/*"D", */"W", "M", "ALL"};

    private boolean init;

    private OnChangeListener listener;

    public RankingTop(Context context, View itemView, OnChangeListener listener) {
        super(itemView);
        this.context = context;
        this.listener = listener;
    }

    @Override
    public void initView(int position, Context context) {
        if (!init) {
            ((RadioButton) holderHelper.getView(/*holderHelper.getView(R.id.top_ranking_day).getVisibility() == View.VISIBLE ? R.id.top_ranking_day : */R.id.top_ranking_week)).setChecked(true);
            ((RadioGroup) holderHelper.getView(R.id.top_ranking_group)).setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if (null != listener) {
                        listener.onChange();
                    }
                }
            });
            init = true;
        }
    }

    public void setDayVisibility(int visibility) {
//        holderHelper.setVisibility(R.id.top_ranking_day, visibility);
//        ((RadioButton) holderHelper.getView(visibility == View.VISIBLE ? R.id.top_ranking_day : R.id.top_ranking_week)).setChecked(true);
    }

    public String getType(){
        int checkedId = ((RadioGroup) holderHelper.getView(R.id.top_ranking_group)).getCheckedRadioButtonId();
        int i;
        for (i = 0; i < radioIds.length; i++) {
            if (radioIds[i] == checkedId) {
                break;
            }
        }
        return typeArr[i < typeArr.length ? i : 0];
    }

    public interface OnChangeListener{
        void onChange();
    }
}
