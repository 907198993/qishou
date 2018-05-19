package com.qifan.qishou.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import com.github.androidtools.SPUtils;
import com.github.androidtools.StringUtils;
import com.github.customview.MyEditText;
import com.github.customview.MyTextView;
import com.qifan.qishou.Config;
import com.qifan.qishou.GetSign;
import com.qifan.qishou.R;
import com.qifan.qishou.base.BaseActivity;
import com.qifan.qishou.base.MyCallBack;
import com.qifan.qishou.network.ApiRequest;
import com.qifan.qishou.network.response.GradObj;
import com.qifan.qishou.network.response.WithdrawalsObj;
import com.qifan.qishou.tools.MaxTextLengthFilter;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/4.
 */

public class WithdrawalsActivity extends BaseActivity {


    @BindView(R.id.et_username)
    MyEditText etUsername;
    @BindView(R.id.et_zhifubao)
    MyEditText etZhifubao;
    @BindView(R.id.et_money)
    MyEditText etMoney;
    @BindView(R.id.tv_commit)
    MyTextView tvCommit;
    @BindView(R.id.tv_enable_withdrawal)
    TextView tv_enable_withdrawal;
   private  double enable_money; //可取金额
    private static final int DEFAULT_DECIMAL_NUMBER = 2;
    /**
     * 保留小数点后多少位
     */
    private int mDecimalNumber = DEFAULT_DECIMAL_NUMBER;

    @Override
    protected int getContentView() {
        setAppTitle("提现");
        return R.layout.activity_withdrawal;
    }

    @Override
    protected void initView() {
        InputFilter[] filters = {new MaxTextLengthFilter(mContext,10,true,"请重新输入")};
        etMoney.setFilters(filters);


    }
    @Override
    protected void initData() {
        showProgress();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", SPUtils.getPrefString(mContext, Config.user_id, null));
        map.put("sign", GetSign.getSign(map));
        ApiRequest.Gettixian(map, new MyCallBack<WithdrawalsObj>(mContext, pcfl, pl_load) {
            @Override
            public void onSuccess(WithdrawalsObj walletObj) {
                etUsername.setText(walletObj.getRealName());
                etZhifubao.setText(walletObj.getAccount());
                tv_enable_withdrawal.setText("本次可提现"+walletObj.getUsable()+"元");
                enable_money =  Double.valueOf(walletObj.getUsable());
            }
        });
    }

    @OnClick({R.id.tv_commit})
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_commit:
                double edit_money = Double.valueOf(etMoney.getText().toString());
                if(edit_money>enable_money){
                    showMsg("请输入正确的金额");
                }else if(enable_money<=0.01){
                    showMsg("请输入正确的金额");
                }else{
                    showProgress();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("userid", SPUtils.getPrefString(mContext, Config.user_id, null));
                    map.put("money",etMoney.getText().toString());
                    map.put("account",etZhifubao.getText().toString());
                    map.put("sign", GetSign.getSign(map));
                    ApiRequest.Withdrawals(map, new MyCallBack<GradObj>(mContext, pcfl, pl_load) {
                        @Override
                        public void onSuccess(GradObj walletObj) {
                           if(walletObj.getResult() ==1){
                               showMsg("申请提现成功");
                               finish();
                           }
                        }
                    });

                }
                break;
        }
    }

}
