package com.gkail.tools.base;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.InflateException;
import android.view.MotionEvent;
import android.view.View;

import com.gkail.tools.MainApplication;
import com.gkail.tools.R;
import com.gkail.tools.util.NetWorkUtil;
import com.gkail.tools.util.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import de.greenrobot.event.Subscribe;
import de.greenrobot.event.ThreadMode;


/**
 * Created by gackor on 2015/7/29.
 */
public abstract class BaseActivity extends Activity {
    private Map<Integer, Runnable> allowablePermissionRunnables = new HashMap<>();
    private Map<Integer, Runnable> disallowablePermissionRunnables = new HashMap<>();
    private Handler handler = new Handler(Looper.getMainLooper()) {
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

    /**
     * 请求权限
     *
     * @param id                   请求授权的id 唯一标识即可
     * @param permission           请求的权限
     * @param allowableRunnable    同意授权后的操作
     * @param disallowableRunnable 禁止权限后的操作
     */
    protected void requestPermission(int id, String permission, Runnable allowableRunnable, Runnable disallowableRunnable) {
        if (allowableRunnable == null) {
            throw new IllegalArgumentException("allowableRunnable == null");
        }
        allowablePermissionRunnables.put(id, allowableRunnable);
        if (disallowableRunnable != null) {
            disallowablePermissionRunnables.put(id, disallowableRunnable);
        }
        //版本判断
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //检查是否拥有权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(getApplicationContext(), permission);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //弹出对话框接收权限
                ActivityCompat.requestPermissions(this, new String[]{permission}, id);
                return;
            } else {
                allowableRunnable.run();
            }
        } else {
            allowableRunnable.run();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Runnable allowRun = allowablePermissionRunnables.get(requestCode);
            allowRun.run();
        } else {
            Runnable disallowRun = disallowablePermissionRunnables.get(requestCode);
            disallowRun.run();
        }
    }
}
