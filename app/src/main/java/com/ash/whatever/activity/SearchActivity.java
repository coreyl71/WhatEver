package com.ash.whatever.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ash.whatever.R;
import com.ash.whatever.adapter.SearchListViewItemAdapater;
import com.ash.whatever.bean.NewsBean;
import com.ash.whatever.utils.ChannelUtils;
import com.ash.whatever.utils.UrlUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

public class SearchActivity extends AppCompatActivity {

    private EditText et_search;
    private ImageView iv_search;
    private TextView tv_search;
    private Intent intent;
    private String channel;
    private ListView lv_search;




    private List<NewsBean.NewslistBean> newslistBean;
    private SearchListViewItemAdapater mAdataper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_search);

        initView();
        initListener();
        downloadData(null);
    }



    /**
     * 根据关键字下载数据
     *
     * @param keyword 关键字
     */
    private void downloadData(final String keyword) {
        OkHttpUtils.get()
                .url(UrlUtils.getUrl(channel, 1, 20, 1, keyword))
                .build()
                .execute(new Callback<NewsBean>(){

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
                        if(newsBean != null){
                            newslistBean = newsBean.getNewslist();
                            if(mAdataper == null){
                                mAdataper = new SearchListViewItemAdapater(SearchActivity.this,newslistBean,keyword);
                                lv_search.setAdapter(mAdataper);
                            }else{
                                mAdataper.updateData(newslistBean,keyword);
                                mAdataper.notifyDataSetChanged();
                            }
                        }else{
                            Toast.makeText(SearchActivity.this, "没有相关新闻！", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void initListener() {
        lv_search.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SearchActivity.this, WebViewActivity.class);
                // 通过intent传递数据
                intent.putExtra("url", newslistBean.get(position).getUrl());
                startActivity(intent);
            }
        });

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (et_search.getText().length() > 0) {
                    iv_search.setVisibility(View.VISIBLE);
                    tv_search.setText("搜索");
                } else {
                    iv_search.setVisibility(View.GONE);
                    tv_search.setText("取消");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void click(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                if ("取消".equals(tv_search.getText())) {
                    finish();
                } else {
                    downloadData(et_search.getText().toString());
                }
                break;
            case R.id.iv_search:
                et_search.setText("");
                break;
            default:
                break;
        }
    }


    private void initView() {
        et_search = (EditText) findViewById(R.id.et_search);
        iv_search = (ImageView) findViewById(R.id.iv_search);
        tv_search = (TextView) findViewById(R.id.tv_search);
        lv_search = (ListView) findViewById(R.id.lv_search);
        //输入框提示信息改为"搜索+频道+相关新闻"
        intent = getIntent();
        channel = intent.getStringExtra("channel");
        et_search.setHint("搜索" + ChannelUtils.getChannelWord(channel) + "相关新闻");
    }
}
