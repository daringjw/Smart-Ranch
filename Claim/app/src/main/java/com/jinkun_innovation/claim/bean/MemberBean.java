package com.jinkun_innovation.claim.bean;

/**
 * Created by yangxing on 2018/3/7.
 */

public class MemberBean {
    private String name;//       人员姓名
    private String address;//    地址
    private String phone;//	  电话
    private String orderNo;//     订单号
    private String orderInstructions;//订单说明   牲畜公共信息表

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderInstructions() {
        return orderInstructions;
    }

    public void setOrderInstructions(String orderInstructions) {
        this.orderInstructions = orderInstructions;
    }


    @Override
    public String toString() {
        return "MemberBean{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", orderNo='" + orderNo + '\'' +
                ", orderInstructions='" + orderInstructions + '\'' +
                '}';
    }
}
