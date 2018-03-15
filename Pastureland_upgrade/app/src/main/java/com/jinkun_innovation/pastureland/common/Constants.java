package com.jinkun_innovation.pastureland.common;

/**
 * Created by Guan on 2018/3/13.
 */

public class Constants {


    public static final String BASE_URL = "http://182.92.3.109:10100";

    //牧场主找回密码
    //1)点击发送验证码：
    public static final String VERIFY_CODE = "/mobleClaim/txCode.do";

    //2)修改密码:
    public static final String MODIFY_PASSWORD = BASE_URL + "/releaseLivestocks/backPass.do";

    //发布认领
    //1、查看发布情况selectLivestock.do，未发布执行release.do
    public static final String SELECT_LIVE_STOCK = BASE_URL + "/releaseLivestocks/selectLivestock.do";

    //2、已发布判断是否被认领isClaimed.do，认领不可更新，否则更新
    public static final String IS_CLAIMED = BASE_URL + "/releaseLivestocks/isClaimed.do";

    //获取已发布牲畜信息
    public static final String LIVE_STOCK_CLAIM_LIST = BASE_URL + "/releaseLivestocks/livestockClaimList.do";


}
