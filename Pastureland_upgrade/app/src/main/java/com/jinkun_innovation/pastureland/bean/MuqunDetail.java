package com.jinkun_innovation.pastureland.bean;

/**
 * Created by Guan on 2018/4/3.
 */

public class MuqunDetail {


    /**
     * msg : 获取牧场详情成功
     * ranch : {"ranchImgUrl":"/jkimg/20180403/f951aabc03415a94c1617d5383ca3cff.jpg","address":"深圳市宝安区金海路12号","sumuId":"9","qiId":"8","updateTime":"2018-04-03 17:38:09","rancherAccount":"18126177726","longtitudeBaidu":"113.876508","imgUrl":"/jkimg/20180328/9bf11c8a4b4ffa63161884c8299a170b.jpg","introduceAnimalCount":"228","createTime":"2018-03-29 14:38:45","ranchId":"11","name":"测试专用","lantitudeBaidu":"22.60329","numSort":"9999","status":"1"}
     * code : success
     */

    private String msg;
    private RanchBean ranch;
    private String code;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RanchBean getRanch() {
        return ranch;
    }

    public void setRanch(RanchBean ranch) {
        this.ranch = ranch;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public static class RanchBean {
        /**
         * ranchImgUrl : /jkimg/20180403/f951aabc03415a94c1617d5383ca3cff.jpg
         * address : 深圳市宝安区金海路12号
         * sumuId : 9
         * qiId : 8
         * updateTime : 2018-04-03 17:38:09
         * rancherAccount : 18126177726
         * longtitudeBaidu : 113.876508
         * imgUrl : /jkimg/20180328/9bf11c8a4b4ffa63161884c8299a170b.jpg
         * introduceAnimalCount : 228
         * createTime : 2018-03-29 14:38:45
         * ranchId : 11
         * name : 测试专用
         * lantitudeBaidu : 22.60329
         * numSort : 9999
         * status : 1
         */

        private String ranchImgUrl;
        private String address;
        private String sumuId;
        private String qiId;
        private String updateTime;
        private String rancherAccount;
        private String longtitudeBaidu;
        private String imgUrl;
        private String introduceAnimalCount;
        private String createTime;
        private String ranchId;
        private String name;
        private String lantitudeBaidu;
        private String numSort;
        private String status;

        public String getRanchImgUrl() {
            return ranchImgUrl;
        }

        public void setRanchImgUrl(String ranchImgUrl) {
            this.ranchImgUrl = ranchImgUrl;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
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

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getRancherAccount() {
            return rancherAccount;
        }

        public void setRancherAccount(String rancherAccount) {
            this.rancherAccount = rancherAccount;
        }

        public String getLongtitudeBaidu() {
            return longtitudeBaidu;
        }

        public void setLongtitudeBaidu(String longtitudeBaidu) {
            this.longtitudeBaidu = longtitudeBaidu;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getIntroduceAnimalCount() {
            return introduceAnimalCount;
        }

        public void setIntroduceAnimalCount(String introduceAnimalCount) {
            this.introduceAnimalCount = introduceAnimalCount;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLantitudeBaidu() {
            return lantitudeBaidu;
        }

        public void setLantitudeBaidu(String lantitudeBaidu) {
            this.lantitudeBaidu = lantitudeBaidu;
        }

        public String getNumSort() {
            return numSort;
        }

        public void setNumSort(String numSort) {
            this.numSort = numSort;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
