package com.jinkun_innovation.claim.fragment_livestock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinkun_innovation.claim.R;
import com.jinkun_innovation.claim.base.BaseFragment;
import com.jinkun_innovation.claim.bean.LivestockBean;
import com.jinkun_innovation.claim.bean.LivestockListBean;
import com.jinkun_innovation.claim.bean.RanchBean;
import com.jinkun_innovation.claim.livestock_detail.LivestockDetailActivity;
import com.jinkun_innovation.claim.ranch_model.RanchDetailActivity;
import com.jinkun_innovation.claim.utilcode.util.ActivityUtils;
import com.jinkun_innovation.claim.utilcode.util.LogUtils;
import com.jinkun_innovation.claim.utilcode.util.ToastUtils;
import com.jinkun_innovation.claim.weight.banner.BannerLayout;
import com.jinkun_innovation.claim.weight.recyclerview.MultiItemTypeAdapter;
import com.jinkun_innovation.claim.weight.recyclerview.wrapper.LoadMoreWrapper;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by yangxing on 2018/3/3.
 */

public class LiveStockFragment extends BaseFragment implements LiveStockView {
    @BindView(R.id.banner_livestock)
    BannerLayout mBannerLivestock;
    Unbinder unbinder;
    @BindView(R.id.recycle_livestock)
    RecyclerView mRecycleLivestock;

    private List<String> mStrings = new ArrayList<>();
    private LiveStockPresenter mLiveStockPresenter;
    private LoadMoreWrapper<LivestockBean> mLoadMoreWrapper;
    private List<LivestockBean> mLiveStockList = new ArrayList<>();
    private List<String> mBannerImgList = new ArrayList<>();

    private LoadService mLoadService;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_livestock;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        mLoadService = LoadSir.getDefault().register(rootView, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {

            }
        });
        return mLoadService.getLoadLayout();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBanner();
        initRecycle();
        mLiveStockPresenter = new LiveStockPresenter();
        mLiveStockPresenter.attachView(this);
        mLiveStockPresenter.getLiveStockList("", "", "1", "10");
    }

    private void initRecycle() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleLivestock.setLayoutManager(linearLayoutManager);
        LivestockAdapter livestockAdapter = new LivestockAdapter(getActivity(),R.layout.item_livestock,mLiveStockList);
        mLoadMoreWrapper = new LoadMoreWrapper<>(livestockAdapter);
        mRecycleLivestock.setAdapter(mLoadMoreWrapper);
        livestockAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), LivestockDetailActivity.class);
                intent.putExtra("id",String.valueOf(mLiveStockList.get(position).getDeviceNo()));
                ActivityUtils.startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void initBanner() {
        mBannerImgList = new ArrayList<>();
        mStrings.add("http://image.wufazhuce.com/Fgnxy66nFQ7wSQekWMCtsclejWHi");
        mStrings.add("http://image.wufazhuce.com/Fgnxy66nFQ7wSQekWMCtsclejWHi");
        mStrings.add("http://image.wufazhuce.com/Fgnxy66nFQ7wSQekWMCtsclejWHi");
        mStrings.add("http://image.wufazhuce.com/Fgnxy66nFQ7wSQekWMCtsclejWHi");
        mStrings.add("http://image.wufazhuce.com/Fgnxy66nFQ7wSQekWMCtsclejWHi");
        mBannerLivestock.setShowIndicator(false);
        mBannerLivestock.initBannerImageView(mStrings);
        mBannerLivestock.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                LogUtils.e(position);
                ActivityUtils.startActivity(RanchDetailActivity.class);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    /**
     * 显示正在加载view
     */
    @Override
    public void showLoading() {

    }

    /**
     * 关闭正在加载view
     */
    @Override
    public void hideLoading() {

    }

    /**
     * 显示提示
     *
     * @param msg
     */
    @Override
    public void showToast(String msg) {
        ToastUtils.showShort(msg);
    }

    /**
     * 显示请求错误提示
     *
     * @param e
     */
    @Override
    public void showErr(Throwable e) {
        LogUtils.e(e.getMessage());
    }

    @Override
    public void showLiveStock(LivestockListBean liveStockListBean) {
        LogUtils.e(liveStockListBean.toString());
        for (RanchBean ranchBean : liveStockListBean.getRanchList()) {
            mBannerImgList.add("http//:192.168.50.215:8080"+ranchBean.getImgUrl());
        }
        mBannerLivestock.setShowIndicator(false);
        mBannerLivestock.initBannerImageView(mBannerImgList);
        mBannerLivestock.setOnBannerItemClickListener(new BannerLayout.OnBannerItemClickListener() {
            @Override
            public void onItemClick(int position) {
                LogUtils.e(position);
                ActivityUtils.startActivity(RanchDetailActivity.class);
            }
        });
        for (LivestockBean liveStockBean : liveStockListBean.getLivestockList()) {
            mLiveStockList.add(liveStockBean);
        }
        mLoadMoreWrapper.notifyDataSetChanged();
        mLoadService.showSuccess();
    }
}
