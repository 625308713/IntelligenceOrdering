package com.shenzhou.intelligenceordering.presenter;


import com.shenzhou.intelligenceordering.bean.OrderCollectionResult;
import com.shenzhou.intelligenceordering.presenter.view.MyView;
import com.shenzhou.intelligenceordering.presenter.view.OrderCollectionView;

import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 菜单列表(多个)
 */
public class OrderCollectionPresenter extends BasePresenter{
    private OrderCollectionView orderView;
    private OrderCollectionResult orderResult;

    @Override
    public void attachView(MyView view) {
        orderView = (OrderCollectionView)view;
    }

    public void orderCollectionReq(Map<String, String> map){
        mCompositeSubscription.add(manager.orderCollectionReq(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderCollectionResult>() {
                    @Override
                    public void onCompleted() {
                        if (orderResult != null){
                            orderView.onSuccess(orderResult);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        orderView.onError("请求失败");
                    }

                    @Override
                    public void onNext(OrderCollectionResult rv) {
                        orderResult = rv;
                    }
                })
        );
    }
}
