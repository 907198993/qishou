package com.qifan.qishou.network.response;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class OrderListObj {
        /**
         * orderid : 4
         * charge : null
         * shopName : XX运动鞋店
         * shopPhone : 134333444
         * shopAddress : 古城街3号
         * customerName : 罗二科
         * customerPhone : 13544451266
         * customerAddress : 详细地址HT.Model.ht_shipping_address
         * remark :
         * qsdistance : null
         * scdistance : 2.453
         * customerLDH : 一栋3009
         */

        private int orderid;
        private double charge;
        private String shopName;
        private String shopPhone;
        private String shopAddress;
        private String customerName;
        private String customerPhone;
        private String customerAddress;
        private String remark;
        private double qsdistance;
        private double scdistance;
        private String customerLDH;

        public int getOrderid() {
            return orderid;
        }

        public void setOrderid(int orderid) {
            this.orderid = orderid;
        }

    public double getCharge() {
        return charge;
    }

    public void setCharge(double charge) {
        this.charge = charge;
    }

    public double getQsdistance() {
        return qsdistance;
    }

    public void setQsdistance(double qsdistance) {
        this.qsdistance = qsdistance;
    }

    public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopPhone() {
            return shopPhone;
        }

        public void setShopPhone(String shopPhone) {
            this.shopPhone = shopPhone;
        }

        public String getShopAddress() {
            return shopAddress;
        }

        public void setShopAddress(String shopAddress) {
            this.shopAddress = shopAddress;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getCustomerPhone() {
            return customerPhone;
        }

        public void setCustomerPhone(String customerPhone) {
            this.customerPhone = customerPhone;
        }

        public String getCustomerAddress() {
            return customerAddress;
        }

        public void setCustomerAddress(String customerAddress) {
            this.customerAddress = customerAddress;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }


        public double getScdistance() {
            return scdistance;
        }

        public void setScdistance(double scdistance) {
            this.scdistance = scdistance;
        }

        public String getCustomerLDH() {
            return customerLDH;
        }

        public void setCustomerLDH(String customerLDH) {
            this.customerLDH = customerLDH;
        }

}
