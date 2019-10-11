package com.wstv.webcam.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.libin.mylibrary.localimage.adapter.PhotoPagerAdapter;
import com.wstv.webcam.BuildConfig;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.base.BaseActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by admin on 2016/5/17.
 */
public class LocalImagePagerAdapter extends PhotoPagerAdapter {

    private BaseActivity activity;
    
    public void setPaths(ArrayList<String> paths){
        this.paths = paths;
        notifyDataSetChanged();
    }
    
    public LocalImagePagerAdapter(RequestManager glide, List<String> paths, BaseActivity activity) {
        super(glide, paths);
        this.activity = activity;
    }
    @Override public Object instantiateItem(ViewGroup container, int position) {
        final Context context = container.getContext();
        View itemView = LayoutInflater.from(context)
                .inflate(R.layout.item_img_detail, container, false);

        final PhotoView imageView = (PhotoView) itemView.findViewById(R.id.givLocalImage);

        final String path = paths.get(position);
        final Uri uri;
        if (path.startsWith("http")) {
            uri = Uri.parse(path);
        } else {
//            uri = Uri.fromFile(new File(path));
            uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".provider", new File(path));
        }
        final PhotoViewAttacher attacher = new PhotoViewAttacher(imageView);
        attacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                activity.finish();
            }
        });
        mGlide.load(uri)
                .thumbnail(0.1f)
                .dontAnimate()
                .dontTransform()
                .override(800, 800)
                .placeholder(com.libin.mylibrary.R.drawable.__picker_ic_photo_black_48dp)
                .error(com.libin.mylibrary.R.drawable.__picker_ic_broken_image_black_48dp)
                .into(new GlideDrawableImageViewTarget(imageView){
                    @Override
                    public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                        super.onResourceReady(resource, animation);
                        attacher.update();
                    }
                });

        container.addView(itemView);

        return itemView;
    }


}
