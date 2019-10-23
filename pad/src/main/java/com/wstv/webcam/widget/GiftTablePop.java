package com.wstv.webcam.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.libin.mylibrary.base.util.Trace;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.LivePageActivity;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.adapter.GiftAdapter;
import com.wstv.webcam.holder.gift.GiftHolder;
import com.wstv.webcam.holder.gift.GiftViewListener;
import com.wstv.webcam.http.model.gift.GiftBean;
import com.wstv.webcam.http.model.gift.GiftType;
import com.wstv.webcam.util.click.ClickProxy;

import java.util.ArrayList;
import java.util.List;

import em.sang.com.allrecycleview.adapter.DefaultAdapter;
import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Created by Kindred on 2019/5/13.
 */

public class GiftTablePop extends PopupWindow {

    private RecyclerView list;

    private Context context;

    private List<GiftBean> data;

    private List<GiftType> typeData;

    private GiftAdapter adapter;

    private TextView count;

    private TextView plus;

    private TextView account;

    private boolean isGuard;

    private RecyclerView typeGroup;

    private DefaultAdapter<GiftType> typeAdapter;

    public GiftTablePop(@NonNull final Context context, final List<GiftType> data, SendListener sendListener, String accountValue) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_gift, null);
        setContentView(contentView);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);//设置Pop可点击
        this.setOutsideTouchable(true);
        this.update();//刷新状态
        this.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
        setAnimationStyle(R.style.BottomDialog_Animation);
        list = contentView.findViewById(R.id.dialog_gift_list);
        count = contentView.findViewById(R.id.dialog_gift_list_count);
        plus = contentView.findViewById(R.id.dialog_gift_list_plus);
        account = contentView.findViewById(R.id.dialog_gift_list_account);
        account.setText(accountValue);
        typeGroup = contentView.findViewById(R.id.dialog_gift_type_group);
//        contentView.findViewById(R.id.dialog_gift_list_send).setOnClickListener(new ClickProxy(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (null != sendListener) {
//                    if (((LivePageActivity) context).giftSelectPosition == -1) {
//                        ToastUtils.showToastGravityCenter("请选择礼物");
//                        return;
//                    }
//                    sendListener.onSendGift(count.getText().toString(), data.get(((LivePageActivity) context).typeIndex).giftDTOList.get(((LivePageActivity) context).giftSelectPosition));
//                }
//            }
//        }));
//        contentView.findViewById(R.id.dialog_gift_list_send).setVisibility(((LivePageActivity) context).giftSelectPosition == -1 ? View.INVISIBLE : View.VISIBLE);
        contentView.findViewById(R.id.dialog_gift_list_get_with).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGuard) {
                    return;
                }
                ((LivePageActivity) context).showGuardBuy();
                dismiss();
            }
        }));
//        contentView.findViewById(R.id.dialog_gift_list_add).setOnClickListener(new ClickProxy(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (((LivePageActivity) context).giftSelectPosition == -1) {
//                    return;
//                }
//                if ("1".equals(count.getText()) && !"0".equals(data.get(((LivePageActivity) context).giftSelectPosition).ifMoreThanOnce) && !TextUtils.isEmpty(data.get(((LivePageActivity) context).giftSelectPosition).fiveTimesUrl)) {
//                    count.setText("5");
//                    plus.setVisibility(!TextUtils.isEmpty(data.get(((LivePageActivity) context).giftSelectPosition).tenTimesUrl) ? View.VISIBLE : View.INVISIBLE);
//                } else if ("5".equals(count.getText()) && !TextUtils.isEmpty(data.get(((LivePageActivity) context).giftSelectPosition).tenTimesUrl)) {
//                    count.setText("10");
//                } else if ("10".equals(count.getText())) {
//                    count.setText("1");
//                }
//            }
//        }));
        this.context = context;
        this.typeData = data;
        this.data = null == typeData || typeData.size() == 0 ? new ArrayList<GiftBean>() : typeData.get(((LivePageActivity) context).typeIndex).giftDTOList;
        initList(sendListener);
        initTypeGroup();
    }

    private void initTypeGroup() {
        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        typeGroup.setLayoutManager(manager);
        typeGroup.setAdapter(typeAdapter = new DefaultAdapter<>(context, typeData, R.layout.item_gift_type, new DefaultAdapterViewListener<GiftType>(){
            @Override
            public CustomHolder<GiftType> getBodyHolder(Context context, List<GiftType> lists, int itemID) {
                return new CustomHolder<GiftType>(context, lists, itemID){
                    @Override
                    public void initView(final int position, List<GiftType> datas, final Context context) {
                        itemView.setOnClickListener(new ClickProxy(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ((LivePageActivity) context).typeIndex = position;
                                notifyTypeData();
                            }
                        }));

                        holderHelper.getView(R.id.item_gift_type_label).setSelected(((LivePageActivity) context).typeIndex == position);
                        holderHelper.setText(R.id.item_gift_type_label, datas.get(position).type);
                    }
                };
            }
        }));
    }

    private void notifyTypeData() {
        typeAdapter.notifyDataSetChanged();
        data.clear();
        data.addAll(typeData.get(((LivePageActivity) context).typeIndex).giftDTOList);
        ((LivePageActivity) context).giftSelectPosition = -1;
        adapter.notifyDataSetChanged();
    }

    private void initList(final SendListener sendListener) {
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        list.setLayoutManager(manager);
        list.setAdapter(adapter = new GiftAdapter(context, data, R.layout.item_gift_list, new GiftViewListener(new OnToolsItemClickListener<GiftBean>() {
            @Override
            public void onItemClick(int position, GiftBean item) {
                if (null != sendListener) {
                    sendListener.onSendGift(count.getText().toString(), data.get(position));
                }
            }
        }, new GiftHolder.OnGiftLongClick() {
            @Override
            public void onLongClick(int position, GiftBean giftBean, View itemView) {
                ((LivePageActivity) context).giftSelectPosition = position;
//                getContentView().findViewById(R.id.dialog_gift_list_send).setVisibility(View.VISIBLE);
                count.setText("1");
//                plus.setVisibility(!"0".equals(data.get(((LivePageActivity) context).giftSelectPosition).ifMoreThanOnce) && !TextUtils.isEmpty(data.get(((LivePageActivity) context).giftSelectPosition).fiveTimesUrl) ? View.VISIBLE : View.INVISIBLE);
                notifyData();
                int[] location = new  int[2] ;
                itemView.getLocationOnScreen(location); //获取在当前窗口内的绝对坐标，含toolBar
                Trace.e("showGiftPop item y : " + location[1]);
                Trace.e("showGiftPop item x : " + location[0]);
                DisplayMetrics outMetrics = new DisplayMetrics();
                ((BaseActivity) context).getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
                int widthPixels = outMetrics.widthPixels;
                if (location[0] + itemView.getWidth() / 2 < 0 || widthPixels - location[0] < itemView.getWidth() / 2) {
                    return;
                }
                ((LivePageActivity) context).showGiftPop(location[0] + itemView.getWidth() / 2, location[1], giftBean.giftComboDTOS);
            }
        })));
    }

    public void setAccount(String account) {
        this.account.setText(String.valueOf(account));
    }

    private void notifyData() {
        adapter.notifyDataSetChanged();
    }

    public void notifyAllData() {
        notifyTypeData();
    }

    public interface SendListener{
        void onSendGift(String count, GiftBean bean);
    }

    public void setGuard(boolean guard) {
        isGuard = guard;
        getContentView().findViewById(R.id.dialog_gift_list_get_with).setVisibility(isGuard ? View.VISIBLE : View.GONE);
    }

    public void show(View parent) {
        Trace.e("showGiftPop");
        // 获取控件的位置，安卓系统>7.0
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
    }
}
