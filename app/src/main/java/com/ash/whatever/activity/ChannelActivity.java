package com.ash.whatever.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ash.whatever.R;
import com.ash.whatever.adapter.OtherChannelAdapter;
import com.ash.whatever.adapter.UserChannelAdapter;
import com.ash.whatever.bean.ChannelBean;
import com.ash.whatever.view.DragGrid;
import com.ash.whatever.view.OtherGridView;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 频道管理页面，实现频道的增删和拖动改变顺序
 */
public class ChannelActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    // 用户栏目的GRIDVIEW
    private DragGrid userGridView;
    // 其它栏目的GRIDVIEW
    private OtherGridView otherGridView;
    // 用户栏目对应的适配器，可以拖动
    UserChannelAdapter userAdapter;
    // 其它栏目对应的适配器
    OtherChannelAdapter otherAdapter;
    // 其它栏目列表
    ArrayList<String> otherChannelList = new ArrayList<>();
    // 用户栏目列表
    ArrayList<String> userChannelList = new ArrayList<>();
    // 是否在移动，由于这边是动画结束后才进行的数据更替，设置这个限制为了避免操作太频繁造成的数据错乱。
    boolean isMove = false;
    private String[] titles = {"微信", "社会", "国内", "国际", "娱乐", "科技", "体育", "健康", "苹果", "奇闻", "旅游"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_channel);

        initView();
        initData();
        initAdapter();
    }

    private void initAdapter() {
        userAdapter = new UserChannelAdapter(this, userChannelList);
        userGridView.setAdapter(userAdapter);
        otherAdapter = new OtherChannelAdapter(this, otherChannelList);
        otherGridView.setAdapter(otherAdapter);
        //设置GRIDVIEW的ITEM的点击监听
        otherGridView.setOnItemClickListener(this);
        userGridView.setOnItemClickListener(this);
    }

    private void initData() {
        // 数据库中读取出频道
        List<ChannelBean> channelBeanList = DataSupport.findAll(ChannelBean.class);
        // 频道个数是否与总数相同
        if (channelBeanList.size() == titles.length) {
            // 数目相同，把所有频道加载到用户选择频道
            for (ChannelBean channelBean : channelBeanList) {
                userChannelList.add(channelBean.getChannel());
            }
        } else {
            // 数目小于总数，把数据库中的频道加载到用户选择频道，剩余频道加载到未选择频道
            for (int i = 0; i < titles.length; i++) {
                if (i < channelBeanList.size()) {
                    userChannelList.add(titles[i]);
                } else {
                    otherChannelList.add(titles[i]);
                }
            }
        }
    }

    private void initView() {
        userGridView = (DragGrid) findViewById(R.id.userGridView);
        otherGridView = (OtherGridView) findViewById(R.id.otherGridView);
    }

    // GRIDVIEW对应的ITEM点击监听接口
    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        //如果点击的时候，之前动画还没结束，那么就让点击事件无效
        if (isMove) {
            return;
        }
        switch (parent.getId()) {
            case R.id.userGridView: // 点击上面的GridView
                if (position != 0 && position != 1) { // 锁定上面GridView的第一项和第二项，使其不可被操作
                    final ImageView moveImageView = getView(view);
                    if (moveImageView != null) {
                        TextView newTextView = (TextView) view.findViewById(R.id.tv_channel_item);
                        final int[] startLocation = new int[2];
                        newTextView.getLocationInWindow(startLocation);
                        final String channel = ((UserChannelAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
                        otherAdapter.setVisible(false);
                        //添加到下面GridView的最后一个
                        otherAdapter.addItem(channel);
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                try {
                                    int[] endLocation = new int[2];
                                    //获取终点的坐标
                                    otherGridView.getChildAt(otherGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                    MoveAnim(moveImageView, startLocation, endLocation, channel, userGridView);
                                    //移除当前GridView被点击的item
                                    userAdapter.setRemove(position);
                                } catch (Exception localException) {
                                }
                            }
                        }, 50L);
                    }
                }
                break;
            case R.id.otherGridView: // 点击下面的GridView
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.tv_channel_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final String channel = ((OtherChannelAdapter) parent.getAdapter()).getItem(position);
                    userAdapter.setVisible(false);
                    //添加到最后一个
                    userAdapter.addItem(channel);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                userGridView.getChildAt(userGridView.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation, channel, otherGridView);
                                otherAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 点击ITEM移动动画
     *
     * @param moveView
     * @param startLocation 起始位置的坐标
     * @param endLocation   结束位置的坐标
     * @param moveChannel   被移动的频道
     * @param clickGridView 被点击的GridView
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final String moveChannel,
                          final GridView clickGridView) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        //动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.setFillAfter(false);
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                isMove = true;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // instanceof 方法判断两边实例是不是一样，判断点击的是DragGrid还是OtherGridView
                if (clickGridView instanceof DragGrid) {
                    otherAdapter.setVisible(true);
                    otherAdapter.notifyDataSetChanged();
                    userAdapter.remove();
                } else {
                    userAdapter.setVisible(true);
                    userAdapter.notifyDataSetChanged();
                    otherAdapter.remove();
                }
                isMove = false;
            }
        });
    }

    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     *
     * @param viewGroup    布局容器
     * @param view         移动的view控件
     * @param initLocation 移动的终点位置坐标
     * @return view控件
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     */
    private ViewGroup getMoveViewGroup() {
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(this);
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }

    /**
     * 获取点击的Item的对应View，
     *
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(this);
        iv.setImageBitmap(cache);
        return iv;
    }

    /**
     * 退出时候保存选择后的频道到数据库
     */
    private void saveChannel() {
        DataSupport.deleteAll(ChannelBean.class);
        for (String channel : userAdapter.getChannnelList()) {
            ChannelBean channelBean = new ChannelBean();
            channelBean.setChannel(channel);
            channelBean.save();
        }
    }

    // 重写返回键实现方法
    @Override
    public void onBackPressed() {
        closeChannel();
    }

    public void close(View view) {
        closeChannel();
    }

    private void closeChannel() {
        saveChannel();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}

