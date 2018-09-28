package com.shenzhou.intelligenceordering.app;

import android.app.Application;

import com.blankj.utilcode.util.Utils;


public class MyApplication extends Application{
    public static int goodsType = 0;//(0代表菜品，1代表酒水)

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
