package com.jinkun_innovation.claim.login_model;

import com.jinkun_innovation.claim.base.Callback;
import com.jinkun_innovation.claim.bean.RespondBean;
import com.jinkun_innovation.claim.net.ApiCall;
import com.jinkun_innovation.claim.net.BaseSubscriber;
import com.jinkun_innovation.claim.net.ExceptionHandle;
import com.jinkun_innovation.claim.net.RetrofitManger;
import com.jinkun_innovation.claim.utilcode.SpUtil;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yangxing on 2018/3/5.
 */

public class RegisterModel {
    public static void register(final String cellPhoneNum, String verificationCode, String nickname,
                                String password, String sex, final Callback<RespondBean> callBack) {
        RetrofitManger.getInstance().createReq(ApiCall.class)
                .register(cellPhoneNum, verificationCode, nickname, password, sex)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<RespondBean>() {
                    @Override
                    public void onCompleted() {
                        callBack.onComplete();
                    }


                    @Override
                    public void onError(ExceptionHandle.ResponseThrowable throwable) {
                        callBack.onError(throwable);
                    }

                    @Override
                    public void onNext(RespondBean bean) {
                        switch (bean.getMsg()) {
                            case "error":
                                callBack.onFailure("验证码错误");
                                break;
                            case "success":
                                callBack.onSuccess(bean);
                                SpUtil.saveAccount(cellPhoneNum);
                                break;
                            case "registered":
                                callBack.onFailure("手机号已被注册");
                                break;
                        }
                    }
                });
    }
}
