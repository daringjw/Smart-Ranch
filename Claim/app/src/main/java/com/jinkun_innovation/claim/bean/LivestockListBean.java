package com.jinkun_innovation.claim.bean;

import java.util.List;

/**
 * Created by yangxing on 2018/3/5.
 */

public class LivestockListBean {
    private List<RanchBean> ranchList;
    private List<LivestockBean> livestockList;

    public List<RanchBean> getRanchList() {
        return ranchList;
    }

    public void setRanchList(List<RanchBean> ranchList) {
        this.ranchList = ranchList;
    }

    public List<LivestockBean> getLivestockList() {
        return livestockList;
    }

    public void setLivestockList(List<LivestockBean> livestockList) {
        this.livestockList = livestockList;
    }

    @Override
    public String toString() {
        return "LivestockListBean{" +
                "ranchList=" + ranchList +
                ", livestockList=" + livestockList +
                '}';
    }
}
