package com.wstv.webcam.tencent.roomutil.misc;

/**
 * Created by jac on 2017/11/17.
 * Copyright © 2013-2017 Tencent Cloud. All Rights Reserved.
 */

public class TextChatMsg {

    public static enum Aligment{
        LEFT(0),
        RIGHT(1),
        CENTER(2);
        final int aligment;
        Aligment(int aligment){
            this.aligment = aligment;
        }
    }

    private String name;
    private String time;
    private String msg;
    private Aligment aligment;

    private int color;

    private String type;

    private int level;

    private String messageType;

    public TextChatMsg() {
    }

    public String getName() {
        return name;
    }

    public TextChatMsg(String name, String time, String msg, Aligment aligment, int color, String type, int level, String messageType) {
        this.name = name;
        this.time = time;
        this.msg = msg;
        this.aligment = aligment;
        this.color = color;
        this.type = type;
        this.level = level;
        this.messageType = messageType;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Aligment aligment() {
        return aligment;
    }

    public void setAligment(Aligment aligment) {
        this.aligment = aligment;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
