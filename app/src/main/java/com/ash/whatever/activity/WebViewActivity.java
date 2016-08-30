package com.ash.whatever.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.ash.whatever.R;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;


/**
 * Created by Corey on 2016/6/22.
 */
public class WebViewActivity extends AppCompatActivity {

    private ImageView iv_back, iv_share;
    private TextView tv_webUrl;
    private WebView mWebView;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_webview);

        // 初始化WebView控件
        initView();
        initWebView();
        initListener();
    }

    private void initListener() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareSDK.initSDK(WebViewActivity.this);
                OnekeyShare oks = new OnekeyShare();
                //关闭sso授权
                oks.disableSSOWhenAuthorize();
                oks.setTitleUrl(url);
                oks.setUrl(url);
                oks.setSiteUrl(url);
                // 启动分享GUI
                oks.show(WebViewActivity.this);
            }
        });
    }

    private void initWebView() {
        // 获取intent中的数据
        Intent intent = getIntent();
        url = intent.getStringExtra("url");
        tv_webUrl.setText(url);
        mWebView.loadUrl(url);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                mWebView.loadUrl(url);
                return true;
            }
        });
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_share = (ImageView) findViewById(R.id.iv_share);
        tv_webUrl = (TextView) findViewById(R.id.tv_webUrl);
        mWebView = (WebView) findViewById(R.id.webview);
    }
}
