package com.jinkun_innovation.claim.bean;

/**
 * Created by yangxing on 2018/3/7.
 */

public class RanchDetailRootBean {

    /**
     * ranch : {"startTime":null,"endTime":null,"ranchId":1,"name":"连江牧场","areaAddress":"兴安盟","address":"兴安盟连江牧场","rancherAccount":"13681542676","rancherName":"连江","rancherPhone":"13681542676","acreage":50000,"introduceAnimalCount":1000,"introduceAnimalType":"什么类型的都有","introduceMeadow":"草场很大","introduceRiver":"河流很宽","isFenceClose":"1","introduceNativeProduct":"羊奶和牛奶","imgUrl":"a","ranchImgUrl":"a","videoUrl":"a","longtitudeGps":null,"longtitudeBaidu":3333,"longtitudeGoogle":null,"lantitudeGps":null,"lantitudeBaidu":3333,"lantitudeGoogle":null,"numSort":9999,"status":1,"createTime":1520564504000,"updateTime":1520564502000,"qiId":1,"sumuId":1,"introduce":"牧场介绍，欢迎来看"}
     */

    private RanchDetailBean ranch;

    public RanchDetailBean getRanch() {
        return ranch;
    }

    public void setRanch(RanchDetailBean ranch) {
        this.ranch = ranch;
    }


}
