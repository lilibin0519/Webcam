package com.libin.mylibrary.localimage.utils;

import android.content.Intent;

import com.libin.mylibrary.localimage.PhotoPickerActivity;

/**
 * Created by donglua on 15/7/2.
 */
public class PhotoPickerIntent {
  public static void setPhotoCount(Intent intent, int photoCount) {
    intent.putExtra(PhotoPickerActivity.EXTRA_MAX_COUNT, photoCount);
  }

  public static void setShowCamera(Intent intent, boolean showCamera) {
    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, showCamera);
  }

  public static void setShowGif(Intent intent, boolean showGif) {
    intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_GIF, showGif);
  }

  public static void setColumn(Intent intent, int column) {
    intent.putExtra(PhotoPickerActivity.EXTRA_GRID_COLUMN, column);
  }

  /**
   * 是否为图片添加水印（默认处理为true添加）
   * @param intent
   * @param addTime
   */
  public static void setAddTimeAble(Intent intent, boolean addTime) {
    intent.putExtra(PhotoPickerActivity.EXTRA_ADD_TIME, addTime);
  }

  /**
   * 是否初始化进入拍照（默认处理为true添加）
   * @param intent
   * @param takePhotoFirst
   */
  public static void setTakePhotoFirst(Intent intent, boolean takePhotoFirst) {
    intent.putExtra(PhotoPickerActivity.EXTRA_TAKE_PHOTO_FIRST, takePhotoFirst);
  }

  /**
   * 图片是否可选（默认处理为true添加）
   * @param intent
   * @param canSelect
   */
  public static void setCanselect(Intent intent, boolean canSelect) {
    intent.putExtra(PhotoPickerActivity.EXTRA_CAN_SELECT, canSelect);
  }
}
