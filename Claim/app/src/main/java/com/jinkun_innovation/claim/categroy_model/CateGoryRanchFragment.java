package com.jinkun_innovation.claim.categroy_model;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinkun_innovation.claim.R;
import com.jinkun_innovation.claim.base.BaseFragment;
import com.jinkun_innovation.claim.bean.TownsInfoListBean;
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
import butterknife.Unbinder;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yangxing on 2018/3/10.
 */

public class CateGoryRanchFragment extends BaseFragment {
    @BindView(R.id.recycle_category)
    RecyclerView mRecycleCategory;
    Unbinder unbinder;
    private CommonAdapter<TownsInfoListBean.TownsInfoBean> mTownsInfoBeanCommonAdapter;
    private List<TownsInfoListBean.TownsInfoBean> mTownsInfoBeen = new ArrayList<>();

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_categroy_recycle;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadDate();

        initRecycle();
    }

    private void initRecycle() {
        LinearLayoutManager linearLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecycleCategory.setLayoutManager(linearLayoutManager);
        mTownsInfoBeanCommonAdapter = new CommonAdapter<TownsInfoListBean.TownsInfoBean>(getActivity(), R.layout.item_category_livestock, mTownsInfoBeen) {
            @Override
            protected void convert(ViewHolder holder, TownsInfoListBean.TownsInfoBean townsInfoBean, int position) {

                holder.setText(R.id.tv_category, townsInfoBean.getName());

            }
        };
        mRecycleCategory.setAdapter(mTownsInfoBeanCommonAdapter);

        mTownsInfoBeanCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), CategorySingleRanchActivity.class);
                intent.putExtra("id", mTownsInfoBeen.get(position).getId());
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
                .getTownsInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TownsInfoListBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TownsInfoListBean townsInfoListBean) {
                        for (TownsInfoListBean.TownsInfoBean townsInfoBean:townsInfoListBean.getTownsInfo()) {
                            mTownsInfoBeen.add(townsInfoBean);
                        }
                        mTownsInfoBeanCommonAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
