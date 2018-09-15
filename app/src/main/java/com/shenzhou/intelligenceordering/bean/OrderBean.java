package com.shenzhou.intelligenceordering.bean;

/**
 * 菜单
 */
public class OrderBean{
    private int id;
    private String orderTime;//点餐时间
    private String orderNo;//订单编号
    private String ztInfo;//桌号
    private String cpName;//菜品名称
    private int cpNum;//菜品数量
    private String cpPro;//备注信息
    private int isPrint;//是否打印：0代表未打印，1代表已打印

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getZtInfo() {
        return ztInfo;
    }

    public void setZtInfo(String ztInfo) {
        this.ztInfo = ztInfo;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public int getCpNum() {
        return cpNum;
    }

    public void setCpNum(int cpNum) {
        this.cpNum = cpNum;
    }

    public String getCpPro() {
        return cpPro;
    }

    public void setCpPro(String cpPro) {
        this.cpPro = cpPro;
    }

    public int getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(int isPrint) {
        this.isPrint = isPrint;
    }
}
