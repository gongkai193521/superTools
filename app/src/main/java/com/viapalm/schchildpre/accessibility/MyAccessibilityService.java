package com.viapalm.schchildpre.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.viapalm.schchildpre.Constant;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MyAccessibilityService extends AccessibilityService {
    String pkgName;
    String clsName;
    private AccessibilityNodeInfo nodeInfo;
    //public AccessibilityNodeInfo mInfo;
    Handler handler = new Handler(Looper.getMainLooper()) {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public void dispatchMessage(Message msg) {
            super.dispatchMessage(msg);
            if (TextUtils.isEmpty(pkgName)) return;
            switch (pkgName) {
                case "com.tencent.mm":
                    if (!Constant.wx_checkd) {
                        break;
                    }
                    if ("com.tencent.mm.ui.base.r".equals(clsName)) {
                        performScroll(nodeInfo);
                    }
                    //取消关注微信公众号
                    if ("com.tencent.mm.plugin.brandservice.ui.BrandServiceIndexUI||com.tencent.mm.ui.base.r".contains(clsName)) {
                        List<AccessibilityNodeInfo> infos = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/un");
                        for (AccessibilityNodeInfo info : infos) {
                            performLongClick(info);
                        }
                    }
                    if ("android.widget.FrameLayout".equals(clsName)) {
                        List<AccessibilityNodeInfo> infos = nodeInfo.findAccessibilityNodeInfosByText("取消关注");
                        for (AccessibilityNodeInfo info : infos) {
                            if (info.isEnabled()) {
                                performClick(info);
                            }
                        }
                    }
                    //不再关注
                    if ("com.tencent.mm.ui.base.i".equals(clsName)) {
                        List<AccessibilityNodeInfo> infos = nodeInfo.findAccessibilityNodeInfosByViewId("com.tencent.mm:id/all");
//                        List<AccessibilityNodeInfo> infos = nodeInfo.findAccessibilityNodeInfosByText("不再关注");
                        for (AccessibilityNodeInfo info : infos) {
                            if (info.isEnabled()) {
                                performClick(info);
                            }
                        }
                    }
                    break;
                case "com.qmsh.hbq"://千米抢红包
                    if ("com.reezy.hongbaoquan.ui.hongbao.dialog.HongbaoOpenDialog".equals(clsName)) {
                        List<AccessibilityNodeInfo> infos = nodeInfo.findAccessibilityNodeInfosByViewId("com.qmsh.hbq:id/btn_open");
                        for (AccessibilityNodeInfo info : infos) {
                            if (info.isEnabled()) {
                                performClick(info);
                            }
                        }
                    }
                    if ("com.reezy.hongbaoquan.ui.hongbao.DetailActivity".equals(clsName)) {
                        performBack();
                    }
                    break;
                default://自动安装应用
                    if ("com.android.packageinstaller.PackageInstallerActivity".equals(clsName)) {
                        List<AccessibilityNodeInfo> infos = nodeInfo.findAccessibilityNodeInfosByText("安装");
                        for (AccessibilityNodeInfo info : infos) {
                            if (info.isEnabled()) {
                                performClick(info);
                            }
                        }
                    }
                    if ("com.android.packageinstaller.InstallAppProgress".equals(clsName)) {
                        List<AccessibilityNodeInfo> infos = nodeInfo.findAccessibilityNodeInfosByText("完成");
                        for (AccessibilityNodeInfo info : infos) {
                            if (info.isEnabled()) {
                                performClick(info);
                            }
                        }
                    }
                    break;
            }
        }
    };

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    //系统会在成功连接上你的服务的时候调用这个方法
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        check();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getSource() != null) {
            if (!"com.wandoujia.phoenix2".equals(event.getPackageName().toString())
                    && !"android.widget.LinearLayout".equals(event.getClassName().toString())) {
                nodeInfo = event.getSource();
                pkgName = event.getPackageName().toString();
                clsName = event.getClassName().toString();
                Log.i("----", " pkgName/clsName== " + pkgName + "/" + clsName);
            }
        }
    }

    private Timer timer;
    private TimerTask task;

    private void check() {
        if (task != null) {
            task.cancel();
        }
        task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
            }
        };
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        timer = new Timer();
        timer.schedule(task, 1000, 500);
    }

    @Override
    public void onInterrupt() {

    }


    @Override
    public boolean onUnbind(Intent intent) {//在系统将要关闭这个AccessibilityService会被调用
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        if (task != null) {
            task.cancel();
            task = null;
        }
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
        super.onDestroy();
    }

    /**
     * 模拟返回事件处理
     */

    private void performBack() {
        performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    /**
     * 模拟滑动事件
     *
     * @param nodeInfo
     */
    private void performScroll(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.isScrollable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
        } else {
            performScroll(nodeInfo.getParent());
//            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
//                if (nodeInfo.getChild(i) != null) {
//                    performScroll(nodeInfo.getChild(i));
//                }
//            }
        }
    }

    /**
     * 模拟点击事件
     *
     * @param nodeInfo
     */
    public void performClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.isClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            performClick(nodeInfo.getParent());
        }
    }

    /**
     * 模拟长按事件
     *
     * @param nodeInfo
     */
    public void performLongClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.isLongClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_LONG_CLICK);
        } else {
            performLongClick(nodeInfo.getParent());
        }
    }

}
