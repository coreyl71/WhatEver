package com.ash.whatever.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.ash.whatever.R;
import com.ash.whatever.activity.ChannelActivity;
import com.ash.whatever.bean.ChannelBean;
import com.ash.whatever.utils.UrlUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 新闻页面
 * Created by corey on 2016/6/20.
 */
public class NewsFragment extends Fragment {

    private TabLayout tab_news;
    private ViewPager vp_news;
    private ImageButton ibtn_add;
    private String[] titles = {"微信", "科技", "社会", "国内", "国际", "娱乐", "体育", "健康", "苹果", "奇闻", "旅游"};
    private String[] channel = {UrlUtils.WEIXIN, UrlUtils.KEJI, UrlUtils.SHEHUI, UrlUtils.GUONEI, UrlUtils.GUOWAI,
            UrlUtils.YULE, UrlUtils.TIYU, UrlUtils.JIANKANG, UrlUtils.PINGGUO, UrlUtils.QIWEN, UrlUtils.LVYOU};
    private List<Fragment> list = new ArrayList<>();
    private Map<String, String> map = new HashMap<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initListener();
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
        initFragment();
        initAdapter();
    }

    private void initListener() {
        ibtn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChannelActivity.class);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });
    }

    private void initAdapter() {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getFragmentManager());
        vp_news.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        tab_news.setupWithViewPager(vp_news);
    }

    private void initFragment() {
        list.clear();
        for (int i = 0; i < titles.length; i++) {
            list.add(PagerItemFragment.getInstance(map.get(titles[i])));
        }
    }

    private void initData() {
        // 读取数据库中的ChannelBean数据放入集合中
        List<ChannelBean> channelBeanList = DataSupport.findAll(ChannelBean.class);
        // 如果数据库中没有数据，把数据存入数据库
        if (channelBeanList.size() == 0) {
            for (int i = 0; i < titles.length; i++) {
                ChannelBean channelBean = new ChannelBean();
                channelBean.setChannel(titles[i]);
                channelBean.save();
            }
        } else {
            // 如果数据库中有数据，把数据读取出来
            titles = new String[channelBeanList.size()];
            for (int i = 0; i < titles.length; i++) {
                titles[i] = channelBeanList.get(i).getChannel();
            }
        }
    }

    private void initView(View view) {
        tab_news = (TabLayout) view.findViewById(R.id.tab_news);
        vp_news = (ViewPager) view.findViewById(R.id.vp_news);
        ibtn_add = (ImageButton) view.findViewById(R.id.ibtn_add);
        for (int i = 0; i < titles.length; i++) {
            map.put(titles[i], channel[i]);
        }
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list == null ? 0 : list.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
