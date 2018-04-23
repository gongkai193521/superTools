package com.viapalm.schchildpre.led;

import android.app.Activity;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.viapalm.schchildpre.R;

import java.io.FileOutputStream;

public class LEDActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "LED";
    Button mLedTest, flashlight;
    int mLedStatus = 0;
    private final int mLedColorRed = 0xFFFF0000;

    final byte[] LIGHT_ON = {'2', '5', '5'};
    final byte[] LIGHT_OFF = {'0'};
    private final int RED = 0;
    private int color = RED;
    String RED_LED_DEV = " /sys/class/leds/torch/brightness";

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        setContentView(R.layout.activity_led);

        mLedTest = ((Button) findViewById(R.id.ledcolor));
        flashlight = ((Button) findViewById(R.id.flashlight));
        mLedTest.setOnClickListener(this);
        flashlight.setOnClickListener(this);

        setledlightcolor(color);
        mLedTest.setTextColor(mLedColorRed);
        mLedTest.setText("RED");
        mLedStatus = 1;

        camera = Camera.open();
        parameter = camera.getParameters();
    }

    Camera.Parameters parameter; //全局变量
    Camera camera; //全局变量

    @Override
    public void onClick(View v) {
        Log.d("LEDActivity", "---onClick---mLedStatus=" + mLedStatus);
        switch (v.getId()) {
            case R.id.ledcolor:
                if (mLedStatus == 0) {
                    mLedTest.setText("开");
                    mLedStatus = 1;
                } else if (mLedStatus == 1) {
                    mLedTest.setText("关");
                    mLedStatus = 0;
                }
                break;
            case R.id.flashlight:
                if ("闪光灯开关".equals(flashlight.getText())
                        || "开".equals(flashlight.getText())) {
                    flashlight.setText("关");
                    parameter.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                } else {
                    flashlight.setText("开");
                    parameter.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                }
                camera.setParameters(parameter);
                break;
        }
    }

    @Override
    public void finish() {
        setledlightcolor(-1);
        super.finish();
    }

    private void setledlightcolor(int color) {
        logd("set:" + color);
        boolean red = false;
        switch (color) {
            case RED:
                red = true;
                break;
            default:
                break;
        }
        try {
            FileOutputStream foRed = new FileOutputStream(RED_LED_DEV);
            Log.d(TAG, "foRed" + foRed);
            foRed.write(red ? LIGHT_ON : LIGHT_OFF);
            foRed.close();
        } catch (Exception e) {
            loge(e);
        }
    }

    void logd(Object d) {
        Log.d(TAG, "" + d);
    }

    void loge(Object e) {
        Log.e(TAG, "" + e);
    }
}