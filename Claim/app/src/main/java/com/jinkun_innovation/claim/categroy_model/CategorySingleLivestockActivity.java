package com.jinkun_innovation.claim.categroy_model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jinkun_innovation.claim.R;
import com.jinkun_innovation.claim.base.BaseActivity;
import com.jinkun_innovation.claim.bean.LivestockVarietyBean;
import com.jinkun_innovation.claim.net.ApiCall;
import com.jinkun_innovation.claim.net.RetrofitManger;
import com.jinkun_innovation.claim.utilcode.util.ActivityUtils;
import com.jinkun_innovation.claim.weight.recyclerview.CommonAdapter;
import com.jinkun_innovation.claim.weight.recyclerview.MultiItemTypeAdapter;
import com.jinkun_innovation.claim.weight.recyclerview.base.ViewHolder;

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

public class CategorySingleLivestockActivity extends BaseActivity {
    @BindView(R.id.recycle_category_single)
    RecyclerView mRecycleCategorySingle;

    private CommonAdapter<LivestockVarietyBean> mLivestockVarietyBeanCommonAdapter;
    private List<LivestockVarietyBean> mLivestockVarietyBeen = new ArrayList<>();
    private String id;

    @Override
    protected void initToolBar() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_category_single;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        loadDate();
        initRecycle();
    }

    private void initRecycle() {
        LinearLayoutManager  linearLayoutManager = new LinearLayoutManager(this);
        mRecycleCategorySingle.setLayoutManager(linearLayoutManager);
        mLivestockVarietyBeanCommonAdapter = new CommonAdapter<LivestockVarietyBean>(this,R.layout.item_single_livastock,mLivestockVarietyBeen) {
            @Override
            protected void convert(ViewHolder holder, LivestockVarietyBean livestockVarietyBean, int position) {
            }
        };

        mRecycleCategorySingle.setAdapter(mLivestockVarietyBeanCommonAdapter);
        mLivestockVarietyBeanCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent();
                intent.setClass(CategorySingleLivestockActivity.this, LiveStockListActivity.class);
                intent.putExtra("variety", mLivestockVarietyBeen.get(position).getVariety());
                ActivityUtils.startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void loadDate() {
        RetrofitManger.getInstance().createReq(ApiCall.class)
                .getLivestockVariety(id,"1","10")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LivestockVarietyBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(LivestockVarietyBean livestockVarietyBean) {
//                        for (LivestockVarietyBean livestockVarietyBean1:livestockVarietyBean) {
//                            mLivestockVarietyBeen.add(livestockVarietyBean1);
//                        }
                    }
                });
    }
}
