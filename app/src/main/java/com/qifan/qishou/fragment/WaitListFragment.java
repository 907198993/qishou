package com.qifan.qishou.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.qifan.qishou.R;
import com.qifan.qishou.base.BaseFragment;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * Created by Administrator on 2018/4/12.
 */

public class WaitListFragment extends BaseFragment {

    @BindView(R.id.rv_order_class)
    RecyclerView rvOrderClass;
    @BindView(R.id.pcfl_refresh)
    PtrClassicFrameLayout pcflRefresh;

    @Override
    protected int getContentView() {
        return R.layout.frgment_waiting_list;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
