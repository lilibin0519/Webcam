package com.wstv.webcam.holder;

import android.content.Context;
import android.view.LayoutInflater;

import com.wstv.webcam.R;

import em.sang.com.allrecycleview.holder.CustomPeakHolder;

/**
 * <p>Description: </p>
 * BlankFooterHolder
 *
 * @author lilibin
 * @createDate 2019/3/11 13:53
 */

public class BlankFooterHolder extends CustomPeakHolder {
    public BlankFooterHolder(Context context) {
        super(LayoutInflater.from(context).inflate(R.layout.footer_blank, null));
    }
}
