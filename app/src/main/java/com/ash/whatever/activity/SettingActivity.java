package com.ash.whatever.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ash.whatever.R;
import com.ash.whatever.utils.DataCleanUtils;

/**
 * Created by Corey on 2016/6/27.
 */
public class SettingActivity extends AppCompatActivity{

    private TextView tv_brightness;
    private SeekBar sb_brightness;
    private Button btn_clean;
    private String totalCacheSize;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_setting);

        initView();
        screenBrightness_check();
        setListener();
    }

    private void setListener() {
        // 按下清除缓存按钮清除缓存
        btn_clean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    totalCacheSize = DataCleanUtils.getTotalCacheSize(getApplicationContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(), "删除了 " + totalCacheSize + "缓存", Toast.LENGTH_LONG).show();
                DataCleanUtils.clearAllCache(getApplicationContext());
            }
        });
        // 滚动滑动条调节屏幕亮度
        sb_brightness.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            // 滑动块滑动的时候调用此方法
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setScreenBritness(progress);
            }
            // 接触滑动块的时候执行此方法
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            // 手指离开滑动块执行此方法
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initView() {
        sb_brightness = (SeekBar) findViewById(R.id.sb_brightness);
        tv_brightness = (TextView) findViewById(R.id.tv_brightness);
        btn_clean = (Button) findViewById(R.id.btn_clean);
    }

    private void screenBrightness_check() {
        //先关闭系统的亮度自动调节
        try {
            if(Settings.System.getInt(getContentResolver(), Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC) {
                Settings.System.putInt(getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS_MODE,
                        Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        //获取当前亮度,获取失败则返回255
        int screenBrightness=(int)(Settings.System.getInt(getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS,
                255));
        //文本、进度条显示
        sb_brightness.setProgress(screenBrightness);

    }

    //屏幕亮度
    private void setScreenBritness(int brightness)
    {
        //不让屏幕全暗
        if(brightness<=1)
        {
            brightness=1;
        }
        //设置当前activity的屏幕亮度
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        //0到1,调整亮度暗到全亮
        lp.screenBrightness = Float.valueOf(brightness/255f);
        this.getWindow().setAttributes(lp);

        //保存为系统亮度方法1
        Settings.System.putInt(getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS,
                brightness);

        //保存为系统亮度方法2
//        Uri uri = android.provider.Settings.System.getUriFor("screen_brightness");
//        android.provider.Settings.System.putInt(getContentResolver(), "screen_brightness", brightness);
//        // resolver.registerContentObserver(uri, true, myContentObserver);
//        getContentResolver().notifyChange(uri, null);

        //更改亮度文本显示
        //mTextView_light.setText(""+brightness*100/255);
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
