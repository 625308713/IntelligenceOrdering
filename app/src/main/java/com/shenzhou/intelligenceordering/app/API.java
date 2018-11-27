package com.shenzhou.intelligenceordering.app;

public class API {
    //庞大涛
//    public static final String baseUrl = "http://192.168.32.103:8080/dc/";
    //测试
//    public static final String baseUrl = "http://192.168.2.213:8080/dc/";
    //旧服务器
//    public static final String baseUrl = "http://52.215.91.43/dc/";
    //新服务器
    public static final String baseUrl = "http://35.177.50.153/dc/";
    //请求菜单间隔 10秒
    public static int REQUEST_ORDERING_TIME  = 10 * 1000;



    /**********************RxBus-caode**********************/
    public static final int R_1 = 1;//修改密码完成，退出系统回到登录页面
    public static final int R_2 = 2;//打印完成，可打印下一条
}
