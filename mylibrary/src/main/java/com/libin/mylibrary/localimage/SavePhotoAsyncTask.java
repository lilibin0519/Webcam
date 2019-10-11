package com.libin.mylibrary.localimage;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.libin.mylibrary.localimage.utils.CameraResultUtil;

import java.io.File;

/**
 * <p>Description: </p>
 * SavePhotoAsyncTask
 *
 * @author lilibin
 * @createDate 2018/3/2 10:22
 */

public class SavePhotoAsyncTask extends AsyncTask<String, Void, String> {

    private Context context;

    private SavePhotoCallback callback;

    private Bitmap photo;

    public SavePhotoAsyncTask(Context context, SavePhotoCallback callback, Bitmap photo){
        this.context = context;
        this.callback = callback;
        this.photo = photo;
    }

    @Override
    protected void onPostExecute(String path) {
        if (null == path || TextUtils.isEmpty(path)) {
            callback.onSaveFailed();
        } else {
            callback.onSaveSuccess(path);
        }
    }

    @Override
    protected String doInBackground(String[] urls) {
        File file = new File(urls[0]);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return CameraResultUtil.savePhotoToSD(photo, urls[0]);
    }

    public interface SavePhotoCallback {
        void onSaveSuccess(String path);

        void onSaveFailed();
    }
}
