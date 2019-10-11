package com.wstv.pad.dialog;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.libin.mylibrary.base.util.Trace;
import com.libin.mylibrary.util.ToastUtils;
import com.wstv.pad.R;
import com.wstv.pad.activity.LivePageActivity;
import com.wstv.pad.adapter.GiftAdapter;
import com.wstv.pad.holder.gift.GiftHolder;
import com.wstv.pad.holder.gift.GiftViewListener;
import com.wstv.pad.http.model.gift.GiftBean;
import com.wstv.pad.util.click.ClickProxy;

import java.util.List;

import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * Created by Kindred on 2019/5/13.
 */

public class GiftDialog extends Dialog {

    private RecyclerView list;

    private Context context;

    private List<GiftBean> data;

    private GiftAdapter adapter;

    private TextView count;

    private TextView plus;

    private TextView account;

    private boolean isGuard;

    public GiftDialog(@NonNull final Context context, final List<GiftBean> data, final SendListener sendListener, String accountValue) {
        super(context, R.style.BottomDialog);
        View contentView = LayoutInflater.from(context).inflate(R.layout.dialog_gift, null);
        setContentView(contentView);
        setCanceledOnTouchOutside(true);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = context.getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        list = contentView.findViewById(R.id.dialog_gift_list);
        count = contentView.findViewById(R.id.dialog_gift_list_count);
        plus = contentView.findViewById(R.id.dialog_gift_list_plus);
        account = contentView.findViewById(R.id.dialog_gift_list_account);
        account.setText(accountValue);
        findViewById(R.id.dialog_gift_list_send).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != sendListener) {
                    if (((LivePageActivity) context).giftSelectPosition == -1) {
                        ToastUtils.showToastGravityCenter("请选择礼物");
                        return;
                    }
                    sendListener.onSendGift(count.getText().toString(), data.get(((LivePageActivity) context).giftSelectPosition));
                }
            }
        }));
        findViewById(R.id.dialog_gift_list_send).setVisibility(((LivePageActivity) context).giftSelectPosition == -1 ? View.INVISIBLE : View.VISIBLE);
        findViewById(R.id.dialog_gift_list_get_with).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isGuard) {
                    return;
                }
                ((LivePageActivity) context).showGuardBuy();
                dismiss();
            }
        }));
        findViewById(R.id.dialog_gift_list_add).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((LivePageActivity) context).giftSelectPosition == -1) {
                    return;
                }
//                if ("1".equals(count.getText()) && !"0".equals(data.get(((LivePageActivity) context).giftSelectPosition).ifMoreThanOnce) && !TextUtils.isEmpty(data.get(((LivePageActivity) context).giftSelectPosition).fiveTimesUrl)) {
//                    count.setText("5");
//                    plus.setVisibility(!TextUtils.isEmpty(data.get(((LivePageActivity) context).giftSelectPosition).tenTimesUrl) ? View.VISIBLE : View.INVISIBLE);
//                } else if ("5".equals(count.getText()) && !TextUtils.isEmpty(data.get(((LivePageActivity) context).giftSelectPosition).tenTimesUrl)) {
//                    count.setText("10");
//                } else if ("10".equals(count.getText())) {
//                    count.setText("1");
//                }
            }
        }));
        this.context = context;
        this.data = data;
        initList();
    }

    private void initList() {
        GridLayoutManager manager = new GridLayoutManager(context, 2);
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        list.setLayoutManager(manager);
        list.setAdapter(adapter = new GiftAdapter(context, data, R.layout.item_gift_list, new GiftViewListener(new OnToolsItemClickListener<GiftBean>() {
            @Override
            public void onItemClick(int position, GiftBean item) {
                ((LivePageActivity) context).giftSelectPosition = position;
                findViewById(R.id.dialog_gift_list_send).setVisibility(View.VISIBLE);
                count.setText("1");
//                plus.setVisibility(!"0".equals(data.get(((LivePageActivity) context).giftSelectPosition).ifMoreThanOnce) && !TextUtils.isEmpty(data.get(((LivePageActivity) context).giftSelectPosition).fiveTimesUrl) ? View.VISIBLE : View.INVISIBLE);
                notifyData();
            }
        }, new GiftHolder.OnGiftLongClick() {
            @Override
            public void onLongClick(int position, GiftBean giftBean, View itemView) {
                int[] location = new  int[2] ;
                itemView.getLocationOnScreen(location); //获取在当前窗口内的绝对坐标，含toolBar
                Trace.e("showGiftPop item y : " + location[1]);
                Trace.e("showGiftPop item x : " + location[0]);
                ((LivePageActivity) context).showGiftPop(/*itemView.getX()*/location[0] + itemView.getWidth() / 2, location[1],/*itemView.getY()*/giftBean.giftComboDTOS);
            }
        })));
    }

    public void setAccount(String account) {
        this.account.setText(String.valueOf(account));
    }

    public void notifyData() {
        adapter.notifyDataSetChanged();
    }

    public interface SendListener{
        void onSendGift(String count, GiftBean bean);
    }

    public void setGuard(boolean guard) {
        isGuard = guard;
        findViewById(R.id.dialog_gift_list_get_with).setVisibility(isGuard ? View.GONE : View.VISIBLE);
    }
}
