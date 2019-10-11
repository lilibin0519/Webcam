package com.wstv.pad.holder.action;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.libin.mylibrary.util.GlideLoadUtils;
import com.wstv.pad.R;
import com.wstv.pad.http.model.action.VideoBean;
import com.wstv.pad.util.click.ClickProxy;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;

public class VideoHolder extends CustomHolder<VideoBean> {

    public VideoHolder(Context context, List<VideoBean> lists, int itemID) {
        super(context, lists, itemID);
    }

    @Override
    public void initView(final int position, final List<VideoBean> datas, Context context) {
        itemView.setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClick(position, datas.get(position));
                }
            }
        }));
        holderHelper.setText(R.id.item_video_times, String.valueOf(datas.get(position).views));
//        Glide.with(context).load(datas.get(position).posterUrl).into((ImageView) holderHelper.getView(R.id.item_video_image));
        GlideLoadUtils.getInstance().glideLoad(context, datas.get(position).posterUrl,
                (ImageView) holderHelper.getView(R.id.item_video_image));
    }
}
