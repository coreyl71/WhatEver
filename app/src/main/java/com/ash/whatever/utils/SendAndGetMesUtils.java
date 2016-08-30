package com.ash.whatever.utils;

import com.ash.whatever.bean.GetUrlMesBean;
import com.ash.whatever.bean.NewsBean;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Corey on 2016/6/28.
 */
public class SendAndGetMesUtils {

    private static SendAndGetMesUtils sendAndGetMesUtils;

    public static SendAndGetMesUtils getInstance() {
        if(sendAndGetMesUtils == null){
            synchronized (SendAndGetMesUtils.class) {
                if (sendAndGetMesUtils == null) {
                    sendAndGetMesUtils = new SendAndGetMesUtils();
                }
            }
        }

        return sendAndGetMesUtils;
    }

    // 用于将数据发送至服务端,sendMessage即需要发送的内容
    public void sendMessage(String sendMessage, MessageCallBack callback) {
        this.callBack = callback;
        OkHttpUtils.get()
                .url(UrlUtils.getMessageUrl() + sendMessage)
                .build()
                .execute(new Callback<GetUrlMesBean>() {
                    @Override
                    public GetUrlMesBean parseNetworkResponse(Response response, int i) throws Exception {
                        GetUrlMesBean getUrlMesBean = new Gson().fromJson(response.body().string(), GetUrlMesBean.class);
                        return getUrlMesBean;
                    }

                    @Override
                    public void onError(Call call, Exception e, int i) {

                    }

                    @Override
                    public void onResponse(GetUrlMesBean getUrlMesBean, int i) {
                        callBack.refreshMessage(getUrlMesBean);
                    }
                });

    }
    // 接口回调，接收到服务端的消息之后
    public interface MessageCallBack{
        void refreshMessage(GetUrlMesBean getUrlMesBean);
    }

    private MessageCallBack callBack;


}
