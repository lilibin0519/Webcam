package com.wstv.pad.holder.audience;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.libin.mylibrary.util.GlideLoadUtils;
import com.wstv.pad.AppConstant;
import com.wstv.pad.R;
import com.wstv.pad.http.model.audience.Audience;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;

/**
 * <p>Description: </p>
 * AudienceHolder
 *
 * @author lilibin
 * @createDate 2019/5/22 12:43
 */

public class AudienceHolder extends CustomHolder<Audience> {

    public AudienceHolder(Context context, List<Audience> lists) {
        super(context, lists, R.layout.item_watch_record);
    }

    @Override
    public void initView(int position, List<Audience> datas, Context context) {

        if (null == datas.get(position)) {
            return;
        }
//        Glide.with(context).load(datas.get(position).getInfo().userAvatar).dontAnimate().placeholder(R.drawable.default_avatar).into((ImageView) holderHelper.getView(R.id.item_watch_record_avatar));
        GlideLoadUtils.getInstance().glideLoad(context, datas.get(position).getInfo().userAvatar,
                (ImageView) holderHelper.getView(R.id.item_watch_record_avatar), R.drawable.default_avatar);
        holderHelper.setText(R.id.item_watch_record_name, datas.get(position).getInfo().userName);
        holderHelper.setImageResource(R.id.item_watch_record_level_logo, AppConstant.levelArr[datas.get(position).getInfo().level > 0 && (datas.get(position).getInfo().level - 1) < AppConstant.levelArr.length ? datas.get(position).getInfo().level - 1 : 0]);
        holderHelper.setVisibility(R.id.item_watch_record_gender, TextUtils.isEmpty(datas.get(position).getInfo().gender) ? View.GONE : View.VISIBLE);
        holderHelper.setImageResource(R.id.item_watch_record_gender, "0".equals(datas.get(position).getInfo().gender) ? R.drawable.icon_gender_female : R.drawable.icon_gender_male);
        holderHelper.setVisibility(R.id.item_watch_record_time, View.INVISIBLE);
    }
}
