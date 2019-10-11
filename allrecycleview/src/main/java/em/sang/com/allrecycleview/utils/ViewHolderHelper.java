package em.sang.com.allrecycleview.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * <p>Description: </p>
 * ViewHolderHelper
 *
 * @author lilibin
 * @createDate 2017/9/22 16:38
 */

public class ViewHolderHelper {

    protected SparseArray<View> mViews;
    protected View mConvertView;
    protected Context mContext;

    public ViewHolderHelper(View convertView){
        mViews = new SparseArray<>();
        mConvertView = convertView;
        mContext = convertView.getContext();
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T getView(int viewId)
    {

        View view = mViews.get(viewId);

        if (view == null)
        {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }

        return (T) view;

    }

    public ViewHolderHelper setText(@IdRes int viewId, String text)
    {
        TextView tv = getView(viewId);
        if (TextUtils.isEmpty(text)){
            tv.setText("");
        }else {
            tv.setText(text);
        }return this;
    }

    public ViewHolderHelper setText(@IdRes int viewId, @StringRes int stringResId)
    {
        TextView tv = getView(viewId);
        tv.setText(mContext.getString(stringResId));
        return this;
    }


    public View getConvertView(){
        return mConvertView;
    }

    public ViewHolderHelper setImageBitmap(@IdRes int viewId, Bitmap bmp)
    {
        ImageView iv = getView(viewId);
        iv.setImageBitmap(bmp);
        return this;
    }

    /**
     * 设置对应id的控件是否选中
     *
     * @param viewId
     * @param checked
     * @return
     */
    public ViewHolderHelper setChecked(@IdRes int viewId, boolean checked)
    {
        Checkable view = getView(viewId);
        view.setChecked(checked);
        return this;
    }

    /**
     * 设置View是否可见
     * @param viewId
     * @param visibility
     * @return
     */
    public ViewHolderHelper setVisibility(@IdRes int viewId, int visibility)
    {
        View view = getView(viewId);
        view.setVisibility(visibility);
        return this;
    }

    /**
     * @param viewId
     * @param textColorResId 颜色资源id
     * @return
     */
    public ViewHolderHelper setTextColorRes(@IdRes int viewId, @ColorRes int textColorResId)
    {
        TextView view = getView(viewId);
        view.setTextColor(ContextCompat.getColor(mContext, textColorResId));
        return this;
    }

    /**
     * @param viewId
     * @param textColor 颜色值
     * @return
     */
    public ViewHolderHelper setTextColor(@IdRes int viewId, int textColor)
    {
        TextView view = getView(viewId);
        view.setTextColor(textColor);
        return this;
    }

    /**
     * @param viewId
     * @param backgroundResId 背景资源id
     * @return
     */
    public ViewHolderHelper setBackgroundRes(@IdRes int viewId, int backgroundResId)
    {
        View view = getView(viewId);
        view.setBackgroundResource(backgroundResId);
        return this;
    }

    /**
     * @param viewId
     * @param color  颜色值
     * @return
     */
    public ViewHolderHelper setBackgroundColor(@IdRes int viewId, int color)
    {
        View view = getView(viewId);
        view.setBackgroundColor(color);
        return this;
    }

    /**
     * @param viewId
     * @param colorResId 颜色值资源id
     * @return
     */
    public ViewHolderHelper setBackgroundColorRes(@IdRes int viewId, @ColorRes int colorResId)
    {
        View view = getView(viewId);
        view.setBackgroundColor(ContextCompat.getColor(mContext, colorResId));
        return this;
    }

    /**
     * @param viewId
     * @param imageResId 图像资源id
     * @return
     */
    public ViewHolderHelper setImageResource(@IdRes int viewId, @DrawableRes int imageResId)
    {
        ImageView view = getView(viewId);
        view.setImageResource(imageResId);
        return this;
    }
}
