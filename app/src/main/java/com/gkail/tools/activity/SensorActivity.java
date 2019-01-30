package com.gkail.tools.activity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.gkail.tools.R;
import com.gkail.tools.base.BaseActivity;
import com.gkail.tools.util.LogUtils;
import com.gkail.tools.util.ToastUtils;

import java.util.List;

public class SensorActivity extends BaseActivity implements OnClickListener {
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private Sensor gyroscopeSensor;
    private Sensor rotationVectorSensor;
    private Sensor gravitySensor;
    private View mView;
    private TextView tv1;
    private Button mBt1;
    private Button mBt2;
    private Button mBt3;
    private Button mBt4;
    private Button mBt5;
    private Button mBt6;
    private Button mBt7;
    private Button mBt8;


    @Override
    public int setContentView() {
        return R.layout.activity_sensor;
    }

    @Override
    public void setupViews(Bundle savedInstanceState) {
        mView = findViewById(R.id.activity_main);
        tv1 = (TextView) findViewById(R.id.tv1);
        mBt1 = (Button) findViewById(R.id.bt1);
        mBt2 = (Button) findViewById(R.id.bt2);
        mBt3 = (Button) findViewById(R.id.bt3);
        mBt4 = (Button) findViewById(R.id.bt4);
        mBt5 = (Button) findViewById(R.id.bt5);
        mBt6 = (Button) findViewById(R.id.bt6);
        mBt7 = (Button) findViewById(R.id.bt7);
        mBt8 = (Button) findViewById(R.id.bt8);
        mBt1.setOnClickListener(this);
        mBt2.setOnClickListener(this);
        mBt3.setOnClickListener(this);
        mBt4.setOnClickListener(this);
        mBt5.setOnClickListener(this);
        mBt6.setOnClickListener(this);
        mBt7.setOnClickListener(this);
        mBt8.setOnClickListener(this);
        // 获取传感器
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        // 获取接近传感器
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if (proximitySensor == null) {
            LogUtils.i("接近传感器不可用");
        }

        // 获取陀螺仪
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroscopeSensor == null) {
            LogUtils.i("陀螺仪传感器不可用");
        }

        // 获取旋转矢量传感器
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (rotationVectorSensor == null) {
            LogUtils.i("旋转矢量传感器不可用");
        }
        // 获取加速度传感器
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 得到设置支持的所有传感器的List
        List<Sensor> sensorList = sensorManager.getSensorList(Sensor.TYPE_ALL);
        for (Sensor sensor : sensorList) {
            LogUtils.i("传感器：" + sensor.getName());
        }
    }

    // Create a 陀螺仪listener
    private SensorEventListener gyroscopeSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // More code goes here
            if (sensorEvent.values[2] > 0.5f) { // anticlockwise
                getWindow().getDecorView().setBackgroundColor(Color.BLUE);
            } else if (sensorEvent.values[2] < -0.5f) { // clockwise
                getWindow().getDecorView().setBackgroundColor(Color.YELLOW);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    // Create a 旋转矢量传感器listener
    private SensorEventListener rvListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // More code goes here
            float[] rotationMatrix = new float[16];
            SensorManager.getRotationMatrixFromVector(
                    rotationMatrix, sensorEvent.values);
            // Remap coordinate system
            float[] remappedRotationMatrix = new float[16];
            SensorManager.remapCoordinateSystem(rotationMatrix,
                    SensorManager.AXIS_Y,
                    SensorManager.AXIS_Z,
                    remappedRotationMatrix);
            // Convert to orientations
            float[] orientations = new float[3];
            SensorManager.getOrientation(remappedRotationMatrix, orientations);
            for (int i = 0; i < 3; i++) {
                orientations[i] = (float) (Math.toDegrees(orientations[i]));
                Log.i("----", "orientations == " + orientations[i]);
                if (orientations[2] > 45) {
                    getWindow().getDecorView().setBackgroundColor(Color.GRAY);
                } else if (orientations[2] < -45) {
                    getWindow().getDecorView().setBackgroundColor(Color.WHITE);
                } else if (Math.abs(orientations[2]) < 10) {
                    getWindow().getDecorView().setBackgroundColor(Color.BLACK);
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    // Create listener  创建接近传感器监听器
    private SensorEventListener proximitySensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            // More code goes here
            Log.i("----", "sensorEvent.values[0] == " + sensorEvent.values[0]);
            if (sensorEvent.values[0] < proximitySensor.getMaximumRange()) {
                // Detected something nearby  检测到附近的东西
                mView.setBackgroundColor(Color.RED);
            } else {
                // Nothing is nearby  附近没什么
                mView.setBackgroundColor(Color.GREEN);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };
    // Create listener  创建重力传感器监听器
    private SensorEventListener gravitySensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float X = sensorEvent.values[0];
            float Y = sensorEvent.values[1];
            float Z = sensorEvent.values[2];
            tv1.setText("X方向的重力加速度：" + X + "\n"
                    + "Y方向的重力加速度：" + Y + "\n"
                    + "Z方向的重力加速度：" + Z);
            if (Z < 0) {
                ToastUtils.showSingleToast(mContext, "请正确使用平板");
                mView.setBackgroundColor(Color.RED);
            } else {
                mView.setBackgroundColor(Color.WHITE);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(proximitySensorListener);
        sensorManager.unregisterListener(gyroscopeSensorListener);
        sensorManager.unregisterListener(rvListener);
        sensorManager.unregisterListener(gravitySensorListener);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt1:
                if (proximitySensor == null) {
                    Toast.makeText(this, "接近传感器不可用", Toast.LENGTH_LONG).show();
                }
                // Register it, specifying the polling interval in  注册，指定轮询间隔
                // microseconds  微秒
                sensorManager.registerListener(proximitySensorListener,
                        proximitySensor, 2 * 1000 * 1000);
                break;
            case R.id.bt2:
                sensorManager.unregisterListener(proximitySensorListener);
                break;
            case R.id.bt3:
                // Register the listener 注册听众
                sensorManager.registerListener(gyroscopeSensorListener, gyroscopeSensor,
                        SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case R.id.bt4:
                sensorManager.unregisterListener(gyroscopeSensorListener);
                break;
            case R.id.bt5:
                // Register
                sensorManager.registerListener(rvListener, rotationVectorSensor,
                        SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case R.id.bt6:
                sensorManager.unregisterListener(rvListener);
                break;
            case R.id.bt7:
                sensorManager.registerListener(gravitySensorListener, gravitySensor,
                        SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case R.id.bt8:
                sensorManager.unregisterListener(gravitySensorListener);
                break;
            default:
                break;
        }
    }
}
