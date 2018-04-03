package com.jinkun_innovation.pastureland.bean;

import java.util.List;

/**
 * Created by Guan on 2018/3/16.
 */

public class RenLing {


    private List<LivestockListBean> livestockList;

    public List<LivestockListBean> getLivestockList() {
        return livestockList;
    }

    public void setLivestockList(List<LivestockListBean> livestockList) {
        this.livestockList = livestockList;
    }

    public static class LivestockListBean {
        /**
         * characteristics : 黑头羊肉
         * livestockId : 267
         * color : 2
         * sumuId : 1
         * qiId : 1
         * weight : 32.0
         * health : 1
         * isVideo : 1
         * deviceNo : 0002180100000001
         * birthTime : 2018-02-20 22:29:36
         * livestockName : 乌珠穆沁黑头羊
         * businesstype : 3
         * imgUrl : http://192.168.50.215:8080/jkimg/20180309/111.jpg
         * isPhotographic : 1
         * variety : 100
         * createTime : 2018-03-21 08:46:26
         * ranchId : 1
         * price : 600.0
         * livestockType : 1
         * livestockDetails :
         * lifeTime : 48
         * isClaimed : 0
         * cellphone : 18611891462
         * videoTime : 2018-03-05 10:57:27
         * photographicTime : 2018-03-04 10:57:27
         * memberId : 33
         * finishTime : 2018-03-06 09:57:27
         * claimTime : 2018-03-05 10:57:27
         */

        public String name;
        public String nickname;

        private String characteristics;
        private String livestockId;
        private String color;
        private String sumuId;
        private String qiId;
        private String weight;
        private String health;
        private String isVideo;
        private String deviceNo;
        private String birthTime;
        private String livestockName;
        private String businesstype;
        private String imgUrl;
        private String isPhotographic;
        private String variety;
        private String createTime;
        private String ranchId;
        private String price;
        private String livestockType;
        private String livestockDetails;
        private String lifeTime;
        private String isClaimed;
        private String cellphone;
        private String videoTime;
        private String photographicTime;
        private String memberId;
        private String finishTime;
        private String claimTime;

        public String getCharacteristics() {
            return characteristics;
        }

        public void setCharacteristics(String characteristics) {
            this.characteristics = characteristics;
        }

        public String getLivestockId() {
            return livestockId;
        }

        public void setLivestockId(String livestockId) {
            this.livestockId = livestockId;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getSumuId() {
            return sumuId;
        }

        public void setSumuId(String sumuId) {
            this.sumuId = sumuId;
        }

        public String getQiId() {
            return qiId;
        }

        public void setQiId(String qiId) {
            this.qiId = qiId;
        }

        public String getWeight() {
            return weight;
        }

        public void setWeight(String weight) {
            this.weight = weight;
        }

        public String getHealth() {
            return health;
        }

        public void setHealth(String health) {
            this.health = health;
        }

        public String getIsVideo() {
            return isVideo;
        }

        public void setIsVideo(String isVideo) {
            this.isVideo = isVideo;
        }

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }

        public String getBirthTime() {
            return birthTime;
        }

        public void setBirthTime(String birthTime) {
            this.birthTime = birthTime;
        }

        public String getLivestockName() {
            return livestockName;
        }

        public void setLivestockName(String livestockName) {
            this.livestockName = livestockName;
        }

        public String getBusinesstype() {
            return businesstype;
        }

        public void setBusinesstype(String businesstype) {
            this.businesstype = businesstype;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getIsPhotographic() {
            return isPhotographic;
        }

        public void setIsPhotographic(String isPhotographic) {
            this.isPhotographic = isPhotographic;
        }

        public String getVariety() {
            return variety;
        }

        public void setVariety(String variety) {
            this.variety = variety;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getRanchId() {
            return ranchId;
        }

        public void setRanchId(String ranchId) {
            this.ranchId = ranchId;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getLivestockType() {
            return livestockType;
        }

        public void setLivestockType(String livestockType) {
            this.livestockType = livestockType;
        }

        public String getLivestockDetails() {
            return livestockDetails;
        }

        public void setLivestockDetails(String livestockDetails) {
            this.livestockDetails = livestockDetails;
        }

        public String getLifeTime() {
            return lifeTime;
        }

        public void setLifeTime(String lifeTime) {
            this.lifeTime = lifeTime;
        }

        public String getIsClaimed() {
            return isClaimed;
        }

        public void setIsClaimed(String isClaimed) {
            this.isClaimed = isClaimed;
        }

        public String getCellphone() {
            return cellphone;
        }

        public void setCellphone(String cellphone) {
            this.cellphone = cellphone;
        }

        public String getVideoTime() {
            return videoTime;
        }

        public void setVideoTime(String videoTime) {
            this.videoTime = videoTime;
        }

        public String getPhotographicTime() {
            return photographicTime;
        }

        public void setPhotographicTime(String photographicTime) {
            this.photographicTime = photographicTime;
        }

        public String getMemberId() {
            return memberId;
        }

        public void setMemberId(String memberId) {
            this.memberId = memberId;
        }

        public String getFinishTime() {
            return finishTime;
        }

        public void setFinishTime(String finishTime) {
            this.finishTime = finishTime;
        }

        public String getClaimTime() {
            return claimTime;
        }

        public void setClaimTime(String claimTime) {
            this.claimTime = claimTime;
        }
    }
}
