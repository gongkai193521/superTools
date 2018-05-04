package com.gkail.tools.call;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.gkail.tools.R;
import com.gkail.tools.base.BaseActivity;

import butterknife.BindView;

/**
 * Created by gongkai on 18/3/30.
 */

public class CallActivity extends BaseActivity {
    @BindView(R.id.tv_callhistory)
    TextView tv_callhistory;

    @Override
    public int setContentView() {
        return R.layout.activity_call;
    }

    @Override
    public void setupViews(Bundle savedInstanceState) {
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
        tv_callhistory.setText("通话记录：" + "\n\n" + callHistoryListStr);
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