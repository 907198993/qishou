package com.qifan.qishou.Activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.github.customview.MyEditText;
import com.google.gson.Gson;
import com.qifan.qishou.R;
import com.qifan.qishou.base.BaseActivity;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
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
                STActivity(RegisterActivity.class);
                break;
            case R.id.tv_login_commit:
//                if(TextUtils.isEmpty(getSStr(et_login_phone))){
//                    showToastS("请输入手机号");
//                    return;
//                }else if(TextUtils.isEmpty(getSStr(et_login_pwd))){
//                    showToastS("请输入密码");
//                    return;
//                }
//                login();
                break;

            case R.id.tv_login_forget_pwd:
                STActivity(ForgetPasswordActivity.class);
                break;
        }
    }

}
