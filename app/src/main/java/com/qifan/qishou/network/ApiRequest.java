package com.qifan.qishou.network;


import com.github.retrofitutil.NoNetworkException;
import com.qifan.qishou.Config;
import com.qifan.qishou.base.BaseApiRequest;
import com.qifan.qishou.base.MyCallBack;

import java.util.Map;

/**
 * Created by Administrator on 2017/6/28.
 */

public class ApiRequest extends BaseApiRequest {

    public static void userRegister(Map map, MyCallBack callBack) {
        if (notNetWork(callBack.getContext())) {
            callBack.onFailure(null, new NoNetworkException(Config.noNetWork));
            return;
        }
        getGeneralClient(IRequest.class).userRegister(map).enqueue(callBack);
    }

    public static void userLogin(Map map, MyCallBack callBack) {
        if (notNetWork(callBack.getContext())) {
            callBack.onFailure(null, new NoNetworkException(Config.noNetWork));
            return;
        }
        getGeneralClient(IRequest.class).userLogin(map).enqueue(callBack);
    }

    public static void userRetrievePwd(Map map, MyCallBack callBack) {
        if (notNetWork(callBack.getContext())) {
            callBack.onFailure(null, new NoNetworkException(Config.noNetWork));
            return;
        }
        getGeneralClient(IRequest.class).userRetrievePwd(map).enqueue(callBack);
    }

}
