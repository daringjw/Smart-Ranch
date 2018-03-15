package com.jinkun_innovation.claim.login_model;

import com.jinkun_innovation.claim.base.Callback;
import com.jinkun_innovation.claim.bean.LoginBean;
import com.jinkun_innovation.claim.net.ApiCall;
import com.jinkun_innovation.claim.net.BaseSubscriber;
import com.jinkun_innovation.claim.net.ExceptionHandle;
import com.jinkun_innovation.claim.net.RetrofitManger;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yangxing on 2018/3/5.
 */

public class LoginModel {
    public static void toLogin(String cellPhoneNum, String verificationCode, final Callback<LoginBean> callBack) {
        RetrofitManger.getInstance().createReq(ApiCall.class)
                .toLogin(cellPhoneNum, verificationCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<LoginBean>() {
                    @Override
                    public void onCompleted() {
                        callBack.onComplete();
                    }


                    @Override
                    public void onError(ExceptionHandle.ResponseThrowable throwable) {
                        callBack.onError(throwable);
                    }

                    @Override
                    public void onNext(LoginBean bean) {
                        // TODO: 2018/3/5 分登录的类型
                        callBack.onSuccess(bean);
                    }
                });
    }
}
