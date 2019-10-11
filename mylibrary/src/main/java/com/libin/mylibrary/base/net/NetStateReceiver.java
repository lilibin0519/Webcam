package com.libin.mylibrary.base.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.ArrayList;

/**
 * 作者：王鹏飞
 * 创建时间：2015/12/30 14:36
 * 邮箱：15291967179@163.com
 * 描述：
 */
public class NetStateReceiver extends BroadcastReceiver
{

    private static boolean isNetAvailable = false;
    private static NetType mNetType;
    private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    private final static String CUSTOM_ANDROID_NET_CHANGE_ACTION = "com.nice.choice.library.net.conn.CONNECTIVITY_CHANGE";

    private static ArrayList<NetChangeObserver> mNetChangeObservers = new ArrayList<>();

    private static BroadcastReceiver mBroadcastReceiver;

    private static BroadcastReceiver getReceiver()
    {
        if (mBroadcastReceiver == null)
        {
            mBroadcastReceiver = new NetStateReceiver();
        }
        return mBroadcastReceiver;
    }


    @Override
    public void onReceive(Context context, Intent intent)
    {

        mBroadcastReceiver = NetStateReceiver.this;

        if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION)
                || intent.getAction().equalsIgnoreCase(CUSTOM_ANDROID_NET_CHANGE_ACTION))
        {
            if (!NetUtils.isNetworkAvailable(context))
            {
                isNetAvailable = false;
            } else
            {
                isNetAvailable = true;
                mNetType = NetUtils.getAPNType(context);
            }
            notifyObserver();
        }
    }

    public static void registerNetworkStateReceiver(Context mContext)
    {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CUSTOM_ANDROID_NET_CHANGE_ACTION);
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        mContext.getApplicationContext().registerReceiver(getReceiver(), filter);
    }

    public static void checkNetworkState(Context mContext)
    {
        Intent intent = new Intent();
        intent.setAction(CUSTOM_ANDROID_NET_CHANGE_ACTION);
        mContext.sendBroadcast(intent);
    }

    public static void unRegisterNetworkStateReceiver(Context mContext)
    {
        if (mBroadcastReceiver != null)
        {
            try
            {
                mContext.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
            } catch (Exception e)
            {
            }
        }

    }

    private void notifyObserver()
    {
        if (!mNetChangeObservers.isEmpty())
        {
            for (int i = 0; i < mNetChangeObservers.size(); i++)
            {
                NetChangeObserver observer = mNetChangeObservers.get(i);
                if (observer != null) {
                    if (isNetworkAvailable()) {
                        observer.onNetConnected(mNetType);
                    } else {
                        observer.onNetDisConnect();
                    }
                }
            }
        }

    }

    public boolean isNetworkAvailable()
    {
        return isNetAvailable;
    }

    public static void registerObserver(NetChangeObserver observer)
    {
        if (mNetChangeObservers == null)
        {
            mNetChangeObservers = new ArrayList<NetChangeObserver>();
        }
        mNetChangeObservers.add(observer);
    }

    public static void removeRegisterObserver(NetChangeObserver observer)
    {
        if (mNetChangeObservers != null)
        {
            if (mNetChangeObservers.contains(observer))
            {
                mNetChangeObservers.remove(observer);
            }
        }
    }


}
