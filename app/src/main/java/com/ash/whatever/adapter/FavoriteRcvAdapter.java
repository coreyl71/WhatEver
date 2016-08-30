package com.ash.whatever.adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ash.whatever.R;
import com.ash.whatever.activity.WebViewActivity;
import com.ash.whatever.bean.NewsBean;
import com.squareup.picasso.Picasso;

import org.litepal.crud.DataSupport;

import java.util.List;


public class FavoriteRcvAdapter extends RecyclerView.Adapter<FavoriteRcvAdapter.RcvViewHolder> {
    private List<NewsBean.NewslistBean> newsBeans;
    private Context context;
    private boolean flag;

    public FavoriteRcvAdapter(Context context, boolean flag) {
        this.context = context;
        this.flag = flag;
        initData();
    }

    private void initData() {
        //从数据库获取数据对象集合
        readFromSql();
        //新建广播接受者，用来监听图片文字模式
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                flag = intent.getBooleanExtra("flag", flag);
            }
        };
        IntentFilter filter = new IntentFilter("android.intent.action.TEXTMODE_FLAG");
        context.registerReceiver(receiver, filter);
    }

    @Override
    public RcvViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RcvViewHolder rcvViewHolder = new RcvViewHolder(
                LayoutInflater.from(context).inflate(R.layout.favorite_rcv_item, parent, false));
        return rcvViewHolder;
    }

    @Override
    public void onBindViewHolder(RcvViewHolder holder, final int position) {
        holder.tv_title_content_recommand.setText(newsBeans.get(position).getTitle());
        holder.tv_descrip_content_recommand.setText(newsBeans.get(position).getDescription());
        holder.tv_time_conent_recommand.setText(newsBeans.get(position).getCtime());


        // 点击之后页面跳转到相应网页
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开WebViewActivity并传递参数
                Intent intent = new Intent(context, WebViewActivity.class);
                // 通过intent传递数据
                intent.putExtra("url", newsBeans.get(position).getUrl());
                context.startActivity(intent);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage("是否删除该条收藏？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int[] ids = new int[newsBeans.size()];
                                for (int i = 0; i < newsBeans.size(); i++) {
                                    ids[i] = newsBeans.get(i).getId();
                                }
                                DataSupport.delete(NewsBean.NewslistBean.class, ids[position]);
                                readFromSql();
                                context.sendBroadcast(new Intent("com.ash.whatever.FAVORITE_CHANGE"));
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                return true;
            }
        });

        if (!flag) {
            if (!"".equals(newsBeans.get(position).getPicUrl())) {
                // 通过Picasso框架下载图片并显示
                Log.i("sfsf", "onBindViewHolder:newsBeans--> " + newsBeans.get(position).toString());
                Picasso.with(context)
                        .load(newsBeans.get(position).getPicUrl())
                        .placeholder(R.mipmap.cd)
                        .resize(200, 200)
                        .centerCrop()
                        .into(holder.iv_content_recommand);
                holder.iv_content_recommand.setVisibility(View.VISIBLE);
            }
        } else {
            holder.iv_content_recommand.setVisibility(View.GONE);
        }
    }

    /**
     * 从数据库读取数据并刷新
     */
    private void readFromSql() {
        newsBeans = DataSupport.findAll(NewsBean.NewslistBean.class);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        Log.i("sfsf", "getItemCount: count-->" + newsBeans.size());
        return newsBeans == null ? 0 : newsBeans.size();
    }

    public class RcvViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title_content_recommand, tv_descrip_content_recommand, tv_time_conent_recommand;
        private ImageView iv_content_recommand;
        // 获取屏幕宽高
        private int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
        private int screenHeight = context.getResources().getDisplayMetrics().heightPixels;

        public RcvViewHolder(View itemView) {
            super(itemView);
            tv_title_content_recommand = (TextView) itemView.findViewById(R.id.tv_title_content_recommand);
            tv_descrip_content_recommand = (TextView) itemView.findViewById(R.id.tv_descrip_content_recommand);
            tv_time_conent_recommand = (TextView) itemView.findViewById(R.id.tv_time_conent_recommand);
            iv_content_recommand = (ImageView) itemView.findViewById(R.id.iv_content_recommand);

            // 设置下载图片的宽高
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(screenWidth / 4, screenHeight / 6);
//            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
            iv_content_recommand.setLayoutParams(params);
            itemView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, screenHeight / 6));
        }
    }
}
