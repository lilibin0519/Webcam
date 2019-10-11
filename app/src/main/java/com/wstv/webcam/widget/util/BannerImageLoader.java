package com.wstv.webcam.widget.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.wstv.webcam.http.model.BannerBean;

/**
 * <p>Description: </p>
 * BannerImageLoader
 *
 * @author lilibin
 * @createDate 2019/3/28 14:24
 */

public class BannerImageLoader extends BaseImageLoader<BannerBean> {

    @Override
    protected void onImageDisplay(Context context, BannerBean bannerBean, ImageView imageView) {
        Glide.with(context).load(bannerBean.image).into(imageView);
    }

//    @Override
//    public ImageView createImageView(Context context) {
//        RoundedImageView riv = new RoundedImageView(context);
////        riv.setScaleType(ImageView.ScaleType.CENTER_CROP);
//        Trace.e("roundImage.radius : " + context.getResources().getDimension(R.dimen.qb_px_40));
//        riv.setCornerRadius(context.getResources().getDimension(R.dimen.qb_px_40));
//        riv.setBorderColor(Color.DKGRAY);
//        riv.mutateBackground(true);
////        riv.setTileModeX(Shader.TileMode.REPEAT);
////        riv.setTileModeY(Shader.TileMode.REPEAT);
////        riv.setPadding(context.getResources().getDimensionPixelSize(R.dimen.qb_px_50), context.getResources().getDimensionPixelSize(R.dimen.qb_px_25), context.getResources().getDimensionPixelSize(R.dimen.qb_px_50), context.getResources().getDimensionPixelSize(R.dimen.qb_px_25));
//        return riv;
//    }
}
