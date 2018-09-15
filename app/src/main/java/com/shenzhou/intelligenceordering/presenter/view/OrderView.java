package com.shenzhou.intelligenceordering.presenter.view;

import com.shenzhou.intelligenceordering.bean.OrderResult;

public interface OrderView extends MyView {
    void onSuccess(OrderResult resultVo);
    void onError(String result);
}
