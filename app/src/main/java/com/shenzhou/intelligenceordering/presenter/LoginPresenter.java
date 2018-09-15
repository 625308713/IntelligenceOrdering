package com.shenzhou.intelligenceordering.presenter;


import com.shenzhou.intelligenceordering.bean.LoginResult;
import com.shenzhou.intelligenceordering.presenter.view.LoginView;
import com.shenzhou.intelligenceordering.presenter.view.MyView;

import java.util.Map;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class LoginPresenter extends BasePresenter{
    private LoginView loginView;
    private LoginResult loginResult;

    @Override
    public void attachView(MyView view) {
        loginView = (LoginView)view;
    }
    //登录
    public void loginReq(Map<String, String> map){
        mCompositeSubscription.add(manager.loginReq(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResult>() {
                    @Override
                    public void onCompleted() {
                        if (loginResult != null){
                            //这里请求完成，把结果回调给Activity
                            loginView.onSuccess(loginResult);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        loginView.onError("请求失败");
                    }

                    @Override
                    public void onNext(LoginResult rv) {
                        loginResult = rv;
                    }
                })
        );
    }
    //修改密码
    public void modifyPsdReq(Map<String, String> map){
        mCompositeSubscription.add(manager.modifyPsdReq(map)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResult>() {
                    @Override
                    public void onCompleted() {
                        if (loginResult != null){
                            //这里请求完成，把结果回调给Activity
                            loginView.onSuccess(loginResult);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        loginView.onError("请求失败");
                    }

                    @Override
                    public void onNext(LoginResult rv) {
                        loginResult = rv;
                    }
                })
        );
    }
}
