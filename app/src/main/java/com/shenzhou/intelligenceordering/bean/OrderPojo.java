package com.shenzhou.intelligenceordering.bean;

import java.util.List;

public class OrderPojo {
    private int id;
    private String orderTime;//点餐时间
    private String orderNo;//订单编号
    private String ztInfo;//桌号
    private int isPrint;//是否打印：0代表未打印，1代表已打印
    private List<OrderItem> orderItems;

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

    public int getIsPrint() {
        return isPrint;
    }

    public void setIsPrint(int isPrint) {
        this.isPrint = isPrint;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
