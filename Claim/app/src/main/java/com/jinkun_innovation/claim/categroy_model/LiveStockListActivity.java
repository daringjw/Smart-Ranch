package com.jinkun_innovation.claim.categroy_model;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jinkun_innovation.claim.R;
import com.jinkun_innovation.claim.base.BaseActivity;
import com.jinkun_innovation.claim.bean.LivestockBean;
import com.jinkun_innovation.claim.bean.LivestockListBean;
import com.jinkun_innovation.claim.fragment_livestock.LivestockAdapter;
import com.jinkun_innovation.claim.net.ApiCall;
import com.jinkun_innovation.claim.net.RetrofitManger;
import com.jinkun_innovation.claim.utilcode.util.LogUtils;
import com.jinkun_innovation.claim.weight.recyclerview.wrapper.LoadMoreWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yangxing on 2018/3/10.
 */

public class LiveStockListActivity extends BaseActivity {
    @BindView(R.id.recycle_livestock)
    RecyclerView mRecycleLivestock;

    private String variety = "";
    private String ranchId = "";

    private LivestockAdapter mLivestockBeanCommonAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private List<LivestockBean> mLivestockBeen = new ArrayList<>();

    @Override
    protected void initToolBar() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_livestock_list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        variety = getIntent().getStringExtra("variety");
        ranchId = getIntent().getStringExtra("ranchId");
        LogUtils.e(variety, ranchId);
        loadData();
        initRecycle();

    }

    private void initRecycle() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycleLivestock.setLayoutManager(linearLayoutManager);
        mLivestockBeanCommonAdapter = new LivestockAdapter(this, R.layout.item_livestock, mLivestockBeen);
        mLoadMoreWrapper = new LoadMoreWrapper(mLivestockBeanCommonAdapter);
        mRecycleLivestock.setAdapter(mLivestockBeanCommonAdapter);
    }

    private void loadData() {
        RetrofitManger.getInstance().createReq(ApiCall.class)
                .getLiveStockList(variety, ranchId, "1", "10")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LivestockListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.e(e.getMessage());

                    }

                    @Override
                    public void onNext(LivestockListBean livestockListBean) {
                        for (LivestockBean livestockBean : livestockListBean.getLivestockList()) {
                            mLivestockBeen.add(livestockBean);
                        }
                        mLivestockBeanCommonAdapter.notifyDataSetChanged();
                        LogUtils.e(livestockListBean.toString(),mLivestockBeen.size());
                    }
                });
    }
}
