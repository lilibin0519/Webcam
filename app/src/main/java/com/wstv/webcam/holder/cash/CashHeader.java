package com.wstv.webcam.holder.cash;

import android.content.Context;
import android.view.View;

import com.wstv.webcam.R;

import em.sang.com.allrecycleview.holder.CustomPeakHolder;

/**
 * Created by Kindred on 2019/3/25.
 */

public class CashHeader extends CustomPeakHolder {

    private String coins;

    public CashHeader(Context context, View itemView, String coins) {
        super(itemView);
        this.itemView = itemView;
        this.context = context;
        this.coins = coins;
    }

    @Override
    public void initView(int position, final Context context) {
        holderHelper.setText(R.id.header_cash_account, coins);
    }

    public void setAccount(String coins){
        holderHelper.setText(R.id.header_cash_account, coins);
    }
}
