package com.gkail.tools.accessibility.accessibility;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.gkail.tools.accessibility.AccessUtil;
import com.gkail.tools.accessibility.AccessibilityData;
import com.gkail.tools.accessibility.AccessibilityManager;
import com.gkail.tools.accessibility.accessibility.bean.AccessEvent;
import com.gkail.tools.accessibility.accessibility.bean.AccessibilityBean;
import com.gkail.tools.accessibility.accessibility.bean.ClickViewBean;
import com.gkail.tools.util.CollectionUtils;
import com.gkail.tools.util.DeviceTypeUtils;
import com.gkail.tools.util.LogUtils;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 权限自动设置
 *
 * @author guohuazhang
 */
public class MyAccessibilityService extends AccessibilityService {
    private static final String LogFlag = "MyAccessibilityService---";
    private AccessibilityNodeInfo source;
    private String currClsName;
    private String currPkgName;

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        checkAccessAction(event);
    }

    /**
     * 处理特殊页面，例如模拟点击，模拟返回，模拟滚动
     */
    private void checkAccessAction(AccessibilityEvent event) {
        if (event == null || event.getPackageName() == null || event.getClassName() == null) {
            return;
        }
        currPkgName = event.getPackageName().toString();
        currClsName = event.getClassName().toString();
        LogUtils.f(LogFlag + currPkgName + "/" + currClsName);
        source = event.getSource();
        if (source == null) return;
//        if (1 == AccessibilityManager.getInstance().getState()) {
//            ArrayList<AccessibilityBean> containList = AccessibilityManager.getInstance().getContainList(currPkgName, currClsName);
//            LogUtils.f(LogFlag, "containList.size：" + containList.size());
//            for (AccessibilityBean bean : containList) {
//                if (bean.getAction() == 5) {
//                    if (clickView(source, bean.getClickView())) {
//                        LogUtils.f(LogFlag + "触发事件：", bean.toString());
//                    } else {
//                        try {
//                            Thread.sleep(1000);
//                        } catch (InterruptedException e) {
//                            e.printStackTrace();
//                        }
//                        if (clickView(source, bean.getClickView())) {
//                            LogUtils.f(LogFlag + "触发事件：", bean.toString());
//                        }
//                    }
//                    continue;
//                } else {
//                    actionDo(bean);
//                    LogUtils.f(LogFlag + "触发事件：", bean.toString());
//                }
//            }
//        }
        try {
            Thread.sleep(2000);
            performScroll(source);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private boolean isContainAllId(AccessibilityNodeInfo nodeInfo, String pageViewId) {
        if (TextUtils.isEmpty(pageViewId)) {
            return true;
        }
        if (nodeInfo != null) {
            if (nodeInfo.getViewIdResourceName() != null) {
                LogUtils.i(LogFlag, "页面ID: " + pageViewId + "--找到--" + nodeInfo.getViewIdResourceName());
            }
            if (pageViewId.contains("&&")) {
                String[] pageViewIds = pageViewId.split("&&");
                boolean isFind = true;
                for (String id : pageViewIds) {
                    if (CollectionUtils.isEmpty(nodeInfo.findAccessibilityNodeInfosByViewId(id))) {
                        isFind = false;
                    }
                }
                if (isFind) return true;
            } else if (CollectionUtils.isNotEmpty(nodeInfo.findAccessibilityNodeInfosByViewId(pageViewId))) {
                LogUtils.i(LogFlag, "找到: " + pageViewId);
                return true;
            }
            if (performScroll(nodeInfo) && isContainAllKeys(nodeInfo, pageViewId)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 当前页面是否包含所有关键字
     *
     * @param nodeInfo
     * @param key
     * @return true表示包含所有
     */
    private boolean isContainAllKeys(AccessibilityNodeInfo nodeInfo, String key) {
        if (TextUtils.isEmpty(key)) {
            return true;
        }
        if (nodeInfo != null) {
            if (nodeInfo.getText() != null) {
                LogUtils.i(LogFlag, "页面关键字: " + key + "--找到--" + nodeInfo.getText().toString());
            }
            if (key.contains("&&")) {
                String[] keyWords = key.split("&&");
                boolean isFind = true;
                for (String k : keyWords) {
                    if (CollectionUtils.isEmpty(nodeInfo.findAccessibilityNodeInfosByText(k))) {
                        isFind = false;
                    }
                }
                if (isFind) return true;
            } else if (CollectionUtils.isNotEmpty(nodeInfo.findAccessibilityNodeInfosByText(key))) {
                LogUtils.i(LogFlag, "找到: " + key);
                return true;
            }
            if (performScroll(nodeInfo) && isContainAllKeys(nodeInfo, key)) {
                return true;
            }
        }
        return false;
    }

    private boolean clickView(AccessibilityNodeInfo source, ClickViewBean clickViewBean) {
        if (!TextUtils.isEmpty(clickViewBean.getViewWord())) {
            return clickViewByText(source, clickViewBean);
        }
        if (!TextUtils.isEmpty(clickViewBean.getViewId())) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                String viewId = clickViewBean.getViewId();
                if (viewId.contains("||")) {
                    String[] viewIds = viewId.split("\\|\\|");
                    for (String id : viewIds) {
                        if (clickViewByID(source, id, clickViewBean)) {
                            return true;
                        }
                    }
                } else {
                    return clickViewByID(source, viewId, clickViewBean);
                }
            }
        }
        return false;
    }

    /**
     * @param bean 1返回，2返回并黑屏并回到桌面，3回到桌面，4悬浮窗锁屏， 5模拟点击
     */

    private void actionDo(AccessibilityBean bean) {
        int action = bean.getAction();
        switch (action) {
            case 1:
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                break;
            case 2:
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                EventBus.getDefault().post(new AccessEvent(2));
                break;
            case 3:
                performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
                AccessUtil.backToDesk();
                break;
            case 4:
                EventBus.getDefault().post(new AccessEvent(4));
                break;
            case 5:
                break;
            default:
                break;
        }
    }

    /**
     * 在当前页面查找文字内容并点击
     */
    public boolean clickViewByText(AccessibilityNodeInfo nodeInfo, ClickViewBean clickViewBean) {
        String viewWord = clickViewBean.getViewWord();
        String viewType = clickViewBean.getViewType();
        if (nodeInfo != null) {
            if (nodeInfo.getText() != null) {
                LogUtils.i(LogFlag, viewWord + "--找到--" + nodeInfo.getText().toString());
            }
            List<AccessibilityNodeInfo> nodeInfos = nodeInfo.findAccessibilityNodeInfosByText(viewWord);
            LogUtils.i(LogFlag, "nodeInfoList.size：" + nodeInfos.size() + viewWord);
            if (CollectionUtils.isNotEmpty(nodeInfos)) {
                for (AccessibilityNodeInfo node : nodeInfos) {
                    if (viewType.contains(node.getClassName().toString())) {
                        LogUtils.i(LogFlag, "点击: " + node.getText().toString());
                        performClick(node);
                        if (clickViewBean.getCallBack()) {
                            EventBus.getDefault().post(new AccessEvent(6));
                            if (DeviceTypeUtils.isVivoDevice()) {
                                AccessibilityManager.getInstance().stop();
                            }
                        }
                        return true;
                    }
                }
            }
            if (performScroll(nodeInfo) && clickViewByText(nodeInfo, clickViewBean)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 在当前页面查找id并点击
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public boolean clickViewByID(AccessibilityNodeInfo nodeInfo, String viewId, ClickViewBean clickViewBean) {
        String viewType = clickViewBean.getViewType();
        boolean checked = clickViewBean.getChecked();
        List<AccessibilityNodeInfo> nodeInfoList = nodeInfo.findAccessibilityNodeInfosByViewId(viewId);
        LogUtils.i(LogFlag, "nodeInfoList.size：" + nodeInfoList.size() + viewId);
        if (CollectionUtils.isNotEmpty(nodeInfoList)) {
            for (AccessibilityNodeInfo node : nodeInfoList) {
                if (node != null && node.getClassName().toString().equals(viewType)) {
                    if (node.isCheckable()) {
                        LogUtils.i(LogFlag, "isChecked：" + node.isChecked());
                        if (node.isChecked() != checked) {
                            performClick(node);
                        }
                    } else {
                        performClick(node);
                    }
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 模拟滑动事件
     *
     * @param nodeInfo
     */
    private boolean performScroll(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return false;
        }
        if (nodeInfo.isScrollable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
            LogUtils.i(LogFlag, "模拟上滑");
            return true;
        } else {
            for (int i = 0; i < nodeInfo.getChildCount(); i++) {
                if (performScroll(nodeInfo.getChild(i))) {
                    continue;
                }
            }
        }
        return false;
    }

    /**
     * 模拟点击事件
     *
     * @param nodeInfo
     */

    private void performClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.isClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            performClick(nodeInfo.getParent());
        }
    }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        // 发送辅助功能已经打开的事件
        AccessibilityManager.getInstance().setAccessibility(1);
        AccessibilityManager.getInstance().setContext(this);
        AccessibilityManager.getInstance().setData(AccessibilityData.datas);
        EventBus.getDefault().post(new AccessEvent(6));
        LogUtils.d(LogFlag + "onServiceConnected");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        // 设置辅助功能关闭状态
        AccessibilityManager.getInstance().setAccessibility(0);
        LogUtils.d(LogFlag + "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        // 设置辅助功能关闭状态
        AccessibilityManager.getInstance().setAccessibility(2);
        LogUtils.d(LogFlag + "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onInterrupt() {
    }
}
