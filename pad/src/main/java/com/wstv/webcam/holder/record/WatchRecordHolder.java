package com.wstv.webcam.holder.record;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.libin.mylibrary.util.GlideLoadUtils;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.PerformerActivity;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.http.model.record.WatchRecord;
import com.wstv.webcam.util.click.ClickProxy;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;

/**
 * Created by Kindred on 2019/3/12.
 */

public class WatchRecordHolder extends CustomHolder<WatchRecord> {
    public WatchRecordHolder(Context context, List<WatchRecord> lists, int itemID) {
        super(context, lists, itemID);
    }

    @Override
    public void initView(final int position, final List<WatchRecord> datas, final Context context) {
        if (null == datas.get(position)) {
            return;
        }
        itemView.setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("showId", String.valueOf(datas.get(position).showId));
                bundle.putString("performerId", datas.get(position).streamer.id);
                ((BaseActivity) context).readyGo(PerformerActivity.class, bundle);
            }
        }));
//        Glide.with(context).load(datas.get(position).streamer.headPortrait).dontAnimate().placeholder(R.drawable.default_avatar).into((ImageView) holderHelper.getView(R.id.item_watch_record_avatar));
        GlideLoadUtils.getInstance().glideLoad(context, datas.get(position).streamer.headPortrait, (ImageView) holderHelper.getView(R.id.item_watch_record_avatar), R.drawable.default_avatar);
        holderHelper.setText(R.id.item_watch_record_name, datas.get(position).streamer.nickname);
        holderHelper.setImageResource(R.id.item_watch_record_level_logo, AppConstant.levelArr[(datas.get(position).streamer.levelInfo.sort - 1) < AppConstant.levelArr.length ? datas.get(position).streamer.levelInfo.sort - 1 : 0]);
        holderHelper.setImageResource(R.id.item_watch_record_gender, "0".equals(datas.get(position).streamer.sex) ? R.drawable.icon_gender_female : R.drawable.icon_gender_male);
        holderHelper.setText(R.id.item_watch_record_time, "0".equals(datas.get(position).streamer.streaming) ? datas.get(position).streamer.lastShowTime : "直播中");
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
