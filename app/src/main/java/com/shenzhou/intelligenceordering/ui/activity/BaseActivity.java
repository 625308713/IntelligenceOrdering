package com.shenzhou.intelligenceordering.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {
    protected Context myContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView();
        myContext = this;
        initView();
        initOther();
        toDo();
    }

    /**
     * 设置布局
     */
    protected abstract void setContentView();

    /**
     * 初始化组件
     */
    protected abstract void initView();

    /**
     * 初始化业务需要
     */
    protected abstract void initOther();

    /**
     * 业务逻辑处理
     */
    protected abstract void toDo();
}
