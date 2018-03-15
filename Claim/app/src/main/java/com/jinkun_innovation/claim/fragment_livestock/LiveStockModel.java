package com.jinkun_innovation.claim.fragment_livestock;

import com.jinkun_innovation.claim.base.Callback;
import com.jinkun_innovation.claim.bean.LivestockListBean;
import com.jinkun_innovation.claim.net.ApiCall;
import com.jinkun_innovation.claim.net.RetrofitManger;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yangxing on 2018/3/5.
 */

public class LiveStockModel {
    public static void getLiveStockList(String variety, String ranchId, String current, String pageSize, final Callback<LivestockListBean> callback) {
        RetrofitManger.getInstance().createReq(ApiCall.class)
                .getLiveStockList(variety, ranchId, current, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LivestockListBean>() {
                    @Override
                    public void onCompleted() {
                        callback.onComplete();
                    }


                    @Override
                    public void onError(Throwable throwable) {
//                        callback.onError(throwable);
                    }

                    @Override
                    public void onNext(LivestockListBean liveStockBean) {
                        callback.onSuccess(liveStockBean);
                    }
                });
    }
}
