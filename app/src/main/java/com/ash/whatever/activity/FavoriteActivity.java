package com.ash.whatever.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Window;

import com.ash.whatever.R;
import com.ash.whatever.adapter.FavoriteRcvAdapter;
import com.jcodecraeer.xrecyclerview.XRecyclerView;


public class FavoriteActivity extends AppCompatActivity {
    private XRecyclerView xrc_favorite;
    private boolean flag;

    private void assignViews() {
        xrc_favorite = (XRecyclerView) findViewById(R.id.xrc_favorite);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_favorite);
        assignViews();

        xrc_favorite.setLayoutManager(new LinearLayoutManager(this));
        flag = getIntent().getBooleanExtra("flag", flag);
        FavoriteRcvAdapter adapter = new FavoriteRcvAdapter(this, flag);
        xrc_favorite.setPullRefreshEnabled(false);
        xrc_favorite.setLoadingMoreEnabled(false);
        xrc_favorite.setAdapter(adapter);
    }
}
