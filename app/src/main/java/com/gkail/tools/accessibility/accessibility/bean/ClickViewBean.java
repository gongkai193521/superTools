package com.gkail.tools.accessibility.accessibility.bean;


/**
 * "viewWord" : string, #可选参数，与viewId二选一，点击的view上有什么文字，只能写一个
 * "viewId":string,#可选参数，与viewWord二选一，点击的view的id是什么，如"com.android.settings:id/action_button"
 * "viewType": string, #必选参数，用来确定是点击哪个view如"android.widget.Button"，"android.widget.CheckBox"
 * "checked":boolean,#可选参数，view点击前的状态，譬如checkbox上可能会用到,true选中 false取消选中
 */
public class ClickViewBean {
    private String viewWord;
    private String viewId;
    private String viewType;
    private boolean checked;
    private boolean callBack;

    public ClickViewBean(String viewWord, String viewId, String viewType, boolean checked, boolean callBack) {
        this.viewWord = viewWord;
        this.viewId = viewId;
        this.viewType = viewType;
        this.checked = checked;
        this.callBack = callBack;
    }

    public String getViewWord() {
        return viewWord;
    }


    public String getViewId() {
        return viewId;
    }


    public String getViewType() {
        return viewType;
    }

    public boolean getChecked() {
        return checked;
    }

    public boolean getCallBack() {
        return callBack;
    }

    @Override
    public String toString() {
        return "ClickViewBean{" +
                "viewWord='" + viewWord + '\'' +
                ", viewId='" + viewId + '\'' +
                ", viewType='" + viewType + '\'' +
                ", checked=" + checked +
                ", callBack=" + callBack +
                '}';
    }
}
