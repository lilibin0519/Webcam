package com.wstv.pad.holder.action;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.libin.mylibrary.util.GlideLoadUtils;
import com.wstv.pad.R;
import com.wstv.pad.activity.ActionDetailActivity;
import com.wstv.pad.activity.LocalImagePagerActivity;
import com.wstv.pad.http.model.action.ActionBean;
import com.wstv.pad.util.click.ClickProxy;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Locale;

import em.sang.com.allrecycleview.holder.CustomPeakHolder;

/**
 * <p>Description: </p>
 * ActionHeader
 *
 * @author lilibin
 * @createDate 2019/5/24 10:10
 */

public class ActionHeader extends CustomPeakHolder<ActionBean> {

    private ActionBean actionBean;

    public ActionHeader(Context context, View itemView, ActionBean actionBean) {
        super(itemView);
        this.context = context;
        this.actionBean = actionBean;
    }

    @Override
    public void initView(final int position, final Context context) {
        // top
        Calendar calendar = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);
        calendar.setTimeInMillis(Long.parseLong(actionBean.createTime));
        holderHelper.setText(R.id.item_action_top_day, (calendar.get(Calendar.DAY_OF_MONTH) < 10 ? "0" : "") + calendar.get(Calendar.DAY_OF_MONTH));
        holderHelper.setText(R.id.item_action_top_month, ActionTopHolder.monthArr[calendar.get(Calendar.MONTH)]);
        holderHelper.setText(R.id.item_action_top_message, actionBean.content);

        // center
        String[] urls = new String[0];
        if (!TextUtils.isEmpty(actionBean.images)) {
            urls = actionBean.images.split("\\|");
        }
        holderHelper.setVisibility(R.id.item_action_center_layout_1, urls.length > 0 ? View.VISIBLE : View.GONE);
        holderHelper.setVisibility(R.id.item_action_center_layout_2, urls.length > 3 ? View.VISIBLE : View.GONE);
        holderHelper.setVisibility(R.id.item_action_center_layout_3, urls.length > 6 ? View.VISIBLE : View.GONE);
        for (int i = 0; i < urls.length; i++) {
//            Glide.with(context).load(urls[i]).into((ImageView) holderHelper.getView(ActionCenterHolder.ids[i]));
            GlideLoadUtils.getInstance().glideLoad(context, urls[i], (ImageView) holderHelper.getView(ActionCenterHolder.ids[i]));
        }
        for (int id : ActionCenterHolder.ids) {
            final ArrayList<String> finalUrls = new ArrayList<>();
            Collections.addAll(finalUrls, urls);
            holderHelper.getView(id).setOnClickListener(new ClickProxy(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < ActionCenterHolder.ids.length; i++) {
                        if (ActionCenterHolder.ids[i] == v.getId()) {
                            Bundle bundle = new Bundle();
                            bundle.putInt("index", i);
                            bundle.putStringArrayList("paths", finalUrls);
                            Intent intent = new Intent(context, LocalImagePagerActivity.class);
                            intent.putExtras(bundle);
                            context.startActivity(intent);
                        }
                    }
                }
            }));
        }

        // bottom
        holderHelper.setText(R.id.item_action_bottom_like_count, actionBean.likeCnt);
        holderHelper.setText(R.id.item_action_bottom_comment_count, actionBean.commentCnt);
        holderHelper.setText(R.id.item_action_bottom_date, new SimpleDateFormat("HH:mm", Locale.SIMPLIFIED_CHINESE).format(Long.parseLong(actionBean.createTime)));
        holderHelper.setImageResource(R.id.item_action_bottom_like, actionBean.ifLike ? R.drawable.icon_action_like : R.drawable.icon_action_not_like);
        holderHelper.getView(R.id.item_action_bottom_like).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener && actionBean.ifLike) {
                    listener.onItemClick(position, actionBean);
                }
            }
        }));
        holderHelper.getView(R.id.item_action_bottom_comment_count_layout).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActionDetailActivity) context).showInputMsgDialog();
            }
        }));
    }

    public void setCommentCount(int size) {
        actionBean.commentCnt = String.valueOf(size);
        holderHelper.setText(R.id.item_action_bottom_comment_count, actionBean.commentCnt);
    }

    public void setLike(boolean ifLike, String likeCnt) {
        actionBean.ifLike = ifLike;
        actionBean.likeCnt = likeCnt;
        holderHelper.setImageResource(R.id.item_action_bottom_like, actionBean.ifLike ? R.drawable.icon_action_like : R.drawable.icon_action_not_like);
        holderHelper.setText(R.id.item_action_bottom_like_count, actionBean.likeCnt);
    }
}
