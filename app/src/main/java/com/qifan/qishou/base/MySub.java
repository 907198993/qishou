package com.qifan.qishou.base;

import android.content.Context;

import com.github.androidtools.ToastUtils;
import com.github.baseclass.view.Loading;
import com.github.retrofitutil.NoNetworkException;
import com.qifan.qishou.view.ProgressLayout;


import in.srain.cube.views.ptr.PtrFrameLayout;
import rx.Subscriber;

/**
 * Created by Administrator on 2017/6/16.
 */
public abstract class MySub<T> extends Subscriber<T> {
    private Context context;
    private boolean noHiddenLoad;
    private PtrFrameLayout pfl;
    private ProgressLayout progressLayout;
    public MySub(Context ctx) {
        this.context = ctx;
    }
    public MySub(Context ctx, ProgressLayout pl) {
        this.context = ctx;
        this.progressLayout=pl;
    }
    public MySub(Context ctx, PtrFrameLayout pfl) {
        this.context = ctx;
        this.pfl=pfl;
    }
    public MySub(Context ctx, PtrFrameLayout pfl, ProgressLayout pl) {
        this.context = ctx;
        this.pfl=pfl;
        this.progressLayout=pl;
    }
    public MySub(Context ctx, boolean noHiddenLoad) {
        this.context = ctx;
        this.noHiddenLoad = noHiddenLoad;
    }
    public abstract void onMyNext(T obj);
    public void onMyCompleted(){
    }

    public void onMyError(Throwable e){
    }

    @Override
    public void onCompleted() {
        onMyCompleted();
        if(!noHiddenLoad){
            Loading.dismissLoading();
        }
        if (progressLayout != null) {
            progressLayout.showContent();
            progressLayout=null;
        }
        this.context=null;
    }
    @Override
    public void onError(Throwable e) {
        if(pfl!=null){
            pfl.refreshComplete();
            pfl=null;
        }
        if (progressLayout != null) {
            progressLayout.showErrorText();
            progressLayout=null;
        }
        if(e instanceof ServerException ||e instanceof NoNetworkException){
            ToastUtils.showToast(context,e.getMessage());
        }else{
            ToastUtils.showToast(context,"连接失败");
            e.printStackTrace();
        }
        onMyError(e);
        Loading.dismissLoading();
        this.context=null;

    }
    @Override
    public void onNext(T obj) {
        if(pfl!=null){
            pfl.refreshComplete();
        }
        onMyNext(obj);
        onCompleted();
    }
}
