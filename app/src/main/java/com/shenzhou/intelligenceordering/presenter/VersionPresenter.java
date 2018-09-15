package com.shenzhou.intelligenceordering.presenter;

import com.shenzhou.intelligenceordering.bean.VersionResult;
import com.shenzhou.intelligenceordering.presenter.view.MyView;
import com.shenzhou.intelligenceordering.presenter.view.VersionView;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 版本更新
 */
public class VersionPresenter extends BasePresenter{
    private VersionView versionView;
    private VersionResult versionBean;
    @Override
    public void attachView(MyView view) {
        versionView = (VersionView)view;
    }
    public void getVersionInfo(){
        mCompositeSubscription.add(manager.getVersionInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<VersionResult>() {
                    @Override
                    public void onCompleted() {
                        if (versionBean != null){
                            versionView.onSuccess(versionBean);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        versionView.onError("请求失败！！");
                    }

                    @Override
                    public void onNext(VersionResult vb) {
                        versionBean = vb;
                    }
                })
        );
    }
}
