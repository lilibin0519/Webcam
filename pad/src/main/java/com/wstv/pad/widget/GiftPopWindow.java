package com.wstv.pad.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.libin.mylibrary.base.util.Trace;
import com.wstv.pad.R;
import com.wstv.pad.http.model.gift.GiftAnim;

import java.util.ArrayList;
import java.util.List;

import em.sang.com.allrecycleview.adapter.DefaultAdapter;
import em.sang.com.allrecycleview.holder.CustomHolder;
import em.sang.com.allrecycleview.holder.CustomPeakHolder;
import em.sang.com.allrecycleview.inter.DefaultAdapterViewListener;
import em.sang.com.allrecycleview.listener.OnToolsItemClickListener;

/**
 * <p>Description: </p>
 * GiftPopWindow
 *
 * @author lilibin
 * @createDate 2017/10/18 14:29
 */

public class GiftPopWindow extends PopupWindow {

    private RecyclerView list;

    private OnToolsItemClickListener<GiftAnim> listener;

    private View contentView;

    private int width, height;

    protected List<GiftAnim> data;

    protected Activity activity;

    private DefaultAdapter<GiftAnim> adapter;

    private int x;

    private int y;

    private ImageView arrow;

    public GiftPopWindow(Activity activity, OnToolsItemClickListener<GiftAnim> listener){
        this.listener = listener;
        this.activity = activity;
        //获得LayoutInflater实例
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.gift_menu, null);
        /*
          获取屏幕的宽高
         */
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        arrow = contentView.findViewById(R.id.gift_menu_arrow);
        initSelf(contentView);
        initList(contentView, activity);
    }

    private void initSelf(View contentView) {
        this.setContentView(contentView);//设置Pop的View
        //设置宽高
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
//        this.setWidth(AutoUtils.getPercentWidthSize(160));//设置Pop的宽
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);//设置Pop的高
        this.setFocusable(true);//设置Pop可点击
        this.setOutsideTouchable(true);
        this.update();//刷新状态
        this.setBackgroundDrawable(new BitmapDrawable(activity.getResources(), (Bitmap) null));
        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
                    if (isShouldHidePopMenu(event)) {
                        dismiss();
                    }
                }
                return false;
            }
        });
    }

    private boolean isShouldHidePopMenu(MotionEvent event) {
        return isShowing() && !(event.getX() > 0 && event.getX() < width && event.getY() > 0 && event.getY() < height);
    }

    public void setNormal(int normal){
//        if (normal == 1 && titles.contains(activity.getString(R.string.menu_label_3))) {
//            titles.remove(activity.getString(R.string.menu_label_3));
//        }
//        if (normal == 2 && titles.contains(activity.getString(R.string.menu_label_2))) {
//            titles.remove(activity.getString(R.string.menu_label_2));
//        }
        adapter.notifyDataSetChanged();
    }

    private void initList(View contentView, Activity activity){
        list = contentView.findViewById(R.id.gift_menu_list);
        LinearLayoutManager leftLayoutManager = new LinearLayoutManager(activity);
        leftLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        list.setLayoutManager(leftLayoutManager);
        data = new ArrayList<>();
        adapter = new DefaultAdapter<>(activity, data, R.layout.item_gift_menu, new DefaultAdapterViewListener<GiftAnim>() {
            @Override
            public CustomHolder getBodyHolder(Context context, List<GiftAnim> lists, int itemID) {
                View itemView = LayoutInflater.from(context).inflate(itemID, list, false);
                return new CustomHolder<GiftAnim>(lists, itemView) {
                    @Override
                    public void initView(int position, List<GiftAnim> datas, Context context) {
                        initItemView(itemView, position, datas);
                    }
                };
            }
        });
        adapter.addHead(new CustomPeakHolder(LayoutInflater.from(activity).inflate(R.layout.header_gift_menu, null)));
        list.setAdapter(adapter);
    }

    private void initItemView(View itemView, final int position, final List<GiftAnim> datas) {
        TextView label = (TextView) itemView.findViewById(R.id.item_gift_menu_label);
        label.setText("X" + datas.get(position).num);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(position, datas.get(position));
            }
        });
    }

    public void show(View parent) {
        Trace.e("showGiftPop");
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        // 获取控件的位置，安卓系统>7.0
        contentView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        this.width = contentView.getMeasuredWidth();
        this.height = contentView.getMeasuredHeight();
        Trace.e("showGiftPop x : " + x);
        int showX = x < width / 2 ? 0 : x - (width / 2);
        Trace.e("showGiftPop showX 0 : " + showX);
        if ((showX + width) > widthPixels) {
            Trace.e("showGiftPop showX 1 : " + showX);
            showX -= showX + width - widthPixels;
        }

        arrow.measure(0, 0);
        if ((x - arrow.getMeasuredWidth() / 2 - showX) > widthPixels - arrow.getMeasuredWidth()) {
            arrow.setX(widthPixels - arrow.getMeasuredWidth());
        } else {
            arrow.setX(x - arrow.getMeasuredWidth() / 2 - showX);
        }
        Trace.e("showGiftPop x : " + x);
        Trace.e("showGiftPop y : " + y);
        Trace.e("showGiftPop ((x + width) > widthPixels) : " + ((x + width) > widthPixels));
        Trace.e("showGiftPop width : " + width);
        Trace.e("showGiftPop widthPixels : " + widthPixels);
        Trace.e("showGiftPop arrow x : " + arrow.getX());
        Trace.e("showGiftPop showX : " + showX);
        Trace.e("showGiftPop showY : " + (y - height - activity.getResources().getDimensionPixelSize(R.dimen.qb_px_20)));
        showAtLocation(parent, Gravity.NO_GRAVITY, showX, y - height - activity.getResources().getDimensionPixelSize(R.dimen.qb_px_20));
        Trace.e("showGiftPop contentView left : " + getContentView().getLeft());
    }

    public void onRefreshData(List<GiftAnim> list){
        data.clear();
        if (null != list) {
            data.addAll(list);
        }
        adapter.notifyDataSetChanged();
    }

    public void addPosition(int x, int y){
        this.x = x;
        this.y = y;
    }

    private int[] getViewWidthAndHeight(View view){
        int[] wh = new int[2];
        int w = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0,
                View.MeasureSpec.UNSPECIFIED);
        view.measure(w, h);
        wh[0] = view.getMeasuredWidth();
        wh[1] = view.getMeasuredHeight();
        return wh;
    }
}
