package com.gkail.tools;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.gkail.tools.base.BaseActivity;
import com.gkail.tools.call.CallActivity;
import com.gkail.tools.customview.ViewActivity;
import com.gkail.tools.led.LEDActivity;
import com.gkail.tools.lock.LockActivity;
import com.gkail.tools.permission.PermissionManager;
import com.gkail.tools.sms.SMSActivatity;
import com.gkail.tools.util.SoundUtils;
import com.gkail.tools.wallpaper.ui.SetWallpaperActivity;
import com.gkail.tools.wifi.NetWorkActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.btn_sms)
    Button sms;
    @BindView(R.id.btn_lock)
    Button lock;
    @BindView(R.id.btn_led)
    Button led;
    @BindView(R.id.btn_customView)
    Button custom_view;
    @BindView(R.id.btn_permission)
    Button permission;
    @BindView(R.id.btn_call)
    Button call;
    @BindView(R.id.btn_accessibility)
    Button accessibility;
    @BindView(R.id.btn_setwallpaper)
    Button setwallpaper;
    @BindView(R.id.btn_silentModel)
    Button silentModel;
    @BindView(R.id.btn_normalModel)
    Button normalModel;
    @BindView(R.id.btn_network)
    Button network;
    @BindView(R.id.viewStub)
    ViewStub mViewStub;
    @BindView(R.id.cb_wx)
    CheckBox cb_wx;

    @Override
    public int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void setupViews(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        if (Constant.wx_checkd) {
            cb_wx.setChecked(true);
        } else {
            cb_wx.setChecked(false);
        }
        cb_wx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Constant.wx_checkd = true;
                } else {
                    Constant.wx_checkd = false;
                }
            }
        });
    }


    private PermissionManager mPermissionManager;

    @OnClick({R.id.btn, R.id.btn_sms, R.id.btn_lock, R.id.btn_led, R.id.btn_customView,
            R.id.btn_permission, R.id.btn_call, R.id.btn_accessibility, R.id.btn_setwallpaper,
            R.id.btn_silentModel, R.id.btn_normalModel, R.id.btn_network})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn:
                mViewStub = (ViewStub) findViewById(R.id.viewStub);
                if (mViewStub != null) {
                    mViewStub.inflate();
                }
                break;
            case R.id.btn_sms:
                startActivity(new Intent(this, SMSActivatity.class));
                break;
            case R.id.btn_lock:
                startActivity(new Intent(this, LockActivity.class));
                break;
            case R.id.btn_led:
                startActivity(new Intent(this, LEDActivity.class));
                break;
            case R.id.btn_customView:
                startActivity(new Intent(this, ViewActivity.class));
                break;
            case R.id.btn_permission:
                if (mPermissionManager == null) {
                    mPermissionManager = new PermissionManager(this);
                }
                mPermissionManager.jumpPermissionPage();
                break;
            case R.id.btn_accessibility:
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;
            case R.id.btn_call:
                startActivity(new Intent(this, CallActivity.class));
                break;
            case R.id.btn_setwallpaper:
                startActivity(new Intent(this, SetWallpaperActivity.class));
                break;
            case R.id.btn_silentModel:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (SoundUtils.checkNotiPermission()) {
                        SoundUtils.setSilentModel();
                    }
                }
                break;
            case R.id.btn_normalModel:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (SoundUtils.checkNotiPermission()) {
                        SoundUtils.setNormalModel();
                    }
                }
                break;
            case R.id.btn_network:
                startActivity(new Intent(this, NetWorkActivity.class));
                break;
        }
    }
}