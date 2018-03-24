package com.jinkun_innovation.pastureland.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.common.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Guan on 2018/3/24.
 */

public class RenlingDetailActivity extends Activity {

    @BindView(R.id.ivBack)
    ImageView mIvBack;
    @BindView(R.id.sdvYang)
    SimpleDraweeView mSdvYang;
    @BindView(R.id.tvVariety)
    TextView mTvVariety;
    @BindView(R.id.tvDevcieNO)
    TextView mTvDevcieNO;
    @BindView(R.id.tvCharacter)
    TextView mTvCharacter;
    @BindView(R.id.tvPhone)
    TextView mTvPhone;
    @BindView(R.id.tvPublishTime)
    TextView mTvPublishTime;
    @BindView(R.id.tvPrice)
    TextView mTvPrice;
    @BindView(R.id.tvState)
    TextView mTvState;
    @BindView(R.id.tvLifeTime)
    TextView tvLifeTime;
    @BindView(R.id.tvBirthDay)
    TextView tvBirthDay;
    @BindView(R.id.tvClaimTime)
    TextView tvClaimTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_renling_detail);
        ButterKnife.bind(this);

        Intent intent = getIntent();

        String getImgUrl = intent.getStringExtra("getImgUrl");
        getImgUrl = Constants.BASE_URL + getImgUrl;
        Uri uri = Uri.parse(getImgUrl);
        mSdvYang.setImageURI(uri);

        String getLivestockName = intent.getStringExtra("getLivestockName");
        mTvVariety.setText("品种：" + getLivestockName);

        String getCharacteristics = intent.getStringExtra("getCharacteristics");
        mTvCharacter.setText("特点：" + getCharacteristics);

        String getCellphone = intent.getStringExtra("getCellphone");
        mTvPhone.setText("认领人手机：" + getCellphone);

        String getCreateTime = intent.getStringExtra("getCreateTime");
        mTvPublishTime.setText("发布时间：" + getCreateTime);

        String getPrice = intent.getStringExtra("getPrice");
        mTvPrice.setText("价格：￥" + getPrice);

        String getIsClaimed = intent.getStringExtra("getIsClaimed");
        if (getIsClaimed.equals("0")) {
            mTvState.setText("认领状态：未认领");
        } else {
            mTvState.setText("认领状态：已认领");
        }

        String getLifeTime = intent.getStringExtra("getLifeTime");
        tvLifeTime.setText("一般寿命：" + getLifeTime + "个月");

        String getBirthTime = intent.getStringExtra("getBirthTime");
        tvBirthDay.setText("生日：" + getBirthTime);

        String getClaimTime = intent.getStringExtra("getClaimTime");
        tvClaimTime.setText("认领时间：" + getClaimTime);


    }


    @OnClick(R.id.ivBack)
    public void onViewClicked() {

        finish();

    }
}
