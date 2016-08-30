package com.ash.whatever.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ash.whatever.R;
import com.ash.whatever.adapter.ChatAdapter;
import com.ash.whatever.bean.GetUrlMesBean;
import com.ash.whatever.bean.MessageBean;
import com.ash.whatever.utils.SendAndGetMesUtils;

import java.util.ArrayList;

/**
 * Created by Corey on 2016/6/28.
 */
public class TulingActivity extends AppCompatActivity implements SendAndGetMesUtils.MessageCallBack {

    private RecyclerView rcv_tuling_layout;
    private EditText et_message_in;
    private Button btn_confirm;
    private ChatAdapter chatAdapter = null;

    private ArrayList<MessageBean> messageList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuling);

        initView();
        setAdapter();
        setListener();


    }

    private void setAdapter() {
        chatAdapter = new ChatAdapter(this);
        rcv_tuling_layout.setLayoutManager(new LinearLayoutManager(this));
        rcv_tuling_layout.setAdapter(chatAdapter);
    }

    private void setListener() {
        // 当按下回车按钮之后，发送消息至服务器，并刷新适配器，显示在界面上
        et_message_in.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    String userSendMessage = et_message_in.getText().toString();
                    Log.i("mtag", "onClick: "+userSendMessage);
                    updateMessage(userSendMessage);
                    /**
                     * 单例模式调用SendAndGetMesUtils中的sendMessage方法
                     * 第一个参数传et中的字符串
                     * 第二个参数传接口的实例，因为当前TulingActivity实现了接口，所以传TulingActivity.this
                     */
                    SendAndGetMesUtils.getInstance().sendMessage(userSendMessage, TulingActivity.this);
                }
                // 如果点返回键，则跳转回Mainactivity的InfoFragment上显示
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    finish();
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
//                et_message_in.setText("");
                return true;
            }

        });
        // 点击btn_confirm按钮之后发送消息
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userSendMessage = et_message_in.getText().toString();
                Log.i("mtag", "onClick: "+userSendMessage);
                updateMessage(userSendMessage);
                /**
                 * 单例模式调用SendAndGetMesUtils中的sendMessage方法
                 * 第一个参数传et中的字符串
                 * 第二个参数传接口的实例，因为当前TulingActivity实现了接口，所以传TulingActivity.this
                 */
                SendAndGetMesUtils.getInstance().sendMessage(userSendMessage, TulingActivity.this);
                et_message_in.setText(" ");
            }
        });

    }

    private void initView() {
        rcv_tuling_layout = (RecyclerView) findViewById(R.id.rcv_tuling_layout);
        et_message_in = (EditText) findViewById(R.id.et_message_in);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
    }

    // 当接口调用此方法的时候，运行此方法进行更新
    @Override
    public void refreshMessage(GetUrlMesBean getUrlMesBean) {
        Log.i("", "refreshMessage: "+getUrlMesBean.getResult());
        MessageBean getBean = new MessageBean(getUrlMesBean.getResult().getText(), MessageBean.TULING);
        messageList.add(getBean);
        chatAdapter.update(messageList);
        rcv_tuling_layout.scrollToPosition(messageList.size() - 1);
    }

    // 当btn_confirm点击之后，需要发送消息至服务器，并刷新适配器，显示在界面上
    public void updateMessage(String userSendMessage) {
        MessageBean sendBean = new MessageBean(userSendMessage, MessageBean.USER);
        messageList.add(sendBean);
        chatAdapter.update(messageList);
        chatAdapter.notifyDataSetChanged();
        rcv_tuling_layout.scrollToPosition(messageList.size() - 1);
    }

}
