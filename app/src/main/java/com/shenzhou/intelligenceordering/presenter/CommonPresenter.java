package com.shenzhou.intelligenceordering.presenter;


import com.shenzhou.intelligenceordering.bean.ResultVo;
import com.shenzhou.intelligenceordering.presenter.view.CommonView;
import com.shenzhou.intelligenceordering.presenter.view.MyView;

import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 基本请求
 */
public class CommonPresenter extends BasePresenter{

    private CommonView commonView;
    private ResultVo resultVo;

    @Override
    public void attachView(MyView view) {
        commonView = (CommonView)view;
    }

    //回执打印状态
    public void updateOrderFlagReq(Map<String, String> map){
        mCompositeSubscription.add(manager.updateOrderFlagReq(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ResultVo>() {
                    @Override
                    public void onCompleted() {
                        if (resultVo != null){
                            commonView.onSuccess(resultVo);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        commonView.onError("请求失败");
                    }

                    @Override
                    public void onNext(ResultVo rv) {
                        resultVo = rv;
                    }
                })
        );
    }
}
