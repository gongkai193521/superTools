package com.gkail.tools.device;

import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.gkail.tools.R;
import com.gkail.tools.base.BaseActivity;
import com.gkail.tools.util.InputUtils;

import butterknife.BindView;

/**
 * Created by gongkai on 18/4/23.
 */

public class DeviceInfoActivity extends BaseActivity {
    @BindView(R.id.tv1)
    TextView mTv1;
    @BindView(R.id.tv2)
    TextView mTv2;
    @BindView(R.id.tv3)
    TextView mTv3;
    @BindView(R.id.tv4)
    TextView mTv4;
    @BindView(R.id.tv5)
    TextView mTv5;
    @BindView(R.id.tv6)
    TextView mTv6;
    @BindView(R.id.tv7)
    TextView mTv7;
    @BindView(R.id.tv8)
    TextView mTv8;
    @BindView(R.id.tv9)
    TextView mTv9;
    @BindView(R.id.tv10)
    TextView mTv10;

    @Override
    public int setContentView() {
        return R.layout.activity_deviceinfo;
    }

    @Override
    public void setupViews(Bundle savedInstanceState) {
        showData();
    }

    private void showData() {
        mTv1.setText("SN:  " + Build.SERIAL);
        //获取手机型号
        mTv2.setText("品牌型号:  " + Build.BRAND + "/" + Build.MODEL);
        //当前输入法
        mTv3.setText("currentInput:  " + InputUtils.getCurrentInput());
        //所有安装的输入法
        mTv4.setText("AllInput:  " + InputUtils.getInput());
    }
}
