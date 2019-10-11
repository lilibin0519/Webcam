package com.wstv.pad.widget.util;

import android.content.Context;
import android.widget.ImageView;

import com.youth.banner.loader.ImageLoader;

/**
 * <p>Description: </p>
 * BaseImageLoader
 *
 * @author lilibin
 * @createDate 2019/3/28 14:24
 */

@SuppressWarnings("unchecked")
public abstract class BaseImageLoader<T> extends ImageLoader {

    @Override
    public void displayImage(Context context, Object path, ImageView imageView) {
        onImageDisplay(context, (T) path, imageView);
    }

    protected abstract void onImageDisplay(Context context, T t, ImageView imageView);
}
