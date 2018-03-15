package com.jinkun_innovation.claim.ranch_model;

import com.jinkun_innovation.claim.base.Callback;
import com.jinkun_innovation.claim.bean.RanchDetailRootBean;
import com.jinkun_innovation.claim.net.ApiCall;
import com.jinkun_innovation.claim.net.RetrofitManger;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yangxing on 2018/3/9.
 */

public class RanchDetailModel {

    public static void getRanchDetail(String id, final Callback<RanchDetailRootBean> callback) {
        RetrofitManger.getInstance().createReq(ApiCall.class)
                .getRanchDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RanchDetailRootBean>() {
                    @Override
                    public void onCompleted() {
                        callback.onComplete();
                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(e);

                    }

                    @Override
                    public void onNext(RanchDetailRootBean ranchDetailBean) {
                        callback.onSuccess(ranchDetailBean);
                    }
                });

    }
}
