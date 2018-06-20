package com.gkail.tools;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.StackView;

import com.gkail.tools.base.BaseActivity;
import com.gkail.tools.util.ToastUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gongkai on 18/6/8.
 */

public class TestActivity extends BaseActivity {
    StackView mStackView;
    int[] imgs = new int[]{R.drawable.icon, R.drawable.welcome, R.drawable.icon, R.drawable.icon};

    @Override
    public int setContentView() {
        return R.layout.activity_test;
    }

    @Override
    public void setupViews(Bundle savedInstanceState) {
        mStackView = v(R.id.mStackView);
        List<Map<String, Object>> map = new ArrayList<>();
        for (int i = 0; i < imgs.length; i++) {
            Map<String, Object> items = new HashMap<>();
            items.put("image", imgs[i]);
            map.add(items);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, map,
                R.layout.item_stackview, new String[]{"image"}, new int[]{R.id.image});
        mStackView.setAdapter(adapter);

    }

    public void prev(View view) {
        mStackView.showPrevious();
    }

    public void next(View view) {
        mStackView.showNext();
    }

    public void changeScreen(View view) {
        Configuration configuration = getResources().getConfiguration();
        //如果是横屏设为竖屏
        if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //如果是竖屏设为横屏
        if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ToastUtils.showSingleToast(this, "系统屏幕方向发生改变" + "\n修改后屏幕方向为："
                + (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE ? "横向" : "竖向"));
    }
}
