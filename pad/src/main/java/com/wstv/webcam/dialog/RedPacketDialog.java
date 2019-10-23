package com.wstv.webcam.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.libin.mylibrary.util.GlideLoadUtils;
import com.makeramen.roundedimageview.RoundedImageView;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.http.HttpService;
import com.wstv.webcam.http.callback.BaseCallback;
import com.wstv.webcam.http.model.redpacket.RedPacketResult;
import com.wstv.webcam.util.click.ClickProxy;

/**
 * Created by Kindred on 2019/5/13.
 */

public class RedPacketDialog extends Dialog {

    private Context context;

    private String redKey;

    private RelativeLayout force;

    private TextView value;

    private TextView nickname;

    private RoundedImageView avatar;

    public RedPacketDialog(@NonNull final BaseActivity context, final OnClickListener sendListener, final String redPacketKey, String avatar, String name) {
        super(context, R.style.BottomDialog);
        this.redKey = redPacketKey;
        this.context = context;
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_red_packet, null);
        setContentView(contentView);
        setCanceledOnTouchOutside(false);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        force = contentView.findViewById(R.id.dialog_red_packet_force);
        value = contentView.findViewById(R.id.dialog_red_packet_cash);
        this.avatar = contentView.findViewById(R.id.dialog_red_packet_avatar);
//        Glide.with(context).load(avatar).error(R.drawable.default_avatar).dontAnimate().placeholder(R.drawable.default_avatar).into(this.avatar);
        GlideLoadUtils.getInstance().glideLoad(context, avatar,
                this.avatar, R.drawable.default_avatar);
        nickname = contentView.findViewById(R.id.dialog_red_packet_nick);
        nickname.setText(name);
        getWindow().setGravity(Gravity.CENTER);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = context.getResources().getDimensionPixelOffset(R.dimen.qb_px_600);
        lp.height = context.getResources().getDimensionPixelOffset(R.dimen.qb_px_800);
        findViewById(R.id.dialog_red_packet_get).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpService.getRedPacket(context, redKey, context.getApp().getUserBean().userId, new BaseCallback<RedPacketResult>(context, context) {
                    @Override
                    public void onSuccess(RedPacketResult redPacketResult, int id) {
                        showValue(redPacketResult.detail.amount);
                    }
                });
            }
        }));
        findViewById(R.id.dialog_red_packet_close).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        }));
    }

    public void setAvatar(String avatar){
//        Glide.with(context).load(avatar).into(this.avatar);
        GlideLoadUtils.getInstance().glideLoad(context, avatar,
                this.avatar);
    }

    public void setNickname(String name) {
        nickname.setText(name);
    }

    private void showValue(String valueStr) {
        value.setText(valueStr);
        force.setVisibility(View.GONE);
    }

    public void setPacketId(String id) {
        redKey = id;
    }
}
