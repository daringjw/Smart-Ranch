package com.jinkun_innovation.claim.net;


import com.jinkun_innovation.claim.bean.ClaimOrderBean;
import com.jinkun_innovation.claim.bean.LiveStockDetailBean;
import com.jinkun_innovation.claim.bean.LivestockListBean;
import com.jinkun_innovation.claim.bean.LivestockTypeBean;
import com.jinkun_innovation.claim.bean.LivestockVarietyBean;
import com.jinkun_innovation.claim.bean.LoginBean;
import com.jinkun_innovation.claim.bean.RanchDetailRootBean;
import com.jinkun_innovation.claim.bean.RanchListBean;
import com.jinkun_innovation.claim.bean.RespondBean;
import com.jinkun_innovation.claim.bean.TownsInfoListBean;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by yangxing on 2017/11/10.
 */

public interface ApiCall {

    /**
     * 发送手机验证码
     *
     * @param cellPhoneNum 手机号
     * @return
     */
    @POST("mobleClaim/verificationCode.do")
    @FormUrlEncoded
    Observable<RespondBean> sendVerification(@Field("cellPhoneNum") String cellPhoneNum);

    /**
     * 注册完善信息
     *
     * @param cellPhoneNum     手机号
     * @param verificationCode 手机验证码
     * @param nickname         用户名
     * @param password         密码
     * @param sex              性别
     * @return
     */
    @POST("mobleClaim/register.do")
    @FormUrlEncoded
    Observable<RespondBean> register(@Field("cellPhoneNum") String cellPhoneNum,
                                     @Field("verificationCode") String verificationCode,
                                     @Field("nickname") String nickname,
                                     @Field("password") String password,
                                     @Field("sex") String sex);

    /**
     * 登录
     *
     * @param cellPhoneNum     手机号
     * @param verificationCode 手机验证码
     * @return
     */
    @POST("mobleClaim/login.do")
    @FormUrlEncoded
    Observable<LoginBean> toLogin(@Field("cellPhoneNum") String cellPhoneNum,
                                  @Field("verificationCode") String verificationCode);


    /**
     * 忘记密码
     *
     * @param cellPhoneNum     手机号
     * @param verificationCode 手机验证码
     * @param password         密码
     * @return
     */
    @POST("mobleClaim/backPass.do")
    @FormUrlEncoded
    Observable<RespondBean> forgetPwd(@Query("cellPhoneNum") String cellPhoneNum,
                                      @Query("verificationCode") String verificationCode,
                                      @Query("password") String password);


    /**
     * 获取首页数据
     *
     * @param variety  种类
     * @param ranchId  牧场id
     * @param current  请求起始地址
     * @param pageSize 请求显示条数
     * @return
     */
    @POST("claimLivestock/livestocksList.do")
    @FormUrlEncoded
    Observable<LivestockListBean> getLiveStockList(@Field("variety") String variety,
                                                   @Field("ranchId") String ranchId,
                                                   @Field("current") String current,
                                                   @Field("pageSize") String pageSize);

    /**
     * 获取牲畜详情
     * @param deviceNo 牲畜id
     * @return
     */
    @POST("/claimLivestock/livestock.do")
    @FormUrlEncoded
    Observable<LiveStockDetailBean> getLiveStockDetail(@Field("deviceNo") String deviceNo);

    /**
     * 获取牧场详情
     * @param id 牧场id
     * @return
     */
    @POST("/claimLivestock/ranch.do")
    Observable<RanchDetailRootBean> getRanchDetail(@Query("id") String id);


    /**
     * 生成订单信息并返回牲畜信息和人员信息
     * @param token
     * @param memberId
     * @param deviceId
     * @param deviceNo
     * @return
     */
    @POST("claimOrder/order.do")
    Observable<ClaimOrderBean> getClaimOrder(@Query("token") String token,
                                             @Query("memberId") String memberId,
                                             @Query("deviceId") String deviceId,
                                             @Query("deviceNo") String deviceNo);


    /**
     * 请求牲畜类型
     * @return
     */
    @GET("claimLivestock/livestockType.do")
    Observable<LivestockTypeBean> getLivestockType();

    /**
     * 获取牧场分类
     * @return
     */
    @GET("/claimLivestock/ranchSumu.do")
    Observable<TownsInfoListBean> getTownsInfo();

    /**
     * 点击牲畜类型图片请求
     * @param livestockType 类型
     * @param current 请求起始地址
     * @param pagesize 请求显示条数
     * @return
     */
    // TODO: 2018/3/7
    @POST("/claimLivestock/livestockVarietyList.do")
    Observable<LivestockVarietyBean> getLivestockVariety(@Query("livestockType") String livestockType,
                                                         @Query("current") String current,
                                                         @Query("pagesize") String pagesize);

   @POST("/claimLivestock/ranchList.do")
   @FormUrlEncoded
    Observable<RanchListBean> getRanchList(@Field("sumuId") String sumuId);




}
