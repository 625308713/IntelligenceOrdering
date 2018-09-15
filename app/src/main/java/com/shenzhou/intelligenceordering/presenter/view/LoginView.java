package com.shenzhou.intelligenceordering.presenter.view;

import com.shenzhou.intelligenceordering.bean.LoginResult;

public interface LoginView extends MyView {
    void onSuccess(LoginResult resultVo);
    void onError(String result);
}
