package com.gkail.tools.accessibility.accessibility.bean;

public class AccessibilityBean {
    /**
     * 与clsName只能二选一，且后面只能跟action属性
     */
    private String pkgName;

    /**
     * 只能写一个
     * 不能为空
     */
    private String className;

    /**
     * 可以写多个，必须为并集，用英文状态&&进行分割
     */
    private String pageWords;

    /**
     * 可以写多个，必须为并集，用英文状态&&进行分割
     */
    private String pageViewId;

    /**
     * 事件类型，1返回，2黑屏并回到桌面，3回到桌面，4悬浮窗锁屏， 5模拟点击
     */
    private int action;

    /**
     * 延迟多少毫秒后执行
     */
    private long delaytime;
    /**
     * #action为5时需要有此对象。
     */
    private ClickViewBean clickView;


    public AccessibilityBean(String pkgName, String className, String pageWords,
                             String pageViewId, int action, ClickViewBean clickView) {
        this.pkgName = pkgName;
        this.className = className;
        this.pageWords = pageWords;
        this.pageViewId = pageViewId;
        this.action = action;
        this.clickView = clickView;
    }

    public AccessibilityBean(String pkgName, String className, String pageWords,
                             String pageViewId, int action, ClickViewBean clickView, long delaytime) {
        this.pkgName = pkgName;
        this.className = className;
        this.pageWords = pageWords;
        this.pageViewId = pageViewId;
        this.action = action;
        this.clickView = clickView;
        this.delaytime=delaytime;
    }

    public String getPageWords() {
        return pageWords;
    }


    public int getAction() {
        return action;
    }

    public ClickViewBean getClickView() {
        return clickView;
    }

    public String getClassName() {
        return className;
    }

    public String getPkgName() {
        return pkgName;
    }

    public String getPageViewId() {
        return pageViewId;
    }


    @Override
    public String toString() {
        return "AccessibilityBean{" +
                "pkgName='" + pkgName + '\'' +
                ", className='" + className + '\'' +
                ", pageWords='" + pageWords + '\'' +
                ", pageViewId='" + pageViewId + '\'' +
                ", action=" + action +
                ", clickView=" + clickView +
                '}';
    }
}
