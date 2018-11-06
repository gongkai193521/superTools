package com.gkail.tools.accessibility.accessibility.bean;

public class AccessEvent {
    //2黑屏，4悬浮窗锁屏，6辅助功能开启
    private int type;

    public AccessEvent(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
