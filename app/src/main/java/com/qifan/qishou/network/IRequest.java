package com.qifan.qishou.network;

import com.qifan.qishou.base.ResponseObj;
import com.qifan.qishou.network.response.LoginObj;
import com.qifan.qishou.network.response.RegisterObj;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

/**
 * Created by Administrator on 2017/6/28.
 */

public interface IRequest {
    //注册
    @POST("api/Login/GetUserRegister")
    Call<ResponseObj<RegisterObj>> userRegister(@QueryMap Map<String,String> map);

    //登陆
    @POST("api/Login/LoginUser")
    Call<ResponseObj<LoginObj>> userLogin(@QueryMap Map<String,String> map);

    //修改密码
    @GET("api/Login/RetrievePwd")
    Call<ResponseObj<Integer>> userRetrievePwd(@QueryMap Map<String,String> map);



}
