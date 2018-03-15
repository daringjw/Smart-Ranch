package com.jinkun_innovation.claim.ranch_model;

import com.jinkun_innovation.claim.base.BasePresenter;
import com.jinkun_innovation.claim.base.Callback;
import com.jinkun_innovation.claim.bean.RanchDetailRootBean;

/**
 * Created by yangxing on 2018/3/9.
 */

public class RanchDetailPresenter extends BasePresenter<RanchDetailView> {
    public void getLiveStock(String id) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoading();
        RanchDetailModel.getRanchDetail(id, new Callback<RanchDetailRootBean>() {
            @Override
            public void onSuccess(RanchDetailRootBean data) {
                getView().showDetail(data.getRanch());
            }

            @Override
            public void onFailure(String msg) {
                getView().hideLoading();
                getView().showToast(msg);
            }

            @Override
            public void onError(Throwable e) {
                getView().hideLoading();
                getView().showErr(e);
            }

            @Override
            public void onComplete() {
                getView().hideLoading();

            }
        });

    }
}
