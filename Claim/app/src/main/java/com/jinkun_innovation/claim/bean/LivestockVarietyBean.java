package com.jinkun_innovation.claim.bean;

/**
 * Created by yangxing on 2018/3/7.
 */

public class LivestockVarietyBean {
    private String variety;       //品种名称
    private String imgUrl;        //品种图片
    private String count;          //总条数

    public String getVariety() {
        return variety;
    }

    public void setVariety(String variety) {
        this.variety = variety;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "LivestockVarietyBean{" +
                "variety='" + variety + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
