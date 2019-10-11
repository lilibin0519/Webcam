package com.libin.mylibrary.localimage.utils;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.libin.mylibrary.R;
import com.libin.mylibrary.localimage.PhotoPickerActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by donglua on 15/6/23.
 *
 *
 * http://developer.android.com/training/camera/photobasics.html
 */
public class ImageCaptureManager {

  private final static String CAPTURED_PHOTO_PATH_KEY = "mCurrentPhotoPath";
  public static final int REQUEST_TAKE_PHOTO = 1;
  public static final int REQUEST_SELECT_PHOTO = 2;

  private String mCurrentPhotoPath;
  private Context mContext;

  public ImageCaptureManager(Context mContext) {
    this.mContext = mContext;
  }

  private File createImageFile() throws IOException {
    // Create an image file name
    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    String imageFileName = "JPEG_" + timeStamp + "_";
    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    if (!storageDir.exists()) {
      if (!storageDir.mkdir()) {
        throw new IOException();
      }
    }
    File image = File.createTempFile(
            imageFileName,  /* prefix */
            ".jpg",         /* suffix */
            storageDir      /* directory */
    );

    // Save a file: path for use with ACTION_VIEW intents
    mCurrentPhotoPath = image.getAbsolutePath();
    return image;
  }


  public Intent dispatchTakePictureIntent() throws IOException {
    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    // Ensure that there's a camera activity to handle the intent
    if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
      // Create the File where the photo should go
      File photoFile = createImageFile();
      // Continue only if the File was successfully created
      if (photoFile != null) {
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
          uri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", photoFile);
        } else {
          uri = Uri.fromFile(photoFile);
        }
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
      }
    }
    return takePictureIntent;
  }


  public void galleryAddPic() {
    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
    File f = new File(mCurrentPhotoPath);
//    Uri contentUri = Uri.fromFile(f);
    Uri contentUri;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
      contentUri = FileProvider.getUriForFile(mContext, mContext.getPackageName() + ".provider", f);
    } else {
      contentUri = Uri.fromFile(f);
    }
    mediaScanIntent.setData(contentUri);
    mContext.sendBroadcast(mediaScanIntent);
  }


  public String getCurrentPhotoPath() {
    return mCurrentPhotoPath;
  }


  public void onSaveInstanceState(Bundle savedInstanceState) {
    if (savedInstanceState != null && mCurrentPhotoPath != null) {
      savedInstanceState.putString(CAPTURED_PHOTO_PATH_KEY, mCurrentPhotoPath);
    }
  }

  public void onRestoreInstanceState(Bundle savedInstanceState) {
    if (savedInstanceState != null && savedInstanceState.containsKey(CAPTURED_PHOTO_PATH_KEY)) {
      mCurrentPhotoPath = savedInstanceState.getString(CAPTURED_PHOTO_PATH_KEY);
    }
  }


  public void showPhotoSelectDialog(int maxCount){
    showPhotoSelectDialog(null, null, maxCount);
  }

  public void showPhotoSelectDialog(final View.OnClickListener takePhoto, final View.OnClickListener selectPhoto, final int maxCount){
    //1、使用Dialog、设置style
    final Dialog dialog = new Dialog(mContext, R.style.DialogTheme);
    //2、设置布局
    View view = View.inflate(mContext, R.layout.dialog_photo_select,null);
    dialog.setContentView(view);

    Window window = dialog.getWindow();
    //设置弹出位置
    window.setGravity(Gravity.BOTTOM);
    //设置弹出动画
    window.setWindowAnimations(R.style.main_menu_animStyle);
    //设置对话框大小
    window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
    dialog.show();

    dialog.findViewById(R.id.tv_take_photo).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (null != takePhoto) {
          takePhoto.onClick(view);
        } else {
          try {
            Intent intent = dispatchTakePictureIntent();
            ((AppCompatActivity) mContext).startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
        dialog.dismiss();
      }
    });

    dialog.findViewById(R.id.tv_take_pic).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        if (null != selectPhoto) {
          selectPhoto.onClick(view);
        } else {
          Intent intent = new Intent(mContext, PhotoPickerActivity.class);
          PhotoPickerIntent.setPhotoCount(intent, maxCount);
          PhotoPickerIntent.setShowGif(intent, false);
          PhotoPickerIntent.setColumn(intent, 3);
          ((AppCompatActivity) mContext).startActivityForResult(intent, REQUEST_SELECT_PHOTO);
        }
        dialog.dismiss();
      }
    });

    dialog.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        dialog.dismiss();
      }
    });
  }
}
