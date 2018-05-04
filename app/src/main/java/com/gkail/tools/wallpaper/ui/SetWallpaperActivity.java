package com.gkail.tools.wallpaper.ui;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.gkail.tools.Constant;
import com.gkail.tools.R;
import com.gkail.tools.base.BaseActivity;
import com.gkail.tools.wallpaper.service.CameraLiveWallpaperService;
import com.gkail.tools.wallpaper.service.VideoLiveWallpaperService;
import com.gkail.tools.wallpaper.util.WallpaperUtil;

import java.io.IOException;

import butterknife.BindView;

/**
 * 壁纸开发
 * 1.资源文件做为壁纸
 * 2.Bitmap做为壁纸
 * 3.动态壁纸
 * <p>
 * author:Coolspan
 */
public class SetWallpaperActivity extends BaseActivity {
    private final static int REQUEST_CODE_SET_WALLPAPER = 0x001;
    static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    @BindView(R.id.cb_voice)
    CheckBox mCbVoice;

    @Override
    public int setContentView() {
        return R.layout.activity_setwallpaper;
    }

    @Override
    public void setupViews(Bundle savedInstanceState) {
        mCbVoice.setChecked(Constant.wallpaper_sound_checkd);
        mCbVoice.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(
                    CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // 静音
                    VideoLiveWallpaperService.voiceSilence(getApplicationContext());
                    Constant.wallpaper_sound_checkd = true;
                } else {
                    VideoLiveWallpaperService.voiceNormal(getApplicationContext());
                    Constant.wallpaper_sound_checkd = false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setVideoToWallPaper(View view) {
        VideoLiveWallpaperService.setToWallPaper(this);
    }

    public void setCameraToWallPaper(View view) {
        checkSelfPermission();
    }

    /**
     * 使用资源文件设置壁纸
     * 直接设置为壁纸，不会有任何界面和弹窗出现
     *
     * @param view
     */
    public void onSetWallpaperForResource(View view) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        try {
            wallpaperManager.setResource(R.raw.wallpaper);
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //WallpaperManager.FLAG_LOCK  WallpaperManager.FLAG_SYSTEM
//                wallpaperManager.setResource(R.raw.wallpaper, WallpaperManager.FLAG_SYSTEM);
//            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用Bitmap设置壁纸
     * 直接设置为壁纸，不会有任何界面和弹窗出现
     * 壁纸切换，会有动态的渐变切换
     *
     * @param view
     */
    public void onSetWallpaperForBitmap(View view) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        try {
            Bitmap wallpaperBitmap = BitmapFactory.decodeResource(getResources(), R.raw.girl);
            wallpaperManager.setBitmap(wallpaperBitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除壁纸
     *
     * @param view
     */
    public void clearWallpaper(View view) {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        try {
            wallpaperManager.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void onLiveWallpaper(View view) {
        WallpaperUtil.setLiveWallpaper(this.getApplicationContext(), SetWallpaperActivity.this, SetWallpaperActivity.REQUEST_CODE_SET_WALLPAPER);
    }

    /**
     * 检查权限
     */
    void checkSelfPermission() {
        if (ContextCompat.checkSelfPermission(this, PERMISSION_CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{PERMISSION_CAMERA}, 100);
        } else {
            CameraLiveWallpaperService.setCameratWallpaper(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CameraLiveWallpaperService.setCameratWallpaper(this);

                } else {
                    Toast.makeText(this, "请先开启相机权限", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SET_WALLPAPER) {
            if (resultCode == RESULT_OK) {
                // TODO: 2017/3/13 设置动态壁纸成功
            } else {
                // TODO: 2017/3/13 取消设置动态壁纸
            }
        }
    }
}
