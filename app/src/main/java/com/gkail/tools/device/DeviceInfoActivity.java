package com.gkail.tools.device;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gkail.tools.R;
import com.gkail.tools.base.BaseActivity;
import com.gkail.tools.funnelchart.FunnelChart02View;
import com.gkail.tools.util.InputUtils;

import org.xclcharts.view.ChartView;

/**
 * Created by gongkai on 18/4/23.
 */

public class DeviceInfoActivity extends BaseActivity {
    private TextView mTv1, mTv2, mTv3, mTv4, mTv5, mTv6, mTv7, mTv8, mTv9, mTv10;

    @Override
    public int setContentView() {
        return R.layout.activity_deviceinfo;
    }

    @Override
    public void setupViews(Bundle savedInstanceState) {
        initView();
        showData();
    }

    private void initView() {
        mTv1 = findViewById(R.id.tv1);
        mTv2 = findViewById(R.id.tv2);
        mTv3 = findViewById(R.id.tv3);
        mTv4 = findViewById(R.id.tv4);
        mTv5 = findViewById(R.id.tv5);
        mTv6 = findViewById(R.id.tv6);
        mTv7 = findViewById(R.id.tv7);
        mTv8 = findViewById(R.id.tv8);
        mTv9 = findViewById(R.id.tv9);
        mTv10 = findViewById(R.id.tv10);
    }

    private void showData() {
        mTv1.setText("SN:  " + Build.SERIAL);
        //获取手机型号
        mTv2.setText("品牌型号:  " + Build.BRAND + "/" + Build.MODEL);
        //当前输入法
        mTv3.setText("currentInput:  " + InputUtils.getCurrentInput());
        //所有安装的输入法
        mTv4.setText("AllInput:  " + InputUtils.getInput());
        LinearLayout layout = findViewById(R.id.layout);

        //图表显示范围在占屏幕大小的90%的区域内
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int scrWidth = (int) (dm.widthPixels * 0.9);
        int scrHeight = (int) (dm.heightPixels * 0.9);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                scrWidth, scrHeight);
        //居中显示
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        //图表view放入布局中，也可直接将图表view放入Activity对应的xml文件中
        final RelativeLayout chartLayout = new RelativeLayout(this);
        ChartView view = new FunnelChart02View(this);//漏斗图(Asc)
        chartLayout.addView(view, layoutParams);
        //增加控件
        layout.addView(chartLayout);

    }
}
