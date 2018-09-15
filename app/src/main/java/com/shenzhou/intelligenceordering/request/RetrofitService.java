package com.shenzhou.intelligenceordering.request;


import com.shenzhou.intelligenceordering.bean.Book;
import com.shenzhou.intelligenceordering.bean.LoginResult;
import com.shenzhou.intelligenceordering.bean.OrderResult;
import com.shenzhou.intelligenceordering.bean.ResultVo;
import com.shenzhou.intelligenceordering.bean.VersionResult;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface RetrofitService {
    //请求版本信息
//    @GET("jsonapi/update_test.json")
//    Observable<VersionBean> getVersionInfo();

    //已确定入参名称
    @GET("book/search")
    Observable<Book> getSearchBook(@Query("q") String name,
                                   @Query("tag") String tag,
                                   @Query("start") int start,
                                   @Query("count") int count);
    //多个不确定入参
    @GET("book/search")
    Observable<Book> getSearchBook2(@QueryMap Map<String, String> options);

    //请求url中间有个值是变量，需要用Path注解
    @GET("group/{id}/users")
    Observable<Book> groupList(@Path("id") int groupId, @Query("sort") String sort);

    //指定一个对象作为HTTP请求体
    @POST("users/new")
    Observable<Book> createBook(@Body Book book);

    //登录
    @POST("hc/login.action")
    Observable<LoginResult> loginReq(@QueryMap Map<String, String> map);

    //修改密码
    @POST("hc/modifyPassword.action")
    Observable<LoginResult> modifyPsdReq(@QueryMap Map<String, String> map);

    //请求菜单列表
    @POST("hc/searchPrintList.action")
    Observable<OrderResult> orderListReq(@QueryMap Map<String, String> map);

    //修改打印状态
    @POST("hc/modifyOrderPrint.action")
    Observable<ResultVo> updateOrderFlagReq(@QueryMap Map<String, String> map);

    //获取版本信息
    @POST("hc/searchHcAppVersion.action")
    Observable<VersionResult> getVersionInfo();
}
