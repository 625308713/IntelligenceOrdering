package com.shenzhou.intelligenceordering.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.shenzhou.intelligenceordering.R;

/**
 * 修改IP弹框
 */
public class ModifyIpDialog extends DialogFragment {
    private EditText ip_1,ip_2,ip_3,ip_4;
    private BtnClickInteface btnClickInteface;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //设置透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.frament_modify_ip, null);
        builder.setView(view);
        ip_1 = view.findViewById(R.id.ip_1);
        ip_2 = view.findViewById(R.id.ip_2);
        ip_3 = view.findViewById(R.id.ip_3);
        ip_4 = view.findViewById(R.id.ip_4);
        String ipStr = SPUtils.getInstance().getString("printIp");
        if(!StringUtils.isEmpty(ipStr)){
            String[] ss = ipStr.split("\\.");
            if(ss != null && ss.length == 4){
                ip_1.setText(ss[0]);
                ip_2.setText(ss[1]);
                ip_3.setText(ss[2]);
                ip_4.setText(ss[3]);
            }
        }
        Button btn_1 = view.findViewById(R.id.btn_modify);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtils.isEmpty(ip_1.getText()+"") || StringUtils.isEmpty(ip_2.getText()+"")
                        || StringUtils.isEmpty(ip_3.getText()+"") || StringUtils.isEmpty(ip_4.getText()+"")){
                    ToastUtils.showShort("请输入正确的IP地址");
                    return;
                }else{
                    btnClickInteface.btnClick(ip_1.getText()+"",ip_2.getText()+"",ip_3.getText()+"",ip_4.getText()+"");
                }
                dismiss();
            }
        });
        ImageView img_close = view.findViewById(R.id.img_close);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        return builder.create();
    }

    //设置IP接口
    public interface BtnClickInteface{
        void btnClick(String ip1,String ip2,String ip3,String ip4);
    }

    public void setBtnClickInteface(BtnClickInteface btnClickInteface) {
        this.btnClickInteface = btnClickInteface;
    }
}
