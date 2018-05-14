package com.qifan.qishou.network.response;

/**
 * Created by Administrator on 2018/5/14 0014.
 */

public class WithdrawalsObj {


        /**
         * result : 1
         * account : 13541108951
         * usable : 300
         * realName : 张三
         */

        private int result;
        private String account;
        private String usable;
        private String realName;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getUsable() {
            return usable;
        }

        public void setUsable(String usable) {
            this.usable = usable;
        }

        public String getRealName() {
            return realName;
        }

        public void setRealName(String realName) {
            this.realName = realName;
        }

}
