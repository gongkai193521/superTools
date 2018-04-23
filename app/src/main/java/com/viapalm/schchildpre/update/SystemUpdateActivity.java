package com.viapalm.schchildpre.update;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.RecoverySystem;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.viapalm.schchildpre.R;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;

import static android.content.ContentValues.TAG;

/**
 * Created by gongkai on 16/11/15.
 */

public class SystemUpdateActivity extends Activity implements View.OnClickListener {
    private TextView tv_update;
    private Button bt_update, bt_verifyPackage;
    private ProgressDialog verifyPackageProgressDialog;
    private File file;
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            switch (msg.what) {
                case 0:
                    Toast.makeText(SystemUpdateActivity.this, "升级包验证完整", Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    Toast.makeText(SystemUpdateActivity.this, "升级包验证失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        initView();
        Log.i("----", " == " + android.os.Build.BRAND);
        Log.i("----", " == " + android.os.Build.MANUFACTURER);
    }

    private void initView() {
//        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/update.zip");
//        file = new File("/sdcard/update.zip");//外置sd卡
        file = new File("/isdcard/update.zip");//内置sd卡
        tv_update = (TextView) findViewById(R.id.tv_update);
        bt_verifyPackage = (Button) findViewById(R.id.bt_verifyPackage);
        bt_update = (Button) findViewById(R.id.bt_update);
        tv_update.setText(file.getAbsolutePath());
        bt_update.setOnClickListener(this);
        bt_verifyPackage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_verifyPackage:
                verifyPackage();
                break;
            case R.id.bt_update:
//                update();
                systemUpdate();
                break;
            default:
                break;
        }
    }

    private void update() {
        if (file.exists()) {
            try {
                android.os.RecoverySystem.installPackage(this, file);
            } catch (IOException e) {
                e.printStackTrace();
                tv_update.setText(e.toString());
            }
        } else {
            Toast.makeText(SystemUpdateActivity.this, "没有找到系统更新包", Toast.LENGTH_LONG).show();
        }
    }

    private void systemUpdate() {
        File RECOVERY_DIR = new File("/cache/recovery");
        // /cache/recovery
        File COMMAND_FILE = new File(RECOVERY_DIR, "command");
        FileWriter command = null;
        String arg = "--update_package=" + "/isdcard/update.zip";
        RECOVERY_DIR.mkdirs(); // In case we need it
        COMMAND_FILE.delete(); // In case it's not writable
        try {
            command = new FileWriter(COMMAND_FILE);
            command.write(arg);
            command.write("\n");
            command.close();
            //AndroidCMD.runCMD("sync");
        } catch (IOException e) {
            e.printStackTrace();
        }
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        pm.reboot("recovery");
    }

    private void verifyPackage() {
        verifyPackageProgressDialog = new ProgressDialog(this);
        verifyPackageProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        verifyPackageProgressDialog.setTitle("验证升级包完整度");
        verifyPackageProgressDialog.setIcon(R.mipmap.ic_launcher);
        verifyPackageProgressDialog.setMessage("正在验证升级包 , 请稍等...");
        verifyPackageProgressDialog.setMax(100);
        verifyPackageProgressDialog.setProgress(0);
        verifyPackageProgressDialog.setSecondaryProgress(0);
        verifyPackageProgressDialog.setIndeterminate(false);
        verifyPackageProgressDialog.setCancelable(false);
        verifyPackageProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    verifyPackageProgressDialog.dismiss();
                    e.printStackTrace();
                }
                onConfirmUpdate();
            }
        }).start();
    }

    private void onConfirmUpdate() {
        try {
            //check the package is available or not
            RecoverySystem.verifyPackage(file, new RecoverySystem.ProgressListener() {
                //该listener只接受一个抽象方法，参数为验证进度
                @Override
                public void onProgress(int progress) {
                    Log.i(TAG, "progress = " + progress);
                    //实时的将验证进度显示到进度条上
                    verifyPackageProgressDialog.setProgress(progress);
                }
            }, null);
            Log.i(TAG, "verifyPackage is completed and it ok");
            //验证成功后，开始系统升级
            Log.i(TAG, "It will install package");
            mHandler.sendEmptyMessage(0);
        } catch (IOException e) {
            //如果验证时读取文件出错，会进入该异常，在这里处理
            e.printStackTrace();
            mHandler.sendEmptyMessage(1);
        } catch (GeneralSecurityException e) {
            //如果验证文件出错或不符合规范，进入该异常，在这里处理
            e.printStackTrace();
            mHandler.sendEmptyMessage(1);
        } finally {
            verifyPackageProgressDialog.dismiss();
        }
    }
}
