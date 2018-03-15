package com.jinkun_innovation.claim.bean;

/**
 * Created by yangxing on 2018/3/5.
 */
public class RanchBean {
    private String imgUrl; //牧场图片路径
    private String ranchId; //牧场ID

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getRanchId() {
        return ranchId;
    }

    public void setRanchId(String ranchId) {
        this.ranchId = ranchId;
    }

    @Override
    public String toString() {
        return "RanchBean{" +
                "imgUrl='" + imgUrl + '\'' +
                ", ranchId='" + ranchId + '\'' +
                '}';
    }
}
