package com.shenzhou.intelligenceordering.presenter;


import com.shenzhou.intelligenceordering.bean.OrderResult;
import com.shenzhou.intelligenceordering.bean.ResultVo;
import com.shenzhou.intelligenceordering.presenter.view.CommonView;
import com.shenzhou.intelligenceordering.presenter.view.MyView;
import com.shenzhou.intelligenceordering.presenter.view.OrderView;

import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 菜单列表
 */
public class OrderListPresenter extends BasePresenter{
    private OrderView orderView;
    private OrderResult orderResult;

    @Override
    public void attachView(MyView view) {
        orderView = (OrderView)view;
    }

    //获取菜单列表
    public void orderListReq(Map<String, String> map){
        mCompositeSubscription.add(manager.orderListReq(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<OrderResult>() {
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
                    public void onNext(OrderResult rv) {
                        orderResult = rv;
                    }
                })
        );
    }
}
