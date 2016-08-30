package com.ash.whatever.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ash.whatever.R;
import com.ash.whatever.activity.WebViewActivity;
import com.ash.whatever.bean.NewsBean;
import com.ash.whatever.utils.UrlUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.google.gson.Gson;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import it.gmariotti.recyclerview.adapter.SlideInBottomAnimatorAdapter;
import it.gmariotti.recyclerview.itemanimator.SlideInOutBottomItemAnimator;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Corey on 2016/6/20.
 * 推荐界面
 * Bug说注释必须比代码多，我很为难，因为写代码就够我头疼的了，还要写注释，还要写的让他们看得懂。
 * 感觉生无所恋！！！
 */
public class RecommandFragment extends Fragment {

    // 声明成员变量
    private TextView tv_title_recommand;
    private XRecyclerView rcv_content_recommand;
    private SwipeRefreshLayout srl_recommand;
    private WebView mWebView;

    private MyAdapter adapter;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayList<NewsBean.NewslistBean> newsList = new ArrayList<>();

    private String url = UrlUtils.getUrl(10);
    private static final String TAG = "mtag";
    private boolean flag;
    private int page = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // 重写onCreateView方法
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // 设置需要进行图片加载的布局
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_recommand_layout, null);
        // 设置不需要进行图片加载的布局

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        // 初始化数据源
        initData();
        // 设置适配器
        setAdapter();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化控件
        initView(view);
        // 设置布局管理器
        rcv_content_recommand.setLayoutManager(new LinearLayoutManager(getContext()));
        // 设置上拉加载、下拉刷新
        initXRecyclerView();
        // 设置下拉监听
        setRefreshListener();
        //设置刷新等待过程中，颜色的渐变
        srl_recommand.setColorSchemeColors(Color.BLUE, Color.RED, Color.GREEN, Color.YELLOW);

    }

    private void setRefreshListener() {
        srl_recommand.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl_recommand.setRefreshing(true);
                OkHttpUtils.get()
                        .url(url)
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
                                // 获取数据并更新页面显示
                                newsList.addAll(0, newsBean.getNewslist());
                                Log.i(TAG, "getData: " + newsList.toString());
                                adapter.notifyDataSetChanged();
                                srl_recommand.setRefreshing(false);
                            }
                        });
            }
        });
    }

    private void initXRecyclerView() {
        // 开启上拉加载
        rcv_content_recommand.setLoadingMoreEnabled(true);
        // 关闭xrecyclerview中的下拉刷新方法
        rcv_content_recommand.setPullRefreshEnabled(false);
        // 上拉加载动画类型
        rcv_content_recommand.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        rcv_content_recommand.setLoadingListener(new XRecyclerView.LoadingListener() {
            // 下拉刷新
            @Override
            public void onRefresh() {
                // 使用异步获取字符串
                OkHttpUtils.get()
                        .url(UrlUtils.getUrl(UrlUtils.KEJI, 1, 10, UrlUtils.IS_RAND))
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
                                // 获取数据并更新页面显示
                                newsList.addAll(0, newsBean.getNewslist());
                                Log.i(TAG, "getData: " + newsList.toString());
                                adapter.notifyDataSetChanged();
                                rcv_content_recommand.refreshComplete();
                            }
                        });
            }

            // 上拉加载
            @Override
            public void onLoadMore() {
                // 使用异步获取字符串
                OkHttpUtils.get()
                        .url(UrlUtils.getUrl(UrlUtils.KEJI, ++page, 10, UrlUtils.IS_RAND))
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
                                // 获取数据并更新页面显示
                                newsList.addAll(newsBean.getNewslist());
                                Log.i(TAG, "getData: " + newsList.toString());
                                adapter.notifyDataSetChanged();
                                rcv_content_recommand.loadMoreComplete();
                            }
                        });
            }
        });

    }

    private void setAdapter() {
        adapter = new MyAdapter();
        SlideInBottomAnimatorAdapter adapterAnim = new SlideInBottomAnimatorAdapter(adapter, rcv_content_recommand);
        rcv_content_recommand.setAdapter(adapterAnim);
    }

    private void initData() {

        // 使用异步获取字符串
        OkHttpUtils.get()
                .url(UrlUtils.getUrl(UrlUtils.KEJI, 1, 10, UrlUtils.IS_RAND))
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
                        // 获取数据并更新页面显示
                        newsList.addAll(newsBean.getNewslist());
                        Log.i(TAG, "getData: " + newsList.toString());
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    private void initView(View view) {
        tv_title_recommand = (TextView) view.findViewById(R.id.tv_title_recommand);
        rcv_content_recommand = (XRecyclerView) view.findViewById(R.id.rcv_content_recommand);
        mWebView = (WebView) view.findViewById(R.id.webview);
        srl_recommand = (SwipeRefreshLayout) view.findViewById(R.id.srl_recommand);
    }

    class MyAdapter extends XRecyclerView.Adapter<MyAdapter.MyHolder> {

        private int sqlId;
        private boolean sqlFlag;

        @Override
        public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new MyHolder(LayoutInflater.from(getContext()).inflate(R.layout.fragment_recommand_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final MyHolder holder, final int position) {
            final NewsBean.NewslistBean news =newsList.get(position);
            holder.tv_title_content_recommand.setText(news.getTitle());
            holder.tv_descrip_content_recommand.setText(news.getDescription());
            holder.tv_time_conent_recommand.setText(news.getCtime());

            // 点击之后页面跳转到相应网页
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i(TAG, "onClick: " + news.getUrl());
                    // 打开WebViewActivity并传递参数
                    Intent intent = new Intent(getActivity(), WebViewActivity.class);
                    // 通过intent传递数据
                    intent.putExtra("url", news.getUrl());
                    startActivity(intent);
                }
            });

            StringBuilder titles = new StringBuilder();
            List<NewsBean.NewslistBean> newslist = DataSupport.findAll(NewsBean.NewslistBean.class);
            for (int k = 0; k < newslist.size(); k++) {
                titles.append(newslist.get(k).getTitle());
            }
            //收藏按钮图标的初始化显示
            if (titles.toString().contains(news.getTitle())) {
                //该新闻在数据库中
                holder.ib_favorite_flag.setImageResource(R.mipmap.timeline_toolbar_iv_favorite_selected);
            } else {
                //该新闻不在数据库中
                holder.ib_favorite_flag.setImageResource(R.mipmap.timeline_toolbar_iv_favorite_normal);
            }
            //收藏按钮的点击事件
            holder.ib_favorite_flag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    List<NewsBean.NewslistBean> sqlData = DataSupport.findAll(NewsBean.NewslistBean.class);
                    Log.i("sfsf", "sqlitelist--> " + sqlData.toString());

                    StringBuilder titles = new StringBuilder();
                    List<NewsBean.NewslistBean> newslist = DataSupport.findAll(NewsBean.NewslistBean.class);
                    for (int k = 0; k < newslist.size(); k++) {
                        titles.append(newslist.get(k).getTitle());
                    }
                    //收藏按钮图标的初始化显示
                    if (titles.toString().contains(news.getTitle())) {
                        //该新闻在数据库中,删除新闻，并改变图标
                        DataSupport.deleteAll(NewsBean.NewslistBean.class, "title = ?", news.getTitle());
                        holder.ib_favorite_flag.setImageResource(R.mipmap.timeline_toolbar_iv_favorite_normal);
                    } else {
                        //该新闻不在数据库中，添加新闻，并改变图标
                        NewsBean.NewslistBean news_ = new NewsBean.NewslistBean();

                        news_.setTitle(news.getTitle());
                        news_.setCtime(news.getCtime());
                        news_.setDescription(news.getDescription());
                        news_.setUrl(news.getUrl());
                        news_.setPicUrl(news.getPicUrl());
                        news_.save();
                        if(news_.save()) {
                            Toast.makeText(getActivity(), "收藏成功！", Toast.LENGTH_SHORT).show();
                            holder.ib_favorite_flag.setImageResource(R.mipmap.timeline_toolbar_iv_favorite_selected);
                        }
                    }

                    Log.i("sfsf", "sqlitelistNewssssss--> " + DataSupport.findAll(NewsBean.NewslistBean.class).toString());
                }
            });

            // 处于图片模式且图片地址不为空
            if (!flag && (news.getPicUrl() != null)) {
                // 通过Glide框架下载图片并显示
                Glide.with(getContext())
                        .load(news.getPicUrl())
                        .override(200, 200)
                        .fitCenter()
                        .into(new GlideDrawableImageViewTarget(holder.iv_content_recommand) {
                            @Override
                            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                super.onResourceReady(resource, animation);
                                holder.pb_loading.setVisibility(View.GONE);
                            }

                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                super.onLoadStarted(placeholder);
                                holder.pb_loading.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                holder.pb_loading.setVisibility(View.GONE);
                            }
                        });
                holder.iv_content_recommand.setVisibility(View.VISIBLE);
            } else {
                holder.pb_loading.setVisibility(View.GONE);
                holder.iv_content_recommand.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return newsList.size();
        }

        class MyHolder extends XRecyclerView.ViewHolder {

            private TextView tv_title_content_recommand, tv_descrip_content_recommand, tv_time_conent_recommand;
            private ImageView iv_content_recommand;
            private ImageButton ib_favorite_flag;
            private ProgressBar pb_loading;
            // 获取屏幕宽高
            private int screenWidth = getActivity().getResources().getDisplayMetrics().widthPixels;
            private int screenHeight = getActivity().getResources().getDisplayMetrics().heightPixels;

            public MyHolder(View itemView) {
                super(itemView);
                itemView.setLayoutParams(new RelativeLayout.LayoutParams(screenWidth, screenHeight / 6));
                tv_title_content_recommand = (TextView) itemView.findViewById(R.id.tv_title_content_recommand);
                tv_descrip_content_recommand = (TextView) itemView.findViewById(R.id.tv_descrip_content_recommand);
                tv_time_conent_recommand = (TextView) itemView.findViewById(R.id.tv_time_conent_recommand);
                iv_content_recommand = (ImageView) itemView.findViewById(R.id.iv_content_recommand);
                ib_favorite_flag = (ImageButton) itemView.findViewById(R.id.ib_favorite_flag);
                pb_loading = (ProgressBar) itemView.findViewById(R.id.pb_loading);
                /*// 设置下载图片的宽高
                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(screenWidth / 4, screenHeight / 6);
                    *//*params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    params.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);*//*
                iv_content_recommand.setLayoutParams(params);*/


            }
        }
    }

    // 列表item加载时动画效果
    public void itemAnimation() {
        rcv_content_recommand.setItemAnimator(new SlideInOutBottomItemAnimator(rcv_content_recommand));
    }

    // 定义一个方法，当MainActivity通过InfoFragment中的接口对象调用此方法时，flag状态发生改变
    public void updataFlag() {
        flag = !flag;
        Log.i(TAG, "updataFlag: " + flag);
        adapter.notifyDataSetChanged();
    }

}
