package com.shenzhou.intelligenceordering.presenter;


import com.shenzhou.intelligenceordering.presenter.view.MyView;

/**
 * 用于网络的请求以及数据的获取
 */
public interface Presenter {
    void onCreate();

    void onStop();

    void attachView(MyView view);
}
