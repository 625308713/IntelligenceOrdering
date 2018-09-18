package com.shenzhou.intelligenceordering.print;

/**
 * Created by ww on 2018/9/10.
 * 打印机错误类型
 */

public enum PrintStatus {

    /**
     * 打印机返回状态
     */
    PS_OK("1400000F", Constants.RESP_STATUS_OK),  //打印机正常
    PS_PRINTING("1400400F", Constants.RESP_STATUS_PRINTING), //正在打印

    PS_OUT_OF_PAPER("1C002C4F", Constants.RESP_STATUS_OUT_OF_PAPER), //缺纸
    PS_OPEN_TOP("3C00000F", Constants.RESP_STATUS_OPEN_TOP), //打印机盒盖打开 只判断第一位
    PS_OTHER_OFF_PRINT("1C00204F", Constants.RESP_STATUS_OTHER_OFF_PRINT), //出错后禁止打印

    PS_NOCOMPLETE_OFF_PRINT("1400204F", Constants.RESP_STATUS_NOCOMPLETE_OFF_PRINT), //错误恢复 禁止打印，未完成
    PS_NOCOMPLETE_CLS_OFF_PRINT("1400202F", Constants.RESP_STATUS_NOCOMPLETE_CLS_OFF_PRINT), //未完成,清除禁止打印状态,并设置清除完成 有【IB 41】发出
    PS_NOCOMPLETE_NORMAL_PRINT("1400200F", Constants.RESP_STATUS_NOCOMPLETE_NORMAL_PRINT); //未完成,打印正常 有【10 06 07 08 04】发出


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    private int code;

    PrintStatus(String value, int code) {
        this.value = value;
        this.code = code;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
