package com.wstv.webcam.holder.action;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.wstv.webcam.R;
import com.wstv.webcam.activity.ActionDetailActivity;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.http.model.action.ActionBean;
import com.wstv.webcam.util.click.ClickProxy;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import em.sang.com.allrecycleview.holder.CustomHolder;

/**
 * <p>Description: </p>
 * ActionTopHolder
 *
 * @author lilibin
 * @createDate 2019/5/17 16:40
 */

public class ActionTopHolder extends CustomHolder<ActionBean> {

    public static String[] monthArr = {"一月", "二月", "三月", "四月", "五月", "六月", "七月", "八月", "九月", "十月", "十一月", "十二月"};

    public ActionTopHolder(Context context, List<ActionBean> lists) {
        super(context, lists, R.layout.item_action_top);
    }

    @Override
    public void initView(final int position, final List<ActionBean> datas, final Context context) {
        Calendar calendar = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);
        calendar.setTimeInMillis(Long.parseLong(datas.get(position / 3).createTime));
        holderHelper.setText(R.id.item_action_top_day, (calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + calendar.get(Calendar.DAY_OF_MONTH));
        holderHelper.setText(R.id.item_action_top_month, monthArr[calendar.get(Calendar.MONTH)]);
        holderHelper.setText(R.id.item_action_top_message, datas.get(position / 3).content);
//        itemView.setOnClickListener(new ClickProxy(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("actionBean", datas.get(position / 3));
//                ((BaseActivity) context).readyGo(ActionDetailActivity.class, bundle);
//            }
//        }));
    }
}
