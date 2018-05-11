package com.qifan.qishou.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.androidtools.PhoneUtils;
import com.github.androidtools.SPUtils;
import com.github.androidtools.ToastUtils;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.adapter.LoadMoreAdapter;
import com.github.baseclass.adapter.LoadMoreViewHolder;
import com.github.baseclass.rx.MySubscriber;
import com.github.baseclass.rx.RxBus;
import com.github.customview.MyTextView;
import com.qifan.qishou.Config;
import com.qifan.qishou.GetSign;
import com.qifan.qishou.R;
import com.qifan.qishou.base.BaseFragment;
import com.qifan.qishou.base.MyCallBack;
import com.qifan.qishou.event.LocationEvent;
import com.qifan.qishou.event.ResetEvent;
import com.qifan.qishou.network.ApiRequest;
import com.qifan.qishou.network.response.OrderListObj;
import com.qifan.qishou.network.response.ScreeningBean;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2018/5/11 0011.
 */

public class WaitingGoodsFragment extends BaseFragment implements LoadMoreAdapter.OnLoadMoreListener {

    @BindView(R.id.rv_order_class)
    RecyclerView rvOrderClass;
    @BindView(R.id.pcfl_refresh)
    PtrClassicFrameLayout pcflRefresh;
    @BindView(R.id.linear_shaixuan)
    LinearLayout linearShaixuan;
    @BindView(R.id.tv_refresh_order)
    MyTextView tvRefreshOrder;
    Unbinder unbinder;
    @BindView(R.id.relative_reset)
    RelativeLayout relativeReset;
    Unbinder unbinder1;
    private Dialog shareDialog;

    private String popScreen;
    private String popSort;

    public String longitudeAndlatitude;//经纬度
    LoadMoreAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.frgment_waiting_list;
    }

    @Override
    protected void initView() {



        adapter = new LoadMoreAdapter<OrderListObj>(mContext, R.layout.item_order_list, pageSize) {
            @Override
            public void bindData(LoadMoreViewHolder holder, int position, OrderListObj bean) {
                holder.setText(R.id.tv_order_price, "¥" + bean.getCharge())
                        .setText(R.id.tv_shop_distance, (bean.getQsdistance()))
                        .setText(R.id.tv_shop_name, bean.getShopName())
                        .setText(R.id.tv_shop_address, bean.getShopAddress())
                        .setText(R.id.tv_customer_distance, (bean.getScdistance()))
                        .setText(R.id.tv_customer_name, bean.getCustomerName())
                        .setText(R.id.tv_customer_address, bean.getCustomerAddress());

                holder.getView(R.id.tv_order_receive).setOnClickListener(new MyOnClickListener() {
                    @Override
                    protected void onNoDoubleClick(View view) {

                    }
                });
            }
        };
        adapter.setOnLoadMoreListener(this);
        rvOrderClass.setLayoutManager(new LinearLayoutManager(mContext));
        rvOrderClass.addItemDecoration(getItemDivider());
        rvOrderClass.setAdapter(adapter);

        pcfl.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                getData(false);
            }
        });

    }

    @Override
    protected void initData() {
        popScreen = SPUtils.getPrefString(mContext, Config.pop_screen, null);
        popSort = SPUtils.getPrefString(mContext, Config.pop_sort, null);
        getData(false);
    }

    @Override
    protected void onViewClick(View v) {

    }

    @Override
    protected void initRxBus() {
        super.initRxBus();
        RxBus.getInstance().getEvent(LocationEvent.class, new MySubscriber<LocationEvent>() {
            @Override
            public void onMyNext(LocationEvent event) {
                showMsg(event.longitudeAndlatitude);
                longitudeAndlatitude = event.longitudeAndlatitude;
            }
        });
        RxBus.getInstance().getEvent(ResetEvent.class, new MySubscriber<ResetEvent>() {
            @Override
            public void onMyNext(ResetEvent event) {
                if (event.ResetEvent == 1) {
                    relativeReset.setVisibility(View.VISIBLE);
                }else{
                    relativeReset.setVisibility(View.GONE);
                }
            }
        });

    }

    private void getData(boolean isLoad) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("coord", longitudeAndlatitude);
        map.put("type", popScreen);
        map.put("status", "1");
        map.put("userid", SPUtils.getPrefString(mContext, Config.user_id, null));
        map.put("orderby", popSort);
        map.put("sign", GetSign.getSign(map));
        ApiRequest.orderList(map, new MyCallBack<List<OrderListObj>>(mContext, pcfl, pl_load) {
            @Override
            public void onSuccess(List<OrderListObj> list) {
                if (isLoad) {
                    pageNum++;
                    adapter.addList(list, true);
                } else {
                    pageNum = 2;
                    adapter.setList(list, true);
                }
            }
        });
    }




    public String setToString(Set<Integer> selectPosSet) {
        String s = "";
        for (Integer str : selectPosSet) {
            System.out.println(str);
            int i = str;
            s += i + ",";
        }
        if (s.length() < 1) {
            return "";
        } else {
            ToastUtils.showToast(getActivity(), "choose:" + s.substring(0, s.length() - 1) + "====" + s.length());
            return s.substring(0, s.length() - 1);
        }
    }

    @Override
    public void loadMore() {
    }
}