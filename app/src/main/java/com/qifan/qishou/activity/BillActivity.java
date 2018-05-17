package com.qifan.qishou.activity;

import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.adapter.LoadMoreAdapter;
import com.github.baseclass.adapter.LoadMoreViewHolder;
import com.qifan.qishou.GetSign;
import com.qifan.qishou.R;
import com.qifan.qishou.base.BaseActivity;
import com.qifan.qishou.base.MyCallBack;
import com.qifan.qishou.network.ApiRequest;
import com.qifan.qishou.network.response.BillObj;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

public class BillActivity extends BaseActivity implements LoadMoreAdapter.OnLoadMoreListener{

    @BindView(R.id.rv_bill)
    RecyclerView rv_bill;

    LoadMoreAdapter adapter;

    @Override
    protected int getContentView() {
        setAppTitle("账单明细");
        return R.layout.activity_bill;
    }

    @Override
    protected void initView() {
        adapter=new LoadMoreAdapter<BillObj>(mContext,R.layout.item_order_list2,pageSize) {
            @Override
            public void bindData(LoadMoreViewHolder holder, int position, BillObj bean) {
                holder.setText(R.id.tv_order_price, "¥" + bean.getCharge())
                        .setText(R.id.tv_shop_name, bean.getShopName())
                        .setText(R.id.tv_shop_address, bean.getShopAddress())
                        .setText(R.id.tv_customer_name, bean.getCustomerName())
                        .setText(R.id.tv_customer_address, bean.getCustomerAddress());

            }
        };
        adapter.setOnLoadMoreListener(this);
        rv_bill.setLayoutManager(new LinearLayoutManager(mContext));

        rv_bill.setAdapter(adapter);

        pcfl.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(1,false);
            }
        });

    }


    @Override
    protected void initData() {
        showProgress();
        getData(1,false);
    }

    private void getData(int page, boolean isLoad) {
        Map<String,String> map=new HashMap<String,String>();
        map.put("userId",getUserId());
        map.put("pageIndex",page+"");
        map.put("pagesize",pageSize+"");
        map.put("sign", GetSign.getSign(map));
        ApiRequest.userBillList(map, new MyCallBack<List<BillObj>>(mContext,pcfl,pl_load) {
            @Override
            public void onSuccess(List<BillObj> list) {
                if(isLoad){
                    pageNum++;
                    adapter.addList(list,true);
                }else{
                    pageNum=2;
                    adapter.setList(list,true);
                }
            }
        });

    }

    @Override
    protected void onViewClick(View v) {

    }

    @Override
    public void loadMore() {
        getData(pageNum,true);
    }
}
