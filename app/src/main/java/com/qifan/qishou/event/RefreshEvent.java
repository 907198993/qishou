package com.qifan.qishou.event;

/**
 * Created by Administrator on 2018/5/11 0011.
 */

public class RefreshEvent {

    public  int Refresh;
    public  int orderSize;

    public RefreshEvent(int Refresh,int orderSize) {
        this.Refresh = Refresh;
        this.orderSize = orderSize;
    }

}
