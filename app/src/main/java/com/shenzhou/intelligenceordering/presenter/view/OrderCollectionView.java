package com.shenzhou.intelligenceordering.presenter.view;

import com.shenzhou.intelligenceordering.bean.OrderCollectionResult;
import com.shenzhou.intelligenceordering.bean.OrderResult;
import com.shenzhou.intelligenceordering.presenter.view.MyView;

public interface OrderCollectionView extends MyView {
    void onSuccess(OrderCollectionResult resultVo);
    void onError(String result);
}
