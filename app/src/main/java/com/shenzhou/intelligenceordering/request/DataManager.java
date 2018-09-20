package com.shenzhou.intelligenceordering.request;


import com.shenzhou.intelligenceordering.bean.LoginResult;
import com.shenzhou.intelligenceordering.bean.OrderCollectionResult;
import com.shenzhou.intelligenceordering.bean.OrderResult;
import com.shenzhou.intelligenceordering.bean.ResultVo;
import com.shenzhou.intelligenceordering.bean.VersionResult;

import java.util.Map;

import rx.Observable;

/**
 * 方便的调用RetrofitService 中定义的方法
 */
public class DataManager {
    private RetrofitService mRetrofitService;
    public DataManager(){
        this.mRetrofitService = RetrofitHelper.getInstance().getServer();
    }

    //登录
    public Observable<LoginResult> loginReq(Map<String, String> map){
        return mRetrofitService.loginReq(map);
    }

    //修改密码
    public Observable<LoginResult> modifyPsdReq(Map<String, String> map){
        return mRetrofitService.modifyPsdReq(map);
    }

    //请求菜单列表(单个)
    public Observable<OrderResult> orderListReq(Map<String, String> map){
        return mRetrofitService.orderListReq(map);
    }

    //请求菜单列表(多个)
    public Observable<OrderCollectionResult> orderCollectionReq(Map<String, String> map){
        return mRetrofitService.orderCollectionReq(map);
    }

    //修改打印状态1
    public Observable<ResultVo> updateOrderFlagReq(Map<String, String> map){
        return mRetrofitService.updateOrderFlagReq(map);
    }

    //修改打印状态2
    public Observable<ResultVo> modifyOrderPrintInfo(Map<String, String> map){
        return mRetrofitService.modifyOrderPrintInfo(map);
    }

    //获取版本信息
    public Observable<VersionResult> getVersionInfo(){
        return mRetrofitService.getVersionInfo();
    }
}
