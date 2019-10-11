package com.libin.mylibrary.localimage.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.libin.mylibrary.R;
import com.libin.mylibrary.acp.Acp;
import com.libin.mylibrary.acp.AcpListener;
import com.libin.mylibrary.acp.AcpOptions;
import com.libin.mylibrary.base.util.Trace;
import com.libin.mylibrary.localimage.PhotoPickerActivity;
import com.libin.mylibrary.localimage.adapter.PhotoGridAdapter;
import com.libin.mylibrary.localimage.adapter.PopupDirectoryListAdapter;
import com.libin.mylibrary.localimage.entity.Photo;
import com.libin.mylibrary.localimage.entity.PhotoDirectory;
import com.libin.mylibrary.localimage.event.OnPhotoClickListener;
import com.libin.mylibrary.localimage.utils.ImageCaptureManager;
import com.libin.mylibrary.localimage.utils.MediaStoreHelper;
import com.libin.mylibrary.localimage.utils.PhotoUtil;
import com.libin.mylibrary.util.ToastUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.libin.mylibrary.localimage.PhotoPickerActivity.DEFAULT_COLUMN_NUMBER;
import static com.libin.mylibrary.localimage.PhotoPickerActivity.EXTRA_SHOW_GIF;
import static com.libin.mylibrary.localimage.utils.MediaStoreHelper.INDEX_ALL_PHOTOS;

/**
 * Created by donglua on 15/5/31.
 */
public class PhotoPickerFragment extends Fragment {

    private ImageCaptureManager captureManager;
    private PhotoGridAdapter photoGridAdapter;

    private PopupDirectoryListAdapter listAdapter;
    private List<PhotoDirectory> directories;

    private int SCROLL_THRESHOLD = 30;
    int column;

    private final static String EXTRA_CAMERA = "camera";
    private final static String EXTRA_COLUMN = "column";
    private final static String EXTRA_COUNT = "count";
    private final static String EXTRA_GIF = "gif";

    private boolean addTimeAble;

    public static String userComment;

    private MediaScannerConnection mMediaonnection;

    private boolean takePhotoFirst;

    private boolean canSelect;

    private Bitmap operateBitmap;

    private String operatePath;

    private MaterialDialog loadingMaterialDialog;

    private String oldAddress;

    public static PhotoPickerFragment newInstance(boolean showCamera, boolean showGif, int column, int maxCount) {
        Bundle args = new Bundle();
        args.putBoolean(EXTRA_CAMERA, showCamera);
        args.putBoolean(EXTRA_GIF, showGif);
        args.putInt(EXTRA_COLUMN, column);
        args.putInt(EXTRA_COUNT, maxCount);
        PhotoPickerFragment fragment = new PhotoPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static PhotoPickerFragment newInstance(boolean showCamera, boolean showGif, int column, int maxCount, boolean addTime, boolean takePhotoFirst, boolean canSelect) {
        PhotoPickerFragment fragment = newInstance(showCamera, showGif, column, maxCount);
        Bundle args = fragment.getArguments();
        args.putBoolean(PhotoPickerActivity.EXTRA_ADD_TIME, addTime);
        args.putBoolean(PhotoPickerActivity.EXTRA_TAKE_PHOTO_FIRST, takePhotoFirst);
        args.putBoolean(PhotoPickerActivity.EXTRA_CAN_SELECT, canSelect);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addTimeAble = getArguments().getBoolean(PhotoPickerActivity.EXTRA_ADD_TIME);
        takePhotoFirst = getArguments().getBoolean(PhotoPickerActivity.EXTRA_TAKE_PHOTO_FIRST);
        canSelect = getArguments().getBoolean(PhotoPickerActivity.EXTRA_CAN_SELECT, true);

        directories = new ArrayList<>();

        column = getArguments().getInt(EXTRA_COLUMN, DEFAULT_COLUMN_NUMBER);
        boolean showCamera = getArguments().getBoolean(EXTRA_CAMERA, true);

        photoGridAdapter = new PhotoGridAdapter(getContext(), directories, column, canSelect);
        photoGridAdapter.setShowCamera(showCamera);

        Bundle mediaStoreArgs = new Bundle();

        boolean showGif = getArguments().getBoolean(EXTRA_GIF);
        mediaStoreArgs.putBoolean(EXTRA_SHOW_GIF, showGif);
        MediaStoreHelper.getPhotoDirs(getActivity(), mediaStoreArgs,
                new MediaStoreHelper.PhotosResultCallback() {
                    @Override
                    public void onResultCallback(List<PhotoDirectory> dirs) {
                        directories.clear();
                        directories.addAll(dirs);
                        photoGridAdapter.notifyDataSetChanged();
                        listAdapter.notifyDataSetChanged();
                    }
                });

        captureManager = new ImageCaptureManager(getActivity());

        if (takePhotoFirst) {
            toTakePhoto();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setRetainInstance(true);

        final View rootView = inflater.inflate(R.layout.__picker_fragment_photo_picker, container, false);

        listAdapter = new PopupDirectoryListAdapter(Glide.with(this), directories);

        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_photos);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(column, OrientationHelper.VERTICAL);
        layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(photoGridAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        final Button btSwitchDirectory = (Button) rootView.findViewById(R.id.button);

        final ListPopupWindow listPopupWindow = new ListPopupWindow(getActivity());
        listPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);
        listPopupWindow.setAnchorView(btSwitchDirectory);
        listPopupWindow.setAdapter(listAdapter);
        listPopupWindow.setModal(true);
        listPopupWindow.setDropDownGravity(Gravity.BOTTOM);
        //listPopupWindow.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                listPopupWindow.dismiss();

                PhotoDirectory directory = directories.get(position);

                btSwitchDirectory.setText(directory.getName());

                photoGridAdapter.setCurrentDirectoryIndex(position);
                photoGridAdapter.notifyDataSetChanged();
            }
        });

        photoGridAdapter.setOnPhotoClickListener(new OnPhotoClickListener() {
            @Override
            public void onClick(View v, int position, boolean showCamera) {
                final int index = showCamera ? position - 1 : position;

                List<String> photos = photoGridAdapter.getCurrentPhotoPaths();

                int[] screenLocation = new int[2];
                v.getLocationOnScreen(screenLocation);
                ImagePagerFragment imagePagerFragment =
                        ImagePagerFragment.newInstance(photos, index, screenLocation, v.getWidth(),
                                v.getHeight());
                imagePagerFragment.setParent(PhotoPickerFragment.this);

                ((PhotoPickerActivity) getActivity()).addImagePagerFragment(imagePagerFragment);

            }
        });

        photoGridAdapter.setOnCameraClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

//        ToastUtils.showToastShort("点击拍照！！！！！！！！！！！！！！");


                toTakePhoto();
        
        
        
       /* try {
          Intent intent = captureManager.dispatchTakePictureIntent();
          startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
          e.printStackTrace();
        }*/
            }
        });

        btSwitchDirectory.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listPopupWindow.isShowing()) {
                    listPopupWindow.dismiss();
                } else if (!getActivity().isFinishing()) {
                    listPopupWindow.setHeight(Math.round(rootView.getHeight() * 0.8f));
                    listPopupWindow.show();
                }
            }
        });


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // Log.d(">>> Picker >>>", "dy = " + dy);
                if (Math.abs(dy) > SCROLL_THRESHOLD) {
                    Glide.with(getActivity()).pauseRequests();
                } else {
                    Glide.with(getActivity()).resumeRequests();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    Glide.with(getActivity()).resumeRequests();
                }
            }
        });


        return rootView;
    }

    private void toTakePhoto() {
        Acp.getInstance(getActivity()).
                request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.CAMERA,
                                        Manifest.permission.READ_EXTERNAL_STORAGE,
                                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                        Manifest.permission.ACCESS_COARSE_LOCATION,
                                        Manifest.permission.ACCESS_FINE_LOCATION,
                                        Manifest.permission.READ_PHONE_STATE)
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
//                        writeSD();
//                            initView();
                                try {
                                    Intent intent = captureManager.dispatchTakePictureIntent();
                                    startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
//                        makeText(permissions.toString() + "权限拒绝");
                                ToastUtils.showToastShort(permissions.toString() + "权限拒绝");
                                getActivity().finish();
                            }
                        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ImageCaptureManager.REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            captureManager.galleryAddPic();
            if (directories.size() > 0) {
                String path = captureManager.getCurrentPhotoPath();
                insertPhotoByPath(path, addTimeAble);
                if (!TextUtils.isEmpty(userComment)) {
                    try {
                        ExifInterface exifInterface = new ExifInterface(path);
                        exifInterface.setAttribute(ExifInterface.TAG_MAKE, userComment);
                        exifInterface.saveAttributes();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
//                if (addTimeAble) {
//                    PhotoMarkUtil.addTimeOnPhotoByCalendar(getContext(), path);
//                }
//                // 更新系统相册
//                ScannerImage(path);
//                PhotoDirectory directory = directories.get(INDEX_ALL_PHOTOS);
//                directory.getPhotos().add(INDEX_ALL_PHOTOS, new Photo(path.hashCode(), path));
//                directory.setCoverPath(path);
//                photoGridAdapter.notifyDataSetChanged();
            }
        }
    }


    public PhotoGridAdapter getPhotoGridAdapter() {
        return photoGridAdapter;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        captureManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        captureManager.onRestoreInstanceState(savedInstanceState);
        super.onViewStateRestored(savedInstanceState);
    }

    public ArrayList<String> getSelectedPhotoPaths() {
        return photoGridAdapter.getSelectedPhotoPaths();
    }

    @Override
    public void onDetach() {
        super.onDetach();

        for (PhotoDirectory directory : directories) {
            directory.getPhotoPaths().clear();
            directory.getPhotos().clear();
            directory.setPhotos(null);
        }
        directories.clear();
        directories = null;

    }
    private void addTime(String address){
//        Bitmap newBitmap = PhotoMarkUtil.addTimeOnPhotoByCalendar(getContext(), operateBitmap, null == address ? "" : address);
//        if (null != operateBitmap)
//            operateBitmap.recycle();
//        savePhoto(operatePath, newBitmap);
//        operatePath = null;
//        operateBitmap = null;
    }

    public void insertPhotoByPath(final String path, boolean addTime) {
        // 其次把文件插入到系统图库
        Bitmap bitmap = PhotoUtil.amendRotatePhoto(path, getContext());
//        PhotoMarkUtil.addTimeOnPhotoByCalendar(getContext(), path/*, 26*/);
//        if (addTime) {
//            operateBitmap = bitmap;
//            operatePath = path;
////            getLocation();
//            addTime(oldAddress);
//        } else {
            savePhoto(path, bitmap);
//        }
    }

    private void savePhoto(String path, Bitmap bitmap){
        PhotoUtil.savePhoto(path, bitmap);
        Trace.e(" image path : " + path);
        ScannerImage(path);

        // 刷新图片列表
        PhotoDirectory directory = directories.get(INDEX_ALL_PHOTOS);
        directory.getPhotos().add(INDEX_ALL_PHOTOS, new Photo(path.hashCode(), path));
        directory.setCoverPath(path);
        photoGridAdapter.notifyDataSetChanged();
    }

    private void ScannerImage(final String path){
        // 最后通知图库更新
        try {
            mMediaonnection = new MediaScannerConnection(getActivity(), new MediaScannerConnection.MediaScannerConnectionClient() {
                @Override
                public void onMediaScannerConnected() {
                    mMediaonnection.scanFile(path,null);
                }
                @Override
                public void onScanCompleted(String path, Uri uri) {
                    mMediaonnection.disconnect();
                }
            });
            mMediaonnection.connect();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void showLoadingDialog() {
        getLoadingDialog();
        if (!loadingMaterialDialog.isShowing()) {
            loadingMaterialDialog.show();
        }
    }

    public MaterialDialog getLoadingDialog(){
        if (null == loadingMaterialDialog) {
            loadingMaterialDialog = new MaterialDialog.Builder(getActivity())
                    .content("处理中...")
                    .contentGravity(GravityEnum.START)
                    .progress(true, 0)
                    .cancelable(false)
                    .progressIndeterminateStyle(false).build();
//            loadingMaterialDialog.getProgressBar().setIndeterminateDrawable(getResources().getDrawable(R.drawable.loading_progress));
        }
        return loadingMaterialDialog;
    }

    public void dismissLoadingDialog() {
        if (loadingMaterialDialog != null && loadingMaterialDialog.isShowing())
            loadingMaterialDialog.dismiss();
    }

    /**
     *  假设地球为一半径为R的表面光滑圆球体，
     *  表面上同一经线圈上相差1"两点间的距离为 2πR/360/3600
     *  表面上同一纬线圈上相差1"两点间的距离为 2πR×cos(纬度)/360/3600
     *  当R取半径平均值6371km时，
     *  地球表面上同一经线圈上相差1"两点间的距离约为30.887m
     *  地球表面上同一纬线圈上相差1"两点间的距离约为30.887m×cos(纬度)
     *  @param radius 误差范围(米)
     */
    public String calculateLl(double latitude, double longitude, double radius) {
        //维度 latitude
        //经度 longitude

        double[] gardenLat = {41.849441,41.745987,41.746646,41.741436,41.743486,41.664889,41.784103,41.84687,41.798287,41.809038,41.9152,41.849253,41.850304,41.745443,41.808026,41.753004,41.777574,41.870655,41.745327};
        double[] gardenLong = {123.404922,123.394737,123.397727,123.393247,123.392423,123.380677,123.421179,123.386515,123.132218,123.443905,123.103624,123.400436,123.398945,123.496819,123.441569,123.395994,123.557388,123.376708,123.391773};
        String[] gardenNameArr = {"格林春晓", "格林生活坊", "格林生活坊", "格林观堂", "格林观堂", "格林喜鹊花园", "格林SOHO", "格林常青藤", "格林繁荣里", "格林豪森", "格林康泉府", "格林梦夏", "格林mini", "格林馨港湾", "格林自由成", "格林玫瑰湾", "格林阳光城", "格林木棉花", "格林生活坊"};

        String gardenName = "";
        for (int i = 0; i < gardenNameArr.length; i++) {
            if (isInRadius(latitude, longitude, gardenLat[i], gardenLong[i], radius)) {
                gardenName = gardenNameArr[i];
                break;
            }
        }
        return gardenName;
    }

    public boolean isInRadius(double currentLatitude, double currentLongitude, double latitude, double longitude, double radius){
        //地球周长
        Double perimeter =  2 * Math.PI * 6371000;
        //纬度latitude的地球周长：latitude
        Double perimeter_latitude =   perimeter * Math.cos(Math.PI * latitude / 180);

        //一米对应的经度（东西方向）1M实际度
        double longitude_per_mi = 360 / perimeter_latitude;
        double latitude_per_mi = 360 / perimeter;
        Trace.e("longitude_per_mi : " + longitude_per_mi);
        Trace.e("latitude_per_mi : " + latitude_per_mi);
        Trace.e("radius : " + radius * longitude_per_mi);
        double leftLo = longitude - (radius * longitude_per_mi);
        double rightLo = longitude + (radius * longitude_per_mi);
        double topLa = latitude + (radius * latitude_per_mi);
        double bottomLa = latitude - (radius * latitude_per_mi);
        return currentLatitude > bottomLa && currentLatitude < topLa && currentLongitude > leftLo && currentLongitude < rightLo;
    }
}
