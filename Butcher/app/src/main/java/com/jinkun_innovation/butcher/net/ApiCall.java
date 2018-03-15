package com.jinkun_innovation.butcher.net;

import com.jinkun_innovation.butcher.bean.ResponseBean;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by yangxing on 2017/11/10.
 */

public interface ApiCall {
    @Multipart
//    @POST("/wisdomRanch/slaughterhouse/beforeSlaughtering.do")
    @POST("/slaughterhouse/beforeSlaughtering.do")
    Observable<ResponseBean> uploadPhoto(@Part MultipartBody.Part file
            , @Part("deviceNo") RequestBody deviceNo
            , @Part("phase") RequestBody phase);


    @POST("/slaughterhouse/toQueryRecord.do")
    @FormUrlEncoded
    Observable<ResponseBean> toQueryRecord(@Field("deviceNo") String deviceNo);

}
