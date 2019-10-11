package com.wstv.pad.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.libin.mylibrary.acp.Acp;
import com.libin.mylibrary.acp.AcpListener;
import com.libin.mylibrary.acp.AcpOptions;
import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.libin.mylibrary.base.util.Trace;
import com.libin.mylibrary.localimage.PhotoPickerActivity;
import com.libin.mylibrary.localimage.utils.PhotoPickerIntent;
import com.libin.mylibrary.util.GlideLoadUtils;
import com.libin.mylibrary.widget.wheel.WheelView;
import com.wstv.pad.AppConstant;
import com.wstv.pad.R;
import com.wstv.pad.activity.base.BaseActivity;
import com.wstv.pad.http.HttpService;
import com.wstv.pad.http.callback.BaseCallback;
import com.wstv.pad.http.model.user.UserBean;
import com.wstv.pad.http.model.user.UserResult;
import com.wstv.pad.util.upload.UploadUtil;
import com.wstv.pad.widget.MyEditText;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * <p>Description: </p>
 * UserInfoActivity
 *
 * @author lilibin
 * @createDate 2019/5/11 09:28
 */

public class UserInfoActivity extends BaseActivity {

    @Bind(R.id.activity_user_info_avatar)
    CircleImageView avatar;
    @Bind(R.id.activity_user_info_nickname)
    MyEditText nickname;
    @Bind(R.id.activity_user_info_sign)
    MyEditText sign;
    @Bind(R.id.activity_user_info_birthday)
    TextView birthday;
    @Bind(R.id.activity_user_info_gender)
    TextView gender;

    private String[] dialogTitle = {"年份", "月份", "日期"};

    private AlertDialog[] dialogArr;

    private WheelView[] wheelArr;

    private String[] currentValue = new String[3];

    private AlertDialog genderDialog;

    private WheelView genderWheel;

    private String currentGender;

    private int REQUEST_CODE_ADD_PICTURE = 0x9c;

    private String avatarPath;
    private String avatarUrl;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_user_info;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void initViewAndData() {
        UserBean userBean = getApp().getUserBean();
        dialogArr = new AlertDialog[3];
        wheelArr = new WheelView[3];
//        Glide.with(this).load(userBean.headPortrait).dontAnimate().placeholder(R.drawable.default_avatar).error(R.drawable.default_avatar).into(avatar);
        GlideLoadUtils.getInstance().glideLoad(this, userBean.headPortrait,
                avatar, R.drawable.default_avatar);
        gender.setText("0".equals(userBean.sex) ? "女" : "男");
        nickname.setText(userBean.nickname);
        sign.setText(userBean.signature);
        birthday.setText(userBean.birthday);
        setRightTextStr("保存");
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComming(EventCenter center) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.activity_user_info_birthday, R.id.activity_user_info_gender, R.id.activity_user_info_avatar})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.activity_user_info_birthday:
                showBirthdayYear();
                break;
            case R.id.activity_user_info_gender:
                showGenderDialog();
                break;
            case R.id.activity_user_info_avatar:
                Acp.getInstance(this)
                        .request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE)
                                .build(), new AcpListener() {
                            @Override
                            public void onGranted() {
                                Intent intent = new Intent(UserInfoActivity.this, PhotoPickerActivity.class);
                                PhotoPickerIntent.setPhotoCount(intent, 1);
                                PhotoPickerIntent.setShowGif(intent, false);
                                PhotoPickerIntent.setColumn(intent, 3);
                                startActivityForResult(intent, REQUEST_CODE_ADD_PICTURE);
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
                                showToastShort(permissions.toString() + "权限拒绝");
                                finish();
                            }
                        });
                break;
        }
    }

    private void showBirthdayYear() {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            data.add(String.valueOf(Calendar.getInstance(Locale.SIMPLIFIED_CHINESE).get(Calendar.YEAR) - 10 - i));
        }
        showBirthdayDialog(data, 0);
    }

    private void showBirthdayMonth() {
        ArrayList<String> data = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            data.add(String.valueOf(i + 1));
        }
        showBirthdayDialog(data, 1);
    }

    private void showBirthdayDay() {
        ArrayList<String> data = new ArrayList<>();
        if ("0".equals(currentValue[1]) || "2".equals(currentValue[1]) || "4".equals(currentValue[1]) || "6".equals(currentValue[1]) || "7".equals(currentValue[1]) || "9".equals(currentValue[1]) || "11".equals(currentValue[1])) {
            for (int i = 0; i < 31; i++) {
                data.add(String.valueOf(i + 1));
            }
        } else {
            for (int i = 0; i < 30; i++) {
                data.add(String.valueOf(i + 1));
            }
        }
        showBirthdayDialog(data, 2);
    }

    private void showBirthdayDialog(List<String> data, final int type) {
        if (null == dialogArr[type]) {
            wheelArr[type] = new WheelView(this);
            wheelArr[type].setOffset(2);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            wheelArr[type].setLayoutParams(params);
            wheelArr[type].setOffset(2);
            wheelArr[type].setItems(data);
            currentValue[type] = data.get(0);
            wheelArr[type].setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    Trace.e("[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                    currentValue[type] = item;
//                requestData();
                }
            });
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.addView(wheelArr[type]);
            dialogArr[type] = new AlertDialog.Builder(this)
                    .setTitle(dialogTitle[type])
                    .setView(linearLayout)
                    .setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (type < 2) {
                                showBirthday(type + 1);
                            } else {
                                birthday.setText(currentValue[0] + "-" + Integer.parseInt(currentValue[1]) + "-" + currentValue[2]);
                            }
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        } else {
            wheelArr[type].setItems(data);
            wheelArr[type].setSeletion(data.indexOf(currentValue[type]) < 0 ? 0 : data.indexOf(currentValue[type]));
            dialogArr[type].setTitle(dialogTitle[type]);
            dialogArr[type].show();
        }
    }

    private void showGenderDialog() {
        if (null == genderDialog) {
            genderWheel = new WheelView(this);
            genderWheel.setOffset(2);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.weight = 1;
            genderWheel.setLayoutParams(params);
            genderWheel.setOffset(2);
            ArrayList<String> data = new ArrayList<>();
            data.add("女");
            data.add("男");
            genderWheel.setItems(data);
            currentGender = data.get(0);
            genderWheel.setOnWheelViewListener(new WheelView.OnWheelViewListener() {
                @Override
                public void onSelected(int selectedIndex, String item) {
                    Trace.e("[Dialog]selectedIndex: " + selectedIndex + ", item: " + item);
                    currentGender = item;
                }
            });
            LinearLayout linearLayout = new LinearLayout(this);
            linearLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.addView(genderWheel);
            genderDialog = new AlertDialog.Builder(this)
                    .setTitle("性别")
                    .setView(linearLayout)
                    .setCancelable(false)
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            gender.setText(currentGender);
                        }
                    })
                    .setNegativeButton("取消", null)
                    .show();
        } else {
            genderWheel.setSeletion("女".equals(gender.getText().toString()) ? 0 : 1);
            genderDialog.show();
        }
    }

    private void showBirthday(int type) {
        if (type == 0) {
            showBirthdayYear();
        } else if (type == 1) {
            showBirthdayMonth();
        } else {
            showBirthdayDay();
        }
    }

    @Override
    public void onRightClick(View v) {
        super.onRightClick(v);
        HttpService.updateUserInfo(this, birthday.getText().toString(), avatarUrl,
                PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), nickname.getText().toString(),
                "女".equals(gender.getText()) ? "0" : "1", sign.getText().toString(),
                new BaseCallback<UserResult>(this, this) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onSuccess(UserResult userResult, int id) {
                        getApp().setUserBean(userResult.detail);
                        EventBus.getDefault().post(new EventCenter(2050));
                        finish();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_ADD_PICTURE) {
            if (data != null) {
                List<String> paths = data.getStringArrayListExtra(PhotoPickerActivity.KEY_SELECTED_PHOTOS);
                if (null != paths && paths.size() > 0) {
                    avatarPath = paths.get(0);
//                    Glide.with(this).load(new File(avatarPath)).into(avatar);
                    GlideLoadUtils.getInstance().glideLoad(this, new File(avatarPath),
                            avatar);
                    uploadAvatar();
                }
            }
        }
    }

    private void uploadAvatar() {
        UploadUtil.uploadAsync(this, avatarPath, "/" + PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID) + String.valueOf(System.currentTimeMillis()).substring(7), new UploadUtil.UploadCallback() {
            @Override
            public void onSuccess(String url) {
                avatarUrl = url;
            }

            @Override
            public void onError() {

            }
        });
    }
}
