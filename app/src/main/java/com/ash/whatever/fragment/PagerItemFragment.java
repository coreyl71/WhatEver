package com.ash.whatever.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.ash.whatever.R;
import com.ash.whatever.activity.SearchActivity;
import com.ash.whatever.adapter.NewsRecyclerAdapter;
import com.ash.whatever.bean.NewsBean;
import com.ash.whatever.utils.UrlUtils;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.recyclerview.adapter.SlideInLeftAnimatorAdapter;
import it.gmariotti.recyclerview.itemanimator.SlideScaleInOutRightItemAnimator;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 新闻页面ViewPager内的Fragment
 * 即每个频道对应的显示页面
 * Created by liujiang on 2016/6/21.
 */
public class PagerItemFragment extends Fragment {

    private TextView tv_news;
    private XRecyclerView rcv_news;
    private List<NewsBean.NewslistBean> list;
    private NewsRecyclerAdapter adapter;
    private String channel;
    private int page = 1;

    // 用于传递外部网址
    public static PagerItemFragment getInstance(String str) {
        PagerItemFragment fragment = new PagerItemFragment();
        Bundle args = new Bundle();
        args.putString("channel", str);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_pager_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
        initAdapter();
        initListener();
        rcv_news.setItemAnimator(new SlideScaleInOutRightItemAnimator(rcv_news));
    }

    private void initListener() {
        // 设置XRecyclerView具备下拉刷新和上拉加载
        rcv_news.setLoadingMoreEnabled(true);
        rcv_news.setPullRefreshEnabled(true);

        // 设置加载数据的监听
        rcv_news.setLoadingListener(new XRecyclerView.LoadingListener() {

            // 下拉刷新
            @Override
            public void onRefresh() {
                OkHttpUtils.get()
                        .url(UrlUtils.getUrl(channel, 20))
                        .build()
                        .execute(new Callback<NewsBean>() {
                            @Override
                            public NewsBean parseNetworkResponse(Response response, int i) throws Exception {
                                NewsBean newsBean = new Gson().fromJson(response.body().string(), NewsBean.class);
                                return newsBean;
                            }

                            @Override
                            public void onError(Call call, Exception e, int i) {
                                Toast.makeText(getContext(), "网络访问出错", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(NewsBean newsBean, int i) {
                                list.addAll(0, newsBean.getNewslist());
                                adapter.update(list, channel);
                                adapter.notifyDataSetChanged();
                                rcv_news.refreshComplete();
                            }
                        });
            }

            // 上拉加载，每加载一次新闻页数加1，即每次加载下一页的新闻
            @Override
            public void onLoadMore() {
                OkHttpUtils.get()
                        .url(UrlUtils.getUrl(channel, ++page, 20, 1))
                        .build()
                        .execute(new Callback<NewsBean>() {

                            @Override
                            public NewsBean parseNetworkResponse(Response response, int i) throws Exception {
                                NewsBean newsBean = new Gson().fromJson(response.body().string(), NewsBean.class);
                                return newsBean;
                            }

                            @Override
                            public void onError(Call call, Exception e, int i) {

                            }

                            @Override
                            public void onResponse(NewsBean newsBean, int i) {
                                list.addAll(newsBean.getNewslist());
                                adapter.update(list, channel);
                                adapter.notifyDataSetChanged();
                                rcv_news.loadMoreComplete();
                            }
                        });
            }
        });

        // 点击搜索框跳转搜索页面
        tv_news.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SearchActivity.class);
                intent.putExtra("channel", channel);
                startActivity(intent);
            }
        });
    }

    private void initAdapter() {
        rcv_news.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new NewsRecyclerAdapter(getActivity());
        rcv_news.setAdapter(new SlideInLeftAnimatorAdapter(adapter, rcv_news));
    }

    // 获取getInstance()传递的数据
    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        channel = args.getString("channel");
    }

    /**
     * OkHttp异步加载网络数据并通知适配器更新数据源
     */
    private void initData() {
        list = new ArrayList<>();
        OkHttpUtils.get()
                .url(UrlUtils.getUrl(channel, 50))
                .build()
                .execute(new Callback<NewsBean>() {

                    @Override
                    public NewsBean parseNetworkResponse(Response response, int i) throws Exception {
                        NewsBean newsBean = new Gson().fromJson(response.body().string(), NewsBean.class);
                        return newsBean;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(NewsBean newsBean, int i) {
                        list.addAll(newsBean.getNewslist());
                        adapter.update(list, channel);
                        adapter.notifyDataSetChanged();
                    }
                });

    }

    private void initView(View view) {
        tv_news = (TextView) view.findViewById(R.id.tv_news);
        rcv_news = (XRecyclerView) view.findViewById(R.id.rcv_news);
    }
}
