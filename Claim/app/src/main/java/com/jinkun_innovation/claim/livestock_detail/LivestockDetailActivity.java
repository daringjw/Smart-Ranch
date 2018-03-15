package com.jinkun_innovation.claim.livestock_detail;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.jinkun_innovation.claim.R;
import com.jinkun_innovation.claim.base.BaseActivity;
import com.jinkun_innovation.claim.bean.LiveStockDetailBean;
import com.jinkun_innovation.claim.utilcode.util.LogUtils;
import com.jinkun_innovation.claim.utilcode.util.TimeUtils;
import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;
import com.youth.banner.Banner;

import java.text.SimpleDateFormat;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yangxing on 2018/3/6.
 */

public class LivestockDetailActivity extends BaseActivity implements LivestockDetailView {
    @BindView(R.id.banner_livestock_detail)
    Banner mBannerLivestockDetail;
    @BindView(R.id.tv_livestock_variety)
    TextView mTvLivestockVariety;
    @BindView(R.id.tv_livestock_id)
    TextView mTvLivestockId;
    @BindView(R.id.tv_livestock_age)
    TextView mTvLivestockAge;
    @BindView(R.id.tv_livestock_life_time)
    TextView mTvLivestockLifeTime;
    @BindView(R.id.tv_livestock_name)
    TextView mTvLivestockName;
    @BindView(R.id.tv_livestock_claim_time)
    TextView mTvLivestockClaimTime;
    @BindView(R.id.tv_livestock_price)
    TextView mTvLivestockPrice;
    @BindView(R.id.tv_livestock_time)
    TextView mTvLivestockTime;
    @BindView(R.id.tv_livestock_des)
    TextView mTvLivestockDes;

    private LoadService mLoadService;
    private LiveStockDetailPresenter mLiveStockDetailPresenter;

    @Override
    protected void initToolBar() {
        setIsShowBack(true);
        setTitle("牲畜详情");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_livestock_detail;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initLoadSir();
        loadData();
        initListener();
    }

    private void initListener() {

    }

    private void initLoadSir() {
        mLoadService = LoadSir.getDefault().register(this, new Callback.OnReloadListener() {
            @Override
            public void onReload(View v) {

            }
        });

    }

    private void loadData() {
        mLiveStockDetailPresenter = new LiveStockDetailPresenter();
        mLiveStockDetailPresenter.attachView(this);
        LogUtils.e(getIntent().getStringExtra("id"));
        mLiveStockDetailPresenter.getLiveStock(getIntent().getStringExtra("id"));
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
        LogUtils.e(e.getMessage());

    }

    @Override
    public void showDetail(LiveStockDetailBean liveStockDetailBean) {
        long birthdayTime = TimeUtils.string2Millis(liveStockDetailBean.getLivestock().getBirthTime(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CANADA));
        mTvLivestockVariety.setText(String.format(Locale.CHINA, "品类名称: %s", liveStockDetailBean.getLivestock().getLivestockName()));
        mTvLivestockId.setText(String.format(Locale.CHINA, "ID: %s", liveStockDetailBean.getLivestock().getLivestockId()));
        mTvLivestockAge.setText(String.format(Locale.CHINA, "年龄: %s月", TimeUtils.getFitTimeSpanMonyh(birthdayTime, TimeUtils.getNowMills(), 1)));
        mTvLivestockLifeTime.setText(String.format(Locale.CHINA, "寿命: %s月", liveStockDetailBean.getLivestock().getLifeTime()));
        mTvLivestockName.setText(String.format(Locale.CHINA, "牧场: %s", liveStockDetailBean.getLivestock().getName()));
        mTvLivestockClaimTime.setText(String.format(Locale.CHINA, "发布时间: %s", liveStockDetailBean.getLivestock().getClaimTime()));
        mTvLivestockDes.setText(liveStockDetailBean.getLivestock().getLivestockDetails());
        mTvLivestockTime.setText(TimeUtils.getNowString(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())));

        mLoadService.showSuccess();

    }
}
