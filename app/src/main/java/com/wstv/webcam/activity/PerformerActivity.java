package com.wstv.webcam.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.libin.mylibrary.base.util.Trace;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.adapter.PerformerPagerAdapter;
import com.wstv.webcam.http.HttpService;
import com.wstv.webcam.http.callback.BaseCallback;
import com.wstv.webcam.http.callback.BaseCamCallback;
import com.wstv.webcam.http.model.IsFollowResult;
import com.wstv.webcam.http.model.audience.AudienceStateResult;
import com.wstv.webcam.http.model.room.Room;
import com.wstv.webcam.http.model.room.RoomStateResult;
import com.wstv.webcam.http.model.user.UserBean;
import com.wstv.webcam.http.model.user.UserResult;
import com.wstv.webcam.util.LiveRoomUtil;
import com.wstv.webcam.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Kindred on 2019/4/10.
 */

public class PerformerActivity extends BaseActivity {

    @Bind(R.id.activity_performer_tab)
    TabLayout tab;

    @Bind(R.id.activity_performer_pager)
    public ViewPager pager;

    @Bind(R.id.layout_user_info_sign)
    TextView sign;

    @Bind(R.id.temp_status)
    View tempStatus;

    @Bind(R.id.layout_user_info_name)
    public TextView userInfoName;

    @Bind(R.id.layout_user_info_num)
    TextView userInfoNum;

    @Bind(R.id.layout_user_info_fun_count)
    TextView fansCount;

    @Bind(R.id.layout_user_info_old)
    TextView age;

    @Bind(R.id.layout_user_info_gender)
    ImageView gender;

    @Bind(R.id.layout_user_info_avatar)
    ImageView avatar;

    @Bind(R.id.layout_user_info_level_logo)
    ImageView levelLogo;

    @Bind(R.id.layout_user_info_follow_count)
    TextView followCount;

    @Bind(R.id.layout_user_info_good_count)
    TextView goodCount;

    private PerformerPagerAdapter pagerAdapter;

    List<String> types;

    private String showId;

    private String performerId;

    private boolean fromCam;

    public String avatarUrl;

    public boolean isFans;

    @Override
    protected void initViewAndData() {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tempStatus.getLayoutParams();
        params.height = getStatusHeight();
        tempStatus.setLayoutParams(params);
        initPager();
        requestUserInfo();
        checkWith();
    }

    private void checkWith() {
        HttpService.getIsFollow(this, showId, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), new BaseCallback<IsFollowResult>(this, this) {
            @Override
            public void onSuccess(IsFollowResult isFollowResult, int id) {
                isFans = !TextUtils.isEmpty(isFollowResult.detail.ifAttention) && Boolean.parseBoolean(isFollowResult.detail.ifAttention);
            }
        });
    }

    private void requestUserInfo(){
        HttpService.getUserInfo(this, performerId, new BaseCallback<UserResult>(this, this) {
            @Override
            public void onSuccess(UserResult userResult, int id) {
                setUserData(userResult.detail);
                requestRoomState();
            }
        });
    }

    private void requestRoomState() {
        HttpService.getRoomState(this, showId, new BaseCallback<RoomStateResult>(this, this) {
            @Override
            public void onSuccess(RoomStateResult roomStateResult, int id) {
                ButterKnife.findById(PerformerActivity.this, R.id.layout_user_info_to_see).setVisibility(roomStateResult.detail.ret == 0 ? View.VISIBLE : View.GONE);
            }
        });
    }

    private void setUserData(UserBean userBean) {
        avatarUrl = userBean.headPortrait;
        Glide.with(this).load(userBean.headPortrait).into(avatar);
        sign.setText(userBean.signature);
        userInfoName.setText(userBean.nickname);
        userInfoNum.setText(userBean.showId);
        fansCount.setText(TextUtils.isEmpty(userBean.fans) ? "0" : NumberUtil.format10_000(Integer.parseInt(userBean.fans)));
        age.setText(TextUtils.isEmpty(userBean.age) ? "0" : userBean.age);
        followCount.setText(TextUtils.isEmpty(userBean.attentionCnt) ? "0" : NumberUtil.format10_000(Integer.parseInt(userBean.attentionCnt)));
        goodCount.setText(TextUtils.isEmpty(userBean.praise) ? "0" : NumberUtil.format10_000(Integer.parseInt(userBean.praise)));
        levelLogo.setImageResource(AppConstant.levelArr[(userBean.level - 1) < AppConstant.levelArr.length ? userBean.level - 1 : 0]);
        gender.setImageResource("0".equals(userBean.sex) ? R.drawable.icon_gender_female : R.drawable.icon_gender_male);
    }

    private void initPager(){
        types = new ArrayList<>();
        types.add("动态");
        types.add("短视频");
        pagerAdapter = new PerformerPagerAdapter(getSupportFragmentManager(), types, performerId);
        pager.setAdapter(pagerAdapter);
        tab.setupWithViewPager(pager);
        ButterKnife.findById(this, R.id.layout_user_info_gift_ranking).setVisibility(View.VISIBLE);
        ButterKnife.findById(this, R.id.layout_user_info_with_ranking).setVisibility(View.VISIBLE);
        ButterKnife.findById(this, R.id.layout_user_info_fun_layout).setVisibility(View.VISIBLE);
        ButterKnife.findById(this, R.id.layout_user_info_good_layout).setVisibility(View.VISIBLE);
    }

    @OnClick({R.id.layout_user_info_gift_ranking, R.id.layout_user_info_with_ranking, R.id.layout_user_info_to_see})
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.layout_user_info_gift_ranking:
                Bundle gift = new Bundle();
                gift.putString("showId", showId);
                readyGo(GiftRankingActivity.class, gift);
                break;
            case R.id.layout_user_info_with_ranking:
                Bundle bundle = new Bundle();
                bundle.putString("userId", performerId);
                bundle.putString("roomTitle", userInfoName.getText().toString());
                bundle.putString("withAvatar", avatarUrl);
                readyGo(ChatRoomActivity.class, bundle);
//                Bundle with = new Bundle();
//                with.putString("showId", showId);
//                readyGo(WithRankingActivity.class, with);
                break;
            case R.id.layout_user_info_to_see:
                if (fromCam) {
                    finish();
                } else {
                    enterRoom();
                }
                break;
        }
    }

    private void enterRoom() {
        HttpService.audienceState(this, showId, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), new BaseCallback<AudienceStateResult>(this, this) {
            @Override
            public void onSuccess(AudienceStateResult audienceStateResult, int id) {
                if (audienceStateResult.detail.state != 0) {
                    toCam(audienceStateResult);
                }
            }

            @Override
            protected void onFailed(String errCode, String errMsg, AudienceStateResult audienceStateResult) {
                super.onFailed(errCode, errMsg, audienceStateResult);
                showToastCenter("获取房间信息失败");
            }
        });
    }

    private void toCam(final AudienceStateResult audienceStateResult) {
        HttpService.getRoom(this, showId, new BaseCamCallback<Room>(this, this) {
            @Override
            public void onSuccess(Room room, int id) {
                Bundle bundle = new Bundle();
                bundle.putString(CamDetailActivity.BUNDLE_KEY_FLV_URL, room.mixedPlayURL);
                bundle.putString(CamDetailActivity.BUNDLE_KEY_LINK_URL, room.pushersMap.get(room.roomCreator).accelerateURL);
                bundle.putString(CamDetailActivity.BUNDLE_KEY_ROOM_ID, room.roomID);
                bundle.putString(CamDetailActivity.BUNDLE_KEY_PERFORMER_ID, room.roomCreator);
                bundle.putSerializable(CamDetailActivity.BUNDLE_KEY_PERFORMER, room.pushersMap.get(room.roomCreator));
                bundle.putSerializable(CamDetailActivity.BUNDLE_KEY_AUDIENCE, room.audiences);
                bundle.putInt(CamDetailActivity.BUNDLE_KEY_MANAGE, audienceStateResult.detail.manager);
                bundle.putLong(CamDetailActivity.BUNDLE_KEY_SHUT_UP, audienceStateResult.detail.endTime);
                LiveRoomUtil.getInstance((BaseActivity) context).getLiveRoom().addRoom(room);
                readyGo(CamDetailActivity.class, bundle);
            }

            @Override
            protected void onFailed(String errCode, String errMsg, Room room) {
                super.onFailed(errCode, errMsg, room);
                showToastCenter("获取房间信息失败");
            }
        });
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_performer;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        showId = extras.getString("showId");
        performerId = extras.getString("performerId");
        fromCam = extras.getBoolean("fromCam", false);
    }

    @Override
    protected int getStatusBarColor() {
        return android.R.color.transparent;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComming(EventCenter center) {

    }

    public int getStatusHeight() {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Trace.e("status height : " + statusHeight);
        return statusHeight;
    }
}
