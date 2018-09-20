package com.shenzhou.intelligenceordering.bean;

public class OrderItem {
    private String cpName;//菜品名称
    private int cpNum;//菜品数量
    private String cpPro;//备注信息

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
}
