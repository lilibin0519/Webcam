package com.wstv.webcam.holder.hall;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.wstv.webcam.R;
import com.wstv.webcam.activity.RecordActivity;
import com.wstv.webcam.http.model.BannerBean;
import com.wstv.webcam.util.click.ClickProxy;
import com.wstv.webcam.widget.util.BannerImageLoader;
import com.youth.banner.Banner;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomPeakHolder;

/**
 * <p>Description: </p>
 * HallHeaderHolder
 *
 * @author lilibin
 * @createDate 2019/3/12 17:08
 */

public class HallHeaderHolder extends CustomPeakHolder {

    private List<BannerBean> bannerList;

    private int index;

    public HallHeaderHolder(Context context, View itemView, List<BannerBean> bannerList, int index) {
        super(itemView);
        this.context = context;
        this.bannerList = bannerList;
        this.index = index;
    }

    @Override
    public void initView(int position, final Context context) {
        ((Banner) holderHelper.getView(R.id.header_hall_banner)).setImages(bannerList).setImageLoader(new BannerImageLoader()).start();
        holderHelper.getView(R.id.header_hall_record).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, RecordActivity.class));
            }
        }));
        holderHelper.setVisibility(R.id.header_hall_bottom, index == 0 ? View.VISIBLE : View.GONE);
    }
}
