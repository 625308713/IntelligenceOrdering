package com.shenzhou.intelligenceordering.request;


import com.shenzhou.intelligenceordering.bean.Book;
import com.shenzhou.intelligenceordering.bean.LoginResult;
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
    public Observable<Book> getSearchBooks(String name, String tag, int start, int count){
        return mRetrofitService.getSearchBook(name,tag,start,count);
    }
    //登录
    public Observable<LoginResult> loginReq(Map<String, String> map){
        return mRetrofitService.loginReq(map);
    }

    //修改密码
    public Observable<LoginResult> modifyPsdReq(Map<String, String> map){
        return mRetrofitService.modifyPsdReq(map);
    }

    //请求菜单列表
    public Observable<OrderResult> orderListReq(Map<String, String> map){
        return mRetrofitService.orderListReq(map);
    }

    //修改打印状态
    public Observable<ResultVo> updateOrderFlagReq(Map<String, String> map){
        return mRetrofitService.updateOrderFlagReq(map);
    }

    //获取版本信息
    public Observable<VersionResult> getVersionInfo(){
        return mRetrofitService.getVersionInfo();
    }
}
