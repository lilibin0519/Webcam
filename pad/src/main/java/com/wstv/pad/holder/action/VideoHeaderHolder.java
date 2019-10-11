package com.wstv.pad.holder.action;

import android.content.Context;
import android.view.View;

import com.wstv.pad.R;

import em.sang.com.allrecycleview.holder.CustomPeakHolder;

/**
 * Created by Kindred on 2019/5/18.
 */

public class VideoHeaderHolder extends CustomPeakHolder {

    public VideoHeaderHolder(View itemView, Context context) {
        super(itemView);
        this.context = context;
    }

    @Override
    public void initView(int position, Context context) {
        holderHelper.setText(R.id.header_video_count, "全部（" + 1 + "）");
    }
}
