package com.qifan.qishou.network.response;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class ScreeningBean {

    private List<TypeLisOneBean> typeListOne;

    private List<TypeListTwoBean> typeListTwo;

    public List<TypeLisOneBean> getTypeListOne() {
        return typeListOne;
    }

    public void setTypeListOne(List<TypeLisOneBean> typeListOne) {
        this.typeListOne = typeListOne;
    }

    public List<TypeListTwoBean> getTypeListTwo() {
        return typeListTwo;
    }

    public void setTypeListTwo(List<TypeListTwoBean> typeListTwo) {
        this.typeListTwo = typeListTwo;
    }

    public static class TypeLisOneBean {

        private int type_id;
        private String type_name;

        public int getType_id() {
            return type_id;
        }

        public void setType_id(int type_id) {
            this.type_id = type_id;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }
    }

    public static class TypeListTwoBean {

        private int type_id;
        private String type_name;

        public int getType_id() {
            return type_id;
        }

        public void setType_id(int type_id) {
            this.type_id = type_id;
        }

        public String getType_name() {
            return type_name;
        }

        public void setType_name(String type_name) {
            this.type_name = type_name;
        }
    }

}
