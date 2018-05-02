package com.qifan.qishou.base;

import android.content.Context;

import com.github.retrofitutil.NetWorkManager;
import com.github.retrofitutil.NetworkMonitor;

/**
 * Created by administartor on 2017/8/31.
 */

public class BaseApiRequest{
    //返回对象-rxjava无缓存
    protected static <T> T getCommonClient(Class<T>clazz){
       /* boolean connected = NetworkMonitor.isConnected(callBack.getContext());
        if(connected){*/
            return NetWorkManager.getCommonClient().create(clazz);
        /*}else{
            throw new NoNetworkException("无网络连接,请稍后再试");
        }*/
    }
    //返回对象无缓存
    protected static <T> T getGeneralClient(Class<T>clazz){
        return NetWorkManager.getGeneralClient().create(clazz);
    }
    //返回String无缓存
    protected static <T> T getGeneralStringClient(Class<T>clazz){
        return NetWorkManager.getGeneralStringClient().create(clazz);
    }


    protected static boolean notNetWork(Context context){
        if(context!=null){
            boolean connected = NetworkMonitor.isConnected(context);
        /*if(connected){
            return NetWorkManager.getCommonClient().create(clazz);
        }else{
            throw new NoNetworkException("无网络连接,请稍后再试");
        }*/
            return !connected;
        }
        return false;
    }
}
