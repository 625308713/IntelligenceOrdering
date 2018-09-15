package com.shenzhou.intelligenceordering.presenter.view;


import com.shenzhou.intelligenceordering.bean.Book;

public interface BookView extends MyView {
    void onSuccess(Book mBook);
    void onError(String result);
}
