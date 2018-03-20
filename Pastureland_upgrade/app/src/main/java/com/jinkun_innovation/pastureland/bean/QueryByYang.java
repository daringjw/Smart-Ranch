package com.jinkun_innovation.pastureland.bean;

import java.util.List;

/**
 * Created by Guan on 2018/3/20.
 */

public class QueryByYang {


    private List<LivestockVarietyListBean> livestockVarietyList;

    public List<LivestockVarietyListBean> getLivestockVarietyList() {
        return livestockVarietyList;
    }

    public void setLivestockVarietyList(List<LivestockVarietyListBean> livestockVarietyList) {
        this.livestockVarietyList = livestockVarietyList;
    }

    public static class LivestockVarietyListBean {
        /**
         * startTime : null
         * endTime : null
         * id : 241
         * deviceNo : 0002180100000250
         * deviceType : 2
         * deviceNoHistory : null
         * weight : 0.0
         * livestockType : 1
         * variety : 100
         * health : 3
         * color : 2
         * imgUrl : null
         * imgUrlQrCode : null
         * isClaimed : 0
         * phase : 1
         * bindStatus : 0
         * createTime : 1521169027000
         * updateTime : 1521169027000
         * imgUrlTime : null
         * unbindTime : null
         * ranchId : 1
         * qiId : 1
         * sumuId : 1
         */

        private Object startTime;
        private Object endTime;
        private int id;
        private String deviceNo;
        private int deviceType;
        private Object deviceNoHistory;
        private double weight;
        private int livestockType;
        private int variety;
        private int health;
        private int color;
        private Object imgUrl;
        private Object imgUrlQrCode;
        private int isClaimed;
        private int phase;
        private int bindStatus;
        private long createTime;
        private long updateTime;
        private Object imgUrlTime;
        private Object unbindTime;
        private int ranchId;
        private int qiId;
        private int sumuId;

        public Object getStartTime() {
            return startTime;
        }

        public void setStartTime(Object startTime) {
            this.startTime = startTime;
        }

        public Object getEndTime() {
            return endTime;
        }

        public void setEndTime(Object endTime) {
            this.endTime = endTime;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }

        public int getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(int deviceType) {
            this.deviceType = deviceType;
        }

        public Object getDeviceNoHistory() {
            return deviceNoHistory;
        }

        public void setDeviceNoHistory(Object deviceNoHistory) {
            this.deviceNoHistory = deviceNoHistory;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }

        public int getLivestockType() {
            return livestockType;
        }

        public void setLivestockType(int livestockType) {
            this.livestockType = livestockType;
        }

        public int getVariety() {
            return variety;
        }

        public void setVariety(int variety) {
            this.variety = variety;
        }

        public int getHealth() {
            return health;
        }

        public void setHealth(int health) {
            this.health = health;
        }

        public int getColor() {
            return color;
        }

        public void setColor(int color) {
            this.color = color;
        }

        public Object getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(Object imgUrl) {
            this.imgUrl = imgUrl;
        }

        public Object getImgUrlQrCode() {
            return imgUrlQrCode;
        }

        public void setImgUrlQrCode(Object imgUrlQrCode) {
            this.imgUrlQrCode = imgUrlQrCode;
        }

        public int getIsClaimed() {
            return isClaimed;
        }

        public void setIsClaimed(int isClaimed) {
            this.isClaimed = isClaimed;
        }

        public int getPhase() {
            return phase;
        }

        public void setPhase(int phase) {
            this.phase = phase;
        }

        public int getBindStatus() {
            return bindStatus;
        }

        public void setBindStatus(int bindStatus) {
            this.bindStatus = bindStatus;
        }

        public long getCreateTime() {
            return createTime;
        }

        public void setCreateTime(long createTime) {
            this.createTime = createTime;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }

        public Object getImgUrlTime() {
            return imgUrlTime;
        }

        public void setImgUrlTime(Object imgUrlTime) {
            this.imgUrlTime = imgUrlTime;
        }

        public Object getUnbindTime() {
            return unbindTime;
        }

        public void setUnbindTime(Object unbindTime) {
            this.unbindTime = unbindTime;
        }

        public int getRanchId() {
            return ranchId;
        }

        public void setRanchId(int ranchId) {
            this.ranchId = ranchId;
        }

        public int getQiId() {
            return qiId;
        }

        public void setQiId(int qiId) {
            this.qiId = qiId;
        }

        public int getSumuId() {
            return sumuId;
        }

        public void setSumuId(int sumuId) {
            this.sumuId = sumuId;
        }
    }
}
