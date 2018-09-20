package com.shenzhou.intelligenceordering.print;

/**
 * Created by ww on 2018/9/10.
 */

public class Constants {

    /*  激活免丢单功能 */
    public static byte[] COMM_LOSS_ORDER_ON = {0x1B,0x73,0x42,0x45,(byte) 0x92, (byte) 0x9A,0x01,0x00,0x5F,0x0A};
    /*  打开自动状态返回 */
    public static byte[] COMM_BACK_STATUS_ON = {0x1D,0x61,0x0F};
    /*  切纸 */
    public static byte[] COMM_CUT_PAPER = {0x0A,0x0A,0x1D,0x56,0x01};

    /*  初始化打印机 */
    public static byte[] COMM_INIT_PRINTER = {0x1B, 0x41};
    /*  实时清除打印机状态 */
    /*  恢复【清除禁止打印位完成】为【正常状态】4.5=0  */
    public static byte[] COMM_CLEAR_OFF_PRINT = {0x10, 0x06, 0x07, 0x08, 0x04};
    /*  恢复正常 3.5=0  */
    public static byte[] COMM_CLEAR_NOCOMPLETE = {0x10, 0x06, 0x07, 0x08, 0x08};



    /*
    打印机返回主界面状态标识
     */
    public static final int RESP_STATUS_OK = 1000;  //状态正常
    public static final int RESP_STATUS_PRINTING = 1001; //正在打印

    public static final int RESP_STATUS_OUT_OF_PAPER = 1011; //缺纸
    public static final int RESP_STATUS_OPEN_TOP = 1012; //打印机盒盖打开
    public static final int RESP_STATUS_OTHER_OFF_PRINT = 1013; //出错后禁止打印

    public static final int RESP_STATUS_NOCOMPLETE_OFF_PRINT = 1101; //错误恢复 禁止打印，未完成
    public static final int RESP_STATUS_NOCOMPLETE_CLS_OFF_PRINT = 1102; //未完成,清除禁止打印状态,并设置清除完成
    public static final int RESP_STATUS_NOCOMPLETE_NORMAL_PRINT = 1103; //未完成,打印正常



    /**
     * 连接、数据信息
     */
    public static final int RESP_MSG_CONNECTING = 2001; //正在连接
    public static final int RESP_MSG_CONNECT_SUCCESS = 2002; //连接成功
    public static final int RESP_MSG_HOST_ERROR = 2003; //地址不存在或没有找到打印机
    public static final int RESP_MSG_CONNECT_EXCEPTION = 2004;  //连接异常或被拒绝
    public static final int RESP_MSG_SOCKET_TIMEOUT= 2005;  //连接超时
    public static final int RESP_MSG_RECONNECT = 2006; //正在重连
    public static final int RESP_MSG_DISCONNECT = 2007; //断开连接

    public static final int RESP_MSG_PRINT_SUCCESS = 2100; //打印成功
    public static final int RESP_MSG_PRINT_FAILED = 2200; //打印失败

    public static final int IP_NULL = 3000; //IP为空
}
