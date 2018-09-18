package com.shenzhou.intelligenceordering.print;

/**
 * Created by ww on 2018/9/8.
 * 返回打印机状态和错误信息接口
 */

public interface PrintStatusListener {
    //打印机返回状态
    public void onPrinterListener(int status);

    //错误状态
    public void onMessageListener(int msgId, String msgContent);

}
