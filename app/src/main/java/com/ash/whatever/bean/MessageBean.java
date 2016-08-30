package com.ash.whatever.bean;

/**
 * Created by Corey on 2016/6/28.
 */
public class MessageBean {
    public static final int TULING = 0;
    public static final int USER = 1;
    private String message;
    private int flag;

    public MessageBean() {
    }

    public MessageBean(String message, int flag) {
        this.message = message;
        this.flag = flag;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    @Override
    public String toString() {
        return "MessageBean{" +
                "message='" + message + '\'' +
                ", flag=" + flag +
                '}';
    }
}
