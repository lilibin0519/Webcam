package com.wstv.webcam.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.libin.mylibrary.acp.Acp;
import com.libin.mylibrary.acp.AcpListener;
import com.libin.mylibrary.acp.AcpOptions;
import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.util.PreferenceUtil;
import com.libin.mylibrary.base.util.Trace;
import com.libin.mylibrary.http.utils.GsonUtils;
import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.wstv.webcam.AppConstant;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.dialog.ChatListDialog;
import com.wstv.webcam.dialog.ChatRoomDialog;
import com.wstv.webcam.dialog.GiftDialog;
import com.wstv.webcam.dialog.RedPacketDialog;
import com.wstv.webcam.entity.CustomMsg;
import com.wstv.webcam.entity.PrivateMessage;
import com.wstv.webcam.gift.NumAnim;
import com.wstv.webcam.holder.audience.AudienceViewListener;
import com.wstv.webcam.holder.chat.ChatViewListener;
import com.wstv.webcam.http.HttpService;
import com.wstv.webcam.http.callback.BaseCallback;
import com.wstv.webcam.http.model.EmptyResult;
import com.wstv.webcam.http.model.IsFollowResult;
import com.wstv.webcam.http.model.PerformerCard;
import com.wstv.webcam.http.model.PerformerCardResult;
import com.wstv.webcam.http.model.account.AccountResult;
import com.wstv.webcam.http.model.audience.Audience;
import com.wstv.webcam.http.model.gift.GiftAnim;
import com.wstv.webcam.http.model.gift.GiftBean;
import com.wstv.webcam.http.model.gift.GiftListResult;
import com.wstv.webcam.http.model.gift.GiftType;
import com.wstv.webcam.http.model.guard.GuardGood;
import com.wstv.webcam.http.model.guard.GuardResult;
import com.wstv.webcam.http.model.ranking.RankingResult;
import com.wstv.webcam.tencent.liveroom.IMLVBLiveRoomListener;
import com.wstv.webcam.tencent.liveroom.MLVBLiveRoom;
import com.wstv.webcam.tencent.liveroom.MLVBLiveRoomImpl;
import com.wstv.webcam.tencent.roomutil.commondef.AnchorInfo;
import com.wstv.webcam.tencent.roomutil.commondef.AudienceInfo;
import com.wstv.webcam.tencent.roomutil.commondef.PusherInfo;
import com.wstv.webcam.tencent.roomutil.misc.TextChatMsg;
import com.wstv.webcam.tencent.roomutil.misc.TextMsgInputDialog;
import com.wstv.webcam.util.NumberUtil;
import com.wstv.webcam.util.click.ClickProxy;
import com.wstv.webcam.widget.BarrageView;
import com.wstv.webcam.widget.GiftPopWindow;
import com.wstv.webcam.widget.GiftTablePop;
import com.wstv.webcam.widget.ManageUserPop;
import com.zhangyf.gift.RewardLayout;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import em.sang.com.allrecycleview.adapter.DefaultAdapter;
import em.sang.com.allrecycleview.cutline.RecycleViewDivider;
import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * <p>Description: </p>
 * CamDetailActivity
 *
 * @author lilibin
 * @createDate 2019/3/29 16:38
 */

@SuppressWarnings("unchecked")
public class CamDetailActivity extends BaseActivity {

    public static final String BUNDLE_KEY_FLV_URL = "bundle.key.flv.url";

    public static final String BUNDLE_KEY_LINK_URL = "bundle.key.link.url";

    public static final String BUNDLE_KEY_PERFORMER_ID = "key.performer.id";

    public static final String BUNDLE_KEY_PERFORMER = "key.performer.entity";

    public static final String BUNDLE_KEY_ROOM_ID = "key.room.id";

    public static final String BUNDLE_KEY_AUDIENCE = "key.performer.audience";

    public static final String BUNDLE_KEY_MANAGE = "key.performer.manage";

    public static final String BUNDLE_KEY_SHUT_UP = "key.performer.shut.up";

    @Bind(R.id.activity_cam_detail_video_view)
    TXCloudVideoView videoView;

    @Bind(R.id.activity_cam_detail_video_view_small)
    TXCloudVideoView videoViewSmall;

    @Bind(R.id.activity_cam_detail_loading)
    TextView loading;

//    @Bind(R.id.activity_cam_detail_input)
//    EditText input;

    @Bind(R.id.activity_cam_detail_performer_avatar)
    CircleImageView performerAvatar;

    @Bind(R.id.activity_cam_detail_performer_name)
    TextView performerName;

    @Bind(R.id.activity_cam_detail_performer_id)
    TextView performerIdTv;

    @Bind(R.id.activity_cam_detail_performer_follow)
    ImageView followButton;

    @Bind(R.id.activity_cam_detail_user_count)
    TextView userCount;

    @Bind(R.id.activity_cam_detail_gold)
    TextView goldCount;

    private int goldValue;

    @Bind(R.id.activity_cam_detail_with)
    TextView withCount;

    @Bind(R.id.activity_cam_detail_user_1)
    CircleImageView avatar1;

    @Bind(R.id.activity_cam_detail_user_2)
    CircleImageView avatar2;

    @Bind(R.id.activity_cam_detail_user_3)
    CircleImageView avatar3;

    private String flvUrl;

    private String linkPlayUrl;

    TXLivePlayer mLivePlayer;

    private String performerId;

    private MLVBLiveRoomImpl liveRoom;

    private String roomId;

    @Bind(R.id.activity_cam_detail_chat_list)
    RecyclerView chatList;

    DefaultAdapter<TextChatMsg> chatAdapter;

    List<TextChatMsg> chatRecord;

    private TextMsgInputDialog mTextMsgInputDialog;

    private List<PusherInfo> mPusherList = new ArrayList<>();

    @Bind(R.id.activity_cam_detail_interview_layout)
    LinearLayout interviewLayout;

    private boolean mIsBeingLinkMic;

    private boolean mIsBeingPK;

    @Bind(R.id.activity_cam_detail_anim_view)
    SVGAImageView animView;

    private boolean joinRequest;

    private AlertDialog dialog;

    @Bind(R.id.activity_cam_detail_gift_list)
    RewardLayout rewardLayout;

    private GiftDialog giftDialog;

    private GiftTablePop giftTable;

    private List<GiftType> giftData;

    CustomMsg msg;

    Map<String, GiftBean> giftMap;

    private String guard = "";

    public int giftSelectPosition = -1;

    @Bind(R.id.activity_cam_detail_barrage)
    BarrageView barrageView;

    private AnchorInfo pusher;

    private RedPacketDialog redPacketDialog;

    private ArrayList<Audience> audiences;

    private CircleImageView[]  avatars;

    private boolean isFans;

    private Dialog audienceDialog;

    private DefaultAdapter<Audience> audienceAdapter;

    private PerformerCard performerCard;

    private Dialog performerDialog;

    private View performerParent;

    private boolean isAt;

    private ChatRoomDialog chatRoomDialog;

    private ChatListDialog chatListDialog;

    @Bind(R.id.activity_cam_detail_message_point)
    View messagePoint;

    private boolean withPerformer;

    private List<GuardGood> guardList = new ArrayList<>();

    private Dialog guardBuy;

    private DefaultAdapter<GuardGood> guardAdapter;

    private int manageState = -1;

    private long shutUpTo;

    private GiftPopWindow giftPopWindow;

    public int typeIndex;

    private ManageUserPop manageUserPop;

    @Override
    protected void initViewAndData() {
        initInputDialog();
        initRecyclerView();
//        input.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_SEND) {
//                    //关闭软键盘
//                    hideKeyboard();
//                    //do something
//                    //doSend();
//                    return true;
//                }
//                return false;
//            }
//        });
//        initPlayer();
        liveRoom = MLVBLiveRoom.sharedInstance(this);
        liveRoom.setListener(createRoomListener());

        msg = new CustomMsg();
        msg.data = new CustomMsg.Data();
        msg.user = new CustomMsg.User();
        msg.user.level = getApp().getUserBean().level;
        msg.user.sex = getApp().getUserBean().sex;

//        flvUrl = flvUrl.replace(".flv", "out.flv");
        Trace.e("flvUrl : " + flvUrl);
        liveRoom.enterRoom(roomId, /*flvUrl, getApp().getUserBean().sex, getApp().getUserBean().level, */videoView, new IMLVBLiveRoomListener.EnterRoomCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                errorGoBack("进入直播间错误", errCode, errInfo);
            }

            @Override
            public void onSuccess() {
                loading.setVisibility(View.GONE);
                sendUserAdd();
                getAud();
            }
        }, getApp().getUserBean().sex, getApp().getUserBean().level);

        initGiftLayout();
        giftData = new ArrayList<>();
        requestGiftList();
        chatList.post(new Runnable() {
            @Override
            public void run() {
                addSystemText("我们提倡绿色直播，严禁涉政、涉恐、涉群体事件、涉黄等直播内容");
            }
        });
        Glide.with(this).load(pusher.userAvatar).into(performerAvatar);
        performerAvatar.setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPerformerInfo();
            }
        }));
        performerName.setText(pusher.userName);
        avatars = new CircleImageView[]{avatar1, avatar2, avatar3};
//        setAudiences();
        performerIdTv.setText("@" + roomId);
        userCount.setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAudienceList();
            }
        }));
        getAccount();
        requestWith();
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
        animView.setCallback(new SVGACallback() {
            @Override
            public void onPause() {

            }

            @Override
            public void onFinished() {
                animView.setVisibility(View.GONE);
            }

            @Override
            public void onRepeat() {

            }

            @Override
            public void onStep(int i, double v) {

            }
        });
    }

    private void getAud(){
        liveRoom.getAudienceList(new IMLVBLiveRoomListener.GetAudienceListCallback() {
            @Override
            public void onError(int errCode, String errInfo) {

            }

            @Override
            public void onSuccess(ArrayList<Audience> audienceList) {
                audiences.clear();
                audiences.addAll(audienceList);

//                if (audiences.size() < 4) {
                    setAudiences();
//                }
                if (null != audienceAdapter) {
                    audienceAdapter.notifyDataSetChanged();
                }
                userCount.setText(NumberUtil.format10_000(audiences.size()) + "人");
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

    private void checkWith() {
        HttpService.getIsFollow(this, roomId, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), new BaseCallback<IsFollowResult>(this, this) {
            @Override
            public void onSuccess(IsFollowResult isFollowResult, int id) {
                isFans = !TextUtils.isEmpty(isFollowResult.detail.ifAttention) && Boolean.parseBoolean(isFollowResult.detail.ifAttention);
                followButton.setImageResource(isFans ? R.drawable.icon_follow_cancel : R.drawable.icon_follow);
//                guard = !TextUtils.isEmpty(isFollowResult.detail.ifGuard) && Boolean.parseBoolean(isFollowResult.detail.ifGuard) ? "Y" : "";
                guard = isFollowResult.detail.guardType == null ? "" : isFollowResult.detail.guardType;

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

    private void setAudiences() {
        for (int i = 0; i < 3; i++) {
            if (null != audiences && i < audiences.size()) {
                Glide.with(this).load(audiences.get(i).getInfo().userAvatar).dontAnimate().placeholder(R.drawable.default_avatar).into(avatars[i]);
            } else {
                Glide.with(this).load(R.drawable.default_avatar).into(avatars[i]);
            }
        }
    }

    private void requestGiftList() {
//        giftDialog = new GiftDialog(CamDetailActivity.this, giftData, new GiftDialog.SendListener() {
//            @Override
//            public void onSendGift(String count, GiftBean bean) {
//                bean.setTheSendGiftSize(Integer.parseInt(count));
//                bean.setUserName(getApp().getUserBean().nickname);
//                bean.setTheUserId(getApp().getUserBean().userId);
//                bean.userAvatar = getApp().getUserBean().headPortrait;
//                buyGift(bean);
////                sendGift(count, bean);
////                        showGiftAnim(bean, msg.data.giftUrl);
//            }
//        }, getApp().getUserBean().coins);
        giftTable = new GiftTablePop(CamDetailActivity.this, giftData, new GiftTablePop.SendListener() {
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

    private void buyGift(final GiftBean bean) {
        HttpService.buyGift(this, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), roomId, bean.id, String.valueOf(bean.getTheSendGiftSize()),
                new BaseCallback<EmptyResult>(this, this) {
                    @Override
                    public void onSuccess(EmptyResult emptyResult, int id) {
                        sendGift(String.valueOf(bean.getTheSendGiftSize()), bean);
                    }

                    @Override
                    protected void onFailed(String errCode, String errMsg, EmptyResult emptyResult) {
                        super.onFailed(errCode, errMsg, emptyResult);
                        if ("-101".equals(errCode)) {
                            // 以后可跳转充值
                            toRecharge();
                        } else {
                            showToastCenter("礼物赠送失败，请重试");
                        }
                    }
                });
    }

    private void initRecyclerView() {
        chatRecord = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(manager);
//        chatList.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(this, android.R.color.transparent)));
        chatList.setAdapter(chatAdapter = new DefaultAdapter<>(this, chatRecord, R.layout.item_chat, new ChatViewListener()));
    }

    private void errorGoBack(String title, int errCode, String errInfo) {
        if (loading.getVisibility() != View.VISIBLE) {
            sendUserExit();
        }
        liveRoom.exitRoom(null);
        if (errCode == 10010) {
            loading.setText("主播暂未开播");
        }
        loading.setVisibility(View.VISIBLE);
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(errInfo + "[" + errCode + "]")
                .setCancelable(false)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
//                        finish();
                    }
                }).show();
    }

    private void initInputDialog(){
        mTextMsgInputDialog = new TextMsgInputDialog(this, R.style.InputDialog);
        mTextMsgInputDialog.setmOnTextSendListener(new TextMsgInputDialog.OnTextSendListener() {
            @Override
            public void onTextSend(final String msg, boolean tanmuOpen) {
//                addMyText(getApp().getUserBean().userName, msg);
                CamDetailActivity.this.msg.type = "chat";
                CamDetailActivity.this.msg.subType = TextUtils.isEmpty(guard) ? "normal" : "guard";
                CamDetailActivity.this.msg.data.message = msg;
                CamDetailActivity.this.msg.user.level = getApp().getUserBean().level;
                CamDetailActivity.this.msg.user.sex = getApp().getUserBean().sex;
                CamDetailActivity.this.msg.user.guard = guard;
                sendCustomMsg("wsMessage", CamDetailActivity.this.msg, new IMLVBLiveRoomListener.SendRoomCustomMsgCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        showToastCenter("send message Error");
                    }

                    @Override
                    public void onSuccess() {
                        isAt = false;
                        addMyText(getApp().getUserBean().nickname, msg);
                    }
                });
            }
        });
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

    private void sendMessage(final String message, final String type){
        liveRoom.sendRoomTextMsg(message, new IMLVBLiveRoomListener.SendRoomTextMsgCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                new AlertDialog.Builder(CamDetailActivity.this, R.style.RtmpRoomDialogTheme).setMessage(errInfo)
                        .setTitle("发送消息失败")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }

            @Override
            public void onSuccess() {
                addMyText(getApp().getUserBean().nickname, message);
            }
        });
    }

    private void addMyText(String userName, String message) {
        addRoomText(userName, message, getApp().getUserBean().level, guard);
    }

    private void addRoomText(String userName, String message, int level, String guard) {
        addMessageItem(userName, message, TextChatMsg.Aligment.LEFT, Color.parseColor("#ffffff"), guard, level, "text");
    }

    private void addSystemText(String message) {
        addMessageItem("", message, TextChatMsg.Aligment.LEFT, Color.parseColor("#3cfff3"), "", 0, "system");
    }

    private void addGiftText(String userName, String message, int level) {
        addMessageItem(userName, message, TextChatMsg.Aligment.LEFT, ContextCompat.getColor(this, R.color.orange), "", level, "gift");
    }

    private void addUserText(String userName, int level, String guard) {
        addMessageItem("", userName + " 进入直播间", TextChatMsg.Aligment.LEFT, Color.parseColor("#F7E796"), guard, level, "join");
        if (!TextUtils.isEmpty(guard)) {
            barrageView.addTextitem("欢迎 " + userName + " 加入直播间", /*msg.user.guard*/"");
        }
    }

    private void exitUserText(String userName, int level, String guard) {
        addMessageItem("", userName + " 退出直播间", TextChatMsg.Aligment.LEFT, Color.parseColor("#F7E796"), guard, level, "exit");
    }

    private void addBarrageText(String userName, int level, String guard) {
//        addMessageItem(userName, "欢迎 " + userName + " 加入直播间", TextChatMsg.Aligment.LEFT, ContextCompat.getColor(this, R.color.orange), guard, level);
    }

    private void addSelfText(String userName, int level) {
        addUserText(userName, level, guard);
    }

    private void addMessageItem(final String userName, final String message, final TextChatMsg.Aligment aligment, final int color, final String type, final int level, final String messageType){
        Trace.e("guard : " + guard);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm", Locale.SIMPLIFIED_CHINESE);
                chatRecord.add(new TextChatMsg(userName, TIME_FORMAT.format(new Date()), message, aligment, color, type, level, messageType));
                while (chatRecord.size() > 100) {
                    chatRecord.remove(chatRecord.size() - 1);
                }
                chatAdapter.notifyDataSetChanged();
                chatList.post(new Runnable() {
                    @Override
                    public void run() {
                        if (chatAdapter.getItemCount() > 0) {
                            chatList.scrollToPosition(chatAdapter.getItemCount() - 1);
                        }
                    }
                });
            }
        });
    }

    private IMLVBLiveRoomListener createRoomListener() {
        return new IMLVBLiveRoomListener() {
            @Override
            public void onError(int errCode, String errMsg, Bundle extraInfo) {
                Trace.e("CamDetail", "errCode : " + errCode);
                if (errCode == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || errCode == TXLiveConstants.PLAY_EVT_PLAY_END) {
                    // 网络断连，且经多次重连亦不能恢复，更多重试请自行重启播放 || 直播正常结束
//                    roomListenerCallback.onDebugLog("[AnswerRoom] 拉流失败：网络断开");
//                    roomListenerCallback.onError(-1, "网络断开，拉流失败");
                    loading.setText("主播已下播");
                    loading.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onWarning(int warningCode, String warningMsg, Bundle extraInfo) {
                Trace.e("CamDetail", "warningCode : " + warningCode);if (warningCode == TXLiveConstants.PLAY_EVT_GET_MESSAGE) {
                    // 自定义消息
                    String msg = null;
                    try {
                        msg = new String(extraInfo.getByteArray(TXLiveConstants.EVT_GET_MSG), "UTF-8");
//                        roomListenerCallback.onRecvAnswerMsg(msg);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else if (warningCode == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                    loading.setVisibility(View.GONE);
                }
            }

            @Override
            public void onDebugLog(String log) {
                Trace.e(log);
            }

            @Override
            public void onRoomDestroy(String roomID) {
                loading.setText("房间关闭了");
                loading.setVisibility(View.VISIBLE);
                showToastCenter("房间关闭了");
            }

            @Override
            public void onAnchorEnter(AnchorInfo anchorInfo) {

            }

            @Override
            public void onAnchorExit(AnchorInfo anchorInfo) {

            }

            @Override
            public void onAudienceEnter(AudienceInfo audienceInfo) {

            }

            @Override
            public void onAudienceExit(AudienceInfo audienceInfo) {

            }

            @Override
            public void onRequestJoinAnchor(AnchorInfo anchorInfo, String reason) {

            }

            @Override
            public void onKickoutJoinAnchor() {
                liveRoom.kickoutJoinAnchor(PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID));
            }

            @Override
            public void onRequestRoomPK(AnchorInfo anchorInfo) {

            }

            @Override
            public void onQuitRoomPK(AnchorInfo anchorInfo) {

            }

            @Override
            public void onRecvRoomTextMsg(String roomID, String userID, String userName, String userAvatar, String message) {
                Trace.e("custom receive : " + message);
                CustomMsg msg = GsonUtils.gsonToBean(message, CustomMsg.class);
                addRoomText(userName, msg.data.message, msg.user.level, /*msg.user.guard*/"");
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
                    showGiftAnim(myBean, msg.data.giftUrl);
                    goldValue += Integer.parseInt(msg.data.giftAmount);
                    goldCount.setText(NumberUtil.format10_000(goldValue));
                } else if ("chat".equals(msg.type)) {
                    if ("join".equals(msg.subType)) {
                        addUser(userID, userAvatar, msg.user.sex, userName, msg.user.level);
                        addUserText(userName, msg.user.level, /*msg.user.guard*/"");
                    } else if ("system".equals(msg.subType)) {
                        addSystemText(msg.data.message);
                    } else if ("normal".equals(msg.subType) || "guard".equals(msg.subType)) {
                        addRoomText(userName, msg.data.message, msg.user.level, /*msg.user.guard*/"");
                    } else if ("guard".equals(msg.subType)) {
                        addRoomText(userName, msg.data.message, msg.user.level, /*msg.user.guard*/"");
                    } else if ("exit".equals(msg.subType)) {
                        removeAudience(userID);
                        exitUserText(userName, msg.user.level, /*msg.user.guard*/"");
                    }
                } else if ("barrage".equals(msg.type)) {
                    barrageView.addTextitem(msg.data.message, /*msg.user.guard*/"");
                } else if ("redPacket".equals(msg.type)) {
                    if ("send".equals(msg.subType)) {
                        showRedPacket(msg, userAvatar, userName);
                    } else if ("receive".equals(msg.subType)) {
                    }
                } else if ("manage".equals(msg.type)) {
                    if ("shutup".equals(msg.subType)) {
                        shutUpTo = System.currentTimeMillis() + msg.data.time;
                    } else if ("kickout".equals(msg.subType) || "black".equals(msg.subType)) {
                        onLeftClick(null);
                    }
                }
            }
        };
    }

//    private IMLVBLiveRoomListener createRoomListener() {
//        return new IMLVBLiveRoomListener() {
//            @Override
//            public void onGetPusherList(List<PusherInfo> pusherList) {
//            }
//
//            @Override
//            public void onPusherJoin(PusherInfo pusherInfo) {
////                liveRoom.addRemoteView();
//            }
//
//            @Override
//            public void onPusherQuit(PusherInfo pusherInfo) {
//
//            }
//
//            @Override
//            public void onRecvJoinPusherRequest(String userID, String userName, String userAvatar) {
//
//            }
//
//            @Override
//            public void onKickOut() {
//
//            }
//
//            @Override
//            public void onRecvPKRequest(String userID, String userName, String userAvatar, String streamUrl) {
//
//            }
//
//            @Override
//            public void onRecvPKFinishRequest(String userID) {
//
//            }
//
//            @Override
//            public void onRecvRoomTextMsg(String roomID, String userID, String userName, String userAvatar, String message) {
//                Trace.e("custom receive : " + message);
//                CustomMsg msg = GsonUtils.gsonToBean(message, CustomMsg.class);
//                addRoomText(userName, msg.data.message, msg.user.level, /*msg.user.guard*/"");
//            }
//
//            @Override
//            public void onRecvRoomCustomMsg(String roomID, String userID, String userName, String userAvatar, String cmd, String message) {
////                if ("wsGift".equals(cmd)) {
////                    GiftEntity giftEntity = GsonUtils.gsonToBean(message, GiftEntity.class);
////                    if ("100".equals(giftEntity.no)) {
////                        showGiftAnim(id);
////                    }
////                }
//                Trace.e("userAvatar : " + userAvatar);
//                CustomMsg msg = GsonUtils.gsonToBean(message, CustomMsg.class);
//                if ("gift".equals(msg.type)) {
//                    GiftBean myBean = getGiftById(msg.data.giftId);
//                    if (null != myBean) {
//                        myBean.setTheSendGiftSize(Integer.parseInt(msg.data.giftCount));
//                        myBean.setTheUserId(userID);
//                        myBean.setUserName(userName);
//                        myBean.userAvatar = userAvatar;
//                    }
//                    showGiftAnim(myBean, msg.data.giftUrl);
//                    goldValue += Integer.parseInt(msg.data.giftAmount);
//                    goldCount.setText(NumberUtil.format10_000(goldValue));
//                } else if ("chat".equals(msg.type)) {
//                    if ("join".equals(msg.subType)) {
//                        addUser(userID, userAvatar, msg.user.sex, userName, msg.user.level);
//                        addUserText(userName, msg.user.level, /*msg.user.guard*/"");
//                    } else if ("system".equals(msg.subType)) {
//                        addSystemText(msg.data.message);
//                    } else if ("normal".equals(msg.subType) || "guard".equals(msg.subType)) {
//                        addRoomText(userName, msg.data.message, msg.user.level, /*msg.user.guard*/"");
//                    } else if ("guard".equals(msg.subType)) {
//                        addRoomText(userName, msg.data.message, msg.user.level, /*msg.user.guard*/"");
//                    } else if ("exit".equals(msg.subType)) {
//                        removeAudience(userID);
//                        exitUserText(userName, msg.user.level, /*msg.user.guard*/"");
//                    }
//                } else if ("barrage".equals(msg.type)) {
//                    barrageView.addTextitem(msg.data.message, /*msg.user.guard*/"");
//                } else if ("redPacket".equals(msg.type)) {
//                    if ("send".equals(msg.subType)) {
//                        showRedPacket(msg, userAvatar, userName);
//                    } else if ("receive".equals(msg.subType)) {
//                    }
//                } else if ("manage".equals(msg.type)) {
//                    if ("shutup".equals(msg.subType)) {
//                        shutUpTo = System.currentTimeMillis() + msg.data.time;
//                    } else if ("kickout".equals(msg.subType) || "black".equals(msg.subType)) {
//                        onLeftClick(null);
//                    }
//                }
//            }
//
//            @Override
//            public void onRoomClosed(String roomID) {
//                loading.setText("房间关闭了");
//                loading.setVisibility(View.VISIBLE);
//                showToastCenter("房间关闭了");
//            }
//
//            @Override
//            public void onDebugLog(String log) {
//                Trace.e(log);
//            }
//
//            @Override
//            public void onError(int errorCode, String errorMessage) {
//
//            }
//
//            @Override
//            public void onLivePlayEvent(int event, Bundle params) {
//                Trace.e("CamDetail", "event : " + event);
//                if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == TXLiveConstants.PLAY_EVT_PLAY_END) {
//                    // 网络断连，且经多次重连亦不能恢复，更多重试请自行重启播放 || 直播正常结束
////                    roomListenerCallback.onDebugLog("[AnswerRoom] 拉流失败：网络断开");
////                    roomListenerCallback.onError(-1, "网络断开，拉流失败");
//
//                    loading.setText("主播已下播");
//                    loading.setVisibility(View.VISIBLE);
//                } else if (event == TXLiveConstants.PLAY_EVT_GET_MESSAGE) {
//                    // 自定义消息
//                    String msg = null;
//                    try {
//                        msg = new String(params.getByteArray(TXLiveConstants.EVT_GET_MSG), "UTF-8");
////                        roomListenerCallback.onRecvAnswerMsg(msg);
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//                } else if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
//                    loading.setVisibility(View.GONE);
//                }
//            }
//        };
//    }

    private void addUser(String userID, String userAvatar, String gender, String userName, int level) {
        Audience audience = new Audience();
        audience.userID = userID;
        audience.info = new Audience.UserInfo();
        audience.info.userAvatar = userAvatar;
        audience.info.gender = gender;
        audience.info.userName = userName;
        audience.info.level = level;
        if (!audiences.contains(audience)) {
            audiences.add(audience);
            if (audiences.size() < 4) {
                setAudiences();
            }
            if (null != audienceAdapter) {
                audienceAdapter.notifyDataSetChanged();
            }
            userCount.setText(NumberUtil.format10_000(audiences.size()) + "人");
        }
    }

    private synchronized void removeAudience(String userID) {
        int i;
        for (i = 0; i < audiences.size(); i++) {
            if (userID.equals(audiences.get(i).userID)) {
                break;
            }
        }
        if (audiences.size() == 0) {
            return;
        }
        audiences.remove(i < audiences.size() ? i : audiences.size() - 1);
        if (i < 3) {
            setAudiences();
        }
        if (null != audienceAdapter) {
            audienceAdapter.notifyDataSetChanged();
        }
        userCount.setText(NumberUtil.format10_000(audiences.size()) + "人");
    }

    private void showRedPacket(CustomMsg msg, String avatar, String name) {
        if (null == redPacketDialog) {
            redPacketDialog = new RedPacketDialog(this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }, msg.data.id, avatar, name);
        } else {
            redPacketDialog.setPacketId(msg.data.id);
            redPacketDialog.setAvatar(avatar);
            redPacketDialog.setNickname(name);
        }
        redPacketDialog.show();
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

    @Override
    public void onLeftClick(View v) {
//        liveRoom.removeCurrentRoom();
        if (loading.getVisibility() != View.VISIBLE) {
            sendUserExit();
        }
        liveRoom.exitRoom(new IMLVBLiveRoomListener.ExitRoomCallback() {
            @Override
            public void onSuccess() {
                Trace.e("exitRoom success");
            }

            @Override
            public void onError(int errCode, String e) {
                Trace.e("exitRoom failed, errorCode = " + errCode + " errMessage = " + e);
            }
        });
        super.onLeftClick(v);
    }

    @OnClick({R.id.activity_cam_detail_close, R.id.activity_cam_detail_talk, R.id.activity_cam_detail_gift,
            R.id.activity_cam_detail_message, R.id.activity_cam_detail_share, R.id.activity_cam_detail_interview_layout,
            R.id.activity_cam_detail_performer_follow, R.id.activity_cam_detail_gold_layout, R.id.activity_cam_detail_with_layout, R.id.activity_cam_detail_user_layout})
    public void onClick(View view){
        /*if (loading.getVisibility() == View.VISIBLE) {
            if (view.getId() == R.id.activity_cam_detail_close) {
                onLeftClick(view);
            }
            return;
        }*/
        switch (view.getId()) {
            case R.id.activity_cam_detail_close:
                onLeftClick(view);
                break;
            case R.id.activity_cam_detail_talk:
//                input.setVisibility(View.VISIBLE);
//                input.requestFocus();
                showInputMsgDialog();
//                liveRoom.sendPrivateMessage("402881f86a09dfd0016a09f345c80000", "");
                break;
            case R.id.activity_cam_detail_gift:
//                sendGift();
//                giftDialog.show();
                giftTable.show(ButterKnife.findById(this, R.id.activity_cam_detail_parent));
                break;
            case R.id.activity_cam_detail_message:
                // 私信
                messagePoint.setVisibility(View.GONE);
                showPrivateListDialog();
                break;
            case R.id.activity_cam_detail_share:
                break;
            case R.id.activity_cam_detail_interview_layout:
                // 连麦
                toLink();
                break;
            case R.id.activity_cam_detail_performer_follow:
                // 关注主播
                if (isFans) {
                    cancelFollow();
                } else {
                    requestFollow();
                }
                break;
            case R.id.activity_cam_detail_gold_layout:
                Bundle bundleGift = new Bundle();
                bundleGift.putString("showId", roomId);
                readyGo(GiftRankingActivity.class, bundleGift);
                break;
            case R.id.activity_cam_detail_with_layout:
                Bundle bundleWith = new Bundle();
                bundleWith.putString("showId", roomId);
                readyGo(WithRankingActivity.class, bundleWith);
                break;
            case R.id.activity_cam_detail_user_layout:
                showAudienceList();
                break;
        }
    }

    private void toLink() {

        Acp.getInstance(this).
                request(new AcpOptions.Builder()
                                .setPermissions(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO)
                                .build(),
                        new AcpListener() {
                            @Override
                            public void onGranted() {
                                if (mIsBeingLinkMic) {
                                    stopLinkMic();
                                } else {
                                    if (null != liveRoom.getCurrentRoomPushers() && liveRoom.getCurrentRoomPushers().size() < 2) {
                                        requestJoinPusher();
                                    } else {
                                        showToastCenter("主播正在连麦中");
                                    }
                                }
                            }

                            @Override
                            public void onDenied(List<String> permissions) {
//                                showToastCenter(permissions.toString() + "权限拒绝");
                            }
                        });
    }

    public void showPrivateChatDialog(String userId, String userName) {
        if (null == chatRoomDialog) {
            chatRoomDialog = new ChatRoomDialog(this, userId, userName, pusher.userAvatar);
        }
        if (!chatRoomDialog.isShowing()) {
            chatRoomDialog.show();
        }
    }

    private void showPrivateListDialog() {
        if (null == chatListDialog) {
            chatListDialog = new ChatListDialog(this);
        }
        if (!chatListDialog.isShowing()) {
            chatListDialog.show();
        }
    }

    private void sendUserAdd(){
        msg.type = "chat";
        msg.subType = "join";
        msg.user.level = getApp().getUserBean().level;
        msg.user.sex = getApp().getUserBean().sex;
        msg.user.guard = guard;
        sendCustomMsg("wsUser", msg, new IMLVBLiveRoomListener.SendRoomCustomMsgCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                showToastCenter("send join Error");
            }

            @Override
            public void onSuccess() {
                showToastCenter("send join success");
//                addUser(getApp().getUserBean().userId, getApp().getUserBean().headPortrait, getApp().getUserBean().sex, getApp().getUserBean().nickname, getApp().getUserBean().level);
//                userCount.setText(NumberUtil.format10_000(audiences.size()) + "人");
                addSelfText(getApp().getUserBean().nickname, getApp().getUserBean().level);
            }
        });
    }

    private void sendUserExit(){
        msg.type = "chat";
        msg.subType = "exit";
        msg.user.level = getApp().getUserBean().level;
        msg.user.sex = getApp().getUserBean().sex;
        msg.user.guard = guard;
        sendCustomMsg("wsUser", msg, new IMLVBLiveRoomListener.SendRoomCustomMsgCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                showToastCenter("send exit Error");
            }

            @Override
            public void onSuccess() {
            }
        });
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
        Trace.e("gift send Custom Msg");
        sendCustomMsg("wsGift", /*"{\"type\":\"gift\", \"no\":\"100\"}"*/msg, new IMLVBLiveRoomListener.SendRoomCustomMsgCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                Trace.e("gift send Custom Msg 1");
                showToastCenter("send gift Error");
            }

            @Override
            public void onSuccess() {
                Trace.e("gift send Custom Msg 2");
                showToastCenter("send gift success");
                try {
                    showGiftAnim(bean, msg.data.giftUrl);

                    goldValue += Integer.parseInt(msg.data.giftAmount);
                    goldCount.setText(NumberUtil.format10_000(goldValue));
                } catch (Exception e) {
                    new AlertDialog.Builder(CamDetailActivity.this).setMessage(getExceptionToString(e)).show();
//                    e.printStackTrace();
                }
            }
        });
    }

    private void showGiftAnim(GiftBean giftBean, String giftUrl) {
        if (null == giftBean) {
            return;
        }
        rewardLayout.put(/*bean*/giftBean);

        if (animView.isAnimating() || TextUtils.isEmpty(giftUrl)) {
            return;
        }
        SVGAParser parser = new SVGAParser(this);
//        parser.decodeFromAssets("posche2.svga", new SVGAParser.ParseCompletion() {
//            @Override
//            public void onError() {
//                Trace.e("svga error");
//            }
//
//            @Override
//            public void onComplete(SVGAVideoEntity svgaVideoEntity) {
//                Trace.e("svga onComplete");
//                if (null == animView) {
//                    Trace.e("svga null == animView");
//                    return;
//                }
//                animView.setVisibility(View.VISIBLE);
//                SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
//                animView.setImageDrawable(drawable);
//                animView.startAnimation();
//            }
//        });
        // start by easy
        try {
            parser.decodeFromURL(new URL(giftUrl), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                    animView.setVisibility(View.VISIBLE);
                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                    animView.setImageDrawable(drawable);
                    animView.startAnimation();
                }

                @Override
                public void onError() {
                    Trace.e("svga error");
                    new AlertDialog.Builder(CamDetailActivity.this).setMessage("svga error").show();
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // end by easy
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

    private void requestJoinPusher(){
        joinRequest = true;
        interviewLayout.setEnabled(false);
        if (null == dialog) {
            dialog = new AlertDialog.Builder(this).setMessage("等待主播应答中...")
                    .setCancelable(false).show();
        } else {
            dialog.show();
        }
        liveRoom.requestJoinAnchor(performerId/*, 10*/, new IMLVBLiveRoomListener.RequestJoinAnchorCallback() {
            @Override
            public void onAccept() {
                showToastCenter("主播接受了您的连麦请求，开始连麦");
                liveRoom.startLocalPreview(true, videoViewSmall);
                liveRoom.setCameraMuteImage(BitmapFactory.decodeResource(getResources(), R.drawable.pause_publish));
                liveRoom.setBeautyStyle(AppConstant.BEAUTY_STYLE, AppConstant.BEAUTY_LEVEL, AppConstant.WHITENING_LEVEL, AppConstant.RUDDY_LEVEL);
                liveRoom.joinAnchor(/*linkPlayUrl, */new IMLVBLiveRoomListener.JoinAnchorCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        stopLinkMic();
                        interviewLayout.setEnabled(true);
                        showToastCenter("连麦失败：" + errInfo);
                        dialog.dismiss();
                    }

                    @Override
                    public void onSuccess() {
                        interviewLayout.setEnabled(true);
//                        mBtnLinkMic.setBackgroundResource(R.drawable.linkmic_stop);
                        mIsBeingLinkMic = true;
                        showToastCenter("连麦成功");
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void onReject(String reason) {
                interviewLayout.setEnabled(true);
                showToastCenter(/*"主播拒绝你的连麦请求"*/reason);
                dialog.dismiss();
            }

            @Override
            public void onTimeOut() {
                interviewLayout.setEnabled(true);
                showToastCenter("连麦请求超时，主播没有做出回应");
                dialog.dismiss();
            }

            @Override
            public void onError(int code, String errInfo) {
                interviewLayout.setEnabled(true);
                showToastCenter("连麦请求失败");
                dialog.dismiss();
            }
        });
    }

    private void stopLinkMic() {
        mIsBeingLinkMic = false;

        interviewLayout.setEnabled(true);
//        interviewLayout.setBackgroundResource(R.drawable.linkmic_start);

        videoViewSmall.setVisibility(View.GONE);

        liveRoom.stopLocalPreview();
        liveRoom.quitJoinAnchor(/*flvUrl, */new IMLVBLiveRoomListener.QuitAnchorCallback() {
            @Override
            public void onError(int errCode, String errInfo) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        liveRoom.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        liveRoom.resume();
    }

    @Override
    protected void hideSoftKeyBoard(IBinder token) {
        super.hideSoftKeyBoard(token);
//        input.setVisibility(View.GONE);
    }

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_cam_detail;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {
        performerId = extras.getString(BUNDLE_KEY_PERFORMER_ID);
        flvUrl = extras.getString(BUNDLE_KEY_FLV_URL);
        roomId = extras.getString(BUNDLE_KEY_ROOM_ID);
        linkPlayUrl = extras.getString(BUNDLE_KEY_LINK_URL);
        pusher = (AnchorInfo) ((ArrayList) extras.getSerializable(BUNDLE_KEY_PERFORMER)).get(0);
//        audiences = (ArrayList<Audience>) extras.getSerializable(BUNDLE_KEY_AUDIENCE);
        if (null == audiences) {
            audiences = new ArrayList<>();
        }
        manageState = extras.getInt(BUNDLE_KEY_MANAGE, -1);
        shutUpTo = extras.getLong(BUNDLE_KEY_SHUT_UP, 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        mLivePlayer.stopPlay(true); // true 代表清除最后一帧画面
        if (null != videoView) {
            videoView.onDestroy();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        liveRoom.stopLocalPreview();
    }

    private void initGiftLayout(){
        rewardLayout.setGiftAdapter(new RewardLayout.GiftAdapter<GiftBean>() {
            @Override
            public View onInit(View view, GiftBean bean) {
                ImageView avatar = (ImageView) view.findViewById(R.id.riv_gift_my_avatar);
                ImageView giftImage = (ImageView) view.findViewById(R.id.iv_gift_img);
                final TextView giftNum = (TextView) view.findViewById(R.id.tv_gift_amount);
                TextView userName = (TextView) view.findViewById(R.id.tv_user_name);
                TextView giftName = (TextView) view.findViewById(R.id.tv_gift_name);

                // 初始化数据
                Glide.with(CamDetailActivity.this).load(bean.userAvatar).dontAnimate().placeholder(R.drawable.default_avatar).into(avatar);
                giftNum.setText("x" + bean.getTheSendGiftSize());
                bean.setTheGiftCount(bean.getTheSendGiftSize());
//                giftImage.setImageResource(bean.getGiftImg());
                Glide.with(CamDetailActivity.this).load(bean.getGiftImg()).into(giftImage);
                userName.setText(bean.getUserName());
                giftName.setText("送出 " + bean.getGiftName());
                return view;
            }

            @Override
            public View onUpdate(View view, GiftBean bean) {
                ImageView avatar = (ImageView) view.findViewById(R.id.riv_gift_my_avatar);
                ImageView giftImage = (ImageView) view.findViewById(R.id.iv_gift_img);
                TextView giftNum = (TextView) view.findViewById(R.id.tv_gift_amount);

                int showNum = (Integer) bean.getTheGiftCount() + bean.getTheSendGiftSize();
                // 刷新已存在的giftview界面数据
                giftNum.setText("x" + showNum);
                Glide.with(CamDetailActivity.this).load(bean.userAvatar).dontAnimate().placeholder(R.drawable.default_avatar).into(avatar);
//                giftImage.setImageResource(bean.getGiftImg());
                Glide.with(CamDetailActivity.this).load(bean.getGiftImg()).into(giftImage);
                // 数字刷新动画
                new NumAnim().start(giftNum);
                // 更新tag
                bean.setTheGiftCount(showNum);
                bean.setTheLatestRefreshTime(System.currentTimeMillis());
                view.setTag(bean);
                return view;
            }

            @Override
            public void onKickEnd(GiftBean bean) {
                Log.e("zyfff", "onKickEnd:" + bean.getTheGiftId() + "," + bean.getGiftName() + "," + bean.getUserName() + "," + bean.getTheGiftCount());
            }

            @Override
            public void onComboEnd(GiftBean bean) {
//                Log.e("zyfff","onComboEnd:"+bean.getTheGiftId()+","+bean.getGiftName()+","+bean.getUserName()+","+bean.getTheGiftCount());
            }

            @Override
            public void addAnim(final View view) {
                final TextView textView = (TextView) view.findViewById(R.id.tv_gift_amount);
                ImageView img = (ImageView) view.findViewById(R.id.iv_gift_img);
                // 整个giftview动画
                Animation giftInAnim = (TranslateAnimation) AnimationUtils.loadAnimation(CamDetailActivity.this, R.anim.gift_in);
                // 礼物图像动画
                Animation imgInAnim = (TranslateAnimation) AnimationUtils.loadAnimation(CamDetailActivity.this, R.anim.gift_in);
                // 首次连击动画
                final NumAnim comboAnim = new NumAnim();
                imgInAnim.setStartTime(500);
                imgInAnim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        textView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        textView.setVisibility(View.VISIBLE);
                        comboAnim.start(textView);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                view.startAnimation(giftInAnim);
                img.startAnimation(imgInAnim);
            }

            @Override
            public AnimationSet outAnim() {
                return (AnimationSet) AnimationUtils.loadAnimation(CamDetailActivity.this, R.anim.gift_out);
            }

            @Override
            public boolean checkUnique(GiftBean o, GiftBean t) {
                return null != o && !TextUtils.isEmpty(o.getTheGiftId())
                        && !TextUtils.isEmpty(o.getTheUserId())
                        && null != t && !TextUtils.isEmpty(t.getTheGiftId())
                        && !TextUtils.isEmpty(t.getTheUserId())
                        && o.getTheGiftId().equals(t.getTheGiftId()) && o.getTheUserId().equals(t.getTheUserId());
            }


            @Override
            public GiftBean generateBean(GiftBean bean) {
                try {
                    return (GiftBean) bean.clone();
                } catch (CloneNotSupportedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    private void cancelFollow() {
        HttpService.cancelFollow(this, performerId, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID),
                new BaseCallback<EmptyResult>(this, this) {
                    @Override
                    public void onSuccess(EmptyResult result, int id) {
                        isFans = false;
                        followButton.setImageResource(R.drawable.icon_follow);
                        followButton.setImageResource(R.drawable.icon_follow);
                        if (null != performerParent) {
                            ((TextView) performerParent.findViewById(R.id.dialog_performer_info_follow_btn)).setText("+ 关注");
                        }
                    }
                });
    }

    private void requestFollow() {
        HttpService.follow(this, performerId, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID),
                new BaseCallback<EmptyResult>(this, this) {
                    @Override
                    public void onSuccess(EmptyResult result, int id) {
                        isFans = true;
                        followButton.setImageResource(R.drawable.icon_follow_cancel);
                        followButton.setImageResource(R.drawable.icon_follow_cancel);
                        if (null != performerParent) {
                            ((TextView) performerParent.findViewById(R.id.dialog_performer_info_follow_btn)).setText("取关");
                        }
                    }
                });
    }

    @Override
    public void setContentView(int layoutResID) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.setContentView(layoutResID);
    }

    private void requestWith(){
        HttpService.getWithRanking(this, "1", "1", roomId, new BaseCallback<RankingResult>(this, this) {
            @Override
            public void onSuccess(RankingResult rankingResult, int id) {
                withCount.setText(rankingResult.errmsg);
            }
        });
    }

    private void showAudienceList(){
        if (null == audienceDialog) {
            audienceDialog = new Dialog(this);
            View parent = LayoutInflater.from(this).inflate(R.layout.dialog_audience, null);
            audienceDialog.setContentView(parent);
            RecyclerView content = parent.findViewById(R.id.dialog_audience_list);
            content.setLayoutManager(new LinearLayoutManager(this));
            content.setAdapter(audienceAdapter = new DefaultAdapter<>(this, audiences, R.layout.item_watch_record, new AudienceViewListener(new OnToolsItemClickListener<Audience>() {
                @Override
                public void onItemClick(int position, Audience item) {
                    if (null == manageUserPop) {
                        manageUserPop = new ManageUserPop(CamDetailActivity.this, item.userID, roomId);
                    }
                    manageUserPop.showAtLocation(ButterKnife.findById(CamDetailActivity.this, R.id.activity_cam_detail_parent), Gravity.BOTTOM, 0, 0);
                }
            })));
        }
        requestAudiences();
    }

    private void requestAudiences() {
        audienceDialog.show();
//        liveRoom.getAudienceList(roomId, new LiveRoom.GetAudienceListCallback() {
//            @Override
//            public void onError(int errCode, String errInfo) {
//
//            }
//
//            @Override
//            public void onSuccess(ArrayList<Audience> audienceList) {
//                audiences.clear();
//                audiences.addAll(audienceList);
//                audienceAdapter.notifyDataSetChanged();
//                audienceDialog.show();
//            }
//        });
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
                                    new AlertDialog.Builder(CamDetailActivity.this).setTitle("提示")
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
//            dialogView.findViewById(R.id.dialog_guard_buy_cancel).setOnClickListener(new ClickProxy(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    guardBuy.dismiss();
//                }
//            }));
//            dialogView.findViewById(R.id.dialog_guard_buy_confirm).setOnClickListener(new ClickProxy(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    guardBuy.dismiss();
//                }
//            }));
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

    private void toRecharge(){
        new AlertDialog.Builder(CamDetailActivity.this).setTitle("提示")
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

    private void requestPerformerInfo(){
        createPerformerCard();
        HttpService.getPerformerCard(this, roomId, PreferenceUtil.readString(AppConstant.KEY_PARAM_USER_ID), new BaseCallback<PerformerCardResult>(this, this) {
            @Override
            public void onSuccess(PerformerCardResult cardResult, int id) {
                performerCard = cardResult.detail;
                performerDialog.show();
                setPerformerData();
            }
        });
    }

    private void createPerformerCard() {
        if (null == performerDialog) {
            performerDialog = new Dialog(this, R.style.BottomDialog);
            performerParent = LayoutInflater.from(this).inflate(R.layout.dialog_performer_info, null);
            performerDialog.setContentView(performerParent);
            performerParent.findViewById(R.id.dialog_performer_info_follow_btn).setOnClickListener(new ClickProxy(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFans) {
                        cancelFollow();
                    } else {
                        requestFollow();
                    }
                }
            }));
            performerParent.findViewById(R.id.dialog_performer_info_at_btn).setOnClickListener(new ClickProxy(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isAt = true;
                    performerDialog.dismiss();
                    showInputMsgDialog();
                }
            }));
            performerParent.findViewById(R.id.dialog_performer_info_private_btn).setOnClickListener(new ClickProxy(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    withPerformer = false;
                    messagePoint.setVisibility(View.GONE);
                    performerDialog.dismiss();
                    showPrivateChatDialog(performerId, pusher.userName);
                }
            }));
            performerParent.findViewById(R.id.dialog_performer_info_info_btn).setOnClickListener(new ClickProxy(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("showId", roomId);
                    bundle.putString("performerId", performerId);
                    bundle.putBoolean("fromCam", true);
                    readyGo(PerformerActivity.class, bundle);
                }
            }));
            ((TextView) performerParent.findViewById(R.id.dialog_performer_info_name)).setText(pusher.userName);
            ((TextView) performerParent.findViewById(R.id.dialog_performer_info_show_id)).setText(roomId);
            Glide.with(this).load(pusher.userAvatar).dontAnimate().placeholder(R.drawable.default_avatar)
                    .error(R.drawable.default_avatar).into(((ImageView) performerParent.findViewById(R.id.dialog_performer_info_avatar)));
        }
    }

    private void setPerformerData() {
        Glide.with(this).load(performerCard.leftHeadPortrait).dontAnimate().placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar).into(((ImageView) performerParent.findViewById(R.id.dialog_performer_info_avatar_1)));
        Glide.with(this).load(performerCard.rightHeadPortrait).dontAnimate().placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar).into(((ImageView) performerParent.findViewById(R.id.dialog_performer_info_avatar_2)));
        ((ImageView) performerParent.findViewById(R.id.dialog_performer_info_level)).setImageResource(AppConstant.levelArr[(Integer.parseInt(performerCard.level) - 1) < AppConstant.levelArr.length ? Integer.parseInt(performerCard.level) - 1 : 0]);
        ((ImageView) performerParent.findViewById(R.id.dialog_performer_info_gender)).setImageResource("0".equals(performerCard.sex) ? R.drawable.icon_gender_female : R.drawable.icon_gender_male);

        ((TextView) performerParent.findViewById(R.id.dialog_performer_info_follow_btn)).setText(isFans ? "取关" : "+ 关注");
        ((TextView) performerParent.findViewById(R.id.dialog_performer_info_sign)).setText(performerCard.signature);
        ((TextView) performerParent.findViewById(R.id.dialog_performer_info_follow)).setText(NumberUtil.format10_000(Integer.parseInt(performerCard.attentionCnt)));
        ((TextView) performerParent.findViewById(R.id.dialog_performer_info_fan)).setText(NumberUtil.format10_000(Integer.parseInt(performerCard.fansCnt)));
        ((TextView) performerParent.findViewById(R.id.dialog_performer_info_send)).setText(NumberUtil.format10_000(Integer.parseInt(performerCard.giveCoinsSum)));
        ((TextView) performerParent.findViewById(R.id.dialog_performer_info_receive)).setText(NumberUtil.format10_000(Integer.parseInt(performerCard.receiveCoinsSum)));
    }

    private void getAccount(){
        HttpService.getAccount(this, performerId, new BaseCallback<AccountResult>(this, this) {
            @Override
            public void onSuccess(AccountResult accountResult, int id) {
                goldValue = accountResult.detail.coins;
                goldCount.setText(NumberUtil.format10_000(goldValue));
            }
        });
    }

    public int getGiftSelectPosition() {
        return giftSelectPosition;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComming(EventCenter center) {
        if (center.getEventCode() == 2048) {
            messagePoint.setVisibility(View.VISIBLE);
            PrivateMessage message = (PrivateMessage) center.getData();
            if (performerId.equals(message.with)) {
                withPerformer = true;
            }
            if (null != chatRoomDialog) {
                chatRoomDialog.addMessage(message);
            }
        } else  if (center.getEventCode() == 2049) {
            if (null != chatListDialog) {
                chatListDialog.refresh();
            }
        }
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Override
    protected int getStatusBarColor() {
        return android.R.color.transparent;
    }

    public void showGiftPop(float x, float y, List<GiftAnim> giftComboDTOS) {
        if (null == giftComboDTOS) {
            showToastCenter("null == giftComboDTOS");
            return;
        }
        giftPopWindow.addPosition((int)x, (int)y);
        giftPopWindow.onRefreshData(giftComboDTOS);
        giftPopWindow.show(ButterKnife.findById(this, R.id.activity_cam_detail_parent));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        videoViewSmall.getLayoutParams().width = getResources().getDimensionPixelSize(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT ? R.dimen.qb_px_460 : R.dimen.qb_px_690);
        videoViewSmall.getLayoutParams().height = getResources().getDimensionPixelSize(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT ? R.dimen.qb_px_690 : R.dimen.qb_px_460);
        videoViewSmall.requestLayout();
        hideKeyboard();
        super.onConfigurationChanged(newConfig);
    }

    private void sendCustomMsg(String cmd, CustomMsg msg, IMLVBLiveRoomListener.SendRoomCustomMsgCallback callback){
        if (loading.getVisibility() == View.VISIBLE) {
            showToastCenter("暂未直播，功能不可用");
            //del by easy
            //update by libin
            return;
        }
        liveRoom.sendRoomCustomMsg(cmd, GsonUtils.gsonString(msg), callback);
    }
}
