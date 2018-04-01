package com.jinkun_innovation.pastureland.bean;

/**
 * Created by Guan on 2018/4/1.
 */

public class SelectLivestock {


    /**
     * LivestockType : 1
     * code : true
     * createTime : 2018-02-01 16:04:54
     * imgUrl : /jkimg/20180401/a629b15b143be61bb6b04ba58d8d58cb.jpg
     * msg : 此牲畜已经发布过
     * typeList : {"牛":2,"猪":4,"羊":1,"马":3,"鸡":5,"鹿":6}
     * variety : 100
     * weight : 100.0
     */

    private int LivestockType;
    private boolean code;
    private String createTime;
    private String imgUrl;
    private String msg;
    private TypeListBean typeList;
    private int variety;
    private double weight;

    public int getLivestockType() {
        return LivestockType;
    }

    public void setLivestockType(int LivestockType) {
        this.LivestockType = LivestockType;
    }

    public boolean isCode() {
        return code;
    }

    public void setCode(boolean code) {
        this.code = code;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public TypeListBean getTypeList() {
        return typeList;
    }

    public void setTypeList(TypeListBean typeList) {
        this.typeList = typeList;
    }

    public int getVariety() {
        return variety;
    }

    public void setVariety(int variety) {
        this.variety = variety;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public static class TypeListBean {
        /**
         * 牛 : 2
         * 猪 : 4
         * 羊 : 1
         * 马 : 3
         * 鸡 : 5
         * 鹿 : 6
         */

        private int 牛;
        private int 猪;
        private int 羊;
        private int 马;
        private int 鸡;
        private int 鹿;

        public int get牛() {
            return 牛;
        }

        public void set牛(int 牛) {
            this.牛 = 牛;
        }

        public int get猪() {
            return 猪;
        }

        public void set猪(int 猪) {
            this.猪 = 猪;
        }

        public int get羊() {
            return 羊;
        }

        public void set羊(int 羊) {
            this.羊 = 羊;
        }

        public int get马() {
            return 马;
        }

        public void set马(int 马) {
            this.马 = 马;
        }

        public int get鸡() {
            return 鸡;
        }

        public void set鸡(int 鸡) {
            this.鸡 = 鸡;
        }

        public int get鹿() {
            return 鹿;
        }

        public void set鹿(int 鹿) {
            this.鹿 = 鹿;
        }
    }
}
