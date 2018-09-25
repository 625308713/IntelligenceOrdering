package com.shenzhou.intelligenceordering.bean;

import java.util.List;

public class OrderCollectionResult extends ResultVo<OrderPojo>{
    private List<OrderPojo> orderPojos;

    public List<OrderPojo> getOrderPojos() {
        return orderPojos;
    }

    public void setOrderPojos(List<OrderPojo> orderPojos) {
        this.orderPojos = orderPojos;
    }
}
