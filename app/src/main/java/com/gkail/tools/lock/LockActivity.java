package com.gkail.tools.lock;


import android.app.Activity;
import android.app.KeyguardManager;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;

import com.gkail.tools.R;


/**
 * Created by gongkai on 17/2/13.
 */

public class LockActivity extends Activity implements View.OnClickListener {
    private DevicePolicyManager devicePolicyManager;
    private ComponentName componentName;
    private boolean isAdminActive;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        init();
        setListener();
    }

    private void setListener() {
        findViewById(R.id.bt_lock).setOnClickListener(this);
        findViewById(R.id.bt_remove).setOnClickListener(this);
    }

    private void init() {
        devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
        // 申请权限
        componentName = new ComponentName(this, AdminManageReceiver.class);
        // 判断该组件是否有系统管理员的权限
        isAdminActive = devicePolicyManager.isAdminActive(componentName);
        if (!isAdminActive) {
            Intent intent = new Intent();
            intent.setAction(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
            //指定给那个组件授权
            intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
            startActivity(intent);
        }
    }

    // 移除程序 如果不移除程序 APP无法被卸载
    public void remove() {
        devicePolicyManager.removeActiveAdmin(componentName);
    }

    // 设置解锁方式
    public void setUnlock() {
        // PASSWORD_QUALITY_ALPHABETIC
        // 用户输入的密码必须要有字母（或者其他字符）。
        // PASSWORD_QUALITY_ALPHANUMERIC
        // 用户输入的密码必须要有字母和数字。
        // PASSWORD_QUALITY_NUMERIC
        // 用户输入的密码必须要有数字
        // PASSWORD_QUALITY_SOMETHING
        // 由设计人员决定的。
        // PASSWORD_QUALITY_UNSPECIFIED
        // 对密码没有要求。
        Intent intent = new Intent(DevicePolicyManager.ACTION_SET_NEW_PASSWORD);
        devicePolicyManager.setPasswordQuality(componentName,
                DevicePolicyManager.PASSWORD_QUALITY_NUMERIC);
        startActivity(intent);
    }

    // 设置5秒后锁屏
    public void setLocktime() {
        devicePolicyManager.setMaximumTimeToLock(componentName, 5000);
    }

    // 恢复出厂设置
    public void setWipe() {
        devicePolicyManager.wipeData(0);
    }

    // 设置密码锁
    public void setPassword(String password) {
        devicePolicyManager.resetPassword(password,
                DevicePolicyManager.RESET_PASSWORD_REQUIRE_ENTRY);
        devicePolicyManager.lockNow();
    }

    private void unlock() {
        //获取电源管理器对象
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
        //点亮屏幕
        wl.acquire();
        //得到键盘锁管理器对象
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
        //解锁
        kl.disableKeyguard();
        //锁屏
        kl.reenableKeyguard();
        //释放wakeLock，关灯
        wl.release();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_lock:
                setPassword("1234");
                break;
            case R.id.bt_remove:
                remove();
                break;
            default:
                break;
        }
    }
}
