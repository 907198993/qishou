package com.qifan.qishou.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.androidtools.PhoneUtils;
import com.github.androidtools.SPUtils;
import com.github.androidtools.ToastUtils;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.adapter.LoadMoreAdapter;
import com.github.baseclass.adapter.LoadMoreViewHolder;
import com.github.customview.FlowLayout;
import com.github.customview.MyTextView;
import com.qifan.qishou.Config;
import com.qifan.qishou.GetSign;
import com.qifan.qishou.R;
import com.qifan.qishou.base.BaseFragment;
import com.qifan.qishou.base.MyCallBack;
import com.qifan.qishou.network.ApiRequest;
import com.qifan.qishou.network.response.OrderListObj;
import com.qifan.qishou.network.response.ScreeningBean;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
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
 * Created by Administrator on 2018/4/12.
 */

public class WaitListFragment extends BaseFragment  implements LoadMoreAdapter.OnLoadMoreListener{

    @BindView(R.id.rv_order_class)
    RecyclerView rvOrderClass;
    @BindView(R.id.pcfl_refresh)
    PtrClassicFrameLayout pcflRefresh;
    @BindView(R.id.linear_shaixuan)
    LinearLayout linearShaixuan;
    @BindView(R.id.tv_refresh_order)
    MyTextView tvRefreshOrder;
    Unbinder unbinder;
    private Dialog shareDialog;

    private ScreeningBean screeningBean = new ScreeningBean();
    private List<ScreeningBean.TypeLisOneBean> typeListOne = new ArrayList<ScreeningBean.TypeLisOneBean>();
    private List<ScreeningBean.TypeListTwoBean> typeListTwo = new ArrayList<ScreeningBean.TypeListTwoBean>();
    ScreeningBean.TypeLisOneBean  typeLisOneBean = new ScreeningBean.TypeLisOneBean();
    ScreeningBean.TypeListTwoBean  typeLisTwoBean = new ScreeningBean.TypeListTwoBean();
    private String[] screen= {"外卖单","帮买单","帮送单"};
    private String[] sorting = {"取货距离近到远","配送距离近到远","运单金额高到低"};

    private String popScreen ;
    private String popSort;


    LoadMoreAdapter adapter;

    @Override
    protected int getContentView() {
        return R.layout.frgment_waiting_list;
    }

    @Override
    protected void initView() {
        //初始化筛选
        for(int i = 0;i< screen.length;i++){
            typeLisOneBean.setType_id(i+1);
            typeLisOneBean.setType_name(screen[i]);
            typeListOne.add(typeLisOneBean);
        }
        screeningBean.setTypeListOne(typeListOne);
        //初始化排序
        for(int i = 0;i< sorting.length;i++){
            typeLisTwoBean.setType_id(i+1);
            typeLisTwoBean.setType_name(screen[i]);
            typeListTwo.add(typeLisTwoBean);
        }
        screeningBean.setTypeListTwo(typeListTwo);

        adapter=new LoadMoreAdapter<OrderListObj>(mContext,R.layout.item_order_list,pageSize) {
            @Override
            public void bindData(LoadMoreViewHolder holder, int position, OrderListObj bean) {
                holder.setText(R.id.tv_order_price, Double.toString(bean.getCharge()))
                        .setText(R.id.tv_shop_distance, Double.toString(bean.getQsdistance()))
                        .setText(R.id.tv_shop_name,bean.getShopName())
                        .setText(R.id.tv_shop_address,bean.getShopAddress())
                        .setText(R.id.tv_customer_distance,Double.toString(bean.getScdistance()))
                        .setText(R.id.tv_customer_name,bean.getCustomerName())
                        .setText(R.id.tv_customer_address,bean.getCustomerAddress());

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
                getData(1,false);
            }
        });

    }

    @Override
    protected void initData() {
        popScreen =  SPUtils.getPrefString(mContext, Config.pop_screen,null);
        popSort =  SPUtils.getPrefString(mContext, Config.pop_sort,null);
        getData(1,false);

    }

    private void getData(int page, boolean isLoad) {
        Map<String,String> map=new HashMap<String,String>();
//        map.put("coord",getUserId());
//        map.put("type",page+"");
//        map.put("status",);
//        map.put("sign", GetSign.getSign(map));
//        map.put("userid", GetSign.getSign(map));
//        map.put("orderby",);
        ApiRequest.orderList(map, new MyCallBack<List<OrderListObj>>(mContext,pcfl,pl_load) {
            @Override
            public void onSuccess(List<OrderListObj> list) {
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

    @OnClick({R.id.linear_shaixuan,R.id.tv_refresh_order})
    protected void onViewClick(View v) {
    switch (v.getId()){
        case R.id.linear_shaixuan:
            showGuiGe();
             break;
        case R.id.tv_refresh_order:
            break;
     }
    }

    private void showGuiGe() {

        shareDialog = new Dialog(mContext);//,R.style.dialogAnimation
        Window win = shareDialog.getWindow();
        win.setGravity(Gravity.CENTER);
        win.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        win.getDecorView().setPadding(0,0,0, PhoneUtils.getNavigationBarHeight(mContext));

        win.setBackgroundDrawableResource(android.R.color.transparent);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        win.setAttributes(lp);

        Context context = shareDialog.getContext();
        int divierId = context.getResources().getIdentifier("android:id/titleDivider", null, null);
        View divider = shareDialog.findViewById(divierId);
        if (divider != null) {
            divider.setBackgroundColor(mContext.getResources().getColor(android.R.color.transparent));
        }

        View guiGeView = LayoutInflater.from(mContext).inflate(R.layout.pop_screening, null);

        NestedScrollView nsv_goods_detail_guige = guiGeView.findViewById(R.id.nsv_goods_detail_guige);
        win.getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect= new Rect();
                win.getDecorView().getWindowVisibleDisplayFrame(rect);
                int screenHeight = win.getDecorView().getRootView().getHeight();
                int heightDifferent = screenHeight - rect.bottom- PhoneUtils.getNavigationBarHeight(mContext);
                win.getDecorView().setPadding(0, 0, 0, heightDifferent+PhoneUtils.getNavigationBarHeight(mContext));
                nsv_goods_detail_guige.fullScroll(View.FOCUS_DOWN);
            }
        });

        LinearLayout ll_guige_bg = guiGeView.findViewById(R.id.ll_guige_bg);
        TagFlowLayout fl_screen = guiGeView.findViewById(R.id.screen_flowlayout);
        TagFlowLayout fl_sort = guiGeView.findViewById(R.id.sort_flowlayout);


        TextView tv_reset = guiGeView.findViewById(R.id.tv_reset); //重置
        TextView tv_commit = guiGeView.findViewById(R.id.tv_commit); //确定

      //运单筛选
        final LayoutInflater mInflater = LayoutInflater.from(getActivity());
        fl_screen.setAdapter(new TagAdapter<String>(screen)
        {
            @Override
            public View getView(com.zhy.view.flowlayout.FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflater.inflate(R.layout.flowlayout_tv,
                        fl_screen, false);
                tv.setText(s);

                return tv;
            }
        });
        fl_screen.setMaxSelectCount(3);

        if(popScreen == null){
            fl_screen.getAdapter().setSelectedList(0,1,2);
        }else{
            String[] str = popScreen.split(",");
            int array[] = new int[str.length];
            for(int i=0;i<str.length;i++) {
                array[i] = Integer.parseInt(str[i]);
            }
                fl_screen.getAdapter().setSelectedList(array);
            }

        fl_screen.setOnSelectListener(new TagFlowLayout.OnSelectListener() {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
//                popScreen = setToString(selectPosSet);
            }
        });

        //运单排序
        final LayoutInflater mInflaterSort = LayoutInflater.from(getActivity());
        fl_sort.setAdapter(new TagAdapter<String>(sorting)
        {
            @Override
            public View getView(com.zhy.view.flowlayout.FlowLayout parent, int position, String s)
            {
                TextView tv = (TextView) mInflaterSort.inflate(R.layout.flowlayout_tv,
                        fl_sort, false);
                tv.setText(s);
                return tv;
            }
        });
        //设置最大选择个数
        fl_sort.setMaxSelectCount(1);
        if(popSort == null){
            fl_sort.getAdapter().setSelectedList(0);
        }else{
            fl_sort.getAdapter().setSelectedList(Integer.parseInt(popSort));
        }

        fl_sort.setOnSelectListener(new TagFlowLayout.OnSelectListener()
        {
            @Override
            public void onSelected(Set<Integer> selectPosSet)
            {
//                popSort =  setToString(selectPosSet);
            }
        });

        tv_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //存储筛选
                SPUtils.setPrefString(mContext, Config.pop_screen,null);
                //存储排序
                SPUtils.setPrefString(mContext, Config.pop_sort,null);
                fl_screen.getAdapter().notifyDataChanged();
                fl_sort.getAdapter().notifyDataChanged();

            }
        });
       //确定最终的选择
        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取所有选择的运单筛选
                popScreen =setToString(fl_screen.getSelectedList());
                if(popScreen.length()<1){
                    showMsg("运单筛选不能为空");
                    return ;
                }
                 //存储筛选
                SPUtils.setPrefString(mContext, Config.pop_screen,popScreen);
                //获取所有选择的运单排序
                popSort =setToString(fl_sort.getSelectedList());
                if(popSort.length()<1){
                    showMsg("运单排序不能为空");
                    return ;
                }
                //存储排序
                SPUtils.setPrefString(mContext, Config.pop_sort,popSort);
                shareDialog.dismiss();
            }
        });
       //点击背景关闭弹出框
        ll_guige_bg.setOnClickListener(new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                shareDialog.dismiss();
            }
        });
        shareDialog.setContentView(guiGeView);
        shareDialog.show();
    }

    public  String setToString(Set<Integer> selectPosSet){
        String s ="";
        for (Integer str : selectPosSet) {
            System.out.println(str);
            int i = str;
            s+=i+",";
        }
        if(s.length()<1){
            return "";
        }else{
            ToastUtils.showToast( getActivity(),"choose:" + s.substring(0,s.length() - 1)+"===="+s.length());
            return s.substring(0,s.length() - 1);
        }
    }

    @Override
    public void loadMore() {

    }



}
