package com.wstv.webcam.holder.action;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.libin.mylibrary.util.GlideLoadUtils;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.LocalImagePagerActivity;
import com.wstv.webcam.http.model.action.ActionBean;
import com.wstv.webcam.util.click.ClickProxy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;

/**
 * Created by Kindred on 2019/5/17.
 */

public class ActionCenterHolder extends CustomHolder<ActionBean> {

    public static int[] ids = {R.id.item_action_center_image_1, R.id.item_action_center_image_2, R.id.item_action_center_image_3,
            R.id.item_action_center_image_4, R.id.item_action_center_image_5, R.id.item_action_center_image_6,
            R.id.item_action_center_image_7, R.id.item_action_center_image_8, R.id.item_action_center_image_9};

    public ActionCenterHolder(Context context, List<ActionBean> lists) {
        super(context, lists, R.layout.item_action_center);
    }

    @Override
    public void initView(int position, List<ActionBean> datas, final Context context) {
        String[] urls = new String[0];
        if (!TextUtils.isEmpty(datas.get(position / 3).images)) {
            urls = datas.get(position / 3).images.split("\\|");
        }
        holderHelper.setVisibility(R.id.item_action_center_layout_1, urls.length > 0 ? View.VISIBLE : View.GONE);
        holderHelper.setVisibility(R.id.item_action_center_layout_2, urls.length > 3 ? View.VISIBLE : View.GONE);
        holderHelper.setVisibility(R.id.item_action_center_layout_3, urls.length > 6 ? View.VISIBLE : View.GONE);
        for (int i = 0; i < 9; i++) {
            if (i < urls.length) {
//                Glide.with(context).load(urls[i]).into((ImageView) holderHelper.getView(ids[i]));
                GlideLoadUtils.getInstance().glideLoad(context, urls[i], (ImageView) holderHelper.getView(ids[i]));
            }
            holderHelper.setVisibility(ids[i], i < urls.length ? View.VISIBLE : View.INVISIBLE);
        }
        for (int id : ids) {
            final ArrayList<String> finalUrls = new ArrayList<>();
            Collections.addAll(finalUrls, urls);
            holderHelper.getView(id).setOnClickListener(new ClickProxy(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < ids.length; i++) {
                        if (ids[i] == v.getId()) {
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
    }
}
