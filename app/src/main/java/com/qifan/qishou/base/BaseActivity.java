package com.qifan.qishou.base;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Point;
import android.graphics.Rect;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.androidtools.ClickUtils;
import com.github.androidtools.PhoneUtils;
import com.github.androidtools.SPUtils;
import com.github.androidtools.StatusBarUtils;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.activity.IBaseActivity;
//import com.hyphenate.chat.ChatClient;
//import com.hyphenate.helpdesk.callback.Callback;
//import com.hyphenate.helpdesk.easeui.util.IntentBuilder;

import com.qifan.qishou.Config;
import com.qifan.qishou.GetSign;
import com.qifan.qishou.R;
import com.qifan.qishou.view.ProgressLayout;
//import com.sk.yangyu.module.my.network.ApiRequest;
//import com.sk.yangyu.module.my.network.response.FenXiangObj;
//import com.umeng.socialize.ShareAction;
//import com.umeng.socialize.UMShareAPI;
//import com.umeng.socialize.UMShareListener;
//import com.umeng.socialize.bean.SHARE_MEDIA;
//import com.umeng.socialize.media.UMImage;
//import com.umeng.socialize.media.UMWeb;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;


/**
 * Created by Administrator on 2017/6/1.
 */

public abstract class BaseActivity extends IBaseActivity implements ProgressLayout.OnAgainInter,View.OnClickListener{

    /*************************************************/
    protected Toolbar toolbar;
    private boolean showNavigationIcon =true;
    private int navigationIcon =-1;
    protected int pageNum=2;
    protected int pageSize= 10;
    private String appTitle,appRightTitle;
    private int appTitleColor,appRightTitleColor;
//    private int appRightImg;
    private int titleBackgroud= R.color.main_background;
    private int statusBarBackgroud= R.color.main_background;
    protected TextView app_title,app_right_tv;
    protected ImageView app_right_iv;
    private View status_bar;
    protected boolean hiddenBottomLine;
    protected PtrClassicFrameLayout pcfl;

    protected boolean isPause;
    protected ProgressLayout pl_load;
    /****************************************************/
    protected abstract int getContentView();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void onViewClick(View v);
    protected void initRxBus(){};
    protected void myReStart() {
    }
    protected void hiddenBottomLine(boolean flag){
        hiddenBottomLine=flag;
    }
    protected void setAppTitle(String title){
        appTitle=title;
        if(app_title!=null){
            app_title.setText(appTitle==null?"":appTitle);
        }
    }
    public void setAppRightTitle(String appRightTitle) {
        this.appRightTitle = appRightTitle;
        if(app_right_tv!=null){
            app_right_tv.setText(appRightTitle==null?"":appRightTitle);
        }
    }
//    public void setAppRightImg(int appRightImg) {
//        this.appRightImg = appRightImg;
//        if(app_right_iv!=null&&appRightImg!=0){
//            app_right_iv.setImageResource(appRightImg);
//
//            app_right_tv.setVisibility(View.GONE);
//            app_right_iv.setVisibility(View.VISIBLE);
//        }
//    }
    @Override
    protected void onPause() {
        super.onPause();
        isPause =true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(isPause){
            isPause =false;
            myReStart();
        }
    }
    public void setTitleBackgroud(int titleBackgroud) {
        this.titleBackgroud = titleBackgroud;
    }
    public void setStatusBarBackgroud(int statusBarBackgroud) {
        this.statusBarBackgroud = statusBarBackgroud;
        if(status_bar!=null){
            status_bar.setBackgroundColor(ContextCompat.getColor(mContext,statusBarBackgroud));
        }
    }
    public void setAppTitleColor(int appTitleColor) {
        this.appTitleColor = appTitleColor;
    }

    public void setAppRightTitleColor(int appRightTitleColor) {
        this.appRightTitleColor = appRightTitleColor;
    }
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getRequestedOrientation()!= ActivityInfo.SCREEN_ORIENTATION_PORTRAIT){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        setTheme(R.style.AppTheme_NoActionBar);
        mContext=this;
        if(getContentView()!=0){
            setContentView(getContentView());
        }
        if(Build.VERSION.SDK_INT< Build.VERSION_CODES.LOLLIPOP){
//            StatusBarUtils.setTransparent(this);
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        ButterKnife.bind(this);
        if(null!=findViewById(R.id.status_bar)){
            status_bar = findViewById(R.id.status_bar);
            int statusBarHeight = StatusBarUtils.getStatusBarHeight(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.height=statusBarHeight;
            status_bar.setLayoutParams(layoutParams);
            status_bar.setBackgroundColor(ContextCompat.getColor(mContext,statusBarBackgroud));
        }
        if(null!=findViewById(R.id.toolbar)){
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(new MyOnClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        finishAfterTransition();
                    }else{
                        finish();
                    }
                }
            });
            toolbar.setBackgroundColor(ContextCompat.getColor(mContext,titleBackgroud));
        }
        if(null!=findViewById(R.id.app_title)){
            app_title= (TextView) findViewById(R.id.app_title);
            app_title.setText(appTitle==null?"":appTitle);
            if(null!=findViewById(R.id.v_bottom_line)){
                if(TextUtils.isEmpty(appTitle)||hiddenBottomLine){
                    findViewById(R.id.v_bottom_line).setVisibility(View.GONE);
                }
            }

            if(appTitleColor!=0){
                app_title.setTextColor(appTitleColor);
            }
        }
        if(null!=findViewById(R.id.app_right_tv)){
            app_right_tv= (TextView) findViewById(R.id.app_right_tv);
        }
        if(null!=findViewById(R.id.app_right_iv)){
            app_right_iv= (ImageView) findViewById(R.id.app_right_iv);
        }
//        if(appRightImg!=0){
//            app_right_iv.setImageResource(appRightImg);
//
//            app_right_tv.setVisibility(View.GONE);
//            app_right_iv.setVisibility(View.VISIBLE);
//        }
        if(appRightTitle!=null){
            app_right_tv.setText(appRightTitle);
            app_right_tv.setVisibility(View.VISIBLE);
//            app_right_iv.setVisibility(View.GONE);
            if(appRightTitleColor!=0){
                app_right_tv.setTextColor(appRightTitleColor);
            }
        }
        if(null!=findViewById(R.id.pcfl_refresh)){
            pcfl = (PtrClassicFrameLayout) findViewById(R.id.pcfl_refresh);
            pcfl.setLastUpdateTimeRelateObject(this);
            pcfl.setOffsetXFlag(3);
            pcfl.setHorizontalMoveFlag(3);
            pcfl.setScaledTouchSlopFlag(1);
            pcfl.disableWhenHorizontalMove(true);
        }
        if(null!=findViewById(R.id.pl_load)){
            pl_load = (ProgressLayout) findViewById(R.id.pl_load);
            pl_load.setInter(this);
        }
        setInput();
        initRxBus();
        initView();
        if(toolbar!=null){
            if(navigationIcon!=-1){
                getSupportActionBar().setHomeAsUpIndicator(navigationIcon);
            }else{
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.my_app_back);
//                getSupportActionBar().setHomeAsUpIndicator(R.drawable.back);
            }
            getSupportActionBar().setDisplayHomeAsUpEnabled(showNavigationIcon);
        }
        initData();
    }
    protected String getUsersId(){
        return   SPUtils.getPrefString(mContext, Config.user_id,"0");
    }

    protected void setBackIcon(int resId){
        navigationIcon=resId;
    }
    protected void hiddenBackIcon(){
        showNavigationIcon=false;
    }
    protected void hiddenBackIcon(boolean show){
        showNavigationIcon=show;
    }
    protected String getSStr(View view){
        if(view instanceof TextView){
            return ((TextView)view).getText().toString();
        } else if (view instanceof EditText) {
            return ((EditText)view).getText().toString();
        }else{
            return null;
        }
    }
    public void showProgress(){
        if (pl_load != null) {
            pl_load.showProgress();
        }
    }
    public void showContent(){
        if (pl_load != null) {
            pl_load.showContent();
        }
    }
    public void showErrorText(){
        if (pl_load != null) {
            pl_load.showErrorText();
        }
    }
    @Override
    public void onClick(View v) {
        if(!ClickUtils.isFastClick(v, 800)){
            onViewClick(v);
        }
    }
    private void setInput() {
        final View rootView = ((ViewGroup) this.findViewById(android.R.id.content))
                .getChildAt(0);
        if(null==rootView){
            Log.i("BaseActivity","rootView=null");
            return;
        }
        final View decorView = getWindow().getDecorView();
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                Rect rect= new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int screenHeight = decorView.getRootView().getHeight();
                int heightDifferent = screenHeight - rect.bottom- PhoneUtils.getNavigationBarHeight(mContext);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) rootView.getLayoutParams();
                lp.setMargins(0, 0, 0, heightDifferent);
                rootView.requestLayout();
            }
        });
    }
    public int getKeyboardHeight(){
        final View decorView = getWindow().getDecorView();
        Rect rect= new Rect();
        decorView.getWindowVisibleDisplayFrame(rect);
        int screenHeight = decorView.getRootView().getHeight();
        int heightDifferent = screenHeight - rect.bottom- PhoneUtils.getNavigationBarHeight(mContext);
        return heightDifferent;
    }

    @Override
    public void again() {
        initData();
    }
    public int getUserType(){
        return SPUtils.getPrefInt(mContext, Config.userType,-1);
    }
    protected String getUserId(){
        return SPUtils.getPrefString(mContext,Config.user_id,null);
    }
    protected String getSign(){
        return getSign("user_id",getUserId());
    }
    protected String getSign(String key,String value){
        return GetSign.getSign(key,value);
    }
    protected boolean isEmpty(List list){
        return list == null || list.size() == 0;
    }
    protected boolean notEmpty(List list){
        return !(list == null || list.size() == 0);
    }
    protected String getRnd(){
        Random random = new Random();
        int rnd = random.nextInt(9000) + 1000;
        return rnd+"";
    }

    protected BaseDividerListItem getItemDivider(){
        return new BaseDividerListItem(mContext,BaseDividerListItem.VERTICAL_LIST,2,R.color.background_f2);
    }
    protected BaseDividerListItem getItemDivider(int height){
        return new BaseDividerListItem(mContext,BaseDividerListItem.VERTICAL_LIST,height,R.color.background_f2);
    }
    protected BaseDividerListItem getItemDivider(int height,int color){
        return new BaseDividerListItem(mContext,BaseDividerListItem.VERTICAL_LIST,height,color);
    }


    protected void initWebViewForContent(WebView webview,String content) {
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        //自适应屏幕  
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webview.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        //设置Web视图
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                initWebTopView();
            }
        });
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                //handler.cancel(); // Android默认的处理方式
                handler.proceed();  // 接受所有网站的证书
                //handleMessage(Message msg); // 进行其他处理
            }
        });
        Log.i("===","getNewContent==="+getNewContent(content));
        webview.loadDataWithBaseURL(null, getNewContent(content), "text/html", "utf-8",null);
//        webview.loadUrl(url);
        // 设置WevView要显示的网页
//        webview.loadDataWithBaseURL(null, content, "text/html", "utf-8",null);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setJavaScriptEnabled(true); //设置支持Javascript
        webview.requestFocus(); //触摸焦点起作用.如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
        webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });
    }
    protected void initWebViewForUrl(WebView webview,String url) {
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setTextSize(WebSettings.TextSize.NORMAL);
        //自适应屏幕  
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);
        webview.getSettings().setDefaultTextEncodingName("UTF -8");//设置默认为utf-8
        //设置Web视图
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                initWebTopView();
            }
        });

//        webview.loadDataWithBaseURL(null, getNewContent(url), "text/html", "utf-8",null);
        webview.loadUrl(url);
        // 设置WevView要显示的网页
//        webview.loadDataWithBaseURL(null, content, "text/html", "utf-8",null);
        webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webview.getSettings().setJavaScriptEnabled(true); //设置支持Javascript
        webview.requestFocus(); //触摸焦点起作用.如果不设置，则在点击网页文本输入框时，不能弹出软键盘及不响应其他的一些事件。
        webview.setWebViewClient(new WebViewClient()
        {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });
    }


    protected static String getNewContent(String htmltext){
        try {
            Document doc= Jsoup.parse(htmltext);
            Elements elements=doc.getElementsByTag("img");
            for (Element element : elements) {
                element.attr("width","100%").attr("height","auto");
            }
            return doc.toString();
        } catch (Exception e) {
            return htmltext;
        }
    }



//    public void goHX(){
//        if(ChatClient.getInstance().isLoggedInBefore()){
//            //已经登录，可以直接进入会话界面
//            OpenHuanXin();
//        }else{
//            showLoading();
//            hxName=SPUtils.getPrefString(mContext,Config.phone,null);
//            if(TextUtils.isEmpty(hxName)){
//                Random random = new Random();
//                int rn1=random.nextInt(9000) + 1000;
//                int rn2=random.nextInt(9000) + 1000;
//                hxName=rn1+""+rn2;
//                ChatClient.getInstance().createAccount(hxName, "123456", new Callback(){
//                    @Override
//                    public void onSuccess() {
//                        loginHXSuccess(hxName);
//                    }
//                    @Override
//                    public void onError(int i, String s) {
//                        Log.i("===",i+"=onError=="+s);
//                    }
//                    @Override
//                    public void onProgress(int i, String s) {
//                        Log.i("===",i+"=onProgress=="+s);
//                    }
//                });
//            }else{
//                loginHXSuccess(hxName);
//            }
//        }
//    }
    String hxName;
//    private void loginHXSuccess(String hxName) {
//        //未登录，需要登录后，再进入会话界面
//        ChatClient.getInstance().login(hxName,"123456", new Callback(){
//            @Override
//            public void onSuccess() {
//                dismissLoading();
//                OpenHuanXin();
//            }
//            @Override
//            public void onError(int i, String s) {
//                dismissLoading();
//
//            }
//            @Override
//            public void onProgress(int i, String s) {
//            }
//        });
//    }
    private void OpenHuanXin() {
//        Intent intent = new IntentBuilder(mContext)
//                .setServiceIMNumber(Config.hx_fwh) //获取地址：kefu.easemob.com，“管理员模式 > 渠道管理 > 手机APP”页面的关联的“IM服务号”
//                .build();
//        startActivity(intent);
    }

    /*****************************************************************第三方分享********************************************************************************/
//    protected void fenXiang(SHARE_MEDIA shareMedia,String  fenXiangId) {
//        showLoading();
//        Map<String,String> map=new HashMap<String,String>();
//        map.put("goods_id",fenXiangId);
//        map.put("sign",GetSign.getSign(map));
//        ApiRequest.fenXiang(map, new MyCallBack<FenXiangObj>(mContext,true) {
//            @Override
//            public void onSuccess(FenXiangObj obj) {
//                UMWeb web = new UMWeb(obj.getShare_link());
//                UMImage image=new UMImage(mContext,R.drawable.app_default);
//                web.setTitle(obj.getTitle());//标题
//                web.setThumb(image);  //缩略图
//                web.setDescription(obj.getContent());//描述
//                new ShareAction(mContext)
//                        .setPlatform(shareMedia)//传入平台
//                        .withMedia(web)
////                      .withText(getSStr(tv_fenxiao_detail_code))//分享内容
//                        .setCallback(getListener())
//                        .share();
//            }
//            @Override
//            public void onError(Throwable e) {
//                super.onError(e);
//                dismissLoading();
//            }
//        });
//    }
//    protected UMShareListener getListener() {
//        return new UMShareListener() {
//            @Override
//            public void onStart(SHARE_MEDIA share_media) {
//                dismissLoading();
//                Log.i("============","============onStart");
//            }
//
//            @Override
//            public void onResult(SHARE_MEDIA share_media) {
//                Log.i("============","============onResult");
//            }
//
//            @Override
//            public void onError(SHARE_MEDIA share_media, Throwable throwable) {
//                showMsg(throwable.getMessage());
//                Log.i("============","============onError");
//            }
//
//            @Override
//            public void onCancel(SHARE_MEDIA share_media) {
//                Log.i("============","============onCancel");
//            }
//        };
//    }
//    BottomSheetDialog fenXiangDialog;
//    public void showFenXiang(String fenXiangId){
//        if (fenXiangDialog == null) {
//            View sexView= LayoutInflater.from(mContext).inflate(R.layout.popu_fen_xiang,null);
//            /*iv_yaoqing_wx
//iv_yaoqing_friend
//iv_yaoqing_qq
//iv_yaoqing_qzone
//iv_yaoqing_sina*/
//            sexView.findViewById(R.id.iv_yaoqing_wx).setOnClickListener(new MyOnClickListener() {
//                @Override
//                protected void onNoDoubleClick(View view) {
//                    if (!UMShareAPI.get(mContext).isInstall(mContext, SHARE_MEDIA.WEIXIN)) {
//                        showMsg("请安装微信之后再试");
//                        return;
//                    }
//                    fenXiang(SHARE_MEDIA.WEIXIN,fenXiangId);
//                    fenXiangDialog.dismiss();
//
//                }
//            });
//            sexView.findViewById(R.id.iv_yaoqing_friend).setOnClickListener(new MyOnClickListener() {
//                @Override
//                protected void onNoDoubleClick(View view) {
//                    if (!UMShareAPI.get(mContext).isInstall(mContext, SHARE_MEDIA.WEIXIN)) {
//                        showMsg("请安装微信之后再试");
//                        return;
//                    }
//                    fenXiang(SHARE_MEDIA.WEIXIN_CIRCLE,fenXiangId);
//                    fenXiangDialog.dismiss();
//
//                }
//            });
//            sexView.findViewById(R.id.iv_yaoqing_qq).setOnClickListener(new MyOnClickListener() {
//                @Override
//                protected void onNoDoubleClick(View view) {
//                    if (!UMShareAPI.get(mContext).isInstall(mContext, SHARE_MEDIA.QQ)) {
//                        showMsg("请安装QQ之后再试");
//                        return;
//                    }
//                    fenXiang(SHARE_MEDIA.QQ,fenXiangId);
//                    fenXiangDialog.dismiss();
//                }
//            });
//            sexView.findViewById(R.id.iv_yaoqing_qzone).setOnClickListener(new MyOnClickListener() {
//                @Override
//                protected void onNoDoubleClick(View view) {
//                    if (!UMShareAPI.get(mContext).isInstall(mContext, SHARE_MEDIA.QQ)) {
//                        showMsg("请安装QQ之后再试");
//                        return;
//                    }
//                    fenXiang(SHARE_MEDIA.QZONE,fenXiangId);
//                    fenXiangDialog.dismiss();
//                }
//            });
//            /*sexView.findViewById(R.id.iv_yaoqing_sina).setOnClickListener(new MyOnClickListener() {
//                @Override
//                protected void onNoDoubleClick(View view) {
//                    showMsg("正在开发中");
//                    fenXiangDialog.dismiss();
//                }
//            });*/
//            sexView.findViewById(R.id.tv_fenxiang_cancle).setOnClickListener(new MyOnClickListener() {
//                @Override
//                protected void onNoDoubleClick(View view) {
//                    fenXiangDialog.dismiss();
//                }
//            });
//            fenXiangDialog=new BottomSheetDialog(mContext);
//            fenXiangDialog.setCanceledOnTouchOutside(true);
//            fenXiangDialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            fenXiangDialog.setContentView(sexView);
//        }
//        fenXiangDialog.show();
//    }
    public boolean keJian(View imageView){
        Point p=new Point();
        getWindowManager().getDefaultDisplay().getSize(p);
        int screenWidth=p.x;
        int screenHeight=p.y;

        Rect rect=new Rect(0,0,screenWidth,screenHeight );


        int[] location = new int[2];
        imageView.getLocationInWindow(location);
        System.out.println(Arrays.toString(location));

        // Rect ivRect=new Rect(imageView.getLeft(),imageView.getTop(),imageView.getRight(),imageView.getBottom());


        if (imageView.getLocalVisibleRect(rect)) {/*rect.contains(ivRect)*/
            return true;
        } else {
            return false;
        }
    }
}
