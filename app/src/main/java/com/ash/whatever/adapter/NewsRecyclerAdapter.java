package com.ash.whatever.adapter;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ash.whatever.R;
import com.ash.whatever.activity.WebViewActivity;
import com.ash.whatever.bean.NewsBean;
import com.ash.whatever.view.ItemViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;


/**
 * 新闻页面ViewPager内的RecyclerView适配器
 * Created by Corey on 2016/6/21.
 */
public class NewsRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    private Context context;
    private Activity activity;
    private LayoutInflater inflater;
    // OkHttp网络请求返回的原始数据集合
    private List<NewsBean.NewslistBean> list;
    // 剔除原始数据中不包含图片的数据集合
    private List<NewsBean.NewslistBean> withIconList;
    // ViewPager条目的数据集合
    private List<NewsBean.NewslistBean> pagerList;
    // 列表条目的数据集合
    private List<NewsBean.NewslistBean> normalList;
    // 屏幕宽高
    private int screenW;
    private int screenH;
    // 标记InfoFragment页面文字模式是否被点击
    private boolean flag;
    // 频道
    private String channel;

    public NewsRecyclerAdapter(Activity activity) {
        this.activity = activity;
        inflater = LayoutInflater.from(activity);

        screenW = activity.getResources().getDisplayMetrics().widthPixels;
        screenH = activity.getResources().getDisplayMetrics().heightPixels;
        // 广播接收器接收InfoFragment发送的flag
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                flag = intent.getBooleanExtra("flag", false);
                notifyDataSetChanged();
            }
        };
        // 动态注册广播
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.ash.whatever.adapter");
        filter.addAction("com.ash.whatever.FAVORITE_CHANGE");
        activity.registerReceiver(receiver, filter);
    }

    // 更新数据源集合
    public void update(List<NewsBean.NewslistBean> list, String channel) {
        this.list = list;
        this.channel = channel;
        withIconList = new ArrayList<>();
        pagerList = new ArrayList<>();
        normalList = new ArrayList<>();
        // 遍历原始数据集合，提取出包含图片的集合对象
        for (NewsBean.NewslistBean newslistBean : list) {
            if (!"".equals(newslistBean)) {
                withIconList.add(newslistBean);
            }
        }
        // 遍历包含图片的集合，前4个数据提供给ViewPager条目，其余的提供给列表条目
        for (int i = 0; i < withIconList.size(); i++) {
            if (i < 4) {
                pagerList.add(withIconList.get(i));
            } else {
                normalList.add(withIconList.get(i));
            }
        }
    }

    /**
     * RecyclerView中每个item的类型
     * !flag时展示图片模式，flag时展示文字模式
     *
     * @param position
     * @return 0是自定义ViewPager类型
     *         1是普通带图片列表类型
     *         2是文字类型
     */
    @Override
    public int getItemViewType(int position) {
        if (!flag) {
            if (position == 0) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 2;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder = null;
        switch (viewType) {
            case 0:
                holder = new PagerHolder(inflater.inflate(R.layout.fragment_pager_item_pager, parent, false));
                break;
            case 1:
                holder = new NormalHolder(inflater.inflate(R.layout.fragment_pager_item_normal, parent, false));
                break;
            case 2:
                holder = new TextHolder(inflater.inflate(R.layout.fragment_pager_item_text, parent, false));
                break;
            default:
                break;
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        switch (getItemViewType(position)) {
            case 0:
                PagerHolder pagerHolder = (PagerHolder) holder;
                if (list != null) {
                    // 把ViewPager数据源传递给自定义ItemViewPager
                    pagerHolder.ivp_pager.updatePager(pagerList);
                }
                break;
            case 1:
                final NormalHolder normalHolder = (NormalHolder) holder;
                if (normalList != null) {
                    final NewsBean.NewslistBean normalNews = normalList.get(position - 1);
                    normalHolder.tv_normal_title.setText(normalNews.getTitle());
                    normalHolder.tv_normal_source.setText(normalNews.getDescription());
                    normalHolder.tv_normal_time.setText(normalNews.getCtime());
                    StringBuilder titles = new StringBuilder();
                    List<NewsBean.NewslistBean> newslist = DataSupport.findAll(NewsBean.NewslistBean.class);
                    for (int k = 0; k < newslist.size(); k++) {
                        titles.append(newslist.get(k).getTitle());
                    }
                    //收藏按钮图标的初始化显示
                    if (titles.toString().contains(normalNews.getTitle())) {
                        //该新闻在数据库中
                        normalHolder.ibtn_favorite.setImageResource(R.mipmap.timeline_toolbar_iv_favorite_selected);
                    } else {
                        //该新闻不在数据库中
                        normalHolder.ibtn_favorite.setImageResource(R.mipmap.timeline_toolbar_iv_favorite_normal);
                    }
                    //收藏按钮的点击事件
                    normalHolder.ibtn_favorite.setOnClickListener(new View.OnClickListener() {
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
                            if (titles.toString().contains(normalNews.getTitle())) {
                                //该新闻在数据库中,删除新闻，并改变图标
                                DataSupport.deleteAll(NewsBean.NewslistBean.class, "title = ?", normalNews.getTitle());
                                normalHolder.ibtn_favorite.setImageResource(R.mipmap.timeline_toolbar_iv_favorite_normal);
                            } else {
                                //该新闻不在数据库中，添加新闻，并改变图标
                                NewsBean.NewslistBean normalNews_ = new NewsBean.NewslistBean();

                                normalNews_.setTitle(normalNews.getTitle());
                                normalNews_.setCtime(normalNews.getCtime());
                                normalNews_.setDescription(normalNews.getDescription());
                                normalNews_.setUrl(normalNews.getUrl());
                                normalNews_.setPicUrl(normalNews.getPicUrl());
                                normalNews_.save();
                                if(normalNews_.save()) {
                                    Toast.makeText(activity, "收藏成功！", Toast.LENGTH_SHORT).show();
                                    normalHolder.ibtn_favorite.setImageResource(R.mipmap.timeline_toolbar_iv_favorite_selected);
                                }
                            }

                            Log.i("sfsf", "sqlitelistNewssssss--> " + DataSupport.findAll(NewsBean.NewslistBean.class).toString());
                        }
                    });

                    Glide.with(activity)
                            .load(normalNews.getPicUrl())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(new GlideDrawableImageViewTarget(normalHolder.iv_normal) {
                                @Override
                                public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> animation) {
                                    super.onResourceReady(resource, animation);
                                    normalHolder.pb_loading.setVisibility(View.GONE);
                                }

                                @Override
                                public void onLoadStarted(Drawable placeholder) {
                                    super.onLoadStarted(placeholder);
                                    normalHolder.pb_loading.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                    super.onLoadFailed(e, errorDrawable);
                                    normalHolder.pb_loading.setVisibility(View.GONE);
                                }
                            });

                    // 设置item点击监听，点击时跳转到对应网址的WebView
                    normalHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, WebViewActivity.class);
                            intent.putExtra("url", normalNews.getUrl());
                            activity.startActivity(intent);
                        }
                    });
                }
                break;
            case 2:
                TextHolder textHolder = (TextHolder) holder;
                if (list != null) {
                    textHolder.tv_text_title.setText(list.get(position).getTitle());
                    textHolder.tv_text_source.setText(list.get(position).getDescription());
                    textHolder.tv_text_time.setText(list.get(position).getCtime());

                    textHolder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(activity, WebViewActivity.class);
                            intent.putExtra("url", list.get(position).getUrl());
                            activity.startActivity(intent);
                        }
                    });
                }
            default:
                break;
        }
    }

    /**
     * RecyclerView中item个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        if (!flag) {
            return normalList == null ? 0 : normalList.size() + 1;
        } else {
            return list == null ? 0 : list.size();
        }
    }

    class PagerHolder extends RecyclerView.ViewHolder {

        ItemViewPager ivp_pager;

        public PagerHolder(View itemView) {
            super(itemView);
            ivp_pager = (ItemViewPager) itemView.findViewById(R.id.ivp_pager);
            ivp_pager.setLayoutParams(new FrameLayout.LayoutParams(screenW, screenH / 4));
        }
    }

    class NormalHolder extends RecyclerView.ViewHolder {

        TextView tv_normal_title, tv_normal_source, tv_normal_time;
        ImageView iv_normal;
        ImageButton ibtn_favorite;
        ProgressBar pb_loading;

        public NormalHolder(View itemView) {
            super(itemView);
            tv_normal_title = (TextView) itemView.findViewById(R.id.tv_normal_title);
            tv_normal_source = (TextView) itemView.findViewById(R.id.tv_normal_source);
            tv_normal_time = (TextView) itemView.findViewById(R.id.tv_normal_time);
            iv_normal = (ImageView) itemView.findViewById(R.id.iv_normal);
            ibtn_favorite = (ImageButton) itemView.findViewById(R.id.ibtn_favorite);
            pb_loading = (ProgressBar) itemView.findViewById(R.id.pb_loading);

            itemView.setLayoutParams(new FrameLayout.LayoutParams(screenW, screenH / 6));
        }
    }

    class TextHolder extends RecyclerView.ViewHolder {

        TextView tv_text_title, tv_text_source, tv_text_time;

        public TextHolder(View itemView) {
            super(itemView);
            tv_text_title = (TextView) itemView.findViewById(R.id.tv_text_title);
            tv_text_source = (TextView) itemView.findViewById(R.id.tv_text_source);
            tv_text_time = (TextView) itemView.findViewById(R.id.tv_text_time);
        }
    }
}
