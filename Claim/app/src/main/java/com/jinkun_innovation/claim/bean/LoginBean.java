package com.jinkun_innovation.claim.bean;

/**
 * Created by yangxing on 2018/3/5.
 */

public class LoginBean {
    private String tocken;
    private String memberId;
    private String msg;

    public String getTocken() {
        return tocken;
    }

    public void setTocken(String tocken) {
        this.tocken = tocken;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "LoginBean{" +
                "tocken='" + tocken + '\'' +
                ", memberId='" + memberId + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
