package com.libin.mylibrary.http.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class GsonUtils
{

    private static Gson mGson = null;
    
    public static Gson getGson(){
        return mGson;
    }

    static
    {
        if (mGson == null)
        {
            mGson = new Gson();
        }
    }

    private GsonUtils()
    {
    }

    public static String gsonString(Object object)
    {
        String gsonStr = null;
        if (mGson != null)
        {
            gsonStr = mGson.toJson(object);
        }
        return gsonStr;
    }

    public static <T> T gsonToBean(String jsonStr, Class<T> clz)
    {
        T t = null;
        if (mGson != null)
        {
            t = mGson.fromJson(jsonStr, clz);
        }
        return t;
    }

    public static <T> T gsonToBean(String jsonStr, TypeToken<T> typeToken)
    {
        T t = null;
        if (mGson != null)
        {
            t = mGson.fromJson(jsonStr, typeToken.getType());
        }
        return t;
    }

    /**
     * 转成list
     *
     * @param gsonString
     * @param cls
     * @return
     */
    public static <T> List<T> gsonToList(String gsonString, Class<T> cls) {
        List<T> list = null;
        if (mGson != null) {
            list = mGson.fromJson(gsonString, new TypeToken<List<T>>() {
            }.getType());
        }
        return list;
    }

    /**
     * 转成list中有map的
     *
     * @param gsonString
     * @return
     */
    public static <T> List<Map<String, T>> GsonToListMaps(String gsonString) {
        List<Map<String, T>> list = null;
        if (mGson != null) {
            list = mGson.fromJson(gsonString,
                    new TypeToken<List<Map<String, T>>>()
                    {
                    }.getType());
        }
        return list;
    }

    /**
     * 转成map的
     *
     * @param gsonString
     * @return
     */
    public static <T> Map<String, T> GsonToMaps(String gsonString) {
        Map<String, T> map = null;
        if (mGson != null) {
            map = mGson.fromJson(gsonString, new TypeToken<Map<String, T>>() {
            }.getType());
        }
        return map;
    }

    /**
     * 将获取到的json字符串转换为对象集合进行返回
     * @param jsonData 需要解析的json字符串
     * @param cls 类模板
     * @return
     */
    public static <T> List<T> getObjList(String jsonData,Class<T> cls){
        List<T> list = new ArrayList<T>();
        if (jsonData.startsWith("[") && jsonData.endsWith("]")) {//当字符串以“[”开始，以“]”结束时，表示该字符串解析出来为集合
            //截取字符串，去除中括号
            jsonData = jsonData.substring(1, jsonData.length() -1);
            //将字符串以"},"分解成数组
            String[] strArr = jsonData.split("\\},");
            //分解后的字符串数组的长度
            int strArrLength = strArr.length;
            //遍历数组，进行解析，将字符串解析成对象
            for (int i = 0; i < strArrLength; i++) {
                String newJsonString = null;
                if (i == strArrLength -1) {
                    newJsonString = strArr[i];
                } else {
                    newJsonString = strArr[i] + "}";
                }
                T bean = gsonToBean(newJsonString, cls);
                list.add(bean);
            }
        }
        if (list == null || list.size() == 0) {
            return null;
        }
        return list;
    }
}
