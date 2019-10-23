package com.wstv.webcam.holder.action;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.wstv.webcam.R;
import com.wstv.webcam.activity.ActionDetailActivity;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.http.model.action.ActionBean;
import com.wstv.webcam.util.click.ClickProxy;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import em.sang.com.allrecycleview.holder.CustomHolder;

/**
 * <p>Description: </p>
 * ActionBottomHolder
 *
 * @author lilibin
 * @createDate 2019/5/17 16:56
 */

public class ActionBottomHolder extends CustomHolder<ActionBean> {

    public ActionBottomHolder(Context context, List<ActionBean> lists) {
        super(context, lists, R.layout.item_action_bottom);
    }

    @Override
    public void initView(final int position, final List<ActionBean> datas, final Context context) {
        holderHelper.setText(R.id.item_action_bottom_like_count, datas.get(position / 3).likeCnt);
        holderHelper.setText(R.id.item_action_bottom_comment_count, datas.get(position / 3).commentCnt);
        holderHelper.setText(R.id.item_action_bottom_date, new SimpleDateFormat("HH:mm", Locale.SIMPLIFIED_CHINESE).format(Long.parseLong(datas.get(position / 3).createTime)));
        holderHelper.setImageResource(R.id.item_action_bottom_like, datas.get(position / 3).ifLike ? R.drawable.icon_action_like : R.drawable.icon_action_not_like);
        holderHelper.getView(R.id.item_action_bottom_like).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener && !datas.get(position / 3).ifLike) {
                    listener.onItemClick(position / 3, datas.get(position / 3));
                }
            }
        }));
//        itemView.setOnClickListener(new ClickProxy(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("actionBean", datas.get(position / 3));
//                ((BaseActivity) context).readyGo(ActionDetailActivity.class, bundle);
//            }
//        }));
        holderHelper.getView(R.id.item_action_bottom_comment_count_layout).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("actionBean", datas.get(position / 3));
                bundle.putBoolean("showInputFirst", true);
                ((BaseActivity) context).readyGo(ActionDetailActivity.class, bundle);
            }
        }));
    }
}
