package com.qifan.qishou.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.github.customview.MyEditText;
import com.github.rxjava.rxbus.RxUtils;
import com.qifan.qishou.GetSign;
import com.qifan.qishou.R;
import com.qifan.qishou.base.BaseActivity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observable;
import rx.Observer;
import rx.Subscription;

/**
 * Created by administartor on 2017/9/5.
 */

public class ForgetPasswordActivity extends BaseActivity {
    @BindView(R.id.et_register_phone)
    MyEditText et_register_phone;
    @BindView(R.id.et_register_code)
    MyEditText et_register_code;
    @BindView(R.id.et_register_pwd)
    MyEditText et_register_pwd;
    @BindView(R.id.et_register_repwd)
    MyEditText et_register_repwd;
    @BindView(R.id.tv_register_getcode)
    TextView tv_register_getcode;

    private String smsCode,yaoQingMa;
    @Override
    protected int getContentView() {
        setAppTitle("找回密码");
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.app_right_tv,R.id.tv_register_getcode, R.id.tv_register_commit})
    protected void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.app_right_tv:
                finish();
                break;
            case R.id.tv_register_getcode:
                if(TextUtils.isEmpty(getSStr(et_register_phone))){
                    showMsg("手机号不能为空");
                    return;
                }else if(!GetSign.isMobile(getSStr(et_register_phone))){
                    showMsg("请输入正确手机号");
                    return;
                }
                getMsgCode();
                break;
            case R.id.tv_register_commit:
                String phone=getSStr(et_register_phone);
                String code=getSStr(et_register_code);
                String pwd=getSStr(et_register_pwd);
                String rePwd=getSStr(et_register_repwd);

                if(TextUtils.isEmpty(getSStr(et_register_phone))){
                    showMsg("手机号不能为空");
                    return;
                }else if(!GetSign.isMobile(getSStr(et_register_phone))){
                    showMsg("请输入正确手机号");
                    return;
                }else if(TextUtils.isEmpty(smsCode)||TextUtils.isEmpty(code)||!code.equals(smsCode)){
                    showMsg("请输入正确验证码");
                    return;
                }else if(TextUtils.isEmpty(pwd)){
                    showMsg("密码不能为空");
                    return;
                }else if(!pwd.equals(rePwd)){
                    showMsg("两次密码不一样");
                    return;
                }
                register(phone,pwd);
                break;
        }
    }
    private void register(String phone, String pwd) {
        showLoading();
//        Map<String,String>map=new HashMap<String,String>();
//        map.put("username",phone);
//        map.put("password",pwd);
//        map.put("distribution_yard",yaoQingMa);
//        map.put("sign",GetSign.getSign(map));
//        ApiRequest.register(map, new MyCallBack<BaseObj>(mContext) {
//            @Override
//            public void onSuccess(BaseObj obj) {
//                showMsg(obj.getMsg());
//                finish();
//            }
//        });
    }

    private void getMsgCode() {
        showLoading();
        Map<String, String> map = new HashMap<String, String>();
        map.put("mobile",getSStr(et_register_phone));
        map.put("rnd",getRnd());
        String sign = GetSign.getSign(map);
        map.put("sign", sign);
        showLoading();
//        ApiRequest.getSMSCode(map, new MyCallBack<BaseObj>(mContext) {
//            @Override
//            public void onSuccess(BaseObj obj) {
//                smsCode = obj.getSMSCode();
//                countDown();
//            }
//        });

    }

    private void countDown() {
        tv_register_getcode.setEnabled(false);
        final long count = 30;
        Subscription subscribe = Observable.interval(1, TimeUnit.SECONDS)
                .take(31)//计时次数
                .map(integer -> count - integer)
                .compose(RxUtils.appSchedulers())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        tv_register_getcode.setEnabled(true);
                        tv_register_getcode.setText("获取验证码");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        tv_register_getcode.setText("剩下" + aLong + "s");
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
        addSubscription(subscribe);
    }
}
