package com.qifan.qishou.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.github.androidtools.SPUtils;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.adapter.LoadMoreAdapter;
import com.github.baseclass.adapter.LoadMoreViewHolder;
import com.github.baseclass.rx.MySubscriber;
import com.github.baseclass.rx.RxBus;
import com.github.customview.MyLinearLayout;
import com.github.customview.MyTextView;
import com.qifan.qishou.Config;
import com.qifan.qishou.GetSign;
import com.qifan.qishou.R;
import com.qifan.qishou.base.BaseFragment;
import com.qifan.qishou.base.MyCallBack;
import com.qifan.qishou.event.LocationEvent;
import com.qifan.qishou.event.RefreshEvent;
import com.qifan.qishou.event.RefreshFragmentEvent;
import com.qifan.qishou.event.ResetEvent;
import com.qifan.qishou.network.ApiRequest;
import com.qifan.qishou.network.response.GradObj;
import com.qifan.qishou.network.response.OrderListObj;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by Administrator on 2018/5/11 0011.
 */
//待送货
public class WaitingSendGoodsFragment extends BaseFragment implements LoadMoreAdapter.OnLoadMoreListener {

    @BindView(R.id.rv_order_class)
    RecyclerView rvOrderClass;
    @BindView(R.id.pcfl_refresh)
    PtrClassicFrameLayout pcflRefresh;
    @BindView(R.id.linear_shaixuan)
    LinearLayout linearShaixuan;
    @BindView(R.id.tv_refresh_order)
    MyTextView tvRefreshOrder;
    @BindView(R.id.relative_reset)
    RelativeLayout relativeReset;
    @BindView(R.id.linear_bottom)
    MyLinearLayout linearBottom;

    public String longitudeAndlatitude;//经纬度
    LoadMoreAdapter adapter;
    private String userStatus;//骑手状态
    private int size;
    List<OrderListObj> list = new ArrayList<>();
    @Override
    protected int getContentView() {
        return R.layout.frgment_waiting_list;
    }

    @Override
    protected void initView() {
        relativeReset.setVisibility(View.GONE);// 后隐藏休息界面
        linearBottom.setVisibility(View.GONE); //隐藏底部栏
        adapter = new LoadMoreAdapter<OrderListObj>(mContext, R.layout.item_order_list, pageSize) {
            @Override
            public void bindData(LoadMoreViewHolder holder, int position, OrderListObj bean) {
                holder.setText(R.id.tv_order_price, "¥" + bean.getCharge())
                        .setText(R.id.tv_shop_distance, (bean.getQsdistance()))
                        .setText(R.id.tv_shop_name, bean.getShopName())
                        .setText(R.id.tv_shop_address, bean.getShopAddress())
                        .setText(R.id.tv_customer_distance, (bean.getScdistance()))
                        .setText(R.id.tv_customer_name, bean.getCustomerName())
                        .setText(R.id.tv_customer_address, bean.getCustomerAddress())
                        .setText(R.id.tv_order_receive,"确认送达");
                holder.getView(R.id.tv_order_receive).setOnClickListener(new MyOnClickListener() {
                    @Override
                    protected void onNoDoubleClick(View view) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("orderid", bean.getOrderid());
                        map.put("userid", SPUtils.getPrefString(mContext, Config.user_id, null));
                        map.put("sign", GetSign.getSign(map));
                        ApiRequest.GrabOrderStep3(map, new MyCallBack<GradObj>(mContext, pcfl, pl_load) {
                            @Override
                            public void onSuccess(GradObj gradObj) {
                                if(gradObj.getResult()==1){
                                    showToastS("送达成功");
                                    RxBus.getInstance().post(new RefreshEvent(2,size-1));//抢单成功后个数减一
                                    RxBus.getInstance().post(new RefreshFragmentEvent(2));//
                                }else{
                                    showToastS("送达失败");
                                }
                            }
                        });
                    }
                });
            }
        };

        adapter.setOnLoadMoreListener(this);
        rvOrderClass.setLayoutManager(new LinearLayoutManager(mContext));
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
                longitudeAndlatitude = event.longitudeAndlatitude;

            }
        });
        RxBus.getInstance().getEvent(ResetEvent.class, new MySubscriber<ResetEvent>() {
            @Override
            public void onMyNext(ResetEvent event) {
                if (event.ResetEvent == 1) { //
                    getData(false);
                }else{

                }
            }
        });
        //每个页面操作后刷新提示
        RxBus.getInstance().getEvent(RefreshFragmentEvent.class, new MySubscriber<RefreshFragmentEvent>() {
            @Override
            public void onMyNext(RefreshFragmentEvent event) {
                if(event.Refresh==1 ||event.Refresh==2){
                    getData(false);
                }
            }
        });

    }

    private void getData(boolean isLoad) {
         userStatus = SPUtils.getPrefString(mContext, Config.user_status,"0");
        if(userStatus.equals("0")){
            relativeReset.setVisibility(View.VISIBLE); //如果休息状态，则不去获取订单数据，只显示休息中的文字
            return ;
        }
        longitudeAndlatitude =  SPUtils.getPrefString(mContext, Config.position,"0");
        relativeReset.setVisibility(View.GONE); //如果休息状态，则不去获取订单数据，只显示休息中的文字
        Map<String, String> map = new HashMap<String, String>();
        map.put("coord", longitudeAndlatitude);
        map.put("type", "3");
        map.put("status", "2");
        map.put("userid", SPUtils.getPrefString(mContext, Config.user_id, null));
        map.put("orderby", "3");
        map.put("sign", GetSign.getSign(map));
        ApiRequest.orderList(map, new MyCallBack<List<OrderListObj>>(mContext, pcfl, pl_load) {
            @Override
            public void onSuccess(List<OrderListObj> list) {

                size = list.size();
                list= list;
                RxBus.getInstance().post(new RefreshEvent(2,list.size()));
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

    @Override
    public void loadMore() {
    }

}