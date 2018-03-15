package com.jinkun_innovation.claim.categroy_model;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.jinkun_innovation.claim.R;
import com.jinkun_innovation.claim.base.BaseActivity;
import com.jinkun_innovation.claim.bean.RanchDetailBean;
import com.jinkun_innovation.claim.bean.RanchListBean;
import com.jinkun_innovation.claim.net.ApiCall;
import com.jinkun_innovation.claim.net.RetrofitManger;
import com.jinkun_innovation.claim.utilcode.util.ActivityUtils;
import com.jinkun_innovation.claim.weight.recyclerview.CommonAdapter;
import com.jinkun_innovation.claim.weight.recyclerview.MultiItemTypeAdapter;
import com.jinkun_innovation.claim.weight.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yangxing on 2018/3/10.
 */

public class CategorySingleRanchActivity extends BaseActivity {
    @BindView(R.id.recycle_category_single)
    RecyclerView mRecycleCategorySingle;

    private CommonAdapter<RanchDetailBean> mRanchDetailBeanCommonAdapter;
    private List<RanchDetailBean> mRanchDetailBeanArrayList = new ArrayList<>();
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
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycleCategorySingle.setLayoutManager(linearLayoutManager);
        mRanchDetailBeanCommonAdapter = new CommonAdapter<RanchDetailBean>(this, R.layout.item_single_ranch, mRanchDetailBeanArrayList) {
            @Override
            protected void convert(ViewHolder holder, RanchDetailBean ranchDetailBean, int position) {
                holder.setText(R.id.tv_single_ranch_name,ranchDetailBean.getName());
                holder.setText(R.id.tv_single_ranch_acreage,String.format(Locale.CHINA,"面积: %s",ranchDetailBean.getAcreage()));
                holder.setText(R.id.tv_single_ranch_count,String.format(Locale.CHINA,"牲畜数量: %s",ranchDetailBean.getIntroduceAnimalCount()));
                holder.setText(R.id.tv_single_ranch_river,String.format(Locale.CHINA,"是否有河流: %s",ranchDetailBean.getIntroduceRiver()));
                holder.setText(R.id.tv_single_ranch_close,String.format(Locale.CHINA,"是否有护栏: %s",ranchDetailBean.getIsFenceClose()));
                holder.setText(R.id.tv_single_ranch_address,String.format(Locale.CHINA,"地址: %s",ranchDetailBean.getAddress()));
            }
        };

        mRecycleCategorySingle.setAdapter(mRanchDetailBeanCommonAdapter);

        mRanchDetailBeanCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent();
                intent.setClass(CategorySingleRanchActivity.this, LiveStockListActivity.class);
                intent.putExtra("ranchId", String.valueOf(mRanchDetailBeanArrayList.get(position).getRanchId()));
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
                .getRanchList(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RanchListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RanchListBean ranchListBean) {
                        for (RanchDetailBean ranchDetailBean : ranchListBean.getRanchList()) {
                            mRanchDetailBeanArrayList.add(ranchDetailBean);
                        }

                        mRanchDetailBeanCommonAdapter.notifyDataSetChanged();
                    }
                });
    }
}
