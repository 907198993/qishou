package com.qifan.qishou.base;

/**
 * Created by Administrator on 2017/7/20.
 */

public class ServerException extends Exception {
    //errorCode不为0时为服务器返回错误信息，需要显示在页面上面，代表不是网络错误
    public int errorCode;
    public ServerException(String msg) {
        super(msg);
    }
    public ServerException(int errorCode,String msg) {
        super(msg);
        this.errorCode=errorCode;
    }

}
