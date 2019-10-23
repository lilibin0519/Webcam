package com.wstv.webcam.util;

import android.content.Context;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;

import com.wstv.webcam.entity.VideoPic;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * <p>Description: </p>
 * VideoPicUtil
 *
 * @author lilibin
 * @createDate 2019/6/25 10:24
 */

public class VideoPicUtil {

    public static void getCamVideoPic(Context context){
        MediaPlayer mediaPlayer = new MediaPlayer();
        setGetPicEnable(mediaPlayer, true, context);
        VideoPic pic = getVideoPic(mediaPlayer, context);
    }

    private static void setGetPicEnable(MediaPlayer media, boolean enable, Context context){
        if (null != media) {
            try {
                Class<?> MediaClass = Class.forName("android.media.MediaPlayer");
                Method method = MediaClass.getMethod("setGetPicEnable", new Class[]{boolean.class});
                method.invoke(media, enable);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                showException(e, context);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                showException(e, context);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                showException(e, context);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                showException(e, context);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                showException(e, context);
            } catch (Exception e) {
                e.printStackTrace();
                showException(e, context);
            }
        }
    }

    private static VideoPic getVideoPic(MediaPlayer media, Context context) {
        VideoPic videoPic = null;
        if (null != media) {
            try {
                Class<?> MediaClass = Class.forName("android.media.MediaPlayer");
                Method method = MediaClass.getMethod("getVideoPic");
                videoPic = (VideoPic) method.invoke(media);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                showException(e, context);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                showException(e, context);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                showException(e, context);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
                showException(e, context);
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                showException(e, context);
            } catch (Exception e) {
                e.printStackTrace();
                showException(e, context);
            }
        }
        return videoPic;
    }

    private static void showException(Exception e, Context context) {
        new AlertDialog.Builder(context)
                .setMessage(getExceptionToString(e)).show();
    }

    /**
     * 将 Exception 转化为 String
     */
    public static String getExceptionToString(Throwable e) {
        if (e == null){
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }
}
