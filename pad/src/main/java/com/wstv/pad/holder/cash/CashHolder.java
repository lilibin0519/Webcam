package com.wstv.pad.holder.cash;

import android.content.Context;

import com.wstv.pad.R;
import com.wstv.pad.http.model.account.AccountLog;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import em.sang.com.allrecycleview.holder.CustomHolder;

/**
 * Created by Kindred on 2019/3/25.
 */

public class CashHolder extends CustomHolder<AccountLog> {

    public CashHolder(Context context, List<AccountLog> lists, int itemID) {
        super(context, lists, itemID);
    }

    @Override
    public void initView(int position, List<AccountLog> datas, Context context) {
        holderHelper.setText(R.id.item_cash_time, new SimpleDateFormat("yyyy.MM.dd\nHH:mm", Locale.SIMPLIFIED_CHINESE).format(Long.parseLong(datas.get(position).createTime)));
        holderHelper.setText(R.id.item_cash_cost, getCashValue(datas.get(position).coins, datas.get(position).type));
        holderHelper.setText(R.id.item_cash_type, getTypeLabel(datas.get(position).type));
    }

    private String getCashValue(String coins, String type) {
        String force;
        if ("200".equals(type) || "201".equals(type) || "202".equals(type) || "203".equals(type)) {
            force = "+";
        } else/* if ("210".equals(type) || "211".equals(type) || "212".equals(type) || "213".equals(type))*/ {
            force = "-";
        }
        return force + coins;
    }

    private String getTypeLabel(String type){
        if ("200".equals(type)) {
            return "其他收入";
        } else if ("201".equals(type)) {
            return "礼物收入";
        } else if ("202".equals(type)) {
            return "充值";
        } else if ("203".equals(type)) {
            return "收入守护";
        } else if ("210".equals(type)) {
            return "消费";
        } else if ("211".equals(type)) {
            return "提现支出";
        } else if ("212".equals(type)) {
            return "购买守护";
        } else if ("213".equals(type)) {
            return "礼物消费";
        }
        return "未知";
    }
}
