package com.libin.mylibrary.base.activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.libin.mylibrary.base.eventbus.EventCenter;
import com.libin.mylibrary.base.net.NetChangeObserver;
import com.libin.mylibrary.base.net.NetStateReceiver;
import com.libin.mylibrary.base.net.NetType;
import com.libin.mylibrary.base.viewswitcher.VaryViewHelperController;
import com.libin.mylibrary.util.BaseAppManager;
import com.libin.mylibrary.util.ToastUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.ButterKnife;

/**
 * <p>Description: </p>
 * BaseAppCompatActivity
 *
 * @author lilibin
 * @createDate 2018/12/8 13:05
 */
public abstract class BaseAppCompatActivity extends AppCompatActivity
{

    /**
     * 网络状态
     */
    protected NetChangeObserver mNetChangeObserver = null;

    /**
     * loading view controller
     */
    private VaryViewHelperController mVaryViewHelperController = null;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        if (null != extras)
        {
            getBundleExtras(extras);
        }

        if (isRegisterEventBus())
        {
            EventBus.getDefault().register(this);
        }


        NetStateReceiver.registerObserver(mNetChangeObserver);
        BaseAppManager.getInstance().addActivity(this);

        if (getContentViewLayoutID() != 0)
        {
            setContentView(getContentViewLayoutID());
        }

        mNetChangeObserver = new NetChangeObserver()
        {

            @Override
            public void onNetConnected(NetType type)
            {
                super.onNetConnected(type);
                onNetworkConnected(type);
            }

            @Override
            public void onNetDisConnect()
            {
                super.onNetDisConnect();
                onNetworkDisConnected();
            }
        };

        initViewAndData();

    }

    /**
     * setContentView(R.layout...);
     *
     * @return
     */
    protected abstract int getContentViewLayoutID();


    /**
     * Bundle  传递数据
     *
     * @param extras
     */
    protected abstract void getBundleExtras(Bundle extras);

    /**
     * 界面初始化
     */
    protected abstract void initViewAndData();

    /**
     * 当前没有网络连接
     */
    protected void onNetworkDisConnected(){}

    /**
     * 当前网络连接类型
     *
     * @param type
     */
    protected void onNetworkConnected(NetType type){}

    /**
     * 切换根View   findViewById(R.id...);
     *
     * @return
     */
    protected abstract View getLoadingTargetView();

    /**
     * EventBus
     *
     * @param center
     */
    protected abstract void onEventComming(EventCenter center);

    @Override
    public void setContentView(int layoutResID)
    {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        if (null != getLoadingTargetView())
        {
            mVaryViewHelperController = new VaryViewHelperController(getLoadingTargetView());
        }
    }

    @Override
    public void finish()
    {
        super.finish();
        BaseAppManager.getInstance().removeActivity(this);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ButterKnife.unbind(this);
        NetStateReceiver.removeRegisterObserver(mNetChangeObserver);
        if (isRegisterEventBus())
        {
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * 界面跳转
     *
     * @param clazz
     */
    public void readyGo(Class<?> clazz)
    {
        readyGo(clazz, null);
    }

    /**
     * 跳转界面，  传参
     *
     * @param clazz
     * @param bundle
     */
    public void readyGo(Class<?> clazz, Bundle bundle)
    {
        Intent intent = new Intent(this, clazz);
        if (null != bundle)
            intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * 跳转界面并关闭当前界面
     *
     * @param clazz
     */
    public void readyGoThenKill(Class<?> clazz)
    {
        readyGoThenKill(clazz, null);
    }

    protected void readyGoThenKill(Class<?> clazz, Bundle bundle)
    {
        readyGo(clazz, bundle);
        finish();
    }

    /**
     * startActivityForResult
     *
     * @param clazz
     * @param requestCode
     */
    public void readyGoForResult(Class<?> clazz, int requestCode)
    {
        Intent intent = new Intent(this, clazz);
        startActivityForResult(intent, requestCode);
    }

    /**
     * startActivityForResult with bundle
     *
     * @param clazz
     * @param requestCode
     * @param bundle
     */
    public void readyGoForResult(Class<?> clazz, int requestCode, Bundle bundle)
    {
        Intent intent = new Intent(this, clazz);
        if (null != bundle)
        {
            intent.putExtras(bundle);
        }
        startActivityForResult(intent, requestCode);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * Toast 提示
     *
     * @param text
     */
    public void showToastShort(String text)
    {
        ToastUtils.showToastShort(text);
    }

    public void showToastShort(int resourceId)
    {
        ToastUtils.showToastShort(resourceId);
    }

    public void showToastLong(String text)
    {
        ToastUtils.showToastLong(text);
    }

    public void showToastLong(int resourceId)
    {
        ToastUtils.showToastLong(resourceId);
    }

    public void showToastCenter(String text)
    {
        ToastUtils.showToastGravityCenter(text);
    }

    /**
     * 是否在当前 Activity 实例中注册  EventBus
     * 默认在当前 Activity 不注册
     * 如果需要注册广播监听，  重写此方法    返回   true
     *
     * @return
     */
    protected boolean isRegisterEventBus()
    {
        return false;
    }


    protected void reStoreView()
    {
        if (null != mVaryViewHelperController)
            mVaryViewHelperController.restore();
    }


    /**
     * 显示  正在加载界面
     *
     * @param toggle
     * @param msg
     */
    protected void toggleShowLoading(boolean toggle, String msg)
    {

        if (null == mVaryViewHelperController)
        {
            throw new IllegalArgumentException("必须传入一个目标视图加载控制器");
        }

        if (toggle)
        {
            mVaryViewHelperController.showLoading(msg, this);
        } else
        {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * 显示返回内容为空界面
     *
     * @param toggle
     * @param msg
     * @param onClickListener
     */
    protected void toggleShowEmpty(boolean toggle, String msg, View.OnClickListener onClickListener)
    {
        if (null == mVaryViewHelperController)
        {
            throw new IllegalArgumentException("必须传入一个目标视图加载控制器");
        }

        if (toggle)
        {
            mVaryViewHelperController.showEmpty(msg, onClickListener);
        } else
        {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * 显示加载失败页面
     *
     * @param toggle
     * @param msg
     * @param onClickListener
     */
    protected void toggleShowError(boolean toggle, String msg, View.OnClickListener onClickListener)
    {
        if (null == mVaryViewHelperController)
        {
            throw new IllegalArgumentException("必须传入一个目标视图加载控制器");
        }

        if (toggle)
        {
            mVaryViewHelperController.showError(msg, onClickListener);
        } else
        {
            mVaryViewHelperController.restore();
        }
    }

    /**
     * 无网络连接时   显示当前界面
     *
     * @param toggle
     * @param onClickListener
     */
    protected void toggleNetworkError(boolean toggle, View.OnClickListener onClickListener)
    {
        if (null == mVaryViewHelperController)
        {
            throw new IllegalArgumentException("必须传入一个目标视图加载控制器");
        }

        if (toggle)
        {
            mVaryViewHelperController.showNetworkError(onClickListener);
        } else
        {
            mVaryViewHelperController.restore();
        }

    }
    
    @Subscribe
    public void onEventMainThread(EventCenter center)
    {

        if (null != center)
        {
            onEventComming(center);
        }

    }


    /**
     * use SytemBarTintManager
     *
     * @param tintDrawable
     */
    protected void setSystemBarTintDrawable(Drawable tintDrawable)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            SystemBarTintManager mTintManager = new SystemBarTintManager(this);
            if (tintDrawable != null)
            {
                mTintManager.setStatusBarTintEnabled(true);
                mTintManager.setTintDrawable(tintDrawable);
            } else
            {
                mTintManager.setStatusBarTintEnabled(false);
                mTintManager.setTintDrawable(null);
            }
        }

    }

    /**
     * set status bar translucency
     *
     * @param on
     */
    protected void setTranslucentStatus(boolean on)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            Window win = getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on)
            {
                winParams.flags |= bits;
            } else
            {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

}
