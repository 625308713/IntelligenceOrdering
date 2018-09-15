package com.shenzhou.intelligenceordering.presenter.view;

import com.shenzhou.intelligenceordering.bean.ResultVo;

public interface CommonView extends MyView {
    void onSuccess(ResultVo resultVo);
    void onError(String result);
}
