package com.gkail.tools.activity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.gkail.tools.R;
import com.gkail.tools.base.BaseActivity;

public class SensorActivity extends BaseActivity implements OnClickListener {
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private Sensor gyroscopeSensor;
    private Sensor rotationVectorSensor;
    private Button mBt3;
    private Button mBt2;
    private Button mBt5;
    private Button mBt4;
    private Button mBt6;
    private Button mBt1;


    @Override
    public int setContentView() {
        return R.layout.activity_sensor;
    }

    @Override
    public void setupViews(Bundle savedInstanceState) {
        mBt3 = (Button) findViewById(R.id.bt3);
        mBt2 = (Button) findViewById(R.id.bt2);
        mBt5 = (Button) findViewById(R.id.bt5);
        mBt4 = (Button) findViewById(R.id.bt4);
        mBt6 = (Button) findViewById(R.id.bt6);
        mBt1 = (Button) findViewById(R.id.bt1);
        mBt1.setOnClickListener(this);
        mBt2.setOnClickListener(this);
        mBt3.setOnClickListener(this);
        mBt4.setOnClickListener(this);
        mBt5.setOnClickListener(this);
        mBt6.setOnClickListener(this);
        // 获取传感器
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // 获取接近传感器
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        // 获取陀螺仪
        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        // 获取旋转矢量传感器
        rotationVectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
    }

    @Override
    protected void onResume() {
        super.onResume();
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
                    SensorManager.AXIS_X,
                    SensorManager.AXIS_Z,
                    remappedRotationMatrix);
            // Convert to orientations
            float[] orientations = new float[3];
            SensorManager.getOrientation(remappedRotationMatrix, orientations);
            for (int i = 0; i < 3; i++) {
                orientations[i] = (float) (Math.toDegrees(orientations[i]));
                if (orientations[2] > 45) {
                    getWindow().getDecorView().setBackgroundColor(Color.GRAY);
                } else if (orientations[2] < -45) {
                    getWindow().getDecorView().setBackgroundColor(Color.BLACK);
                } else if (Math.abs(orientations[2]) < 10) {
                    getWindow().getDecorView().setBackgroundColor(Color.WHITE);
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
            if (sensorEvent.values[0] < proximitySensor.getMaximumRange()) {
                // Detected something nearby  检测到附近的东西
                getWindow().getDecorView().setBackgroundColor(Color.RED);
            } else {
                // Nothing is nearby  附近没什么
                getWindow().getDecorView().setBackgroundColor(Color.GREEN);
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(proximitySensorListener);
        sensorManager.unregisterListener(gyroscopeSensorListener);
        sensorManager.unregisterListener(rvListener);
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
            default:
                break;
        }
    }
}
