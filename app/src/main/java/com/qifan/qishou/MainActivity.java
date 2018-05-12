package com.qifan.qishou;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.androidtools.PhoneUtils;
import com.github.androidtools.SPUtils;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.rx.MySubscriber;
import com.github.baseclass.rx.RxBus;
import com.github.baseclass.view.MyPopupwindow;
import com.qifan.qishou.activity.LoginActivity;
import com.qifan.qishou.activity.MyWalletActivity;
import com.qifan.qishou.adapter.MyViewPagerAdapter;
import com.qifan.qishou.base.BaseActivity;
import com.qifan.qishou.event.LocationEvent;
import com.qifan.qishou.event.RefreshEvent;
import com.qifan.qishou.event.ResetEvent;
import com.qifan.qishou.fragment.WaitListFragment;
import com.qifan.qishou.fragment.WaitingGoodsFragment;
import com.qifan.qishou.fragment.WaitingSendGoodsFragment;
import com.qifan.qishou.service.LocationServices;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.iv_my)
    ImageView ivMy;
    @BindView(R.id.nav_view)
    NavigationView navView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;

    @BindView(R.id.main_tablayout)
    TabLayout mainTablayout;
    @BindView(R.id.main_viewpager)
    ViewPager mainViewpager;
    @BindView(R.id.tv_start_or_rest)
    TextView tvStartOrRest;
    private long mExitTime;
    private String userStatus;
    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navView.setItemIconTintList(null);
        userStatus = SPUtils.getPrefString(mContext, Config.user_status,"0");
        if(userStatus.equals("0")){
            tvStartOrRest.setText("未开工 v");
            RxBus.getInstance().post(new ResetEvent(0));
        }else{
            tvStartOrRest.setText("接单中 v");
            RxBus.getInstance().post(new ResetEvent(1));
        }
    }

    @Override
    protected void initData() {
        initUiAndData();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ivMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO 点击按钮打开侧滑菜单
                if (!drawer.isDrawerOpen(navView)) {
                    drawer.openDrawer(navView);
                }
            }
        });

    }

    @OnClick({R.id.tv_start_or_rest,R.id.iv_my_alarm})
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_start_or_rest:
                showPop();
                break;
            case R.id.iv_my_alarm:
                STActivity(LoginActivity.class);
                break;

        }
    }


    /**
     * 初始化话界面和数据
     */
    private void initUiAndData() {
        if (mainTablayout != null) {
            mainTablayout.setupWithViewPager(mainViewpager);
        }
        FragmentManager fm = getSupportFragmentManager();
        MyViewPagerAdapter pagerAdapter = new MyViewPagerAdapter(fm);
        pagerAdapter.addFragment(new WaitListFragment(), "待抢单");
        pagerAdapter.addFragment(new WaitingGoodsFragment(), "待取货");
        pagerAdapter.addFragment(new WaitingSendGoodsFragment(), "待送货");
        mainViewpager.setAdapter(pagerAdapter);
        mainViewpager.setOffscreenPageLimit(3);

    }

    @Override
    protected void initRxBus() {
        super.initRxBus();
         // 更新tab栏的订单数量
        RxBus.getInstance().getEvent(RefreshEvent.class, new MySubscriber<RefreshEvent>() {
            @Override
            public void onMyNext(RefreshEvent event) {
                if(event.Refresh == 0){
                    mainTablayout.getTabAt(0).setText("待抢单("+event.orderSize+")");
                }else if(event.Refresh == 1){
                    mainTablayout.getTabAt(1).setText("待取货("+event.orderSize+")");
                }else{
                    mainTablayout.getTabAt(2).setText("待送货("+event.orderSize+")");
                }
            }
        });
    }
//


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if ((System.currentTimeMillis() - mExitTime) > 1500) {
                showToastS("再按一次退出程序");
                mExitTime = System.currentTimeMillis();
            } else {
                super.onBackPressed();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.job_summary) {
            // Handle the camera action
        } else if (id == R.id.my_wallet) {
            STActivity(MyWalletActivity.class);

        } else if (id == R.id.message) {

        } else if (id == R.id.work_setting) {

        } else if (id == R.id.help) {

        } else if (id == R.id.setting) {

        }else if (id == R.id.kefu) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    MyPopupwindow Pop;

    private void showPop() {
        if (Pop == null) {
            View priceOrderView = LayoutInflater.from(mContext).inflate(R.layout.pop_item, null);

            LinearLayout tv_search_price_order = priceOrderView.findViewById(R.id.tv_start_work);
            tv_search_price_order.setOnClickListener(getOrderListener(0, 0, tv_search_price_order, "接单中 v"));

            LinearLayout tv_search_price_order_asc = priceOrderView.findViewById(R.id.tv_rest);
            tv_search_price_order_asc.setOnClickListener(getOrderListener(0, 1, tv_search_price_order_asc, "未开工 v"));

            Pop = new MyPopupwindow(mContext, priceOrderView, PhoneUtils.getScreenWidth(mContext) /4, -1);
            Pop.setBackground(new ColorDrawable(0));
        }
        Pop.showAsDropDown(tvStartOrRest,-20, 10);
    }
    @NonNull
    private MyOnClickListener getOrderListener(int flag,final  int index, LinearLayout textView,final String text) {
        return new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if(index==0){//
                    tvStartOrRest.setText("接单中 v");
                    Intent startIntent = new Intent(MainActivity.this, LocationServices.class);
                    startService(startIntent);
                    showMsg("开始接单");
                    //发送接单通知
                    RxBus.getInstance().post(new ResetEvent(1));
                    SPUtils.setPrefString(mContext, Config.user_status,"1");//1 可接单状态
                }else{
                    tvStartOrRest.setText("未开工 v");
                    Intent stopIntent = new Intent(MainActivity.this, LocationServices.class);
                    stopService(stopIntent);
                    showMsg("停止接单");
                   //发送休息通知
                    RxBus.getInstance().post(new ResetEvent(0));
                    SPUtils.setPrefString(mContext, Config.user_status,"0");//0 代表休息状态
                }
                Pop.dismiss();
//                showLoading();
            }
        };
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        Log.d("tag", "MainActivity.onDestroy()");
//        Intent stopIntent = new Intent(MainActivity.this, LocationServices.class);
//        stopService(stopIntent);


        super.onDestroy();
    }
}
