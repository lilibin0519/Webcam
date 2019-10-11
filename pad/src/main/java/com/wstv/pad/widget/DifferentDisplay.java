package com.wstv.pad.widget;

import android.app.Dialog;
import android.app.Presentation;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.libin.mylibrary.base.util.Trace;
import com.libin.mylibrary.http.utils.GsonUtils;
import com.libin.mylibrary.util.GlideLoadUtils;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.wstv.pad.R;
import com.wstv.pad.activity.LivePageActivity;
import com.wstv.pad.dialog.RedPacketDialog;
import com.wstv.pad.entity.CustomMsg;
import com.wstv.pad.gift.NumAnim;
import com.wstv.pad.holder.chat.ChatViewListener;
import com.wstv.pad.http.HttpService;
import com.wstv.pad.http.callback.BaseCallback;
import com.wstv.pad.http.model.PerformerCard;
import com.wstv.pad.http.model.Pusher;
import com.wstv.pad.http.model.account.AccountResult;
import com.wstv.pad.http.model.audience.Audience;
import com.wstv.pad.http.model.gift.GiftBean;
import com.wstv.pad.http.model.gift.GiftType;
import com.wstv.pad.http.model.guard.GuardGood;
import com.wstv.pad.http.model.ranking.RankingResult;
import com.wstv.pad.tencent.liveroom.ILiveRoomListener;
import com.wstv.pad.tencent.roomutil.commondef.PusherInfo;
import com.wstv.pad.tencent.roomutil.misc.TextChatMsg;
import com.wstv.pad.util.NumberUtil;
import com.zhangyf.gift.RewardLayout;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import em.sang.com.allrecycleview.adapter.DefaultAdapter;

/**
 * Created by Kindred on 2019/7/2.
 */

public class DifferentDisplay extends Presentation {

    private LivePageActivity activity;

    @Bind(R.id.activity_cam_detail_video_view)
    public
    TXCloudVideoView videoView;

    @Bind(R.id.activity_cam_detail_loading)
    public
    TextView loading;

    @Bind(R.id.activity_cam_detail_performer_avatar)
    CircleImageView performerAvatar;

    @Bind(R.id.activity_cam_detail_performer_name)
    TextView performerName;

    @Bind(R.id.activity_cam_detail_performer_id)
    TextView performerIdTv;

    @Bind(R.id.activity_cam_detail_user_count)
    public
    TextView userCount;

    @Bind(R.id.activity_cam_detail_gold)
    public
    TextView goldCount;

    public int goldValue;

    @Bind(R.id.activity_cam_detail_with)
    TextView withCount;

    @Bind(R.id.activity_cam_detail_user_1)
    CircleImageView avatar1;

    @Bind(R.id.activity_cam_detail_user_2)
    CircleImageView avatar2;

    @Bind(R.id.activity_cam_detail_user_3)
    CircleImageView avatar3;

    private String performerId;

    private String roomId;

    @Bind(R.id.activity_cam_detail_chat_list)
    RecyclerView chatList;

    DefaultAdapter<TextChatMsg> chatAdapter;

    List<TextChatMsg> chatRecord;

    @Bind(R.id.activity_cam_detail_gift_list)
    RewardLayout rewardLayout;

    @Bind(R.id.activity_cam_detail_anim_view)
    SVGAImageView animView;

    @Bind(R.id.activity_cam_detail_barrage)
    public
    BarrageView barrageView;

    public Pusher pusher;

    public ArrayList<Audience> audiences = new ArrayList<>();

    private CircleImageView[]  avatars;

    private boolean isFans;

    private Dialog audienceDialog;

    public DefaultAdapter<Audience> audienceAdapter;

    private PerformerCard performerCard;

    private Dialog performerDialog;

    private View performerParent;

    private List<GuardGood> guardList = new ArrayList<>();

    private Dialog guardBuy;

    private RedPacketDialog redPacketDialog;

    private List<GiftType> giftData;

    private String guard = "";

    public CustomMsg msg;

    public DifferentDisplay(LivePageActivity activity, Display display) {
        super(activity, display);
        this.activity = activity;
    }

    private void initGiftLayout(){
        rewardLayout.setActivity(activity);
        rewardLayout.setGiftAdapter(new RewardLayout.GiftAdapter<GiftBean>() {
            @Override
            public View onInit(View view, GiftBean bean) {
                ImageView avatar = (ImageView) view.findViewById(R.id.riv_gift_my_avatar);
                ImageView giftImage = (ImageView) view.findViewById(R.id.iv_gift_img);
                final TextView giftNum = (TextView) view.findViewById(R.id.tv_gift_amount);
                TextView userName = (TextView) view.findViewById(R.id.tv_user_name);
                TextView giftName = (TextView) view.findViewById(R.id.tv_gift_name);

                // 初始化数据
//                Glide.with(activity).load(bean.userAvatar).dontAnimate().placeholder(R.drawable.default_avatar).into(avatar);
                GlideLoadUtils.getInstance().glideLoad(activity, bean.userAvatar,
                        avatar, R.drawable.default_avatar);
                giftNum.setText("x" + bean.getTheSendGiftSize());
                bean.setTheGiftCount(bean.getTheSendGiftSize());
//                giftImage.setImageResource(bean.getGiftImg());
//                Glide.with(activity).load(bean.getGiftImg()).into(giftImage);
                GlideLoadUtils.getInstance().glideLoad(activity, bean.getGiftImg(),
                        giftImage);
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
//                Glide.with(activity).load(bean.userAvatar).dontAnimate().placeholder(R.drawable.default_avatar).into(avatar);
                GlideLoadUtils.getInstance().glideLoad(activity, bean.userAvatar,
                        avatar, R.drawable.default_avatar);
//                giftImage.setImageResource(bean.getGiftImg());
//                Glide.with(activity).load(bean.getGiftImg()).into(giftImage);
                GlideLoadUtils.getInstance().glideLoad(activity, bean.getGiftImg(),
                        giftImage);
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
                Animation giftInAnim = (TranslateAnimation) AnimationUtils.loadAnimation(activity, R.anim.gift_in);
                // 礼物图像动画
                Animation imgInAnim = (TranslateAnimation) AnimationUtils.loadAnimation(activity, R.anim.gift_in);
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
                return (AnimationSet) AnimationUtils.loadAnimation(activity, R.anim.gift_out);
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

    private void initView() {
        initRecyclerView();

        msg = new CustomMsg();
        msg.data = new CustomMsg.Data();
        msg.user = new CustomMsg.User();
        msg.user.level = activity.getApp().getUserBean().level;
        msg.user.sex = activity.getApp().getUserBean().sex;

        chatList.post(new Runnable() {
            @Override
            public void run() {
                addSystemText("我们提倡绿色直播，严禁涉政、涉恐、涉群体事件、涉黄等直播内容");
            }
        });
        avatars = new CircleImageView[]{avatar1, avatar2, avatar3};
        setPerformerInfo();

        initGiftLayout();
        activity.enterRoom2(videoView);
    }

    public void setPerformerInfo() {
        if (null == performerAvatar) {
            return;
        }
//        Glide.with(activity).load(pusher.userAvatar).into(performerAvatar);
        GlideLoadUtils.getInstance().glideLoad(activity, pusher.userAvatar,
                performerAvatar, R.drawable.default_avatar);
        performerName.setText(pusher.userName);
        setAudiences();
        performerIdTv.setText("@" + roomId);
        getAccount();
        requestWith();
    }

    private void getAccount(){
        HttpService.getAccount(null, performerId, new BaseCallback<AccountResult>(activity, null) {
            @Override
            public void onSuccess(AccountResult accountResult, int id) {
                goldValue = accountResult.detail.coins;
                goldCount.setText(NumberUtil.format10_000(goldValue));
            }
        });
    }

    private void requestWith(){
        HttpService.getWithRanking(null, "1", "1", roomId, new BaseCallback<RankingResult>(activity, null) {
            @Override
            public void onSuccess(RankingResult rankingResult, int id) {
                withCount.setText(rankingResult.errmsg);
            }
        });
    }

    private void initRecyclerView() {
        chatRecord = new ArrayList<>();
        LinearLayoutManager manager = new LinearLayoutManager(activity);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        chatList.setLayoutManager(manager);
//        chatList.addItemDecoration(new RecycleViewDivider(this, LinearLayoutManager.VERTICAL, 10, ContextCompat.getColor(this, android.R.color.transparent)));
        chatList.setAdapter(chatAdapter = new DefaultAdapter<>(activity, chatRecord, R.layout.item_chat, new ChatViewListener()));
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
//                        showRedPacket(msg, userAvatar, userName);
                    } else if ("receive".equals(msg.subType)) {
                    }
                } else if ("manage".equals(msg.type)) {
                    if ("shutup".equals(msg.subType)) {
                        activity.shutUpTo = System.currentTimeMillis() + msg.data.time;
                    } else if ("kickout".equals(msg.subType) || "black".equals(msg.subType)) {
                        activity.onLeftClick(null);
                    }
                }
            }

            @Override
            public void onRoomClosed(String roomID) {
                loading.setText("房间关闭了");
                loading.setVisibility(View.VISIBLE);
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

                    loading.setText("主播已下播");
                    loading.setVisibility(View.VISIBLE);
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
                    loading.setVisibility(View.GONE);
                }
            }
        };
    }

    public void exitUserText(String userName, int level, String guard) {
        addMessageItem("", userName + " 退出直播间", TextChatMsg.Aligment.LEFT, Color.parseColor("#F7E796"), guard, level, "exit");
    }

    public synchronized void removeAudience(String userID) {
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

    public void addMyText(String userName, String message) {
        addRoomText(userName, message, activity.getApp().getUserBean().level, guard);
    }

    public void addSystemText(String message) {
        addMessageItem("", message, TextChatMsg.Aligment.LEFT, Color.parseColor("#3cfff3"), "", 0, "system");
    }

    public void addUserText(String userName, int level, String guard) {
        addMessageItem("", userName + " 进入直播间", TextChatMsg.Aligment.LEFT, Color.parseColor("#F7E796"), guard, level, "join");
        if (!TextUtils.isEmpty(guard)) {
            barrageView.addTextitem("欢迎 " + userName + " 加入直播间", guard);
        }
    }

    public void addUser(String userID, String userAvatar, String gender, String userName, int level) {
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

    public void setAudiences() {
        for (int i = 0; i < 3; i++) {
            if (null != audiences && i < audiences.size()) {
//                Glide.with(activity).load(audiences.get(i).getInfo().userAvatar).dontAnimate().placeholder(R.drawable.default_avatar).into(avatars[i]);
                GlideLoadUtils.getInstance().glideLoad(activity, audiences.get(i).getInfo().userAvatar,
                        avatars[i], R.drawable.default_avatar);
            } else {
//                Glide.with(activity).load(R.drawable.default_avatar).into(avatars[i]);
                GlideLoadUtils.getInstance().glideLoad(activity, R.drawable.default_avatar,
                        avatars[i]);
            }
        }
    }

    public void addRoomText(String userName, String message, int level, String guard) {
        addMessageItem(userName, message, TextChatMsg.Aligment.LEFT, Color.parseColor("#ffffff"), guard, level, "text");
    }

    private void addMessageItem(final String userName, final String message, final TextChatMsg.Aligment aligment, final int color, final String type, final int level, final String messageType){
        Trace.e("guard : " + guard);
        activity.runOnUiThread(new Runnable() {
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

    public void showGiftAnim(GiftBean giftBean, String giftUrl) {
        if (null == giftBean) {
            return;
        }
        rewardLayout.put(/*bean*/giftBean);

        if (animView.isAnimating() || TextUtils.isEmpty(giftUrl)) {
            return;
        }
        SVGAParser parser = new SVGAParser(activity);
        try {
            parser.decodeFromURL(new URL(giftUrl), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(SVGAVideoEntity svgaVideoEntity) {
                    SVGADrawable drawable = new SVGADrawable(svgaVideoEntity);
                    animView.setImageDrawable(drawable);
                    animView.startAnimation();
                }

                @Override
                public void onError() {

                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private void showRedPacket(CustomMsg msg, String avatar, String name) {
        if (null == redPacketDialog) {
            redPacketDialog = new RedPacketDialog(activity, new DialogInterface.OnClickListener() {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam_detail);
        ButterKnife.bind(this);
        initView();
    }

    public void setGiftData(List<GiftType> giftData) {
        this.giftData = giftData;
    }

    public TXCloudVideoView getVideoView() {
        return videoView;
    }

    public void setPerformerId(String performerId) {
        this.performerId = performerId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
}
