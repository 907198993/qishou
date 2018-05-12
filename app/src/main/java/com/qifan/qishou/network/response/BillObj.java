package com.qifan.qishou.network.response;

/**
 * Created by Administrator on 2018/5/12 0012.
 */

public class BillObj {

        /**
         * staus : 已被接单
         * orderid : 1
         * charge : null
         * shopName : XX运动鞋店
         * shopPhone : 134333444
         * shopAddress : 古城街3号
         * customerName : 罗二科
         * customerPhone : 13544451266
         * customerAddress : 详细地址HT.Model.ht_shipping_address
         * remark :
         * scdistance : null
         * customerLDH : 一栋3009
         * arriveDate : null
         */

        private String staus;
        private String orderid;
        private String charge;
        private String shopName;
        private String shopPhone;
        private String shopAddress;
        private String customerName;
        private String customerPhone;
        private String customerAddress;
        private String remark;
        private String scdistance;
        private String customerLDH;
        private String arriveDate;

        public String getStaus() {
            return staus;
        }

        public void setStaus(String staus) {
            this.staus = staus;
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



        public String getCustomerLDH() {
            return customerLDH;
        }

        public void setCustomerLDH(String customerLDH) {
            this.customerLDH = customerLDH;
        }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public String getScdistance() {
        return scdistance;
    }

    public void setScdistance(String scdistance) {
        this.scdistance = scdistance;
    }

    public String getArriveDate() {
        return arriveDate;
    }

    public void setArriveDate(String arriveDate) {
        this.arriveDate = arriveDate;
    }
}
