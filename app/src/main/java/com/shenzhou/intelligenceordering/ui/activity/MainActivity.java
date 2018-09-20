package com.shenzhou.intelligenceordering.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.shenzhou.intelligenceordering.R;
import com.shenzhou.intelligenceordering.adapter.OrderListAdapter;
import com.shenzhou.intelligenceordering.app.API;
import com.shenzhou.intelligenceordering.bean.OrderBean;
import com.shenzhou.intelligenceordering.bean.OrderResult;
import com.shenzhou.intelligenceordering.bean.ResultVo;
import com.shenzhou.intelligenceordering.dialog.ModifyIpDialog;
import com.shenzhou.intelligenceordering.dialog.PersonInfoDialog;
import com.shenzhou.intelligenceordering.presenter.CommonPresenter;
import com.shenzhou.intelligenceordering.presenter.OrderListPresenter;
import com.shenzhou.intelligenceordering.presenter.view.CommonView;
import com.shenzhou.intelligenceordering.presenter.view.OrderView;
import com.shenzhou.intelligenceordering.print.Constants;
import com.shenzhou.intelligenceordering.print.PrintService;
import com.shenzhou.intelligenceordering.print.PrintStatusListener;
import com.shenzhou.intelligenceordering.until.MyUntil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import gorden.rxbus2.RxBus;
import gorden.rxbus2.Subscribe;

public class MainActivity extends BaseActivity implements View.OnClickListener,ModifyIpDialog.BtnClickInteface {
    private OrderListPresenter orderListPresenter;
    private CommonPresenter commonPresenter;
    private RecyclerView my_recycler;
    private List<OrderBean> orderBeans;
    private OrderListAdapter orderAdapter;
    //定时请求数据线程
    private ScheduledExecutorService scheduler;
    private long mExitTime;
    //是否全部打印完成
    private boolean isAllPrinted = true;
    private QMUITipDialog tipDialog;
    //当前打印的订单
    private OrderBean currentOrderBean;
    private ServiceConnection sc;
    private PrintService printService;
    //设置IP弹框
    private ModifyIpDialog modifyIpDialog;
    //打印IP
    private String printIp = SPUtils.getInstance().getString("printIp");
    //打印机状态
    private TextView printer_state;
    //每次请求来的未打印数据
    private List<OrderBean> orderBeanList;
    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            int aa = msg.what;
            switch (aa) {
                case Constants.RESP_STATUS_OK:
                    SPUtils.getInstance().put("printIp",printIp);
                    printer_state.setText("打印机就绪");
                    Log.i("dai","打印机就绪"+printIp);
                    break;
                case Constants.RESP_STATUS_PRINTING:
                    printer_state.setText("正在打印...");
                    Log.i("dai","正在打印...");
                    break;
                case Constants.RESP_STATUS_OUT_OF_PAPER:
                    printer_state.setText("打印机缺纸");
                    Log.i("dai","打印机缺纸");
                    break;
                case Constants.RESP_STATUS_OPEN_TOP:
                    printer_state.setText("打印机盖未关闭");
                    Log.i("dai","打印机盖未关闭");
                    break;
                case Constants.RESP_STATUS_NOCOMPLETE_OFF_PRINT:
                    printer_state.setText("等待重新初始化打印机");
                    Log.i("dai","等待重新初始化打印机");
                    break;
                case Constants.RESP_STATUS_OTHER_OFF_PRINT:
                    printer_state.setText("等待重新初始化打印机");
                    Log.i("dai","等待重新初始化打印机");
                    break;
                case Constants.RESP_MSG_PRINT_SUCCESS:
                    Log.i("dai","当前打印菜名:"+currentOrderBean.getCpName());
                    //修改打印状态
                    currentOrderBean.setIsPrint(1);
                    orderAdapter.notifyDataSetChanged();
                    //修改服务端状态
                    Map map = new HashMap<String, String>();
                    map.put("orderNo", currentOrderBean.getOrderNo());
                    map.put("id",currentOrderBean.getId()+"");
                    commonPresenter.updateOrderFlagReq(map);
                    Log.i("dai","打印成功");
                    //发送消息，可打印下一条
                    RxBus.get().send(API.R_2);
                    break;
                case Constants.RESP_MSG_PRINT_FAILED:
                    Log.i("dai","打印失败");
                    break;
                case Constants.RESP_MSG_CONNECTING:
                    printer_state.setText("正在连接....");
                    break;
                case Constants.RESP_MSG_CONNECT_SUCCESS:
                    printer_state.setText("打印机连接成功");
                    break;
                case Constants.RESP_MSG_HOST_ERROR:
                    printer_state.setText("没有找到打印机");
                    break;
                case Constants.RESP_MSG_CONNECT_EXCEPTION:
                    printer_state.setText("打印机连接异常，请检查网络和打印机");
                    break;
                case Constants.RESP_MSG_SOCKET_TIMEOUT:
                    printer_state.setText("打印机连接超时");
                    break;
                case Constants.RESP_MSG_DISCONNECT:
                    printer_state.setText("打印机连接断开，请检查网络和打印机");
                    break;
                case Constants.RESP_MSG_RECONNECT:
                    Bundle bundle = msg.getData();
                    if(bundle != null){
                        printer_state.setText("正在重连: " + bundle.get("msgContent"));
                    }
                    break;
                case Constants.IP_NULL:
                    //弹出设置IP
                    modifyIpDialog.show(getFragmentManager(),"ModifyIpDialog");
                    break;
            }
            return false;
        }
    });

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void initView() {
        my_recycler = findViewById(R.id.my_recycler);
    }

    @Override
    protected void initOther() {
        //注册RxBus
        RxBus.get().register(this);
        orderListPresenter = new OrderListPresenter();
        orderListPresenter.onCreate();
        modifyIpDialog = new ModifyIpDialog();
        modifyIpDialog.setBtnClickInteface(MainActivity.this);
        commonPresenter = new CommonPresenter();
        commonPresenter.onCreate();
        orderBeans = new ArrayList<OrderBean>();
        orderAdapter = new OrderListAdapter(R.layout.adapter_order_list_item,orderBeans);
        scheduler = Executors.newSingleThreadScheduledExecutor();
        printer_state = findViewById(R.id.printer_state);
        tipDialog = new QMUITipDialog.Builder(myContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("获取数据...")
                .create();
    }

    @Override
    protected void toDo() {
        tipDialog.show();
        orderListPresenter.attachView(orderView);
        commonPresenter.attachView(commonView);
        my_recycler.setLayoutManager(new LinearLayoutManager(this));
        // 没有数据的时候默认显示该布局
//        orderAdapter.setEmptyView(View.inflate(myContext,R.layout.empty_view, null));
        my_recycler.setAdapter(orderAdapter);
        setAdapter();
        //开启定时请求服务
        long period = API.REQUEST_ORDERING_TIME;
        long initDelay = 2*1000;
        scheduler.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try {
                    if(isAllPrinted){
                        Log.i("dai","请求菜单列表"+ TimeUtils.getNowString());
                        Map map = new HashMap<String, String>();
                        map.put("eNo", SPUtils.getInstance().getString("eNo"));
                        orderListPresenter.orderListReq(map);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, initDelay, period, TimeUnit.MILLISECONDS);

        //绑定打印服务
        bindSocketService();
    }

    private void bindSocketService() {
        /*通过binder拿到service*/
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                printService = ((PrintService.SocketBinder) service).getService();
                printService.setPrintStatusListener(new PrintStatusListener() {
                    @Override
                    public void onPrinterListener(int status) {
                        Message message = mHandler.obtainMessage();
                        message.what = status;
                        mHandler.sendMessage(message);
                    }

                    @Override
                    public void onMessageListener(int msgId, String msgContent) {
                        Message message = mHandler.obtainMessage();
                        message.what = msgId;
                        Bundle bundle = new Bundle();
                        bundle.putString("msgContent", msgContent);
                        message.setData(bundle);
                        mHandler.sendMessage(message);
                    }
                });
                String ipStr = SPUtils.getInstance().getString("printIp");
                if(!StringUtils.isEmpty(ipStr)){
                    printer_state.setText("正在连接打印机....");
                    printService.initSocket(ipStr, 9100);
                }else{
                    Message message = mHandler.obtainMessage();
                    message.what = Constants.IP_NULL;
                    mHandler.sendMessage(message);
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                printService = null;
            }
        };

        Intent intent = new Intent(getApplicationContext(), PrintService.class);
        bindService(intent, sc, BIND_AUTO_CREATE);
    }

    private void setAdapter(){
        //设置是否开启上滑加载更多
        orderAdapter.setEnableLoadMore(false);
        //添加加载list动画
        orderAdapter.openLoadAnimation(BaseQuickAdapter.ALPHAIN);
    }

    private OrderView orderView = new OrderView() {
        @Override
        public void onSuccess(OrderResult resultVo) {
            tipDialog.dismiss();
            orderBeanList = resultVo.getResultList();
            Log.i("dai",TimeUtils.getNowString()+" 本次获取未打印订单数："+orderBeanList.size());
            if(MyUntil.isNotNullCollect(orderBeanList)){
                //有未打印数据
                isAllPrinted = false;
                //拿出第一条打印
                currentOrderBean = orderBeanList.get(0);
                if(currentOrderBean != null && printService != null){
                    printService.printMenu(currentOrderBean);
                }
                //新数据都放在最前端
                orderBeans.addAll(0,orderBeanList);
                my_recycler.setAdapter(orderAdapter);
                orderAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onError(String result) {
            tipDialog.dismiss();
            ToastUtils.showShort("获取数据失败");
        }
    };

    private CommonView commonView = new CommonView() {
        @Override
        public void onSuccess(ResultVo resultVo) {
            //每打印完成一个后遍历看是否还有未打印的。如果没有则可再请求数据
            isAllPrinted = true;
            for(OrderBean ob:orderBeanList){
                if(ob.getIsPrint() == 0){
                    isAllPrinted = false;
                    return;
                }
            }
            Log.i("dai","是否全部打印完成:"+isAllPrinted);
        }

        @Override
        public void onError(String result) {

        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_person:
                PersonInfoDialog personInfoDialog = new PersonInfoDialog();
                personInfoDialog.show(getFragmentManager(),"PersonInfoDialog");
                break;
            case R.id.img_ip:
                modifyIpDialog.show(getFragmentManager(),"ModifyIpDialog");
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(MainActivity.this, "再按一次退出系统", Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            finish();
            System.exit(0);
        }
    }

    //修改完成密码,退到登录页面
    @Subscribe(code = API.R_1)
    public void closeAndTurn(){
        startActivity(new Intent(myContext,LoginActivity.class));
        finish();
    }

    //可打印下一条
    @Subscribe(code = API.R_2)
    public void printOtherItem(){
        if(MyUntil.isNotNullCollect(orderBeanList)){
            for(int i=1;i<orderBeanList.size();i++){
                //当前数据还有未打印的
                OrderBean ob = orderBeanList.get(i);
                if(ob.getIsPrint() == 0){
                    currentOrderBean = ob;
                    if(currentOrderBean != null && printService != null){
                        printService.printMenu(currentOrderBean);
                    }
                    return;
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除绑定RxBus
        RxBus.get().unRegister(this);
        unbindService(sc);
        Intent intent = new Intent(getApplicationContext(), PrintService.class);
        stopService(intent);
        //及时清除消息
        mHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void btnClick(String ip1, String ip2, String ip3, String ip4) {
        printService.releaseSocket();
        printService.setmConnectCount(3);
        printIp = ip1+"."+ip2+"."+ip3+"."+ip4;
        printer_state.setText("正在连接打印机....");
        printService.initSocket(printIp, 9100);
    }
}
