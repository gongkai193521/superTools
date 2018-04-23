package com.viapalm.schchildpre.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.InflateException;
import android.view.MotionEvent;
import android.view.View;

import com.viapalm.schchildpre.MainApplication;
import com.viapalm.schchildpre.R;
import com.viapalm.schchildpre.util.NetWorkUtil;
import com.viapalm.schchildpre.util.ToastUtils;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


/**
 * Created by gackor on 2015/7/29.
 */
public abstract class BaseActivity extends Activity {
    private Handler handler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            View view = findViewById(R.id.linear_no_net_state);
            switch (msg.what) {
                case 100:
                    if (view != null) {
                        view.setVisibility(View.VISIBLE);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                try {
                                    Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS");
                                    wifiSettingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(wifiSettingsIntent);
                                } catch (Exception e) {

                                }
                            }
                        });
                    } else {
                        ToastUtils.showSingleToast(MainApplication.getContext(), "无法连接到网络");
                    }
                    break;
                case 101:
                    if (view != null) {
                        view.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    };
    public Activity mContext;

    /**
     * 填充布局文件,格式 return R.layout.activity_main
     */
    public abstract int setContentView();

    /**
     * 获取控件填充数据
     */
    public abstract void setupViews(Bundle savedInstanceState);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(setContentView());
        ButterKnife.bind(this);
        setupViews(savedInstanceState);
        EventBus.getDefault().register(this);
        ActivityStackManager.addActivity(this);
    }

    public <T extends View> T v(int id) {
        View view = this.findViewById(id);
        if (view == null) {
            throw new InflateException();
        }
        return (T) view;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onDestroy() {
        ActivityStackManager.removeActivity(this);
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public static void closeActivitys() {
        ActivityStackManager.closeActivitys(null);
    }

    @Subscribe(threadMode = ThreadMode.Async)
    public void onUserEvent(EventNoNet event) {
        if (this.getClass().getSimpleName().equals(ActivityStackManager.getLastBaseActvity())) {
            if (!NetWorkUtil.hasActiveInternetConnection()) {
                handler.sendEmptyMessage(100);
            } else {
                handler.sendEmptyMessage(101);
            }
        }
    }
}
