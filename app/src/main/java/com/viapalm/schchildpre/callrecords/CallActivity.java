package com.viapalm.schchildpre.callrecords;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.viapalm.schchildpre.R;

/**
 * Created by gongkai on 18/3/30.
 */

public class CallActivity extends Activity {
    private TextView tv_callhistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call);
        initView();
        showData();
    }

    private void showData() {
        //获取通话记录
        String callHistoryListStr = CallUtil.getCallHistoryList(this, getContentResolver());
        tv_callhistory.setText("通话记录：" + "\n\n" + callHistoryListStr);
    }

    private void initView() {
        tv_callhistory = (TextView) findViewById(R.id.tv_callhistory);
    }

}