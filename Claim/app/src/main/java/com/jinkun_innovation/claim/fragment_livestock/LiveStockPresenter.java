package com.jinkun_innovation.claim.fragment_livestock;

import com.jinkun_innovation.claim.base.BasePresenter;
import com.jinkun_innovation.claim.base.Callback;
import com.jinkun_innovation.claim.bean.LivestockListBean;

/**
 * Created by yangxing on 2018/3/5.
 */

public class LiveStockPresenter extends BasePresenter<LiveStockView> {

    public void getLiveStockList(String variety, String ranchId, String current, String pageSize) {
        if (!isViewAttached()) {
            return;
        }
        getView().showLoading();
        LiveStockModel.getLiveStockList(variety, ranchId, current, pageSize, new Callback<LivestockListBean>() {
            @Override
            public void onSuccess(LivestockListBean data) {
                getView().showLiveStock(data);
            }

            @Override
            public void onFailure(String msg) {
                getView().showToast(msg);
            }

            @Override
            public void onError(Throwable throwable ) {
                getView().hideLoading();
                getView().showErr(throwable);

            }

            @Override
            public void onComplete() {
                getView().hideLoading();

            }
        });
    }
}
