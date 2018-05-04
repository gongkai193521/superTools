package com.gkail.tools.wifi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.gkail.tools.R;
import com.gkail.tools.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


/**
 * Created by gongkai on 18/5/4.
 */

public class NetWorkActivity extends BaseActivity {
    @BindView(R.id.et_wifiHostName)
    EditText wifiHostName;
    @BindView(R.id.sp_security)
    Spinner mSpinner;
    @BindView(R.id.et_wifiHostPas)
    EditText wifiHostPas;
    boolean security = false;

    @Override
    public int setContentView() {
        return R.layout.activity_network;
    }

    @Override
    public void setupViews(Bundle savedInstanceState) {
        final List<String> list = new ArrayList<>();
        list.add("无");
        list.add("WPA2 PSK");
        ArrayAdapter arrayAdapter = new ArrayAdapter(mContext, R.layout.layout_spiner, list);
        //传入的参数分别为 Context , 未选中项的textview , 数据源List
        mSpinner.setAdapter(arrayAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    security = false;
                    wifiHostPas.setVisibility(View.GONE);
                } else {
                    security = true;
                    wifiHostPas.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setWifi(View view) {
        //第一种
        Intent intent = new Intent();
        intent.setAction("android.net.wifi.PICK_WIFI_NETWORK");
        startActivity(intent);
        //第二种
//        startActivity(new Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));
        //第三种
//        Intent i = new Intent();
//        if (android.os.Build.VERSION.SDK_INT >= 11) {
//            //Honeycomb
//            i.setClassName("com.android.settings", "com.android.settings.Settings$WifiSettingsActivity");
//        } else {
//            //other versions
//            i.setClassName("com.android.settings"
//                    , "com.android.settings.wifi.WifiSettings");
//        }
//        startActivity(i);
        //第四种
//        startActivity(new Intent("android.settings.WIFI_SETTINGS"));
    }

    public void setNetWork(View view) {

    }

    public void setWifiHost(View view) {
        new WifiHostManager(this).setWifiApEnabled(
                wifiHostName.getText().toString(), security, wifiHostPas.getText().toString());
//        new WifiHostManager(this).startWifiAp();

    }

    public void closeWifiHost(View view) {
        new WifiHostManager(this).closeWifiHotspot();
    }
}
