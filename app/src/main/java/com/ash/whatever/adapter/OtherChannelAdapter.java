package com.ash.whatever.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ash.whatever.R;

import java.util.ArrayList;

/**
 * Created by liujiang on 2016/6/27.
 */
public class OtherChannelAdapter extends BaseAdapter {

    private Context context;
    public ArrayList<String> channelList;
    private LayoutInflater inflater;
    private TextView item_text;
    // 是否可见
    boolean isVisible = true;
    // 要删除的position
    public int remove_position = -1;

    public OtherChannelAdapter(Context context, ArrayList<String> channelList) {
        this.context = context;
        this.channelList = channelList;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return channelList == null ? 0 : channelList.size();
    }

    @Override
    public String getItem(int position) {
        if (channelList != null && channelList.size() != 0) {
            return channelList.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = inflater.inflate(R.layout.channel_grid_item, null);
        item_text = (TextView) view.findViewById(R.id.tv_channel_item);
        String channel = getItem(position);
        item_text.setText(channel);
        if (!isVisible && (position == -1 + channelList.size())) {
            item_text.setText("");
        }
        if (remove_position == position) {
            item_text.setText("");
        }
        return view;
    }

    // 获取频道列表
    public ArrayList<String> getChannnelList() {
        return channelList;
    }

    // 添加频道列表
    public void addItem(String channel) {
        channelList.add(channel);
        notifyDataSetChanged();
    }

    // 设置删除的position
    public void setRemove(int position) {
        remove_position = position;
        notifyDataSetChanged();
    }

    // 删除频道列表
    public void remove() {
        channelList.remove(remove_position);
        remove_position = -1;
        notifyDataSetChanged();
    }
    // 设置频道列表
    public void setListDate(ArrayList<String> list) {
        channelList = list;
    }

    // 获取是否可见
    public boolean isVisible() {
        return isVisible;
    }

    // 设置是否可见
    public void setVisible(boolean visible) {
        isVisible = visible;
    }
}
