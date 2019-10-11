package com.libin.mylibrary.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;

/**
 * <p>Description: </p>
 * GlideLoadUtils
 *
 * @author lilibin
 * @createDate 2019/8/27 15:33
 */

public class GlideLoadUtils {

    /**
     * 借助内部类 实现线程安全的单例模式
     * 属于懒汉式单例，因为Java机制规定，内部类SingletonHolder只有在getInstance()
     * 方法第一次调用的时候才会被加载（实现了lazy），而且其加载过程是线程安全的。
     * 内部类加载的时候实例化一次instance。
     */
    private GlideLoadUtils() {
    }

    private static class GlideLoadUtilsHolder {
        private final static GlideLoadUtils INSTANCE = new GlideLoadUtils();
    }

    public static GlideLoadUtils getInstance() {
        return GlideLoadUtilsHolder.INSTANCE;
    }

    /**
     * Glide 加载 简单判空封装 防止异步加载数据时调用Glide 抛出异常
     *
     * @param context       上下文对象
     * @param url           加载图片的url地址  String
     * @param imageView     加载图片的ImageView 控件
     * @param default_image 图片展示错误的本地图片 id
     */
    public void glideLoad(Context context, String url, ImageView imageView, int default_image) {
        if (context != null) {
            Glide.with(context).load(url).dontAnimate().placeholder(default_image).error(default_image).into(imageView);
        }
    }

    public void glideLoad(Context context, String url, ImageView imageView) {
        if (context != null) {
            Glide.with(context).load(url).into(imageView);
        }
    }

    public void glideLoad(Context context, int res, ImageView imageView) {
        if (context != null) {
            Glide.with(context).load(res).into(imageView);
        }
    }

    public void glideLoad(Context context, File file, ImageView imageView) {
        if (context != null) {
            Glide.with(context).load(file).into(imageView);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void glideLoad(Activity activity, String url, ImageView imageView, int default_image) {
        if (null != activity && !activity.isDestroyed()) {
            Glide.with(activity).load(url).dontAnimate().placeholder(default_image).error(default_image).into(imageView);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void glideLoad(Activity activity, String url, ImageView imageView) {
        if (null != activity && !activity.isDestroyed()) {
            Glide.with(activity).load(url).into(imageView);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void glideLoad(Activity activity, int res, ImageView imageView) {
        if (null != activity && !activity.isDestroyed()) {
            Glide.with(activity).load(res).into(imageView);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public void glideLoad(Activity activity, File file, ImageView imageView) {
        if (null != activity && !activity.isDestroyed()) {
            Glide.with(activity).load(file).into(imageView);
        }
    }

    public void glideLoad(Fragment fragment, String url, ImageView imageView, int default_image) {
        if (fragment != null && fragment.getActivity() != null) {
            Glide.with(fragment).load(url).dontAnimate().placeholder(default_image).error(default_image).into(imageView);
        }
    }

    public void glideLoad(Fragment fragment, String url, ImageView imageView) {
        if (fragment != null && fragment.getActivity() != null) {
            Glide.with(fragment).load(url).into(imageView);
        }
    }

    public void glideLoad(android.app.Fragment fragment, String url, ImageView imageView, int default_image) {
        if (fragment != null && fragment.getActivity() != null) {
            Glide.with(fragment).load(url).dontAnimate().placeholder(default_image).error(default_image).into(imageView);
        }
    }

    public void glideLoad(android.app.Fragment fragment, String url, ImageView imageView) {
        if (fragment != null && fragment.getActivity() != null) {
            Glide.with(fragment).load(url).into(imageView);
        }
    }
}
