package com.qifan.qishou.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.androidtools.SPUtils;
import com.qifan.qishou.Config;
import com.qifan.qishou.GetSign;
import com.qifan.qishou.R;
import com.qifan.qishou.base.BaseActivity;
import com.qifan.qishou.base.MyCallBack;
import com.qifan.qishou.network.ApiRequest;
import com.qifan.qishou.network.response.WalletObj;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/4.
 */

public class MyWalletActivity extends BaseActivity {

    @BindView(R.id.history_detail_tv)
    TextView historyDetailTv;
    @BindView(R.id.tv_total_money)
    TextView tvTotalMoney;
    @BindView(R.id.tv_today_income)
    TextView tvTodayIncome;
    @BindView(R.id.tv_cannot_mention_money)
    TextView tvCannotMentionMoney;

    @Override
    protected int getContentView() {
        setAppTitle("我的钱包");
        return R.layout.activity_wallet;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        showProgress();
        Map<String, String> map = new HashMap<String, String>();
        map.put("userid", SPUtils.getPrefString(mContext, Config.user_id, null));
        map.put("sign", GetSign.getSign(map));
        ApiRequest.userWallet(map, new MyCallBack<WalletObj>(mContext, pcfl, pl_load) {
            @Override
            public void onSuccess(WalletObj walletObj) {
                tvTotalMoney.setText(walletObj.getUsable());
                tvTodayIncome.setText(walletObj.getToday());
                if(!walletObj.getUnable().equals(0)){
                    tvCannotMentionMoney.setText("不可取金额："+walletObj.getUnable()+"（元）");
                }

            }
        });
    }

    @OnClick({R.id.history_detail_tv,R.id.iv_tixian})
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.history_detail_tv:
                STActivity(BillActivity.class);
                break;
            case R.id.iv_tixian:
                STActivity(WithdrawalsActivity.class);
                break;

        }
    }
}
