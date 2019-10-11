package com.wstv.pad.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.wstv.pad.R;
import com.wstv.pad.activity.base.BaseActivity;

import java.util.Calendar;

import butterknife.Bind;

/**
 * Created by Kindred on 2019/5/14.
 */

public class AboutUsActivity extends BaseActivity {

    @Bind(R.id.activity_about_us_version)
    TextView version;

    @Bind(R.id.activity_about_us_copyright)
    TextView copyright;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.activity_about_us;
    }

    @Override
    protected void getBundleExtras(Bundle extras) {

    }

    @Override
    protected void initViewAndData() {
        version.setText("Android版\t" + getVerName(this));
        copyright.setText("Copyright© " + Calendar.getInstance().get(Calendar.YEAR) + "万秀直播 版权所有");
    }

    /**
     * 获取版本号名称
     *
     * @param context 上下文
     * @return
     */
    public static String getVerName(Context context) {
        String verName = "";
        try {
            verName = context.getPackageManager().
                    getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return verName;
    }

    @Override
    protected View getLoadingTargetView() {
        return null;
    }

    @Override
    protected void onEventComming(EventCenter center) {

    }
}
