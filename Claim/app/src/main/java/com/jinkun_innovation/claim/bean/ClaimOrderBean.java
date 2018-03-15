package com.jinkun_innovation.claim.bean;

import java.util.List;

/**
 * Created by yangxing on 2018/3/7.
 */

public class ClaimOrderBean {
    private List<LivestockBean> livestock;
    private List<MemberBean> member;

    public List<LivestockBean> getLivestock() {
        return livestock;
    }

    public void setLivestock(List<LivestockBean> livestock) {
        this.livestock = livestock;
    }

    public List<MemberBean> getMember() {
        return member;
    }

    public void setMember(List<MemberBean> member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "ClaimOrderBean{" +
                "livestock=" + livestock +
                ", member=" + member +
                '}';
    }
}
