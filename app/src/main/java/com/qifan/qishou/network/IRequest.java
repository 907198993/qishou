package com.qifan.qishou.network;

import com.qifan.qishou.base.ResponseObj;
import com.qifan.qishou.network.response.BillObj;
import com.qifan.qishou.network.response.GradObj;
import com.qifan.qishou.network.response.LoginObj;
import com.qifan.qishou.network.response.OrderListObj;
import com.qifan.qishou.network.response.RegisterObj;
import com.qifan.qishou.network.response.UploadImageObj;
import com.qifan.qishou.network.response.UploadImgItem;
import com.qifan.qishou.network.response.WalletObj;
import com.qifan.qishou.network.response.WithdrawalsObj;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
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

    //获取订单列表
    @POST("api/Order/GetOrder")
    Call<ResponseObj<List<OrderListObj>>> orderList(@QueryMap Map<String,String> map);

    //抢单
    @POST("api/Order/GrabOrder")
    Call<ResponseObj<GradObj>> GrabOrderStep1(@QueryMap Map<String,String> map);

    //上报到店
    @POST("api/Order/takegoods")
    Call<ResponseObj<GradObj>> GrabOrderStep2(@QueryMap Map<String,String> map);

    //送达
    @POST("api/Order/completeOrder")
    Call<ResponseObj<GradObj>> GrabOrderStep3(@QueryMap Map<String,String> map);

    //钱包
    @POST("api/Account/GetMyWallet")
    Call<ResponseObj<WalletObj>> userWallet(@QueryMap Map<String,String> map);

    //账单列表
    @POST("api/Account/GetBillList")
    Call<ResponseObj<List<BillObj>>> userBillList(@QueryMap Map<String,String> map);

    //获取提现
    @POST("api/Account/Gettixian")
    Call<ResponseObj<WithdrawalsObj>> Gettixian(@QueryMap Map<String,String> map);

    //账单列表
    @POST("api/Account/tixian")
    Call<ResponseObj<GradObj>> Withdrawals(@QueryMap Map<String,String> map);

    //图片上传
    @POST("api/Lib/PostUploadFileBase64")
    Call<ResponseObj<UploadImageObj>> uploadImg(@QueryMap Map<String, String> map, @Body UploadImgItem item);

    //实名认证
    @POST("api/Login/UploadCard")
    Call<ResponseObj<GradObj>> UploadCard(@QueryMap Map<String,String> map);



}
