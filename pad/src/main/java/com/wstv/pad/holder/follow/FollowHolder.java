package com.wstv.pad.holder.follow;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.libin.mylibrary.util.GlideLoadUtils;
import com.wstv.pad.R;
import com.wstv.pad.activity.PerformerActivity;
import com.wstv.pad.activity.base.BaseActivity;
import com.wstv.pad.http.model.follow.Follow;
import com.wstv.pad.util.click.ClickProxy;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;

/**
 * Created by Kindred on 2019/3/12.
 */

public class FollowHolder extends CustomHolder<Follow> {
    public FollowHolder(Context context, List<Follow> lists, int itemID) {
        super(context, lists, itemID);
    }

    @Override
    public void initView(final int position, final List<Follow> datas, final Context context) {
        if (null == datas.get(position)) {
            return;
        }
        itemView.setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("showId", datas.get(position).showId);
                bundle.putString("performerId", datas.get(position).idol);
                ((BaseActivity) context).readyGo(PerformerActivity.class, bundle);
            }
        }));
//        Glide.with(context).load(datas.get(position).headPortrait).into((ImageView) holderHelper.getView(R.id.item_follow_avatar));
        GlideLoadUtils.getInstance().glideLoad(context, datas.get(position).headPortrait, (ImageView) holderHelper.getView(R.id.item_follow_avatar));
        holderHelper.setText(R.id.item_follow_name, datas.get(position).nickname);
        holderHelper.setText(R.id.item_follow_motto, datas.get(position).signature);
        holderHelper.setText(R.id.item_follow_time, "0".equals(datas.get(position).liveStatus) ? calculateTime(datas.get(position).lastShowTime) : "直播中");
    }

    private String calculateTime(String timeStamp){
        String result;
        try {
            if (!TextUtils.isEmpty(timeStamp)) {
                long lastTime = (System.currentTimeMillis() - Long.parseLong(timeStamp)) / 1000;
                if (lastTime > 24 * 3600) {
                    result = String.valueOf(lastTime / 24 / 3600) + "天前";
                } else if (lastTime > 3600) {
                    result = String.valueOf(lastTime / 3600) + "小时前";
                } else if (lastTime > 60) {
                    result = String.valueOf(lastTime / 60) + "分钟前";
                } else {
                    result = "刚刚下播";
                }
            } else {
                result = "未开播";
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = "未开播";
        }
        return result;
    }
}
