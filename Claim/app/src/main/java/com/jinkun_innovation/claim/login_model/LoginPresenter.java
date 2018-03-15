package com.jinkun_innovation.claim.login_model;

import com.jinkun_innovation.claim.base.BasePresenter;
import com.jinkun_innovation.claim.base.Callback;
import com.jinkun_innovation.claim.bean.LoginBean;

/**
 * Created by yangxing on 2018/3/5.
 */

public class LoginPresenter extends BasePresenter<LoginView> {
    public void toLogin(String cellPhoneNum, String verificationCode) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoading();
        LoginModel.toLogin(cellPhoneNum, verificationCode, new Callback<LoginBean>() {
            @Override
            public void onSuccess(LoginBean data) {
                getView().toLogin(data);
            }

            @Override
            public void onFailure(String msg) {
                getView().hideLoading();
                getView().showToast(msg);
            }

            @Override
            public void onError(Throwable throwable) {
                getView().showErr(throwable);
            }

            @Override
            public void onComplete() {
                getView().hideLoading();
            }
        });
    }
}
