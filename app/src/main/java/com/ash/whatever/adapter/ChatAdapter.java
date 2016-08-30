package com.ash.whatever.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ash.whatever.R;
import com.ash.whatever.bean.MessageBean;

import java.util.List;

/**
 * Created by Corey on 2016/6/28.
 * 图灵机器人适配器类
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<MessageBean> messageBeanList;
    private static final int USER = 1;
    private static final int TULING = 0;
    private LayoutInflater inflater;
    private RecyclerView recyclerView;
    private Context mContext;

    public ChatAdapter(Context context) {
        super();

        mContext = context;
        Log.i("mtag", "onBindViewHolder: 是否运行到这里**1");
    }

    public void update(List<MessageBean> list) {
        messageBeanList = list;
        notifyDataSetChanged();
        Log.i("mtag", "update: " +messageBeanList.toString()+messageBeanList.size());
    }

    @Override
    public int getItemViewType(int position) {
        Log.i("mtag", "onBindViewHolder: 是否运行到这里**2");
        MessageBean messageBean = messageBeanList.get(position);
        if (messageBean.getFlag() == MessageBean.USER) {
            // 通过list中的bean对象，得到的类型，其中类型为USER则代表是用户发出的信息
            return MessageBean.USER;
        } else {
            // 其中类型为TULING则代表是机器人发出的信息
            return MessageBean.TULING;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i("mtag", "onBindViewHolder: 是否运行到这里**3");
        switch (viewType) {
            // 如果是用户发出的信息，则填充用户相应的布局
            case MessageBean.USER:
                return new MyHolderUser(LayoutInflater.from(mContext).inflate(R.layout.activity_tuling_item_second,parent,false));
            // 如果是图灵机器人发出的信息，则填充机器人相应的布局
            case MessageBean.TULING:
                return new MyHolderTuling(LayoutInflater.from(mContext).inflate(R.layout.activity_tuling_item_first,parent,false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.i("mtag", "onBindViewHolder: 是否运行到这里**4");
        Log.i("mtag", "onBindViewHolder: 是否运行到这里1");
        MessageBean messageBean = messageBeanList.get(position);
        int flag = messageBean.getFlag();
        Log.i("mtag", "onBindViewHolder: 是否运行到这里2"+flag);
        if (flag == MessageBean.USER) {
            // 如果标记为用户，则将消息放在用户的布局中
            showUser(holder, messageBean);
        } else {
            // 如果标记为机器人，则将消息放在机器人的布局中
            showTuling(holder, messageBean);
        }

    }

    @Override
    public int getItemCount() {
        Log.i("mtag", "onBindViewHolder: 是否运行到这里**6"+messageBeanList);
//     Log.i("mtag", "getItemCount: 当前适配器中ITEM的数量" +messageBeanList.size());
        return messageBeanList == null ? 0 : messageBeanList.size();

    }
    private void showTuling(RecyclerView.ViewHolder holder, MessageBean messageBean) {Log.i("mtag", "onBindViewHolder: 是否运行到这里**5");
        MyHolderTuling tulingHolder = (MyHolderTuling) holder;
        String tulingMessage = messageBean.getMessage();
        tulingHolder.tv_message_tuling.setText(tulingMessage);
        tulingHolder.iv_icon_tuling.setImageResource(R.mipmap.tuling);
    }

    private void showUser(RecyclerView.ViewHolder holder, MessageBean messageBean) {
        Log.i("mtag", "onBindViewHolder: 是否运行到这里**5");
        MyHolderUser userHolder = (MyHolderUser) holder;
        String userMessage = messageBean.getMessage();
        Log.i("mtag", "showUser: 是否传会数据"+userMessage);
        userHolder.tv_message_user.setText(userMessage);
        userHolder.iv_icon_user.setImageResource(R.mipmap.user);
    }
    class MyHolderTuling extends RecyclerView.ViewHolder{
        private ImageView iv_icon_tuling;
        private TextView tv_message_tuling;
        private RelativeLayout layout;
        public MyHolderTuling(View itemView) {
            super(itemView);

            Log.i("mtag", "onBindViewHolder: 是否运行到这里**7");
            iv_icon_tuling = (ImageView) itemView.findViewById(R.id.iv_icon_tuling);
            tv_message_tuling = (TextView) itemView.findViewById(R.id.tv_message_tuling);
            layout = (RelativeLayout) itemView.findViewById(R.id.activity_tuling_item_first);
        }
    }
    class MyHolderUser extends RecyclerView.ViewHolder{
        private ImageView iv_icon_user;
        private TextView tv_message_user;
        private RelativeLayout layout;
        public MyHolderUser(View itemView) {
            super(itemView);
            Log.i("mtag", "onBindViewHolder: 是否运行到这里**8");
            iv_icon_user = (ImageView) itemView.findViewById(R.id.iv_icon_user);
            tv_message_user = (TextView) itemView.findViewById(R.id.tv_message_user);
            layout = (RelativeLayout) itemView.findViewById(R.id.activity_tuling_item_second);
        }
    }

}
