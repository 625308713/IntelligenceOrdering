package com.shenzhou.intelligenceordering.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shenzhou.intelligenceordering.R;
import com.shenzhou.intelligenceordering.bean.OrderBean;

import java.util.List;

/**
 * 菜单列表适配器
 */
public class OrderListAdapter extends BaseQuickAdapter<OrderBean, BaseViewHolder> {
    public OrderListAdapter(int layoutResId, @Nullable List<OrderBean> data) {
        super(layoutResId, data);
    }
    @Override
    protected void convert(BaseViewHolder helper, OrderBean item) {
        helper.setText(R.id.orderTime,item.getOrderTime())
            .setText(R.id.orderNo,item.getOrderNo())
            .setText(R.id.ztInfo,item.getZtInfo())
            .setText(R.id.cpName,item.getCpName())
            .setText(R.id.cpNum,item.getCpNum()+"")
            .setText(R.id.cpPro,item.getCpPro());
            if(item.getIsPrint() == 0){
                helper.setTextColor(R.id.isPrint, ContextCompat.getColor(mContext,R.color.red));
                helper.setText(R.id.isPrint,"未打印");
            }else{
                helper.setTextColor(R.id.isPrint, ContextCompat.getColor(mContext,R.color.green_1));
                helper.setText(R.id.isPrint,"已打印");
            }
        helper.addOnClickListener(R.id.isPrint);
    }
}
