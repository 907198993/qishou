package com.qifan.qishou.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.github.androidtools.SPUtils;
import com.github.customview.MyEditText;
import com.qifan.qishou.Config;
import com.qifan.qishou.GetSign;
import com.qifan.qishou.MainActivity;
import com.qifan.qishou.R;
import com.qifan.qishou.base.BaseActivity;
import com.qifan.qishou.base.MyCallBack;
import com.qifan.qishou.network.ApiRequest;
import com.qifan.qishou.network.response.LoginObj;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/12.
 */

public class LoginActivity extends BaseActivity {
    @BindView(R.id.et_login_phone)
    MyEditText etLoginPhone;
    @BindView(R.id.et_login_pwd)
    MyEditText etLoginPwd;

    @Override
    protected int getContentView() {
        setAppTitle("登录");
        setAppRightTitle("去注册");
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }


    @OnClick({R.id.tv_login_commit,R.id.tv_login_forget_pwd,R.id.app_right_tv})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.app_right_tv:
                Intent i = new Intent();
                STActivity(i,RegisterActivity.class);
                break;
            case R.id.tv_login_commit:
                if(TextUtils.isEmpty(getSStr(etLoginPhone))){
                    showToastS("请输入手机号");
                    return;
                }else if(TextUtils.isEmpty(getSStr(etLoginPwd))){
                    showToastS("请输入密码");
                    return;
                }
                login();
                break;
            case R.id.tv_login_forget_pwd:
                STActivity(ForgetPasswordActivity.class);
                break;
        }
    }

    private void login() {
        showLoading();
        Map<String,String> map=new HashMap<String,String>();
        map.put("username",getSStr(etLoginPhone));
        map.put("password",getSStr(etLoginPwd));
        map.put("sign", GetSign.getSign(map));
        ApiRequest.userLogin(map, new MyCallBack<LoginObj>(mContext) {
            @Override
            public void onSuccess(LoginObj obj) {
                SPUtils.setPrefString(mContext, Config.user_name,getSStr(etLoginPhone));
                SPUtils.setPrefString(mContext, Config.user_id,obj.getUserid());
                SPUtils.setPrefString(mContext, Config.user_status,"0");//0 代表休息状态
                STActivity(MainActivity.class);
                finish();
            }
        });
    }


}
