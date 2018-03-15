package com.jinkun_innovation.claim.livestock_detail;

import com.jinkun_innovation.claim.base.Callback;
import com.jinkun_innovation.claim.bean.LiveStockDetailBean;
import com.jinkun_innovation.claim.net.ApiCall;
import com.jinkun_innovation.claim.net.RetrofitManger;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yangxing on 2018/3/9.
 */

public class LiveStockDetailModel {

    public static void getliveStockDetail(String id, final Callback<LiveStockDetailBean> callback) {
        RetrofitManger.getInstance().createReq(ApiCall.class)
                .getLiveStockDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LiveStockDetailBean>() {
                    @Override
                    public void onCompleted() {
                        callback.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);

                    }

                    @Override
                    public void onNext(LiveStockDetailBean liveStockDetailBean) {
                        callback.onSuccess(liveStockDetailBean);
                    }
                });

    }
}
