package com.ash.whatever.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ash.whatever.R;

import java.util.ArrayList;


/**
 * Created by liujiang on 2016/6/24.
 */
public class DragListAdapter extends ArrayAdapter<String> {

    private ArrayList<String> list;

    public DragListAdapter(Context context, ArrayList<String> list) {
        super(context, 0, list);
        this.list = list;
    }

    public ArrayList<String> getList() {
        return list;
    }

    //检查数据项是否为标题项然后标记是否可以更改
    @Override
    public boolean isEnabled(int position) {
        return super.isEnabled(position);
    }


    //利用模板布局不同的listview
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        view = LayoutInflater.from(getContext()).inflate(R.layout.drag_item_text_iamge, null);
        TextView textView = (TextView) view.findViewById(R.id.tv_dragItem);
        textView.setText(getItem(position));
        return view;
    }
}
