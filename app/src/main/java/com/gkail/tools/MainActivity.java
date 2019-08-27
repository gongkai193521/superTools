package com.gkail.tools;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.gkail.tools.activity.TestActivity;
import com.gkail.tools.base.BaseActivity;
import com.gkail.tools.call.CallActivity;
import com.gkail.tools.customview.ViewActivity;
import com.gkail.tools.device.DeviceInfoActivity;
import com.gkail.tools.floatwindow.FloatWindowManager;
import com.gkail.tools.led.LEDActivity;
import com.gkail.tools.lock.LockActivity;
import com.gkail.tools.permission.PermissionManager;
import com.gkail.tools.recyclerview.RecyclerViewActivity;
import com.gkail.tools.sensor.SensorActivity;
import com.gkail.tools.service.ScreenService;
import com.gkail.tools.sms.SMSActivatity;
import com.gkail.tools.util.ScreenBrightnessUtils;
import com.gkail.tools.util.SoundUtils;
import com.gkail.tools.wallpaper.ui.SetWallpaperActivity;
import com.gkail.tools.wifi.NetWorkActivity;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
    @BindView(R.id.btn_test)
    Button stackView;
    @BindView(R.id.btn_capture)
    Button capture;
    @BindView(R.id.viewStub)
    ViewStub mViewStub;
    @BindView(R.id.cb_wx)
    CheckBox cb_wx;
    @BindView(R.id.cb1)
    CheckBox cb1;
    @BindView(R.id.btn_recyclerView)
    Button btn_recyclerView;
    @BindView(R.id.btn_deviceinfo)
    Button btn_deviceinfo;

    @Override
    public int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void setupViews(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        mMediaProjectionManager = (MediaProjectionManager) getApplication().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (Constant.wx_checkd) {
            cb_wx.setChecked(true);
        } else {
            cb_wx.setChecked(false);
        }
        cb_wx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Constant.wx_checkd = isChecked;
            }
        });
        cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ScreenBrightnessUtils.autoBrightness(mContext, isChecked);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
//        ContentResolver resolver = getContentResolver();
//        ContentValues values = new ContentValues();
//        values.put("_id", 4);
//        values.put("name", "王麻子");
//        resolver.insert(Uri.parse("content://com.test.MyProvider/user"), values);

        Intent intent = new Intent("com.viapalm.kidcares.study.finishtask");
        intent.putExtra("from", "com.test");
        intent.putExtra("type", 0);
        intent.setComponent(new ComponentName("com.viapalm.educhildeluhuhang",
                "com.kidcares.educontrol.receiver.StudyTaskReceiver"));
        if (Build.VERSION.SDK_INT >= 26) {
            //加上这句话，可以解决在android8.0系统以上2个module之间发送广播接收不到的问题
            intent.addFlags(0x01000000);
        }
        mContext.sendBroadcast(intent);//int 1表示已接收到指令
    }

    private PermissionManager mPermissionManager;

    @OnClick({R.id.btn, R.id.btn_sms, R.id.btn_lock, R.id.btn_led, R.id.btn_customView,
            R.id.btn_permission, R.id.btn_call, R.id.btn_accessibility, R.id.btn_setwallpaper,
            R.id.btn_silentModel, R.id.btn_normalModel, R.id.btn_network, R.id.btn_test,
            R.id.btn_capture, R.id.btn_recyclerView, R.id.btn_deviceinfo})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn:
                mViewStub = findViewById(R.id.viewStub);
                if (mViewStub != null) {
                    mViewStub.inflate();
                }
                startActivity(new Intent(mContext, SensorActivity.class));
//                startActivity(new Intent(mContext, RxjavaTestActivity.class));
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
                startActivity(new Intent(mContext, NetWorkActivity.class));
                break;
            case R.id.btn_test:
                startActivity(new Intent(mContext, TestActivity.class));
                break;
            case R.id.btn_capture:
//                startIntent();
                FloatWindowManager.showFloatView();
                break;
            case R.id.btn_recyclerView:
                startActivity(new Intent(mContext, RecyclerViewActivity.class));
                break;
            case R.id.btn_deviceinfo:
                startActivity(new Intent(mContext, DeviceInfoActivity.class));
                break;
        }
    }

    private int result = 0;
    private Intent intent = null;
    private int REQUEST_MEDIA_PROJECTION = 1;
    private MediaProjectionManager mMediaProjectionManager;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void startIntent() {
        if (intent != null && result != 0) {
            ((MainApplication) getApplication()).setResultCode(result);
            ((MainApplication) getApplication()).setIntent(intent);
            Intent intent = new Intent(getApplicationContext(), ScreenService.class);
            startService(intent);
        } else {
            startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
            ((MainApplication) getApplication()).setmMediaProjectionManager(mMediaProjectionManager);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode != Activity.RESULT_OK) {
                return;
            } else if (data != null && resultCode != 0) {
                result = resultCode;
                intent = data;
                ((MainApplication) getApplication()).setResultCode(resultCode);
                ((MainApplication) getApplication()).setIntent(data);
                Intent intent = new Intent(getApplicationContext(), ScreenService.class);
                startService(intent);
            }
        }
    }
}
