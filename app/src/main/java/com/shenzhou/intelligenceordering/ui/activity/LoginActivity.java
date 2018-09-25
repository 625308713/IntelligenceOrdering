package com.shenzhou.intelligenceordering.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener;
import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;
import com.shenzhou.intelligenceordering.R;
import com.shenzhou.intelligenceordering.bean.LoginResult;
import com.shenzhou.intelligenceordering.bean.VersionBean;
import com.shenzhou.intelligenceordering.bean.VersionResult;
import com.shenzhou.intelligenceordering.presenter.LoginPresenter;
import com.shenzhou.intelligenceordering.presenter.VersionPresenter;
import com.shenzhou.intelligenceordering.presenter.view.LoginView;
import com.shenzhou.intelligenceordering.presenter.view.VersionView;

import java.util.HashMap;
import java.util.Map;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener{
    private LoginPresenter loginPresenter;
    private VersionPresenter versionPresenter;
    private EditText edit_name,edit_psd;
    private QMUITipDialog tipDialog,versionDialog;
    private String updateTitle;
    private String apkUrl;
    private String updateContent;
    private int versionCode;
    private DownloadBuilder builder;

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_login);
    }

    @Override
    protected void initView() {
        edit_name = findViewById(R.id.edit_name);
        edit_psd = findViewById(R.id.edit_psd);
    }

    @Override
    protected void initOther() {
        loginPresenter = new LoginPresenter();
        loginPresenter.onCreate();
        versionPresenter = new VersionPresenter();
        versionPresenter.onCreate();
        tipDialog = new QMUITipDialog.Builder(myContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("登录中...")
                .create();
        versionDialog = new QMUITipDialog.Builder(myContext)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在检查版本...")
                .create();
    }

    @Override
    protected void toDo() {
        loginPresenter.attachView(loginView);
        versionPresenter.attachView(vv);
        edit_name.setText(SPUtils.getInstance().getString("loginName"));
        edit_psd.setText(SPUtils.getInstance().getString("loginPsd"));
        versionDialog.show();
        versionPresenter.getVersionInfo();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_login:
                if(StringUtils.isEmpty(edit_name.getText()+"")){
                    ToastUtils.showShort("请输入用户名");
                    return;
                }
                if(StringUtils.isEmpty(edit_psd.getText()+"")){
                    ToastUtils.showShort("请输入密码");
                    return;
                }
                Map<String,String> map = new HashMap<>();
                map.put("userName",edit_name.getText().toString());
                map.put("passWord",edit_psd.getText().toString());
                loginPresenter.loginReq(map);
                tipDialog.show();
                break;
        }
    }

    private LoginView loginView = new LoginView() {
        @Override
        public void onSuccess(LoginResult resultVo) {
            tipDialog.dismiss();
            if("0".equals(resultVo.getCode())){
                //登录成功,保存用户名,登录名和密码
                SPUtils.getInstance().put("userName",resultVo.getUserName());
                SPUtils.getInstance().put("userId",resultVo.getUserId());
                SPUtils.getInstance().put("eNo",resultVo.geteNo());
                SPUtils.getInstance().put("loginPassword",resultVo.getPassWord());
                SPUtils.getInstance().put("loginName",edit_name.getText().toString());
                SPUtils.getInstance().put("loginPsd",edit_psd.getText().toString());
                startActivity(new Intent(LoginActivity.this,MainActivity2.class));
                finish();
            }else{
                ToastUtils.showShort(resultVo.getMessage());
            }
        }

        @Override
        public void onError(String result) {
            tipDialog.dismiss();
            ToastUtils.showShort("请求出错");
        }
    };

    private VersionView vv = new VersionView() {
        @Override
        public void onSuccess(VersionResult vb) {
            versionDialog.dismiss();
            VersionBean versionBean = vb.getVersionHc();
            if(versionBean != null){
                updateTitle = versionBean.getUpdateTitle();
                apkUrl = versionBean.getApkUrl();
                updateContent = versionBean.getUpdateContent();
                versionCode = versionBean.getVersionCode();
                if(versionCode > AppUtils.getAppVersionCode()){
                    builder = AllenVersionChecker
                            .getInstance()
                            .downloadOnly(crateUIData());
                    //有版本更新
                    if(versionBean.getIsForce() == 1){
                        //强制更新,设置这个监听表明就是强制更新
                        builder.setForceUpdateListener(new ForceUpdateListener() {
                            @Override
                            public void onShouldForceUpdate() {
                                finish();
                            }
                        });
                    }
                    builder.excuteMission(myContext);
                }
            }
        }

        @Override
        public void onError(String result) {
            versionDialog.dismiss();
            Log.i("dai","版本请求出错:"+result);
        }
    };
    private UIData crateUIData() {
        UIData uiData = UIData.create();
        uiData.setTitle(updateTitle);
        uiData.setDownloadUrl(apkUrl);
        uiData.setContent(updateContent);
        return uiData;
    }
}
