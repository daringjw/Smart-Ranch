package com.jinkun_innovation.claim.ranch_model;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jinkun_innovation.claim.R;
import com.jinkun_innovation.claim.base.BaseActivity;
import com.jinkun_innovation.claim.bean.RanchDetailBean;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.youth.banner.Banner;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yangxing on 2018/3/9.
 */

public class RanchDetailActivity extends BaseActivity implements RanchDetailView{
    @BindView(R.id.banner_ranch_detail)
    Banner mBannerRanchDetail;
    @BindView(R.id.tv_ranch_name)
    TextView mTvRanchName;
    @BindView(R.id.tv_ranch_rancher)
    TextView mTvRanchRancher;
    @BindView(R.id.tv_ranch_acreage)
    TextView mTvRanchAcreage;
    @BindView(R.id.tv_ranch_animal_count)
    TextView mTvRanchAnimalCount;
    @BindView(R.id.tv_ranch_river)
    TextView mTvRanchRiver;
    @BindView(R.id.tv_ranch_type)
    TextView mTvRanchType;
    @BindView(R.id.tv_ranch_close)
    TextView mTvRanchClose;
    @BindView(R.id.tv_ranch_cellphone)
    TextView mTvRanchCellphone;
    @BindView(R.id.tv_ranch_des)
    TextView mTvRanchDes;

    private RanchDetailPresenter mRanchDetailPresenter;
    private LoadService mLoadService;

    @Override
    protected void initToolBar() {
        setIsShowBack(true);
        setTitle("牧场详情");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_ranch;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initLoadSir();
        loadData();
    }

    private void loadData() {
        mRanchDetailPresenter = new RanchDetailPresenter();
        mRanchDetailPresenter.attachView(this);
        mRanchDetailPresenter.getLiveStock("1");
    }

    private void initLoadSir() {
        mLoadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {

            }
        });
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

    }

    /**
     * 显示请求错误提示
     *
     * @param e
     */
    @Override
    public void showErr(Throwable e) {

    }

    @Override
    public void showDetail(RanchDetailBean ranchBean) {
        mTvRanchName.setText(String.format(Locale.CHINA,"牧场名称: %s",ranchBean.getName()));
        mTvRanchRancher.setText(String.format(Locale.CHINA,"牧场主: %s",ranchBean.getRancherName()));
        mTvRanchAcreage.setText(String.format(Locale.CHINA,"牧场面积: %s",ranchBean.getAcreage()));
        mTvRanchAnimalCount.setText(String.format(Locale.CHINA,"牲畜面积: %s",ranchBean.getName()));
        mTvRanchRiver.setText(String.format(Locale.CHINA,"附近河流: %s",ranchBean.getIntroduceRiver()));
        mTvRanchType.setText(String.format(Locale.CHINA,"种类: %s",ranchBean.getIsFenceClose()));
        mTvRanchClose.setText(String.format(Locale.CHINA,"围栏封闭: %s",ranchBean.getName()));
        mTvRanchCellphone.setText(String.format(Locale.CHINA,"电话: %s",ranchBean.getRancherPhone()));
        mTvRanchDes.setText(String.format(Locale.CHINA,"%s",ranchBean.getIntroduce()));
        mLoadService.showSuccess();

    }
}
