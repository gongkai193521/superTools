package com.viapalm.schchildpre.customview;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.widget.SeekBar;

import com.viapalm.schchildpre.R;
import com.viapalm.schchildpre.base.BaseActivity;

public class ViewActivity extends BaseActivity {
    private static final String TAG = "MainActivity";
    private FirewormsView mFirwWormsView;
    private SeekBar mSeekBarScale;
    private SeekBar mSeekBarRotate;

    @Override
    public int setContentView() {
        return R.layout.activity_view;
    }

    @Override
    public void setupViews(Bundle savedInstanceState) {
        initView();
        setListener();
    }

    private void setListener() {
        mSeekBarScale.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "mSeekBarScale: " + progress);
                int realProgress = seekBar.getMax() - progress;
                float ratio = Float.intBitsToFloat(realProgress) / Float.intBitsToFloat(seekBar.getMax());
                ViewCompat.setScaleX(mFirwWormsView, ratio);
                ViewCompat.setScaleY(mFirwWormsView, ratio);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekBarRotate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Log.d(TAG, "mSeekBarRotate: " + progress);
                ViewCompat.setRotation(mFirwWormsView, progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void initView() {
        mFirwWormsView = (FirewormsView) findViewById(R.id.fire_worms_view);
        mSeekBarScale = (SeekBar) findViewById(R.id.seekbar_1);
        mSeekBarRotate = (SeekBar) findViewById(R.id.seekbar_2);
    }
}
