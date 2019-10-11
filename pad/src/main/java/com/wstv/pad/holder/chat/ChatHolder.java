package com.wstv.pad.holder.chat;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.wstv.pad.AppConstant;
import com.wstv.pad.R;
import com.wstv.pad.tencent.roomutil.misc.TextChatMsg;
import com.wstv.pad.widget.CenterAlignImageSpan;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;

/**
 * Created by Kindred on 2019/4/10.
 */

public class ChatHolder extends CustomHolder<TextChatMsg> {

    public ChatHolder(Context context, List<TextChatMsg> lists, int itemID) {
        super(context, lists, itemID);
    }

    @Override
    public void initView(int position, List<TextChatMsg> datas, Context context) {

        SpannableStringBuilder builder = new SpannableStringBuilder();
        int start = 0;
        if (!"join".equals(datas.get(position).getMessageType()) && !"system".equals(datas.get(position).getMessageType()) && !"exit".equals(datas.get(position).getMessageType())) {
            if (!TextUtils.isEmpty(datas.get(position).getType())) {
                builder.append("0");
                //获取一张图片
                Drawable drawable = ContextCompat.getDrawable(context, "M".equals(datas.get(position).getType()) ? R.drawable.icon_user_guard_month : R.drawable.icon_user_guard_year);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                CenterAlignImageSpan imgSpan = new CenterAlignImageSpan(drawable);
                builder.setSpan(imgSpan, start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(" ");
            }
            if (datas.get(position).getLevel() > 0) {
                start = builder.length();
                builder.append("1");
                //获取一张图片
                Drawable drawable = ContextCompat.getDrawable(context, AppConstant.levelArr[datas.get(position).getLevel() <= AppConstant.levelArr.length ? datas.get(position).getLevel() - 1 : 0]);
                drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                CenterAlignImageSpan imgSpan = new CenterAlignImageSpan(drawable);
                builder.setSpan(imgSpan, start, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                builder.append(" ");
            }
            if (!TextUtils.isEmpty(datas.get(position).getName())) {
                start = builder.length();
                builder.append(datas.get(position).getName()).append(":");
                builder.setSpan(new ForegroundColorSpan(Color.parseColor("#99abff")), start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        if (!TextUtils.isEmpty(datas.get(position).getMsg())) {
            start = builder.length();
            builder.append(datas.get(position).getMsg());
            builder.setSpan(new ForegroundColorSpan(datas.get(position).getColor()), start, builder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        ((TextView) holderHelper.getView(R.id.item_chat_message)).setText(builder);
    }
}
