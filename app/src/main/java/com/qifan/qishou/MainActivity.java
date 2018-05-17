package com.qifan.qishou;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.qzs.voiceannouncementlibrary.VoiceUtils;
import com.github.androidtools.PhoneUtils;
import com.github.androidtools.SPUtils;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.rx.MySubscriber;
import com.github.baseclass.rx.RxBus;
import com.github.baseclass.view.MyPopupwindow;
import com.qifan.qishou.activity.AdverActivity;
import com.qifan.qishou.activity.CheckPermissionsActivity;
import com.qifan.qishou.activity.CommitIdCardActivity;
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
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionNo;
import com.yanzhenjie.permission.PermissionYes;

import java.util.List;

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

    private static final String TAG = "MainActivity";

    private static final int CAMERA_REQUESTCODE = 101;
    private static final int LOCATION_CONTACTS_REQUESTCODE = 102;
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
    // 成功回调的方法，用注解即可，这里的300就是请求时的requestCode。
    @PermissionYes(300)
    private void getPermissionYes(List<String> grantedPermissions) {
        // TODO 申请权限成功。
    }

    @PermissionNo(300)
    private void getPermissionNo(List<String> deniedPermissions) {
        // TODO 申请权限失败。
    }
    @Override
    protected void initData() {
        // 在Activity：
        AndPermission.with(MainActivity.this)
                .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                .requestCode(300)
                .callback(this)
                .start();


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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch(requestCode){
            case 0:
                // 处理用户授权的返回结果
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    // 授权失败
                }
                break;
            default:
                break;
        }
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
            STActivity(CommitIdCardActivity.class);
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

            RelativeLayout tv_search_price_order = priceOrderView.findViewById(R.id.tv_start_work);
            tv_search_price_order.setOnClickListener(getOrderListener(0, 0, tv_search_price_order, "接单中 v"));

            RelativeLayout tv_search_price_order_asc = priceOrderView.findViewById(R.id.tv_rest);
            tv_search_price_order_asc.setOnClickListener(getOrderListener(0, 1, tv_search_price_order_asc, "未开工 v"));

            Pop = new MyPopupwindow(mContext, priceOrderView, PhoneUtils.getScreenWidth(mContext) /4, -1);
            Pop.setBackground(new ColorDrawable(0));
        }
        Pop.showAsDropDown(tvStartOrRest,-20, 10);
    }
    private MediaPlayer mPlayer = null;
    @NonNull
    private MyOnClickListener getOrderListener(int flag,final  int index, RelativeLayout textView,final String text) {
        return new MyOnClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                userStatus = SPUtils.getPrefString(mContext, Config.user_status,"0");
                if(userStatus=="0"&& index ==1){ //如果休息中，点击休息则跳过。
                  return ;
                } else
                if(userStatus=="1"&& index ==0){
                    return ;
                }
               else if(index==0){//
                   if(isOPen(mContext)==false) {

                     showMsg("未开启gps");
                       return ;
                   }
//
//                    mPlayer = MediaPlayer.create(MainActivity.this,R.raw.music);
//                    mPlayer.setLooping(true);
//                    mPlayer.start();

//                    Play("开始接单啦");
                            tvStartOrRest.setText("接单中 v");
                            Intent startIntent = new Intent(MainActivity.this, LocationServices.class);
                            startService(startIntent);
                            showMsg("开始接单");
                            //发送接单通知
                            SPUtils.setPrefString(mContext, Config.user_status,"1");//1 可接单状态
                            RxBus.getInstance().post(new ResetEvent(1));

                }else{
//                    Play("停止接单");
                    tvStartOrRest.setText("未开工 v");
                    Intent stopIntent = new Intent(MainActivity.this, LocationServices.class);
                    stopService(stopIntent);
                    showMsg("停止接单");
                   //发送休息通知
                    SPUtils.setPrefString(mContext, Config.user_status,"0");//0 代表休息状态
                    RxBus.getInstance().post(new ResetEvent(0));

                }
                Pop.dismiss();
//                showLoading();
            }
        };
    }

//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void requestLocationAndContacts() {
//        if(!shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)){
//            Toast.makeText(this, "xxxxx", Toast.LENGTH_LONG).show();
//        }
//
//        String[] perms = { Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION};
//        if (EasyPermissions.hasPermissions(this, perms)) {
//            Toast.makeText(this, "已授权 联系人和 位置信息", Toast.LENGTH_LONG).show();
//
//        } else {
//            Uri packageURI = Uri.parse("package:" + mContext.getPackageName());
//            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI);
//            startActivity(intent);
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        // EasyPermissions 权限处理请求结果
//        Log.i(TAG,"onRequestPermissionsResult:"+ requestCode);
//        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
//    }
//
//    //同意授权
//    @Override
//    public void onPermissionsGranted(int requestCode, List<String> perms) {
//        Log.d(TAG, "onPermissionsGranted:" + requestCode + ":" + perms.size());
//    }
//
//    //拒绝授权
//    @Override
//    public void onPermissionsDenied(int requestCode, List<String> perms) {
//        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());
//    }
//
//    public boolean showContacts(){
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED
//                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
//                != PackageManager.PERMISSION_GRANTED) {
//            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
////            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
//            return false;
////            ActivityCompat.requestPermissions(getApplicationContext(),new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE}, BAIDU_READ_PHONE_STATE);
//        }else{
//            return true;
//        }
//    }

    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }
        return false;
      }

    public static final void openGPS(Context context) {
        Intent GPSIntent = new Intent();
        GPSIntent.setClassName("com.android.settings",
                "com.android.settings.widget.SettingsAppWidgetProvider");
        GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
        GPSIntent.setData(Uri.parse("custom:3"));
        try {
            PendingIntent.getBroadcast(context, 0, GPSIntent, 0).send();
        } catch (PendingIntent.CanceledException e) {
            e.printStackTrace();
        }
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
