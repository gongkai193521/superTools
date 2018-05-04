package com.gkail.tools.wifi;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.gkail.tools.R;
import com.gkail.tools.base.BaseActivity;


/**
 * Created by gongkai on 18/5/4.
 */

public class NetWorkActivity extends BaseActivity {
    @Override
    public int setContentView() {
        return R.layout.activity_network;
    }

    @Override
    public void setupViews(Bundle savedInstanceState) {

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
        new WifiHostManager(this).setWifiApEnabled(true);
        new WifiHostManager(this).startWifiAp();

    }
}
