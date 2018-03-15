package com.jinkun_innovation.claim.bean;

import java.util.List;

/**
 * Created by yangxing on 2018/3/10.
 */

public class TownsInfoListBean {
    private List<TownsInfoBean> townsInfo;

    public List<TownsInfoBean> getTownsInfo() {
        return townsInfo;
    }

    public void setTownsInfo(List<TownsInfoBean> townsInfo) {
        this.townsInfo = townsInfo;
    }

    public static class TownsInfoBean {
        /**
         * startTime : null
         * endTime : null
         * id : 1
         * address : 苏木地址
         * name : 苏木名称
         * shortName : 苏木简称
         * qiId : 1
         * imgUrl : a
         * createTime : 1520575637000
         * updateTime : 1520575639000
         */

        private Object startTime;
        private Object endTime;
        private int id;
        private String address;
        private String name;
        private String shortName;
        private int qiId;
        private String imgUrl;
        private long createTime;
        private long updateTime;

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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getShortName() {
            return shortName;
        }

        public void setShortName(String shortName) {
            this.shortName = shortName;
        }

        public int getQiId() {
            return qiId;
        }

        public void setQiId(int qiId) {
            this.qiId = qiId;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
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
    }
}
