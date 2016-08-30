package com.ash.whatever.bean;

import org.litepal.crud.DataSupport;

/**
 * Created by Corey on 2016/6/28.
 */
public class ChannelBean extends DataSupport{

    private int id;
    private String channel;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "ChannelBean{" +
                "id=" + id +
                ", channel='" + channel + '\'' +
                '}';
    }
}
