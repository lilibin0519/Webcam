package com.wstv.webcam.dialog;

import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import com.libin.mylibrary.util.GlideLoadUtils;
import com.wstv.webcam.R;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.util.click.ClickProxy;

/**
 * <p>Description: </p>
 * OrderCodeDialog
 *
 * @author lilibin
 * @createDate 2019/7/22 10:36
 */

public class OrderCodeDialog extends AlertDialog {

    public OrderCodeDialog(@NonNull BaseActivity activity, String url, View.OnClickListener onPaidClick) {
        super(activity, R.style.BottomDialog);
        View view = View.inflate(activity, R.layout.dialog_order_code, null);
        setView(view);
        setCanceledOnTouchOutside(false);
//        Glide.with(activity).load(url).into((ImageView) view.findViewById(R.id.dialog_order_code_image));
        GlideLoadUtils.getInstance().glideLoad(activity, url,
                (ImageView) view.findViewById(R.id.dialog_order_code_image));
        view.findViewById(R.id.dialog_order_code_cancel).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        }));
        view.findViewById(R.id.dialog_order_code_paid).setOnClickListener(new ClickProxy(onPaidClick));
    }
}
