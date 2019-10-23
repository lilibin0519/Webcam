package com.wstv.webcam.fragment;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.libin.mylibrary.base.net.NetUtils;
import com.libin.mylibrary.base.util.Trace;
import com.libin.mylibrary.base.view.BaseView;
import com.wstv.webcam.R;
import com.wstv.webcam.WsApp;
import com.wstv.webcam.activity.base.BaseActivity;
import com.wstv.webcam.util.LoadingInterface;

/**
 * <p>Description: </p>
 * BaseFragment
 *
 * @author lilibin
 * @createDate 2018/12/8 13:24
 */

public abstract class BaseFragment extends com.libin.mylibrary.base.fragment.BaseFragment implements BaseView, LoadingInterface {

    protected TextView title;

    protected LinearLayout left;

    protected LinearLayout right;

    protected TextView leftText;

    protected TextView rightText;

    protected ImageView leftImage;

    protected ImageView rightImage;

    /**
     * 点击屏幕重新加载方法
     */
    protected void onReloadThe(){}

    private MaterialDialog loadingMaterialDialog;


    @Override
    public void showLoading(String msg)
    {
        toggleShowLoading(true, msg);
    }

    @Override
    public void hideLoading()
    {
        toggleShowLoading(false , null);
    }

    @Override
    public void showError(String msg)
    {
        toggleShowError(true, msg, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onReloadThe();
            }
        });
    }

    @Override
    public void showException(String msg)
    {
        showError(msg);
    }

    @Override
    public void showNetError()
    {
        toggleNetworkError(true, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onReloadThe();
            }
        });
    }

    @Override
    public void showNormal()
    {
        toggleShowEmpty(true, null, new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                onReloadThe();
            }
        });
    }

    protected boolean checkNet(){
        if(!NetUtils.isNetworkConnected(getActivity())){
            showNetError();
            return false;
        }else{
            return true;
        }
    }

    public void showEmpty(){
        showEmpty("暂无数据\n点击重试");
    }

    public void showEmpty(String msg){
        toggleShowEmpty(true, msg, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReloadThe();
            }
        });
    }

    @Override
    public void showLoadingDialog() {
        getLoadingDialog();
        if (!loadingMaterialDialog.isShowing()) {
            loadingMaterialDialog.show();
        }
    }

    @Override
    public MaterialDialog getLoadingDialog(){
        if (null == loadingMaterialDialog) {
            loadingMaterialDialog = new MaterialDialog.Builder(getContext())
                    .content(R.string.please_wait)
                    .contentGravity(GravityEnum.START)
                    .progress(true, 0)
                    .cancelable(false)
                    .progressIndeterminateStyle(false).build();
//            loadingMaterialDialog.getProgressBar().setIndeterminateDrawable(getResources().getDrawable(R.drawable.loading_progress));
        }
        return loadingMaterialDialog;
    }

    @Override
    public void dismissLoadingDialog() {
        if (loadingMaterialDialog != null && loadingMaterialDialog.isShowing())
            loadingMaterialDialog.dismiss();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Toolbar toolbar = view.findViewById(R.id.title_bar_parent);
        if (null != toolbar) {
            Rect rectangle= new Rect();
            getActivity().getWindow().getDecorView().getWindowVisibleDisplayFrame(rectangle);
            toolbar.setPadding(0, rectangle.top, 0, 0);
        }
        initTitle(view);
        super.onViewCreated(view, savedInstanceState);
    }

    private void initTitle(View view) {
        title = view.findViewById(R.id.title_bar_title);
        if (null != title){
            left = view.findViewById(R.id.title_bar_left);
            left.setVisibility(View.GONE);
            right = view.findViewById(R.id.title_bar_right);
            right.setVisibility(View.GONE);
            rightText = view.findViewById(R.id.title_bar_right_text);
            leftText = view.findViewById(R.id.title_bar_left_text);
            rightImage = view.findViewById(R.id.title_bar_right_image);
            leftImage = view.findViewById(R.id.title_bar_left_image);
            if (null != left) {
                left.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onLeftClick(v);
                    }
                });
            }

            if (null != right) {
                right.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRightClick(v);
                    }
                });
            }
        }
    }

    protected void onRightClick(View v) {

    }

    protected void onLeftClick(View v) {

    }

    public int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Trace.e("status height : " + statusHeight);
        return statusHeight;
    }

    public WsApp getApp(){
        return ((BaseActivity) getActivity()).getApp();
    }
}
