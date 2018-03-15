package com.jinkun_innovation.claim.livestock_detail;

import com.jinkun_innovation.claim.base.BasePresenter;
import com.jinkun_innovation.claim.base.Callback;
import com.jinkun_innovation.claim.bean.LiveStockDetailBean;

/**
 * Created by yangxing on 2018/3/9.
 */

public class LiveStockDetailPresenter extends BasePresenter<LivestockDetailView> {
    public void getLiveStock(String id) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoading();
        LiveStockDetailModel.getliveStockDetail(id, new Callback<LiveStockDetailBean>() {
            @Override
            public void onSuccess(LiveStockDetailBean data) {
                getView().showDetail(data);
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
