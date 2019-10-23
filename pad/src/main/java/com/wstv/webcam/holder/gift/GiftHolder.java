package com.wstv.webcam.holder.gift;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.libin.mylibrary.base.util.Trace;
import com.libin.mylibrary.util.GlideLoadUtils;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.LivePageActivity;
import com.wstv.webcam.http.model.gift.GiftBean;
import com.wstv.webcam.util.click.ClickProxy;

import java.util.List;

import em.sang.com.allrecycleview.holder.CustomHolder;

public class GiftHolder extends CustomHolder<GiftBean> {

    private OnGiftLongClick longClickListener;

    public GiftHolder(Context context, List<GiftBean> lists, int itemID) {
        super(context, lists, itemID);
    }

    @Override
    public void initView(final int position, final List<GiftBean> datas, Context context) {
        Trace.e("position start : " + position);
        itemView.setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onItemClick(position, datas.get(position));
                }
            }
        }));
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (null != datas.get(position).giftComboDTOS && datas.get(position).giftComboDTOS.size() > 0) {
                    if (null != longClickListener) {
                        longClickListener.onLongClick(position, datas.get(position), v);
                    }
                }
                return true;
            }
        });
        holderHelper.setBackgroundRes(R.id.item_gift_list_bg, ((LivePageActivity) context).giftSelectPosition == position ? R.drawable.bg_gift_selected : R.drawable.bg_gift_not_select);
        holderHelper.setText(R.id.item_gift_list_cost, datas.get(position).showCoin + "秀币");
//        Glide.with(context).load(datas.get(position).iconUrl).into((ImageView) holderHelper.getView(R.id.item_gift_list_image));
        GlideLoadUtils.getInstance().glideLoad(context, datas.get(position).iconUrl, (ImageView) holderHelper.getView(R.id.item_gift_list_image));
//        Glide.with(context).load(R.drawable.icon_gift_temp_2).into((ImageView) holderHelper.getView(R.id.item_gift_list_image));
        Trace.e("position end : " + position);
    }

    public void setLongClickListener(OnGiftLongClick longClickListener) {
        this.longClickListener = longClickListener;
    }

    public interface OnGiftLongClick{
        void onLongClick(int position, GiftBean giftBean, View itemView);
    }
}
