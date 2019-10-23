package com.wstv.webcam.holder.performer;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.libin.mylibrary.util.GlideLoadUtils;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.PerformerActivity;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.http.model.Performer;
import com.wstv.webcam.util.click.ClickProxy;

import java.math.BigDecimal;
import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;

/**
 * <p>Description: </p>
 * PerformerHolder
 *
 * @author lilibin
 * @createDate 2019/3/11 13:55
 */

public class PerformerHolder extends CustomHolder<Performer> {

    public PerformerHolder(Context context, List<Performer> lists, int itemID) {
        super(context, lists, itemID);
    }

    @Override
    public void initView(final int position, final List<Performer> datas, final Context context) {
        itemView.setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("showId", datas.get(position).showId);
                bundle.putString("performerId", datas.get(position).id);
                ((BaseActivity) context).readyGo(PerformerActivity.class, bundle);
            }
        }));
//        Glide.with(context).load(datas.get(position).headPortrait).into((ImageView) holderHelper.getView(R.id.item_performer_avatar));
        GlideLoadUtils.getInstance().glideLoad(context, datas.get(position).headPortrait,
                (ImageView) holderHelper.getView(R.id.item_performer_avatar));
        holderHelper.setText(R.id.item_performer_name, datas.get(position).nickname);
        holderHelper.setText(R.id.item_performer_follower_count, format10_000(TextUtils.isEmpty(datas.get(position).fans) ? 0 : Integer.parseInt(datas.get(position).fans)));
        holderHelper.setText(R.id.item_performer_like_count, format10_000(TextUtils.isEmpty(datas.get(position).fans) ? 0 : Integer.parseInt(datas.get(position).praise)));
        holderHelper.setText(R.id.item_performer_follow, datas.get(position).isfans ? "取关" : "关注");
        holderHelper.getView(R.id.item_performer_follow).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClick(position, datas.get(position));
                }
            }
        }));
    }

    private String format10_000(int praise) {
        String result;
        if (praise > 9999) {
            BigDecimal bg = new BigDecimal( praise / (double)10000);
            result = String.valueOf(bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue()) + "万";
        } else {
            result = String.valueOf(praise);
        }
        return result;
    }
}
