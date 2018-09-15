package com.shenzhou.intelligenceordering.presenter;


import com.shenzhou.intelligenceordering.presenter.view.MyView;
import com.shenzhou.intelligenceordering.request.DataManager;

import rx.subscriptions.CompositeSubscription;

public class BasePresenter implements Presenter{
    protected DataManager manager;
    protected CompositeSubscription mCompositeSubscription;

    @Override
    public void onCreate() {
        manager = new DataManager();
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onStop() {
        if (mCompositeSubscription.hasSubscriptions()){
            mCompositeSubscription.unsubscribe();
        }
    }

    @Override
    public void attachView(MyView view) {

    }
}
