package com.qifan.qishou.activity;

import android.view.View;
import android.widget.TextView;

import com.qifan.qishou.R;
import com.qifan.qishou.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/5/4.
 */

public class MyWalletActivity extends BaseActivity {

    @BindView(R.id.history_detail_tv)
    TextView historyDetailTv;

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

    }


    @OnClick({R.id.history_detail_tv})
    protected void onViewClick(View v) {
    switch (v.getId()){
        case R.id.history_detail_tv:
            STActivity(BillActivity.class);
            break;
    }

    }
}
