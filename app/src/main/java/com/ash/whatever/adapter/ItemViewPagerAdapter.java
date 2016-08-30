package com.ash.whatever.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * 自定义ItemViewPager中ViewPage的适配器
 * Created by liujiang on 2016/6/22.
 */
public class ItemViewPagerAdapter extends PagerAdapter {

    private List<ImageView> imageList;

    // 接收数据源的集合
    public void updateImageList(List<ImageView> imageList) {
        this.imageList = imageList;
    }

    @Override
    public int getCount() {
        return imageList == null ? 0 : imageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView iv = imageList.get(position % imageList.size());
        container.addView(iv);
        return iv;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
