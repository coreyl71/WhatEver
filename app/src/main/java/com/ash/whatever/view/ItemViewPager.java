package com.ash.whatever.view;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ash.whatever.R;
import com.ash.whatever.activity.WebViewActivity;
import com.ash.whatever.adapter.ItemViewPagerAdapter;
import com.ash.whatever.bean.NewsBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;


/**
 * 自定义View，方便在RecyclerView中使用
 * Created by liujiang on 2016/6/22.
 */
public class ItemViewPager extends RelativeLayout {

    private Context context;
    private ViewPager vp_pager;
    private TextView tv_pager_title, tv_pager_sourse, tv_pager_time;
    private RadioGroup rg_pager;
    private ItemViewPagerAdapter adapter;
    private List<NewsBean.NewslistBean> pagerList;
    private List<ImageView> imageList;
    private Handler handler = new Handler();
    private Runnable r;
    private boolean isLoad;
    private boolean flag;

    public ItemViewPager(Context context) {
        super(context);
        init(context);
    }

    public ItemViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        View view = LayoutInflater.from(context).inflate(R.layout.pager_item_viewpager, this, true);
        imageList = new ArrayList<>();
        initView(view);
        initAdapter();
        initListener();
    }

    private void initListener() {

        // 设置ViewPager监听，ViewPager页面跳转时选择相应的RadioButton和新闻数据
        vp_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                rg_pager.check(position);
                tv_pager_title.setText(pagerList.get(position).getTitle());
                tv_pager_sourse.setText(pagerList.get(position).getDescription());
                tv_pager_time.setText(pagerList.get(position).getCtime());
                imageList.get(position).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, WebViewActivity.class);
                        intent.putExtra("url", pagerList.get(position).getUrl());
                        context.startActivity(intent);
                    }
                });
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                switch (state) {
                    case ViewPager.SCROLL_STATE_DRAGGING:
                        flag = true;
                        handler.removeCallbacks(r);
                        break;
                    case ViewPager.SCROLL_STATE_IDLE:
                        if (flag) {
                            flag = false;
                            handler.postDelayed(r, 2000);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initAdapter() {
        adapter = new ItemViewPagerAdapter();
        vp_pager.setAdapter(adapter);
    }

    // 接收NewsRecyclerAdapter传递的数据源
    public void updatePager(List<NewsBean.NewslistBean> pagerList) {
        this.pagerList = pagerList;
        if (pagerList != null) {
            for (int i = 0; i < pagerList.size(); i++) {
                tv_pager_title.setText(pagerList.get(i).getTitle());
                tv_pager_sourse.setText(pagerList.get(i).getDescription());
                tv_pager_time.setText(pagerList.get(i).getCtime());
                ImageView iv = new ImageView(context);
                iv.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                iv.setScaleType(ImageView.ScaleType.FIT_XY);

                Glide.with(context)
                        .load(pagerList.get(i).getPicUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(iv);
                imageList.add(iv);
            }
            adapter.updateImageList(imageList);
            adapter.notifyDataSetChanged();
            initHandler();
            initRadioButton();
        }
    }

    private void initHandler() {
        if (!isLoad) { //防止重新进行加载时创建新线程
            r = new Runnable() {
                @Override
                public void run() {
                    int position = vp_pager.getCurrentItem() + 1;
                    if (pagerList != null && position == pagerList.size()) {
                        position = 0;
                    }
                    vp_pager.setCurrentItem(position);
                    handler.postDelayed(this, 2000);
                }
            };
            handler.postDelayed(r, 2000);
        }
    }

    private void initRadioButton() {
        if (!isLoad) { //防止重新进行加载时创建新RadioButton
            rg_pager.removeAllViews();
            for (int i = 0; i < imageList.size(); i++) {
                RadioButton rbtn = new RadioButton(context);
                rbtn.setId(i);
                rbtn.setButtonDrawable(R.drawable.ad_video_controller_point);
                rbtn.setScaleX(0.5f);
                rbtn.setScaleY(0.5f);
                RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT, RadioGroup.LayoutParams.WRAP_CONTENT);
                params.leftMargin = 0;
                rbtn.setLayoutParams(params);
                rg_pager.addView(rbtn);
            }
            isLoad = true;
            rg_pager.check(0);
        }
    }

    private void initView(View view) {
        vp_pager = (ViewPager) view.findViewById(R.id.vp_pager);
        tv_pager_title = (TextView) view.findViewById(R.id.tv_pager_title);
        tv_pager_sourse = (TextView) view.findViewById(R.id.tv_pager_sourse);
        tv_pager_time = (TextView) view.findViewById(R.id.tv_pager_time);
        rg_pager = (RadioGroup) view.findViewById(R.id.rg_pager);
    }
}
