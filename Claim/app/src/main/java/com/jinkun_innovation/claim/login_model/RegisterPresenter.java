package com.jinkun_innovation.claim.login_model;

import com.jinkun_innovation.claim.base.BasePresenter;
import com.jinkun_innovation.claim.base.Callback;
import com.jinkun_innovation.claim.bean.RespondBean;

/**
 * Created by yangxing on 2018/3/5.
 */

public class RegisterPresenter extends BasePresenter<RegisterView> {
    public void register(String cellPhoneNum, String verificationCode, String nickname,
                          String password, final String sex) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoading();
        RegisterModel.register(cellPhoneNum, verificationCode, nickname, password, sex,
                new Callback<RespondBean>() {
                    @Override
                    public void onSuccess(RespondBean data) {
                        getView().success(data);
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
