package com.wstv.pad.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.wstv.pad.R;
import com.wstv.pad.util.click.ClickProxy;

/**
 * <p>Description: </p>
 * RankingTypePop
 *
 * @author lilibin
 * @createDate 2017/10/18 14:29
 */

@SuppressWarnings("deprecation")
public class RankingTypePop extends PopupWindow {

    private final TypeCheckListener listener;

    private View contentView;

    protected Activity activity;

    private int width, height;

    public RankingTypePop(Activity activity, TypeCheckListener listener){
        this.activity = activity;
        //获得LayoutInflater实例
        LayoutInflater inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        contentView = inflater.inflate(R.layout.pop_menu, null);
        /*
          获取屏幕的宽高
         */
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);

        this.listener = listener;
        initSelf(contentView);
    }

    private void initSelf(View contentView) {
        this.setContentView(contentView);//设置Pop的View
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);//设置Pop的高
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);//设置Pop的高
        this.setFocusable(true);//设置Pop可点击
        this.setOutsideTouchable(true);
        this.update();//刷新状态
        this.setBackgroundDrawable(new BitmapDrawable());
//        setTouchInterceptor(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
//                    if (isShouldHidePopMenu(event)) {
//                        dismiss();
//                    }
//                }
//                return false;
//            }
//        });

        contentView.findViewById(R.id.pop_menu_s).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onCheck(0);
                    dismiss();
                }
            }
        }));

        contentView.findViewById(R.id.pop_menu_r).setOnClickListener(new ClickProxy(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != listener) {
                    listener.onCheck(1);
                    dismiss();
                }
            }
        }));
    }

    private boolean isShouldHidePopMenu(MotionEvent event) {
        return isShowing() && !(event.getX() > 0 && event.getX() < width && event.getY() > 0 && event.getY() < height);
    }

    public void show(LinearLayout layout) {
//        contentView.measure(0, 0);
//        this.width = contentView.getMeasuredWidth();
//        this.height = contentView.getMeasuredHeight();
        int[] location = new int[2];
        layout.getLocationOnScreen(location);
        WindowManager wm = (WindowManager) layout.getContext().getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        contentView.measure(0, 0);
        this.width = contentView.getMeasuredWidth();
        this.height = contentView.getMeasuredHeight();
        showAtLocation(layout, Gravity.NO_GRAVITY, width - contentView.getContext().getResources().getDimensionPixelSize(R.dimen.qb_px_500), location[1] + layout.getHeight());


//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
//            showAsDropDown(layout);
//        } else {
//            Rect visibleFrame = new Rect();
//            layout.getGlobalVisibleRect(visibleFrame);
//            height = layout.getResources().getDisplayMetrics().heightPixels - visibleFrame.bottom;
//            setHeight(height);
//            showAsDropDown(layout, 0, 0);
//        }
    }

    public void setChecked(String checked){
        contentView.findViewById(R.id.pop_menu_s).setBackgroundColor(ContextCompat.getColor(activity, "S".equals(checked) ? R.color.pink : android.R.color.transparent));
        ((TextView) contentView.findViewById(R.id.pop_menu_s)).setTextColor(ContextCompat.getColor(activity, "S".equals(checked) ? android.R.color.white : R.color.pink));
        contentView.findViewById(R.id.pop_menu_r).setBackgroundColor(ContextCompat.getColor(activity, "R".equals(checked) ? R.color.pink : android.R.color.transparent));
        ((TextView) contentView.findViewById(R.id.pop_menu_r)).setTextColor(ContextCompat.getColor(activity, "R".equals(checked) ? android.R.color.white : R.color.pink));
    }

    public interface TypeCheckListener{
        void onCheck(int position);
    }
}
