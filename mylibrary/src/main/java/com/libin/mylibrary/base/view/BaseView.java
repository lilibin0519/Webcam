package com.libin.mylibrary.base.view;

/**
 * 作者：王鹏飞
 * 创建时间：2015/12/31 10:23
 * 邮箱：15291967179@163.com
 * 描述：
 */
public interface BaseView
{
    
    void showLoading(String msg);
    
    void hideLoading();
    
    void showError(String msg);
    
    void showException(String msg);
    
    void showNetError();
    
    void showNormal();
    
}
