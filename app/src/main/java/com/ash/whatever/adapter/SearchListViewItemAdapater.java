package com.ash.whatever.adapter;


import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.BackgroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ash.whatever.R;
import com.ash.whatever.bean.NewsBean;

import java.util.List;

public class SearchListViewItemAdapater extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<NewsBean.NewslistBean> data;
    private Context context;
    private String word;

    public SearchListViewItemAdapater(Context context, List<NewsBean.NewslistBean> data, String word) {
        this.context = context;
        this.data = data;
        this.word = word;
    }

    public void updateData(List<NewsBean.NewslistBean> data,String word) {
        this.word = word;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        mInflater = LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.search_lv_item, parent, false);

        TextView tv_title = (TextView) view.findViewById(R.id.tv_title_search_lv_item);
        TextView tv_description = (TextView) view.findViewById(R.id.tv_description_search_lv_item);
        TextView tv_ctime = (TextView) view.findViewById(R.id.tv_ctime_search_lv_item);

        NewsBean.NewslistBean news = data.get(position);
        String title = news.getTitle();
        SpannableString span_title;
        if (word != null) {
            //拿到关键字所在的下标，一个
            int word_index = title.indexOf(word);
            Log.i("mtag", "getView: "+word);
            span_title = new SpannableString(title);
            BackgroundColorSpan colorSpan = new BackgroundColorSpan(Color.parseColor("#AC00FF30"));
            span_title.setSpan(colorSpan, word_index, (word_index + word.length()), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        } else {
            span_title = new SpannableString(title);
        }
        tv_title.setText(span_title);
        tv_description.setText(news.getDescription());
        tv_ctime.setText(news.getCtime());
        return view;
    }


}
