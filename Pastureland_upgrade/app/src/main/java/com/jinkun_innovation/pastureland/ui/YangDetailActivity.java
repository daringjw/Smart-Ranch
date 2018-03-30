package com.jinkun_innovation.pastureland.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.common.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Guan on 2018/3/26.
 */

public class YangDetailActivity extends Activity {

    private static final String TAG1 = YangDetailActivity.class.getSimpleName();
    @BindView(R.id.ivBack)
    ImageView mIvBack;
    @BindView(R.id.sdvYang)
    SimpleDraweeView mSdvYang;
    @BindView(R.id.tvVariety)
    TextView mTvVariety;
    @BindView(R.id.tvDevcieNO)
    TextView mTvDevcieNO;
    @BindView(R.id.tvWeight)
    TextView mTvWeight;
    @BindView(R.id.tvBindStatus)
    TextView mTvBindStatus;
    @BindView(R.id.tvCreateTime)
    TextView mTvCreateTime;
    @BindView(R.id.tvState)
    TextView mTvState;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_yang_detail);
        ButterKnife.bind(this);

//        intent.putExtra("getImgUrl", mLivestockVarietyList.get(position).getImgUrl());
//        intent.putExtra("getDeviceNo", mLivestockVarietyList.get(position).getDeviceNo());
//        intent.putExtra("getWeight", mLivestockVarietyList.get(position).getWeight());
//        intent.putExtra("getBindStatus", mLivestockVarietyList.get(position).getBindStatus());
//        intent.putExtra("getIsClaimed", mLivestockVarietyList.get(position).getIsClaimed());
//        intent.putExtra("getUpdateTime", mLivestockVarietyList.get(position).getUpdateTime());

        Intent intent = getIntent();
        String getImgUrl = intent.getStringExtra("getImgUrl");
        getImgUrl = Constants.BASE_URL + getImgUrl;
        Uri uri = Uri.parse(getImgUrl);
        mSdvYang.setImageURI(uri);

        String getVariety = intent.getStringExtra("getVariety");
        Log.d(TAG1,"getVariety="+getVariety);
        if (getVariety.equals("100")){
            mTvVariety.setText("品种：乌珠木漆黑羊");
        }else {

            mTvVariety.setText("品种：山羊");

        }


        String getDeviceNo = intent.getStringExtra("getDeviceNo");
        mTvDevcieNO.setText("设备号：" + getDeviceNo);
        String getWeight = intent.getStringExtra("getWeight");
        mTvWeight.setText("价格：" + getWeight + " 元");
        String getBindStatus = intent.getStringExtra("getBindStatus");
        if (getBindStatus.equals("1")) {
            mTvBindStatus.setText("绑定状态：已绑定");
        } else {
            mTvBindStatus.setText("绑定状态：未绑定");
        }
        String getIsClaimed = intent.getStringExtra("getIsClaimed");
        if (getIsClaimed.equals("0")) {
            mTvState.setText("认领状态：未认领");
        } else {
            mTvState.setText("认领状态：已认领");
        }
        String getUpdateTime = intent.getStringExtra("getUpdateTime");
        mTvCreateTime.setText("登记时间：" + getUpdateTime);


    }


    @OnClick(R.id.ivBack)
    public void onViewClicked() {

        finish();

    }


}
