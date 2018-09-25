package com.shenzhou.intelligenceordering.request;


import com.shenzhou.intelligenceordering.bean.LoginResult;
import com.shenzhou.intelligenceordering.bean.OrderCollectionResult;
import com.shenzhou.intelligenceordering.bean.OrderResult;
import com.shenzhou.intelligenceordering.bean.ResultVo;
import com.shenzhou.intelligenceordering.bean.VersionResult;

import java.util.Map;

import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface RetrofitService {
    //登录
    @POST("hc/login.action")
    Observable<LoginResult> loginReq(@QueryMap Map<String, String> map);

    //修改密码
    @POST("hc/modifyPassword.action")
    Observable<LoginResult> modifyPsdReq(@QueryMap Map<String, String> map);

    //请求菜单列表(单个菜品)
    @POST("hc/searchPrintList.action")
    Observable<OrderResult> orderListReq(@QueryMap Map<String, String> map);

    //请求菜单列表(多个菜品)
    @POST("hc/searchOrderPrintList.action")
    Observable<OrderCollectionResult> orderCollectionReq(@QueryMap Map<String, String> map);

    //修改打印状态
    @POST("hc/modifyOrderPrint.action")
    Observable<ResultVo> updateOrderFlagReq(@QueryMap Map<String, String> map);

    //修改打印状态
    @POST("hc/modifyOrderPrint.action")
    Observable<ResultVo> modifyOrderPrintInfo(@QueryMap Map<String, String> map);

    //获取版本信息
    @POST("hc/searchHcAppVersion.action")
    Observable<VersionResult> getVersionInfo();
}
