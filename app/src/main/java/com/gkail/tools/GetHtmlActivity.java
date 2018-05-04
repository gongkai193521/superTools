package com.gkail.tools;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by gongkai on 18/2/1.
 */

public class GetHtmlActivity extends Activity {
    TextView mTextView;
    StringBuilder mBuilder = new StringBuilder();
    private Handler mHandler = new Handler() {
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            mTextView.setText(mBuilder.toString());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gethtml);
        mTextView = (TextView) findViewById(R.id.tv_html);
        new Thread(new Runnable() {
            @Override
            public void run() {
                getPageSource();
                mHandler.sendEmptyMessage(0);
            }
        }).start();
    }

    private String pageUrl;//定义需要操作的网页地址
    private String pageEncode = "UTF8";//定义需要操作的网页的编码

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getPageEncode() {
        return pageEncode;
    }

    public void setPageEncode(String pageEncode) {
        this.pageEncode = pageEncode;
    }

    //定义取源码的方法
    public String getPageSource() {
        StringBuffer sb = new StringBuffer();
        try {
            //构建一URL对象
            URL url = new URL(pageUrl);
            //使用openStream得到一输入流并由此构造一个BufferedReader对象
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), pageEncode));
            String line;
            //读取www资源
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            in.close();
        } catch (Exception ex) {
            System.err.println(ex);
        }
        Log.i("----", " == " + sb.toString());
        return sb.toString();
    }
}
