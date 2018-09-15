package com.shenzhou.intelligenceordering.ui.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.SPUtils;
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

public class MainActivity extends BaseActivity implements View.OnClickListener{
    private OrderListPresenter orderListPresenter;
    private CommonPresenter commonPresenter;
    private RecyclerView my_recycler;
    private List<OrderBean> orderBeans;
    private OrderListAdapter orderAdapter;
    private ScheduledExecutorService scheduler;
    private long mExitTime;
    //是否全部打印完成
    private boolean isAllPrinted = true;
    private QMUITipDialog tipDialog;
    //当前打印的订单
    private OrderBean currentOrderBean;

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
        commonPresenter = new CommonPresenter();
        commonPresenter.onCreate();
        orderBeans = new ArrayList<OrderBean>();
        orderAdapter = new OrderListAdapter(R.layout.adapter_order_list_item,orderBeans);
        scheduler = Executors.newSingleThreadScheduledExecutor();
        tipDialog = new QMUITipDialog.Builder(myContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("获取数据...")
                .create();

        //修改状态 todo 要去掉
//        orderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
//            @Override
//            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//                currentOrderBean = orderBeans.get(position);
//                if(currentOrderBean != null){
//                    Map map = new HashMap<String, String>();
//                    map.put("orderNo", currentOrderBean.getOrderNo());
//                    map.put("id",currentOrderBean.getId()+"");
//                    commonPresenter.updateOrderFlagReq(map);
//                }
//            }
//        });
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
        long initDelay = 1*1000;
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
            List<OrderBean> orderBeanList = resultVo.getResultList();
            if(MyUntil.isNotNullCollect(orderBeanList)){
               //有未打印数据
                isAllPrinted = false;
                //新数据都放在最前端
                orderBeans.addAll(0,orderBeanList);
                my_recycler.setAdapter(orderAdapter);
                orderAdapter.notifyDataSetChanged();
                Log.i("dai",TimeUtils.getNowString()+" 本次获取未打印订单数："+orderBeans.size());
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
            currentOrderBean.setIsPrint(1);
            orderAdapter.notifyDataSetChanged();
            //每打印完成一个后遍历看是否还有未打印的。如果没有则可再请求数据
            isAllPrinted = true;
            for(OrderBean ob:orderBeans){
                if(ob.getIsPrint() == 0){
                    isAllPrinted = false;
                    return;
                }
            }
            Log.i("dai","是否还有未打印数据:"+isAllPrinted);
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
                ModifyIpDialog modifyIpDialog = new ModifyIpDialog();
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
    //接收RxBus消息
    @Subscribe(code = API.R_1)
    public void closeAndTurn(){
        startActivity(new Intent(myContext,LoginActivity.class));
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解除绑定RxBus
        RxBus.get().unRegister(this);
    }
}
