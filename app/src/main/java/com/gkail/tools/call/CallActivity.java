package com.gkail.tools.call;

import android.Manifest;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.gkail.tools.R;
import com.gkail.tools.base.BaseActivity;
import com.gkail.tools.databinding.ActivityCallBinding;

/**
 * Created by gongkai on 18/3/30.
 */

public class CallActivity extends BaseActivity {
    private ActivityCallBinding binding;

    @Override
    public int setContentView() {
        return R.layout.activity_call;
    }

    @Override
    public void setupViews(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(mContext, R.layout.activity_call);
        checkPermission();
    }

    private void checkPermission() {

        requestPermission(100, Manifest.permission.CALL_PHONE, new Runnable() {
            @Override
            public void run() {
                showData();
            }
        }, new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    private void showData() {
        //获取通话记录
        String callHistoryListStr = CallUtil.getCallHistoryList(this, getContentResolver());
        binding.tvCallhistory.setText("通话记录：" + "\n\n" + callHistoryListStr);
    }

    public void openCallIntercept(View view) {
        if (!CallUtil.checkCallPermission(this)) {
            return;
        }
        startService(new Intent(this, EndCallService.class));
    }

    public void closeCallIntercept(View view) {
        if (!CallUtil.checkCallPermission(this)) {
            return;
        }
        stopService(new Intent(this, EndCallService.class));
    }
}