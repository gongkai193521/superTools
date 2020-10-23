package com.gkail.tools;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.CallLog;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;
import android.widget.CompoundButton;

import com.gkail.tools.activity.TestActivity;
import com.gkail.tools.base.BaseActivity;
import com.gkail.tools.call.BadgeIntentService;
import com.gkail.tools.call.CallActivity;
import com.gkail.tools.call.CallLogObserver;
import com.gkail.tools.customview.ViewActivity;
import com.gkail.tools.databinding.ActivityMainBinding;
import com.gkail.tools.device.DeviceInfoActivity;
import com.gkail.tools.floatwindow.FloatWindowManager;
import com.gkail.tools.funnelchart.FunneActivity;
import com.gkail.tools.led.LEDActivity;
import com.gkail.tools.lock.LockActivity;
import com.gkail.tools.permission.PermissionManager;
import com.gkail.tools.recyclerview.RecyclerViewActivity;
import com.gkail.tools.sensor.SensorActivity;
import com.gkail.tools.service.ScreenService;
import com.gkail.tools.sms.SMSActivatity;
import com.gkail.tools.util.ScreenBrightnessUtils;
import com.gkail.tools.util.SoundUtils;
import com.gkail.tools.util.ToastUtils;
import com.gkail.tools.wallpaper.ui.SetWallpaperActivity;
import com.gkail.tools.wifi.NetWorkActivity;

import io.reactivex.functions.Consumer;

public class MainActivity extends BaseActivity {
    private ActivityMainBinding binding;

    @Override
    public int setContentView() {
        return R.layout.activity_main;
    }

    @Override
    public void setupViews(Bundle savedInstanceState) {
        initView();
    }

    private void initView() {
        binding = DataBindingUtil.setContentView(mContext, R.layout.activity_main);
        mMediaProjectionManager = (MediaProjectionManager) getApplication().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        if (Constant.wx_checkd) {
            binding.cbWx.setChecked(true);
        } else {
            binding.cbWx.setChecked(false);
        }
        binding.cbWx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Constant.wx_checkd = isChecked;
            }
        });
        binding.cb1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                ScreenBrightnessUtils.autoBrightness(mContext, isChecked);
            }
        });
        rxPermissions.request(Manifest.permission.READ_CALL_LOG, Manifest.permission.READ_PHONE_STATE).subscribe(new Consumer<Boolean>() {
            @Override
            public void accept(Boolean aBoolean) {
                if (aBoolean) {
                    getContentResolver().registerContentObserver(CallLog.Calls.CONTENT_URI,
                            true, new CallLogObserver(new Handler()));
                } else {
                    ToastUtils.showLong(mContext, "请授予读取通话记录权限");
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("----", "getPhoneIMEI == " + getPhoneIMEI());
        Log.d("----", "----" + Build.BRAND);
//        ContentResolver resolver = getContentResolver();
//        ContentValues values = new ContentValues();
//        values.put("_id", 4);
//        values.put("name", "王麻子");
//        resolver.insert(Uri.parse("content://com.test.MyProvider/user"), values);
    }

    /**
     * 获取手机的imei码
     *
     * @return
     */
    private String getPhoneIMEI() {
        String imei = "";
        TelephonyManager tm = (TelephonyManager) mContext.getSystemService(TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                == PackageManager.PERMISSION_GRANTED) {
            imei = tm.getDeviceId();
        }
        return imei;
    }

    private PermissionManager mPermissionManager;

    public void btn(View view) {
        ViewStub mViewStub = findViewById(R.id.viewStub);
        if (mViewStub != null) {
            mViewStub.inflate();
        }
        startActivity(new Intent(mContext, SensorActivity.class));
//        startActivity(new Intent(mContext, RxjavaTestActivity.class));
    }

    public void btn_sms(View view) {
        startActivity(new Intent(this, SMSActivatity.class));
    }

    public void btn_lock(View view) {
        startActivity(new Intent(this, LockActivity.class));
    }

    public void btn_led(View view) {
        startActivity(new Intent(this, LEDActivity.class));
    }

    public void btn_customView(View view) {
        startActivity(new Intent(this, ViewActivity.class));
    }

    public void btn_permission(View view) {
        if (mPermissionManager == null) {
            mPermissionManager = new PermissionManager(this);
        }
        mPermissionManager.jumpPermissionPage();
    }

    public void btn_accessibility(View view) {
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void btn_call(View view) {
        startActivity(new Intent(this, CallActivity.class));
    }

    public void btn_setwallpaper(View view) {
        startActivity(new Intent(this, SetWallpaperActivity.class));
    }

    public void btn_network(View view) {
        startActivity(new Intent(mContext, NetWorkActivity.class));
    }

    public void btn_silentModel(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (SoundUtils.checkNotiPermission()) {
                SoundUtils.setSilentModel();
            }
        }
    }

    public void btn_normalModel(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (SoundUtils.checkNotiPermission()) {
                SoundUtils.setNormalModel();
            }
        }
    }

    public void btn_test(View view) {
        startActivity(new Intent(mContext, TestActivity.class));
    }

    public void btn_capture(View view) {
        startIntent();
        FloatWindowManager.showFloatView();
    }

    public void btn_recyclerView(View view) {
        startActivity(new Intent(mContext, RecyclerViewActivity.class));
    }

    public void btn_deviceinfo(View view) {
        startActivity(new Intent(mContext, DeviceInfoActivity.class));
    }

    public void btn_funne(View view) {
        startActivity(new Intent(mContext, FunneActivity.class));
    }

    public void btn_callPhone(View view) {
    }

    public void btn_Badge(View view) {
        String s = binding.etBadgeCount.getText().toString();
        if (!TextUtils.isEmpty(s)) {
            Intent i = new Intent(mContext, BadgeIntentService.class);
            i.putExtra("badgeCount", Integer.valueOf(s));
            startService(i);
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
