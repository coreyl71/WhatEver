package com.ash.whatever.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ash.whatever.R;
import com.ash.whatever.fragment.InfoFragment;
import com.ash.whatever.fragment.NewsFragment;
import com.ash.whatever.fragment.RecommandFragment;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements InfoFragment.ClickCallBack{

    private TabLayout mTablayout;
    private ArrayList<Fragment> list = new ArrayList<>();
    private FrameLayout mFrameLayout;
    private FragmentManager manager;
    private RecommandFragment recommandFragment;
    private InfoFragment infoFragment;
    private NewsFragment newsFragment;
    private int mBackKeyPressedTimes = 0;
    private static final String SHARE_APP_TAG = "mtag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // 生成快捷方式
        setIcon();
        // 找控件
        initView();
        // 将Fragment添加到list集合中，每次添加完Fragment之后对Tablayout按钮进行初始化
        initTabs();
        // 通过manager将Fragment显示到MainActivity中
        initFragment();

    }

    private void setIcon() {
        SharedPreferences setting = getSharedPreferences(SHARE_APP_TAG, 0);
        Boolean user_first = setting.getBoolean("FIRST", true);
        if (user_first) {
            setting.edit().putBoolean("FIRST", false).commit();
            new AlertDialog.Builder(this).setTitle("是否创建快捷方式").setNegativeButton("取消", null).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    initDesktopIcon();
                }
            }).show();
        }
    }

    private void initFragment() {
        // 获取FragmentManager对象
        manager = getSupportFragmentManager();
        // 将四个Fragment添加到页面，但是重叠显示，依次覆盖
        for (int i = 0; i < list.size(); i++) {
            manager.beginTransaction().add(R.id.layout_recommand, list.get(i)).commit();
        }
        // 显示页面的初始化，第一个页面显示，其余页面隐藏
        for (int i = 0; i < list.size(); i++) {
            if(i == 0) {
                manager.beginTransaction().show(list.get(i)).commit();
            } else {
                manager.beginTransaction().hide(list.get(i)).commit();
            }
        }

    }

    private void initTabs() {
        // 定义tablayout对象
        TabLayout.Tab tab_news = mTablayout.newTab().setCustomView(R.layout.tab_custom_view_news).setTag(0);
        TabLayout.Tab tab_recommand = mTablayout.newTab().setCustomView(R.layout.tab_custom_view_recomm).setTag(1);
        TabLayout.Tab tab_info = mTablayout.newTab().setCustomView(R.layout.tab_custom_view_me).setTag(2);
        // 实例化Fragment对象
        recommandFragment = new RecommandFragment();
        infoFragment = new InfoFragment();
        newsFragment = new NewsFragment();
        // 将Fragment对象存入list中，作为数据源
        list.add(newsFragment);
        list.add(recommandFragment);
        list.add(infoFragment);
        // 将tab对象存入tablayout对象中
        mTablayout.addTab(tab_news);
        mTablayout.addTab(tab_recommand);
        mTablayout.addTab(tab_info);
        // 设置tablayout的选择监听，选中后显示相应的Fragment对象
        mTablayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tag = Integer.parseInt(tab.getTag().toString());
                for (int i = 0; i < list.size(); i++) {
                    if(tag == i) {
                        // 相应Fragment显示
                        manager.beginTransaction().show(list.get(i)).commit();
                    } else {
                        // 其余Fragment隐藏
                        manager.beginTransaction().hide(list.get(i)).commit();
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initView() {
        mTablayout = (TabLayout) findViewById(R.id.tablayout);
        mFrameLayout = (FrameLayout) findViewById(R.id.layout_recommand);
    }

    // 重写InfoFragment定义的接口中的方法，将点击事件传过来
    @Override
    public void clickCallBack() {
        recommandFragment.updataFlag();
    }

    // 点击两次返回键退出程序
    @Override
    public void onBackPressed() {
        if (mBackKeyPressedTimes == 0) {
            Toast.makeText(this, "再按一次退出程序 ", Toast.LENGTH_SHORT).show();
            mBackKeyPressedTimes = 1;
            new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        mBackKeyPressedTimes = 0;
                    }
                }
            }.start();
            return;
        } else{
                this.finish();
            }
        }

    public void initDesktopIcon() {
        // 创建添加快捷方式的Intent
        Intent addIntent = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        // 加载快捷方式的图标
        Parcelable icon = Intent.ShortcutIconResource.fromContext(this,R.mipmap.ash);
        // 创建点击快捷方式后对应的Intent,该处指定当点击指定的快捷方式后，将再次启动该程序
        Intent myIntent = new Intent(this,FirstActivity.class);
        // 设置快捷方式的标题
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,"ash");
        // 设置快捷方式的图标
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,icon);
        // 设置快捷方式对应的Intent
        addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,myIntent);
        sendBroadcast(addIntent);
        Toast.makeText(MainActivity.this, "已生成快捷方式", Toast.LENGTH_SHORT).show();
    }
}

