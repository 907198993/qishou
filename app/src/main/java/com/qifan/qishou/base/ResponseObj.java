package com.qifan.qishou.base;

/**
 * Created by Administrator on 2017/6/28.
 */

public class ResponseObj<T> {

    /**
     * ErrCode : 0
     * ErrMsg : 注册成功，欢迎成为本站会员！
     * Response : {"avatar":"http://yinya.hai-tao.net","mobile":"15601772922","nick_name":"156***2922","sex":"保密","user_name":"15601772922","user_id":"1117"}
     */

    private int ErrCode;
    private String ErrMsg;
    private T Response;

    public int getErrCode() {
        return ErrCode;
    }

    public void setErrCode(int ErrCode) {
        this.ErrCode = ErrCode;
    }

    public String getErrMsg() {
        return ErrMsg;
    }

    public void setErrMsg(String ErrMsg) {
        this.ErrMsg = ErrMsg;
    }

    public T getResponse() {
        return Response;
    }

    public void setResponse(T Response) {
        this.Response = Response;
    }
    public boolean isSuccess(){
        if(getErrCode()==0){
            return true;
        }
        return false;
    }
}
