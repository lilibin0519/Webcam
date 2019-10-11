package com.libin.mylibrary.base.eventbus;

/**
 * 作者：王鹏飞
 * 创建时间：2015/12/31 09:44
 * 邮箱：15291967179@163.com
 * 描述：
 */
public class EventCenter<T>
{
    
    private int eventCode = -1;
    
    private T data;

    public EventCenter(int eventCode)
    {
        this.eventCode = eventCode;
    }

    public EventCenter(int eventCode, T data)
    {
        this.eventCode = eventCode;
        this.data = data;
    }

    public int getEventCode()
    {
        return eventCode;
    }

    public T getData()
    {
        return data;
    }
}
