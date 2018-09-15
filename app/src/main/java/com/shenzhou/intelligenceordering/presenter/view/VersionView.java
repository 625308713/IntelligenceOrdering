package com.shenzhou.intelligenceordering.presenter.view;


import com.shenzhou.intelligenceordering.bean.VersionResult;

public interface VersionView extends MyView {
    void onSuccess(VersionResult vb);
    void onError(String result);
}
