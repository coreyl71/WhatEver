package com.ash.whatever.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.ash.whatever.R;
import com.ash.whatever.activity.FavoriteActivity;
import com.ash.whatever.activity.SettingActivity;
import com.ash.whatever.activity.TulingActivity;
import com.ash.whatever.activity.WebViewActivity;
import com.ash.whatever.utils.CreateQRCodeUtils;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.zxing.encoding.EncodingHandler;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;


/**
 * Created by Corey on 2016/6/22.
 * 个人信息界面
 */
public class InfoFragment extends Fragment {

    private ImageView iv_menu_setting, iv_user_icon, iv_guest_chat, iv_qrcode;
    private TextView tv_login_notify;
    private Button btn_night, btn_textmode, btn_download, btn_attention, btn_favorite, btn_comment, btn_game, btn_feedback;
    private List<String> data;
    private String[] str_info = {"我的关注", "我的收藏", "我的评论", "游戏", "意见反馈"};
    private SimpleAdapter adapter;
    public boolean flag = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_info_layout, null);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化控件
        initView(view);
        // 初始化数据源
        initData();
        // 设置按钮的点击监听事件
        setListener();
        // 生成二维码
        createQRCode();
    }

    private void createQRCode() {
        String urlQR = "http://info.3g.qq.com/g/channel_home.htm?chId=news_h_nch#/tab/news_yaowen_tab?_k=atiqo5";
        try {
            Bitmap bitmap = CreateQRCodeUtils.createQRCode(urlQR, 400, BitmapFactory.decodeResource(getResources(), R.mipmap.android_icon));
            iv_qrcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private void setListener() {
        // 通信按钮按下后跳转聊天机器人界面
        iv_guest_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), TulingActivity.class), -1);
                getActivity().overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        // 设置按钮按下后跳转设置界面
        iv_menu_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SettingActivity.class));
                getActivity().overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        // 文字模式按钮按下后图片不加载
        btn_textmode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 接口实例调用MainActivity重写的接口中的方法
                mClickCallBack.clickCallBack();
                flag = !flag;
                if (flag) {
                    btn_textmode.setText("图片模式");
                } else {
                    btn_textmode.setText("文字模式");
                }
                // 通过发送广播传递flag
                Intent intent = new Intent("com.ash.whatever.adapter");
                intent.putExtra("flag",flag);
                getActivity().sendBroadcast(intent);
            }
        });
        // 游戏按钮按下后跳转到游戏WebView页面
        btn_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 打开WebViewActivity并传递参数
                Intent intent = new Intent(getActivity(), WebViewActivity.class);
                // 通过intent传递数据
                intent.putExtra("url", "http://wow.duowan.com/");
                startActivity(intent);
            }
        });

        //点击收藏按钮
        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), FavoriteActivity.class);
                intent.putExtra("flag",flag);
                startActivity(intent);
            }
        });
    }

    // 使用接口回调的方法将点击事件传到Fragment中
    // 定义接口
    public interface ClickCallBack {
        void clickCallBack();
    }

    // 申明接口对象
    private ClickCallBack mClickCallBack;

    // 初始化接口对象
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mClickCallBack = (ClickCallBack) context;
    }

    private void initData() {
        data = new ArrayList<String>();
        for (int i = 0; i < 5; i++) {
            data.add(str_info[i]);
        }
    }

    private void initView(View view) {
        btn_night = (Button) view.findViewById(R.id.btn_night);
        btn_textmode = (Button) view.findViewById(R.id.btn_textmode);
        btn_download = (Button) view.findViewById(R.id.btn_download);
        btn_favorite = (Button) view.findViewById(R.id.btn_favorite);
        btn_game = (Button) view.findViewById(R.id.btn_game);
        iv_menu_setting = (ImageView) view.findViewById(R.id.iv_menu_setting);
        iv_user_icon = (ImageView) view.findViewById(R.id.iv_user_icon);
        iv_guest_chat = (ImageView) view.findViewById(R.id.iv_guest_chat);
        tv_login_notify = (TextView) view.findViewById(R.id.tv_login_notify);
        iv_qrcode = (ImageView) view.findViewById(R.id.iv_qrcode);
    }

}
