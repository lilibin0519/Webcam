package com.wstv.pad.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.display.DisplayManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.libin.mylibrary.base.util.Trace;
import com.libin.mylibrary.http.utils.GsonUtils;
import com.libin.mylibrary.util.GlideLoadUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.wstv.pad.AppConstant;
import com.wstv.pad.R;
import com.wstv.pad.activity.base.BaseActivity;
import com.wstv.pad.adapter.LivePagerAdapter;
import com.wstv.pad.dialog.OrderCodeDialog;
import com.wstv.pad.entity.CustomMsg;
import com.wstv.pad.http.HttpService;
import com.wstv.pad.http.callback.BaseCallback;
import com.wstv.pad.http.model.EmptyResult;
import com.wstv.pad.http.model.IsFollowResult;
import com.wstv.pad.http.model.Pusher;
import com.wstv.pad.http.model.audience.Audience;
import com.wstv.pad.http.model.audience.AudienceStateResult;
import com.wstv.pad.http.model.gift.GiftAnim;
import com.wstv.pad.http.model.gift.GiftBean;
import com.wstv.pad.http.model.gift.GiftListResult;
import com.wstv.pad.http.model.gift.GiftType;
import com.wstv.pad.http.model.guard.GuardGood;
import com.wstv.pad.http.model.guard.GuardResult;
import com.wstv.pad.http.model.order.CodeResult;
import com.wstv.pad.http.model.room.Room;
import com.wstv.pad.http.model.room.RoomType;
import com.wstv.pad.http.model.room.RoomTypeResult;
import com.wstv.pad.http.model.user.UserResult;
import com.wstv.pad.tencent.liveroom.ILiveRoomListener;
import com.wstv.pad.tencent.liveroom.LiveRoom;
import com.wstv.pad.tencent.roomutil.commondef.PusherInfo;
import com.wstv.pad.tencent.roomutil.misc.TextMsgInputDialog;
import com.wstv.pad.util.LiveRoomUtil;
import com.wstv.pad.util.NumberUtil;
import com.wstv.pad.util.click.ClickProxy;
import com.wstv.pad.widget.DifferentDisplay;
import com.wstv.pad.widget.GiftPopWindow;
import com.wstv.pad.widget.GiftTablePop;
import com.wstv.pad.widget.NoScrollViewPager;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import em.sang.com.allrecycleview.adapter.DefaultAdapter;
import em.sang.com.allrecycleview.cutline.RecycleViewDivider;
import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Created by Kindred on 2019/7/10.
 */

public class LivePageActivity extends BaseActivity {

    public static final String BUNDLE_KEY_FLV_URL = "bundle.key.flv.url";

    public static final String BUNDLE_KEY_LINK_URL = "bundle.key.link.url";

    public static final String BUNDLE_KEY_PERFORMER_ID = "key.performer.id";

    public static final String BUNDLE_KEY_PERFORMER = "key.performer.entity";

    public static final String BUNDLE_KEY_ROOM_ID = "key.room.id";

    public static final String BUNDLE_KEY_AUDIENCE = "key.performer.audience";

    public static final String BUNDLE_KEY_MANAGE = "key.performer.manage";

    public static final String BUNDLE_KEY_SHUT_UP = "key.performer.shut.up";

    @Bind(R.id.activity_live_page_tab)
    TabLayout tab;

    @Bind(R.id.activity_live_page_pager)
    public NoScrollViewPager pager;

    private LivePagerAdapter pagerAdapter;

    List<RoomType> types;

    /* 直播相关 */
    private String flvUrl;

    private String performerId;

    private LiveRoom liveRoom;

    private String roomId;

    private TextMsgInputDialog mTextMsgInputDialog;

    private GiftTablePop giftTable;

    private List<GiftType> giftData;

    public int giftSelectPosition = -1;

    private String guard = "";

    private List<GuardGood> guardList = new ArrayList<>();

    private Dialog guardBuy;

    private DefaultAdapter<GuardGood> guardAdapter;

    private int manageState = -1;

    public long shutUpTo;

    private GiftPopWindow giftPopWindow;

    public int typeIndex;

    private DifferentDisplay mPresentation;

    CustomMsg msg;

    private String linkPlayUrl;

    private Pusher pusher;

//    private ArrayList<Audience> audiences;

    @Bind(R.id.activity_live_page_current_avatar)
    public RoundedImageView currentAvatar;

    @Bind(R.id.activity_live_page_sign)
    public TextView currentSign;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_live_page;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void getBundleExtras(Bundle extras) {
        performerId = extras.getString(BUNDLE_KEY_PERFORMER_ID);
        flvUrl = extras.getString(BUNDLE_KEY_FLV_URL);
        roomId = extras.getString(BUNDLE_KEY_ROOM_ID);
        linkPlayUrl = extras.getString(BUNDLE_KEY_LINK_URL);
        pusher = (Pusher) extras.getSerializable(BUNDLE_KEY_PERFORMER);
//        audiences = (ArrayList<Audience>) extras.getSerializable(BUNDLE_KEY_AUDIENCE);
//        if (null == audiences) {
//            audiences = new ArrayList<>();
//        }
        manageState = extras.getInt(BUNDLE_KEY_MANAGE, -1);
        shutUpTo = extras.getLong(BUNDLE_KEY_SHUT_UP, 0);
    }

    @Override
    protected void initViewAndData() {
        types = new ArrayList<>();

        msg = new CustomMsg();
        msg.data = new CustomMsg.Data();
        msg.user = new CustomMsg.User();
        msg.user.level = getApp().getUserBean().level;
        msg.user.sex = getApp().getUserBean().sex;

        requestRoomType();
        initInputDialog();
        liveRoom = LiveRoomUtil.getInstance(this).getLiveRoom();
        liveRoom.setLiveRoomListener(createRoomListener());

        giftData = new ArrayList<>();
        requestGiftList();
        getGuardGood();

        giftPopWindow = new GiftPopWindow(this, new OnToolsItemClickListener<GiftAnim>() {
            @Override
            public void onItemClick(int position, GiftAnim item) {
                GiftBean bean = giftData.get(typeIndex).giftDTOList.get(giftSelectPosition);
                bean.setTheSendGiftSize(Integer.parseInt(item.num));
                bean.setUserName(getApp().getUserBean().nickname);
                bean.setTheUserId(getApp().getUserBean().userId);
                bean.userAvatar = getApp().getUserBean().headPortrait;
                buyGift(bean);
//                sendGift(count, bean);
//                        showGiftAnim(bean, msg.data.giftUrl);
//                showToastCenter(giftData.get(typeIndex).giftDTOList.get(giftSelectPosition).getGiftName() + " x " + item.num);
            }
        });

        askForPermission();

        setCurrentPerformer();
    }

    private void setCurrentPerformer() {
//        Glide.with(this).load(pusher.userAvatar).into(currentAvatar);
        GlideLoadUtils.getInstance().glideLoad(this, pusher.userAvatar,
                currentAvatar);
        requestCurrentSign();
    }

    public void showGuardBuy(){
        if (null == guardBuy) {
            guardBuy = new Dialog(this, R.style.BottomDialog);
            guardBuy.setCanceledOnTouchOutside(true);
            guardBuy.setCancelable(true);
            View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_guard_buy, null);
            guardBuy.setContentView(dialogView);
            RecyclerView guardContent = dialogView.findViewById(R.id.dialog_guard_buy_list);
            guardContent.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL));
            guardContent.setLayoutManager(new LinearLayoutManager(this));
            guardContent.setAdapter(guardAdapter = new DefaultAdapter<>(this, guardList, R.layout.item_guard_type, new DefaultAdapterViewListener<GuardGood>() {
                @Override
                public CustomHolder getBodyHolder(Context context, List<GuardGood> lists, int itemID) {
                    return new CustomHolder<GuardGood>(context, lists, itemID) {
                        @Override
                        public void initView(final int position, final List<GuardGood> datas, Context context) {
                            itemView.setOnClickListener(new ClickProxy(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new AlertDialog.Builder(LivePageActivity.this).setTitle("提示")
                                            .setMessage("确认购买" + datas.get(position).name + "?")
                                            .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    buyGuard(datas.get(position));
                                                    dialog.dismiss();
                                                }
                                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).show();
                                }
                            }));
                            holderHelper.setImageResource(R.id.item_guard_type_logo, "1".equals(datas.get(position).id) ? R.drawable.icon_user_guard_month : R.drawable.icon_user_guard_year);
                            holderHelper.setText(R.id.item_guard_type_label, datas.get(position).name);
                        }
                    };
                }
            }));
        }
        if (!guardBuy.isShowing()) {
            guardBuy.show();
        }
    }

    private void buyGuard(final GuardGood guardGood) {
        HttpService.buyGuard(this, guardGood.id, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), roomId, new BaseCallback<EmptyResult>(this, this) {
            @Override
            public void onSuccess(EmptyResult emptyResult, int id) {
                guard = guardGood.name.contains("月") ? "M" : "Y";
//                if (null != giftDialog) {
//                    giftDialog.findViewById(R.id.dialog_gift_list_get_with).setVisibility(View.GONE);
//                }
                if (null != giftTable) {
                    giftTable.getContentView().findViewById(R.id.dialog_gift_list_get_with).setVisibility(View.GONE);
                }
                showToastCenter(guardGood.name + "办理成功");
                guardBuy.dismiss();
            }

            @Override
            protected void onFailed(String errCode, String errMsg, EmptyResult emptyResult) {
                if ("-101".equals(errCode)) {
                    // 以后可跳转充值
                    toRecharge();
                }
            }
        });
    }

    public void showGiftPop(float x, float y, List<GiftAnim> giftComboDTOS) {
        if (null == giftComboDTOS) {
            showToastCenter("null == giftComboDTOS");
            return;
        }
        giftPopWindow.addPosition((int)x, (int)y);
        giftPopWindow.onRefreshData(giftComboDTOS);
        giftPopWindow.show(ButterKnife.findById(this, R.id.activity_live_page_parent));
    }

    public void changeRoom(Room room){
        if (null == mPresentation) {
            showToastCenter("未找到第二个屏幕");
            return;
        }
        audienceState(room);
    }

    private void audienceState(final Room room) {
        HttpService.audienceState(this, room.roomID, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), new BaseCallback<AudienceStateResult>(this, this) {
            @Override
            public void onSuccess(AudienceStateResult audienceStateResult, int id) {
                if (audienceStateResult.detail.state != 0) {
                    liveRoom.removeCurrentRoom();
                    if (mPresentation.loading.getVisibility() != View.VISIBLE) {
                        sendUserExit();
                    }
                    liveRoom.exitRoom(new LiveRoom.ExitRoomCallback() {
                        @Override
                        public void onSuccess() {
                            Trace.e("exitRoom success");
                        }

                        @Override
                        public void onError(int errCode, String e) {
                            Trace.e("exitRoom failed, errorCode = " + errCode + " errMessage = " + e);
                        }
                    });
                    performerId = room.roomCreator;
                    flvUrl = room.mixedPlayURL;
                    roomId = room.roomID;
                    manageState = audienceStateResult.detail.manager;
                    shutUpTo = audienceStateResult.detail.endTime;
                    LiveRoomUtil.getInstance(LivePageActivity.this).getLiveRoom().addRoom(room);
                    mPresentation.audiences = room.audiences;
                    pusher = room.pushersMap.get(room.roomCreator);
                    setPresentationData();
                    setCurrentPerformer();
                    enterRoom2(mPresentation.videoView);
                }
            }
        });
    }

    public void requestCurrentSign(){
        HttpService.getUserInfo(this, performerId, new BaseCallback<UserResult>(this, this) {
            @Override
            public void onSuccess(UserResult userResult, int id) {
                currentSign.setText(userResult.detail.signature);
            }
        });
    }

    /**
     * 请求用户给予悬浮窗的权限
     */
    public void askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "当前无权限，请授权！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 0x66);
            } else {

                initDifferentDisplay();
            }
        } else {

            initDifferentDisplay();
        }
    }

    @SuppressWarnings("deprecation")
    private void initDifferentDisplay() {

        showToastCenter("授权成功");
        DisplayManager mDisplayManager;// 屏幕管理类
        mDisplayManager = (DisplayManager) this
                .getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = mDisplayManager.getDisplays();

        if (mPresentation == null/* && displays.length > 1*/) {
            mPresentation = new DifferentDisplay(this, displays[displays.length - 1]);// displays[1]是副屏
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//6.0
                mPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
            } else {
                mPresentation.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            }
            setPresentationData();

            try {
                mPresentation.show();
            } catch (Exception e) {
                showToastCenter("Exception");
                String exception = getExceptionToString(e);
                new AlertDialog.Builder(this).setMessage(TextUtils.isEmpty(exception) ? "null" : exception).show();
            }
        } else {
            new AlertDialog.Builder(this).setMessage("未找到第二个屏幕")
                    .setCancelable(false).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            }).show();
        }
    }

    private void setPresentationData(){
        mPresentation.setPerformerId(performerId);
        mPresentation.setRoomId(roomId);
        mPresentation.setGiftData(giftData);
        mPresentation.pusher = pusher;
        mPresentation.setPerformerInfo();
    }

    /**
     * 将 Exception 转化为 String
     */
    public static String getExceptionToString(Throwable e) {
        String result;
        if (e == null){
            return "";
        }
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        result = stringWriter.toString();
        try {
            stringWriter.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return result;
    }

    public void enterRoom2(TXCloudVideoView videoView){
        liveRoom.enterRoom(roomId, flvUrl, getApp().getUserBean().sex, getApp().getUserBean().level, videoView, new LiveRoom.EnterRoomCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                errorGoBack("进入直播间错误", errCode, errInfo);
            }

            @Override
            public void onSuccess() {
                sendUserAdd();
                mPresentation.loading.setVisibility(View.GONE);
                getAud();
            }
        });
    }

    private void getAud(){
        liveRoom.getAudienceList(roomId, new LiveRoom.GetAudienceListCallback() {
            @Override
            public void onError(int errCode, String errInfo) {

            }

            @Override
            public void onSuccess(ArrayList<Audience> audienceList) {
                mPresentation.audiences.clear();
                mPresentation.audiences.addAll(audienceList);

//                if (audiences.size() < 4) {
                mPresentation.setAudiences();
//                }
                if (null != mPresentation.audienceAdapter) {
                    mPresentation.audienceAdapter.notifyDataSetChanged();
                }
                mPresentation.userCount.setText(NumberUtil.format10_000(mPresentation.audiences.size()) + "人");
            }
        });
    }

    private void sendUserAdd(){
        msg.type = "chat";
        msg.subType = "join";
        msg.user.level = getApp().getUserBean().level;
        msg.user.sex = getApp().getUserBean().sex;
        msg.user.guard = guard;
        liveRoom.sendRoomCustomMsg("wsUser", /*"{\"type\":\"gift\", \"no\":\"100\"}"*/GsonUtils.gsonString(msg), new LiveRoom.SendCustomMessageCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                showToastCenter("send join Error");
            }

            @Override
            public void onSuccess() {
                showToastCenter("send join success");
//                mPresentation.addUser(getApp().getUserBean().userId, getApp().getUserBean().headPortrait, getApp().getUserBean().sex, getApp().getUserBean().nickname, getApp().getUserBean().level);
//                mPresentation.userCount.setText(NumberUtil.format10_000(mPresentation.audiences.size()) + "人");
                addSelfText(getApp().getUserBean().nickname, getApp().getUserBean().level);
            }
        });
    }

    private void addSelfText(String userName, int level) {
        mPresentation.addUserText(userName, level, guard);
    }

    private void errorGoBack(String title, int errCode, String errInfo) {
        if (mPresentation.loading.getVisibility() != View.VISIBLE) {
            sendUserExit();
        }
        liveRoom.exitRoom(null);
        if (errCode == 10010) {
            mPresentation.loading.setText("主播暂未开播");
        }
        mPresentation.loading.setVisibility(View.VISIBLE);
//        new AlertDialog.Builder(this)
//                .setTitle(title)
//                .setMessage(errInfo + "[" + errCode + "]")
//                .setCancelable(false)
//                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
////                        finish();
//                    }
//                }).show();
    }

    private void sendUserExit(){
        msg.type = "chat";
        msg.subType = "exit";
        msg.user.level = getApp().getUserBean().level;
        msg.user.sex = getApp().getUserBean().sex;
        msg.user.guard = guard;
        liveRoom.sendRoomCustomMsg("wsUser", /*"{\"type\":\"gift\", \"no\":\"100\"}"*/GsonUtils.gsonString(msg), new LiveRoom.SendCustomMessageCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                showToastCenter("send exit Error");
            }

            @Override
            public void onSuccess() {
            }
        });
    }

    private void getGuardGood() {
        HttpService.getGuardGood(this, new BaseCallback<GuardResult>(this, this) {
            @Override
            public void onSuccess(GuardResult guardResult, int id) {
                guardList.addAll(guardResult.detail);
            }
        });
    }

    private void requestGiftList() {
        giftTable = new GiftTablePop(this, giftData, new GiftTablePop.SendListener() {
            @Override
            public void onSendGift(String count, GiftBean bean) {
                bean.setTheSendGiftSize(Integer.parseInt(count));
                bean.setUserName(getApp().getUserBean().nickname);
                bean.setTheUserId(getApp().getUserBean().userId);
                bean.userAvatar = getApp().getUserBean().headPortrait;
                buyGift(bean);
            }
        }, getApp().getUserBean().coins);
        HttpService.getGiftList(this, new BaseCallback<GiftListResult>(this, this) {
            @Override
            public void onSuccess(GiftListResult giftListResult, int id) {
                giftData.addAll(giftListResult.detail);
//                giftDialog.notifyData();
                giftTable.notifyAllData();
                checkWith();
            }

            @Override
            protected void onFailed(String errCode, String errMsg, GiftListResult giftListResult) {
                checkWith();
            }
        });
    }

    private void checkWith() {
        HttpService.getIsFollow(this, roomId, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), new BaseCallback<IsFollowResult>(this, this) {
            @Override
            public void onSuccess(IsFollowResult isFollowResult, int id) {
//                isFans = !TextUtils.isEmpty(isFollowResult.detail.ifAttention) && Boolean.parseBoolean(isFollowResult.detail.ifAttention);
//                followButton.setImageResource(isFans ? R.drawable.icon_follow_cancel : R.drawable.icon_follow);
//                guard = !TextUtils.isEmpty(isFollowResult.detail.ifGuard) && Boolean.parseBoolean(isFollowResult.detail.ifGuard) ? "Y" : "";
                guard = isFollowResult.detail.guardType == null ? "" : isFollowResult.detail.guardType;

                // TODO 隐藏守护的所有功能
                guard = "";
//                if (null != giftDialog) {
//                    giftDialog.setGuard(!TextUtils.isEmpty(guard));
//                }
                if (null != giftTable) {
                    giftTable.setGuard(!TextUtils.isEmpty(guard));
                }
            }
        });
    }

    private void buyGift(final GiftBean bean) {
//        HttpService.buyGift(this, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), roomId, bean.id, String.valueOf(bean.getTheSendGiftSize()),
//                new BaseCallback<EmptyResult>(this, this) {
//                    @Override
//                    public void onSuccess(EmptyResult emptyResult, int id) {
//                        sendGift(String.valueOf(bean.getTheSendGiftSize()), bean);
//                    }
//
//                    @Override
//                    protected void onFailed(String errCode, String errMsg, EmptyResult emptyResult) {
//                        super.onFailed(errCode, errMsg, emptyResult);
//                        if ("-101".equals(errCode)) {
//                            // 以后可跳转充值
//                            toRecharge();
//                        } else {
//                            showToastCenter("礼物赠送失败，请重试");
//                        }
//                    }
//                });

        HttpService.requestOrderCode(this, bean.getTheSendGiftSize(), bean.id, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID),
                new BaseCallback<CodeResult>(this, this) {
            @Override
            public void onSuccess(CodeResult codeResult, int id) {
                showOrderCode(codeResult, bean);
            }
        });
    }

    private void showOrderCode(final CodeResult codeResult, final GiftBean bean) {
        new OrderCodeDialog(this, codeResult.detail.url, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpService.requestPayResult(LivePageActivity.this, bean.getTheSendGiftSize(), bean.id, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID),
                        codeResult.detail.orderId, roomId, new BaseCallback<EmptyResult>(LivePageActivity.this, LivePageActivity.this) {
                            @Override
                            public void onSuccess(EmptyResult emptyResult, int id) {
                                sendGift(String.valueOf(bean.getTheSendGiftSize()), bean);
                            }

                            @Override
                            protected void onFailed(String errCode, String errMsg, EmptyResult emptyResult) {
                                super.onFailed(errCode, errMsg, emptyResult);
                            }
                        });
            }
        }).show();
    }

    private void sendGift(String count, final GiftBean bean){
        msg.type = "gift";
        msg.subType = "gift";
        msg.data.iconUrl = bean.iconUrl;
//        if ("1".equals(count)) {
//            msg.data.giftUrl = bean.animationUrl;
//        } else if ("5".equals(count)) {
//            msg.data.giftUrl = bean.fiveTimesUrl;
//        } else if ("10".equals(count)) {
//            msg.data.giftUrl = bean.tenTimesUrl;
//        }
        if ("1".equals(count)) {
            msg.data.giftUrl = bean.animationUrl;
        } else {
            for (GiftAnim anim : bean.giftComboDTOS) {
                if (anim.num.equals(count)) {
                    msg.data.giftUrl = anim.svgaUrl;
                }
            }
        }
        msg.data.giftName = bean.name;
        msg.data.giftId = bean.id;
        msg.data.giftCount = count;
        msg.data.giftAmount = String.valueOf(Integer.parseInt(bean.showCoin) * bean.getTheSendGiftSize());

        getApp().getUserBean().coins = String.valueOf(Integer.parseInt(getApp().getUserBean().coins) - Integer.parseInt(msg.data.giftAmount));
//        giftDialog.setAccount(getApp().getUserBean().coins);
        giftTable.setAccount(getApp().getUserBean().coins);

        liveRoom.sendRoomCustomMsg("wsGift", /*"{\"type\":\"gift\", \"no\":\"100\"}"*/GsonUtils.gsonString(msg), new LiveRoom.SendCustomMessageCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                showToastCenter("send gift Error");
            }

            @Override
            public void onSuccess() {
                showToastCenter("send gift success");
                mPresentation.showGiftAnim(bean, msg.data.giftUrl);

                mPresentation.goldValue += Integer.parseInt(msg.data.giftAmount);
                mPresentation.goldCount.setText(NumberUtil.format10_000(mPresentation.goldValue));
            }
        });
    }

    private void toRecharge(){
        new AlertDialog.Builder(this).setTitle("提示")
                .setMessage("余额不足，是否前往充值？")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        readyGo(RechargeActivity.class);
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();
    }

    private ILiveRoomListener createRoomListener() {
        return new ILiveRoomListener() {
            @Override
            public void onGetPusherList(List<PusherInfo> pusherList) {
            }

            @Override
            public void onPusherJoin(PusherInfo pusherInfo) {
//                liveRoom.addRemoteView();
            }

            @Override
            public void onPusherQuit(PusherInfo pusherInfo) {

            }

            @Override
            public void onRecvJoinPusherRequest(String userID, String userName, String userAvatar) {

            }

            @Override
            public void onKickOut() {

            }

            @Override
            public void onRecvPKRequest(String userID, String userName, String userAvatar, String streamUrl) {

            }

            @Override
            public void onRecvPKFinishRequest(String userID) {

            }

            @Override
            public void onRecvRoomTextMsg(String roomID, String userID, String userName, String userAvatar, String message) {
                Trace.e("custom receive : " + message);
                CustomMsg msg = GsonUtils.gsonToBean(message, CustomMsg.class);
                mPresentation.addRoomText(userName, msg.data.message, msg.user.level, /*msg.user.guard*/"");
            }

            @Override
            public void onRecvRoomCustomMsg(String roomID, String userID, String userName, String userAvatar, String cmd, String message) {
//                if ("wsGift".equals(cmd)) {
//                    GiftEntity giftEntity = GsonUtils.gsonToBean(message, GiftEntity.class);
//                    if ("100".equals(giftEntity.no)) {
//                        showGiftAnim(id);
//                    }
//                }
                Trace.e("userAvatar : " + userAvatar);
                CustomMsg msg = GsonUtils.gsonToBean(message, CustomMsg.class);
                if ("gift".equals(msg.type)) {
                    GiftBean myBean = getGiftById(msg.data.giftId);
                    if (null != myBean) {
                        myBean.setTheSendGiftSize(Integer.parseInt(msg.data.giftCount));
                        myBean.setTheUserId(userID);
                        myBean.setUserName(userName);
                        myBean.userAvatar = userAvatar;
                    }
                    mPresentation.showGiftAnim(myBean, msg.data.giftUrl);
                    mPresentation.goldValue += Integer.parseInt(msg.data.giftAmount);
                    mPresentation.goldCount.setText(NumberUtil.format10_000(mPresentation.goldValue));
                } else if ("chat".equals(msg.type)) {
                    if ("join".equals(msg.subType)) {
                        mPresentation.addUser(userID, userAvatar, msg.user.sex, userName, msg.user.level);
                        mPresentation.addUserText(userName, msg.user.level, /*msg.user.guard*/"");
                    } else if ("system".equals(msg.subType)) {
                        mPresentation.addSystemText(msg.data.message);
                    } else if ("normal".equals(msg.subType) || "guard".equals(msg.subType)) {
                        mPresentation.addRoomText(userName, msg.data.message, msg.user.level, /*msg.user.guard*/"");
                    } else if ("guard".equals(msg.subType)) {
                        mPresentation.addRoomText(userName, msg.data.message, msg.user.level, /*msg.user.guard*/"");
                    } else if ("exit".equals(msg.subType)) {
                        mPresentation.removeAudience(userID);
                        mPresentation.exitUserText(userName, msg.user.level, /*msg.user.guard*/"");
                    }
                } else if ("barrage".equals(msg.type)) {
                    mPresentation.barrageView.addTextitem(msg.data.message, /*msg.user.guard*/"");
                } else if ("redPacket".equals(msg.type)) {
//                    if ("send".equals(msg.subType)) {
//                        showRedPacket(msg, userAvatar, userName);
//                    } else if ("receive".equals(msg.subType)) {
//                    }
                } else if ("manage".equals(msg.type)) {
                    if ("shutup".equals(msg.subType)) {
                        shutUpTo = System.currentTimeMillis() + msg.data.time;
                    } else if ("kickout".equals(msg.subType) || "black".equals(msg.subType)) {
                        onLeftClick(null);
                    }
                }
            }

            @Override
            public void onRoomClosed(String roomID) {
                mPresentation.loading.setText("房间关闭了");
                mPresentation.loading.setVisibility(View.VISIBLE);
            }

            @Override
            public void onDebugLog(String log) {
                Trace.e(log);
            }

            @Override
            public void onError(int errorCode, String errorMessage) {

            }

            @Override
            public void onLivePlayEvent(int event, Bundle params) {
                Trace.e("CamDetail", "event : " + event);
                if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == TXLiveConstants.PLAY_EVT_PLAY_END) {
                    // 网络断连，且经多次重连亦不能恢复，更多重试请自行重启播放 || 直播正常结束
//                    roomListenerCallback.onDebugLog("[AnswerRoom] 拉流失败：网络断开");
//                    roomListenerCallback.onError(-1, "网络断开，拉流失败");

                    mPresentation.loading.setText("主播已下播");
                    mPresentation.loading.setVisibility(View.VISIBLE);
                } else if (event == TXLiveConstants.PLAY_EVT_GET_MESSAGE) {
                    // 自定义消息
                    String msg = null;
                    try {
                        msg = new String(params.getByteArray(TXLiveConstants.EVT_GET_MSG), "UTF-8");
//                        roomListenerCallback.onRecvAnswerMsg(msg);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                    mPresentation.loading.setVisibility(View.GONE);
                }
            }
        };
    }

    private GiftBean getGiftById(String giftId) {
        for (GiftType type : giftData) {
            for (GiftBean bean : type.giftDTOList) {
                if (bean.id.equals(giftId)) {
                    return bean;
                }
            }
        }
        return null;
    }

    @OnClick({/*R.id.activity_cam_detail_close, */R.id.activity_live_page_talk, R.id.activity_live_page_gift/*,
            R.id.activity_cam_detail_message, R.id.activity_cam_detail_share, R.id.activity_cam_detail_interview_layout,
            R.id.activity_cam_detail_performer_follow, *//*R.id.activity_cam_detail_gold_layout, R.id.activity_cam_detail_with_layout, R.id.activity_cam_detail_user_layout*/})
    public void onClick(View view){
        if (null == mPresentation || mPresentation.loading.getVisibility() == View.VISIBLE) {
//            if (view.getId() == R.id.activity_cam_detail_close) {
//                onLeftClick(view);
//            }
            return;
        }
        switch (view.getId()) {
//            case R.id.activity_cam_detail_close:
//                onLeftClick(view);
//                break;
            case R.id.activity_live_page_talk:
//                input.setVisibility(View.VISIBLE);
//                input.requestFocus();
                showInputMsgDialog();
//                liveRoom.sendPrivateMessage("402881f86a09dfd0016a09f345c80000", "");
                break;
            case R.id.activity_live_page_gift:
//                sendGift();
//                giftDialog.show();
                giftTable.show(ButterKnife.findById(this, R.id.activity_live_page_parent));
                break;
//            case R.id.activity_cam_detail_message:
//                // 私信
//                messagePoint.setVisibility(View.GONE);
//                break;
//            case R.id.activity_cam_detail_share:
//                break;
//            case R.id.activity_cam_detail_interview_layout:
//                // 连麦
//                if (mIsBeingLinkMic) {
//                    stopLinkMic();
//                } else {
//                    if (null != liveRoom.getCurrentRoomPushers() && liveRoom.getCurrentRoomPushers().size() < 2) {
//                        requestJoinPusher();
//                    } else {
//                        showToastCenter("主播正在连麦中");
//                    }
//                }
//                break;
//            case R.id.activity_cam_detail_performer_follow:
//                // 关注主播
//                if (isFans) {
//                    cancelFollow();
//                } else {
//                    requestFollow();
//                }
//                break;
//            case R.id.activity_cam_detail_gold_layout:
//                Bundle bundleGift = new Bundle();
//                bundleGift.putString("showId", roomId);
//                readyGo(GiftRankingActivity.class, bundleGift);
//                break;
//            case R.id.activity_cam_detail_with_layout:
//                Bundle bundleWith = new Bundle();
//                bundleWith.putString("showId", roomId);
//                readyGo(WithRankingActivity.class, bundleWith);
//                break;
//            case R.id.activity_cam_detail_user_layout:
////                showAudienceList();
//                break;
        }
    }

    @SuppressWarnings("deprecation")
    private void showInputMsgDialog() {
        if (System.currentTimeMillis() < shutUpTo) {
            showToastCenter("禁言期间，无法参与聊天");
            return;
        }
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams lp = mTextMsgInputDialog.getWindow().getAttributes();

        lp.width = (display.getWidth()); //设置宽度
        mTextMsgInputDialog.getWindow().setAttributes(lp);
        mTextMsgInputDialog.setCancelable(true);
        mTextMsgInputDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        mTextMsgInputDialog.show();
    }

    private void initInputDialog(){
        mTextMsgInputDialog = new TextMsgInputDialog(this, R.style.InputDialog);
        mTextMsgInputDialog.setmOnTextSendListener(new TextMsgInputDialog.OnTextSendListener() {
            @Override
            public void onTextSend(final String msg, boolean tanmuOpen) {
//                addMyText(getApp().getUserBean().userName, msg);
                LivePageActivity.this.msg.type = "chat";
                LivePageActivity.this.msg.subType = TextUtils.isEmpty(guard) ? "normal" : "guard";
                LivePageActivity.this.msg.data.message = msg;
                LivePageActivity.this.msg.user.level = getApp().getUserBean().level;
                LivePageActivity.this.msg.user.sex = getApp().getUserBean().sex;
                LivePageActivity.this.msg.user.guard = guard;
                liveRoom.sendRoomCustomMsg("wsMessage", GsonUtils.gsonString(LivePageActivity.this.msg), new LiveRoom.SendCustomMessageCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        showToastCenter("send message Error");
                    }

                    @Override
                    public void onSuccess() {
                        mPresentation.addMyText(getApp().getUserBean().nickname, msg);
                    }
                });
            }
        });
    }

    private void requestRoomType() {
        HttpService.getRoomTypes(this, new BaseCallback<RoomTypeResult>(this, this) {
            @Override
            public void onSuccess(RoomTypeResult roomTypeResult, int id) {
                types.addAll(roomTypeResult.detail);
                initPager();
            }
        });
    }

    private void initPager(){
        pagerAdapter = new LivePagerAdapter(getSupportFragmentManager(), types);
        pager.setAdapter(pagerAdapter);
        tab.setupWithViewPager(pager);
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComming(EventCenter center) {

    }

    /**
     * 用户返回
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0x66) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "权限授予失败，无法开启悬浮窗", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "权限授予成功！", Toast.LENGTH_SHORT).show();
                    initDifferentDisplay();
                }
            }

        }
    }
}
