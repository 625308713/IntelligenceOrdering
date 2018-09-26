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
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.shenzhou.intelligenceordering.R;
import com.shenzhou.intelligenceordering.app.API;
import com.shenzhou.intelligenceordering.bean.LoginResult;
import com.shenzhou.intelligenceordering.presenter.LoginPresenter;
import com.shenzhou.intelligenceordering.presenter.view.LoginView;

import java.util.HashMap;
import java.util.Map;

import gorden.rxbus2.RxBus;

/**
 * 修改密码弹框
 */
public class ModifyPsdDialog extends DialogFragment {
    private EditText edit_old_psd,edit_new_psd,edit_new_psd_again;
    private LoginPresenter loginPresenter;
    //loading
    private QMUITipDialog tipDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //设置透明
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //注册RxBus
        RxBus.get().register(this);
        loginPresenter = new LoginPresenter();
        loginPresenter.onCreate();
        loginPresenter.attachView(loginView);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.frament_modifypsd, null);
        builder.setView(view);
        edit_old_psd = view.findViewById(R.id.edit_old_psd);
        edit_new_psd = view.findViewById(R.id.edit_new_psd);
        edit_new_psd_again = view.findViewById(R.id.edit_new_psd_again);
        Button btn_1 = view.findViewById(R.id.btn_modify);
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StringUtils.isEmpty(edit_old_psd.getText()+"")){
                    ToastUtils.showShort("请输入原密码");
                    return;
                }
                if(StringUtils.isEmpty(edit_new_psd.getText()+"")||StringUtils.isEmpty(edit_new_psd_again.getText()+"")){
                    ToastUtils.showShort("请输入新密码");
                    return;
                }
                if(!edit_new_psd.getText().toString().equals(edit_new_psd_again.getText().toString())){
                    ToastUtils.showShort("输入新密码不一致");
                    return;
                }
                tipDialog = new QMUITipDialog.Builder(getActivity())
                        .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                        .setTipWord("修改中...")
                        .create();
                tipDialog.show();
                Map<String,String> map = new HashMap<>();
                map.put("oldPassword",edit_old_psd.getText().toString());
                map.put("newPassword",edit_new_psd.getText().toString());
                map.put("loginPassword",SPUtils.getInstance().getString("loginPassword"));
                map.put("userId", SPUtils.getInstance().getString("userId"));
                loginPresenter.modifyPsdReq(map);
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

    private LoginView loginView = new LoginView() {
        @Override
        public void onSuccess(LoginResult resultVo) {
            //发送消息，关闭activity,跳转到登录页面,取消本地保存的密码
            SPUtils.getInstance().put("loginPsd","");
//            RxBus.get().send(API.R_1);
            ToastUtils.showLong(resultVo.getMessage());
            tipDialog.dismiss();
        }

        @Override
        public void onError(String result) {
            tipDialog.dismiss();
            ToastUtils.showShort("请求出错");
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        //解除绑定RxBus
        RxBus.get().unRegister(this);
    }
}
