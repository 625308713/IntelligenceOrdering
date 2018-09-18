package com.shenzhou.intelligenceordering.print;

import com.shenzhou.intelligenceordering.bean.OrderBean;

/**
 * 打印小票工具和模板类
 * Created by ww on 2018/9/17.
 */

public class PrintFormatUtil {
    public static final byte EOT = 4;
    public static final byte ENQ = 5;
    public static final byte HT = 9;
    public static final byte LF = 10;
    public static final byte FF = 12;
    public static final byte CR = 13;
    public static final byte DLE = 16;
    public static final byte DC4 = 20;
    public static final byte CAN = 24;
    public static final byte ESC = 27;
    public static final byte FS = 28;
    public static final byte GS = 29;
    public static final byte SP = 32;
    public static final byte D = 68;
    public static final byte E = 69;
    public static final byte G = 71;
    public static final byte J = 74;
    public static final byte L = 76;
    public static final byte M = 77;
    public static final byte R = 82;
    public static final byte S = 83;
    public static final byte T = 84;
    public static final byte V = 86;

    //字体大小
    public static final int FONT_ONE_SIZE = 0;  // 正常
    public static final int FONT_ONE_HEIGH = 1;  // 倍高
    public static final int FONT_ONE_WIDTH = 2;  // 倍宽
    public static final int FONT_TWO_SIZE = 11;  // 2倍
    public static final int FONT_TWO_HEIGH = 12;  // 2倍高
    public static final int FONT_TWO_WIDTH = 13;  // 2倍宽
    public static final int FONT_THREE_SIZE = 31;    // 3倍

    //对齐方式
    public static final int ALIGN_LEFT = 0;   // 靠左
    public static final int ALIGN_CENTER = 1;  // 居中
    public static final int ALIGN_RIGHT = 2;  // 靠右

    //加粗模式
    public static final int FONT_BOLD = 0;       // 字体加粗
    public static final int FONT_BOLD_CANCEL = 1;    // 取消加粗

    //切纸模式
    public static final int CUT_ALL = 0;       // 全切
    public static final int CUT_HALF = 1;    // 半切
    public static final int CUT_FEED_HALF = 66; //进纸并半切

    /**
     * 打印换行 2
     * @return
     */
    public static String cmdLineFeed(){
        byte[] data = {(byte)0x0A};
        return new String(data);
    }

    /**
     * 恢复字符默认
     * @return
     */
    public static String cmdResetFontDefault() {
        byte[] data = {(byte) 0x1B, (byte) 0x21, (byte) 0x0};
        return new String(data);
    }

    /**
     * 加粗模式 22
     * @param fontBold
     * @return
     */
    public static String cmdFontBold(int fontBold) {
        byte[] data = {(byte) 0x1B, (byte) 0x45, (byte) 0x0};

        if (fontBold == FONT_BOLD) {
            data[2] = (byte) 0x01;
        } else if (fontBold == FONT_BOLD_CANCEL) {
            data[2] = (byte) 0x00;
        }

        return new String(data);
    }

    /**
     * 打印并走纸n行  24
     * @param lineCount [0,255]
     * @return
     */

    public static String cmdBlankLines(int lineCount){
        byte[] data = {(byte) 0x1B, (byte) 0x4A, (byte) 0x0};
        if(lineCount<255){
            data[2] = (byte) lineCount;
        }
        return new String(data);
    }

    /**
     * 对齐方式 33
     * @param alignMode
     * @return
     */
    public static String cmdAlign(int alignMode) {
        byte[] data = {(byte) 0x1B, (byte) 0x61, (byte) 0x0};

        if (alignMode == ALIGN_LEFT) {
            data[2] = (byte) 0x00;
        } else if (alignMode == ALIGN_CENTER) {
            data[2] = (byte) 0x01;
        } else if (alignMode == ALIGN_RIGHT) {
            data[2] = (byte) 0x02;
        }

        return new String(data);
    }

    /**
     * 字体大小 43
     * @param fontSize
     * @return
     */
    public static String cmdFontSize(int fontSize){
        byte[] data = {(byte) 0x1D, (byte) 0x21, (byte) 0x0};

        if (fontSize == FONT_ONE_SIZE) {
            data[2] = (byte) 0x00;
        } else if (fontSize == FONT_ONE_HEIGH) {
            data[2] = (byte) 0x01;
        } else if (fontSize == FONT_ONE_WIDTH) {
            data[2] = (byte) 0x10;
        } else if (fontSize == FONT_TWO_SIZE) {
            data[2] = (byte) 0x11;
        } else if (fontSize == FONT_TWO_HEIGH) {
            data[2] = (byte) 0x12;
        } else if (fontSize == FONT_TWO_WIDTH) {
            data[2] = (byte) 0x21;
        } else if (fontSize == FONT_THREE_SIZE) {
            data[2] = (byte) 0x22;
        }
        return new String(data);
    }

    /**
     * 设置横向或纵向的移动单位 52
     * @param iXUnit   1单位 = 25.4/iUnit mm
     * @return
     */
    public static String cmdMoveUnit(int iXUnit, int iYUnit){
        byte[] data = {(byte) 0x1D, (byte) 0x50, (byte) 0x0, (byte) 0x0};
        data[2] = (byte) iXUnit;
        data[3] = (byte) iYUnit;
        return new String(data);
    }

    /**
     * 切纸模式 53
     * @param cutMode
     * @param feedUnit 进纸几个单位
     * @return
     */
    public static String cmdCutPaper(int cutMode, int feedUnit){
        byte[] data = {(byte) 0x1D, (byte) 0x56, (byte) 0x0, (byte) 0x0};
        if (cutMode == CUT_ALL) {
            data[2] = (byte) 0;
        } else if (cutMode == CUT_HALF) {
            data[2] = (byte) 1;
        } else if (cutMode == CUT_FEED_HALF) {
            data[2] = (byte) 66;
            data[3] = (byte) feedUnit;
        }
        return new String(data);
    }

    public static String getFoodMenuTemplate(OrderBean menu){
        StringBuffer sb = new StringBuffer();
        sb.append(cmdResetFontDefault());
        sb.append(cmdMoveUnit(0, 8));
        sb.append(cmdAlign(ALIGN_LEFT));
        sb.append(cmdFontSize(FONT_ONE_HEIGH));
        sb.append("订单号：" + menu.getOrderNo());
        sb.append(cmdLineFeed());
        sb.append("桌号：" + menu.getZtInfo());
        sb.append(cmdLineFeed());
        sb.append("时间：" + menu.getOrderTime());
        sb.append(cmdLineFeed());
        sb.append(cmdResetFontDefault());
        sb.append("-----------------------------------------------");
        sb.append(cmdLineFeed());
        sb.append(cmdBlankLines(1));
        sb.append(cmdFontSize(FONT_THREE_SIZE));
        sb.append(cmdAlign(ALIGN_CENTER));
        sb.append(menu.getCpName());
        sb.append(cmdLineFeed());
        sb.append(cmdBlankLines(1));
        sb.append(cmdResetFontDefault());
        sb.append(cmdAlign(ALIGN_LEFT));
        sb.append("-----------------------------------------------");
        sb.append(cmdLineFeed());
        sb.append(cmdFontSize(FONT_ONE_HEIGH));
        sb.append("数量：" + menu.getCpNum());
        sb.append(cmdLineFeed());
        sb.append("备注：" + menu.getCpPro());
        sb.append(cmdLineFeed());
        sb.append(cmdBlankLines(3));
//        sb.append(cmdCutPaper(CUT_FEED_HALF, 1));
        return sb.toString();
    }
}
