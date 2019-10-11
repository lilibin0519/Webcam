package com.wstv.webcam.util;

import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * <p>Description: </p>
 * RealmUtils
 *
 * @author lilibin
 * @createDate 2019/5/21 13:54
 */

public class RealmUtils {

    private Context context;
    private static RealmUtils mInstance;
    private String realName="webCam.realm";

    public RealmUtils(Context context){
        this.context= context;
    }

    /**单例方式*/
    public static RealmUtils getInstance(Context context) {
        if(mInstance == null) {
            synchronized(RealmUtils.class) {
                if(mInstance == null) {
                    mInstance = new RealmUtils(context);
                }
            }
        }
        return mInstance;
    }

    /***获得realm对象*/
    public Realm getRealm(){
        RealmConfiguration configuration = new RealmConfiguration
                .Builder()
                .name(realName)
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(configuration);
        return Realm.getDefaultInstance();
    }
}
