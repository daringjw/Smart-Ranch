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
import com.google.gson.Gson;
import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.bean.LiveStock;
import com.jinkun_innovation.pastureland.bean.LoginSuccess;
import com.jinkun_innovation.pastureland.common.Constants;
import com.jinkun_innovation.pastureland.utilcode.util.ToastUtils;
import com.jinkun_innovation.pastureland.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Guan on 2018/3/24.
 */

public class RenlingDetailActivity extends Activity {

    private static final String TAG1 = RenlingDetailActivity.class.getSimpleName();

    @BindView(R.id.ivBack)
    ImageView mIvBack;
    @BindView(R.id.sdvYang)
    SimpleDraweeView mSdvYang;


    String mLogin_success;
    LoginSuccess mLoginSuccess;
    String mUsername;

    TextView tvVariety, tvDevcieNO, tvDetail;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_renling_detail);
        ButterKnife.bind(this);


        tvVariety = (TextView) findViewById(R.id.tvVariety);
        tvDevcieNO = (TextView) findViewById(R.id.tvDeviceNo);
        tvDetail = (TextView) findViewById(R.id.tvDetail);

        Intent intent = getIntent();
        String getDeviceNo = intent.getStringExtra("getDeviceNo");

        mLogin_success = PrefUtils.getString(this, "login_success", null);
        Gson gson = new Gson();
        mLoginSuccess = gson.fromJson(mLogin_success, LoginSuccess.class);
        mUsername = PrefUtils.getString(this, "username", null);

        OkGo.<String>get(Constants.LIVESTOCK)
                .tag(this)
                .params("token", mLoginSuccess.getToken())
                .params("username", mUsername)
                .params("ranchID", mLoginSuccess.getRanchID())
                .params("deviceNO", getDeviceNo)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String s = response.body().toString();
                        Log.d(TAG1, s);
                        Gson gson1 = new Gson();
                        LiveStock liveStock = gson1.fromJson(s, LiveStock.class);
                        String msg = liveStock.getMsg();
                        if (msg.equals("获取牲畜详情成功")) {

                            LiveStock.LivestockBean lives = liveStock.getLivestock();

                            String imgUrl = lives.getImgUrl();
                            imgUrl=Constants.BASE_URL+imgUrl;
                            Uri uri=Uri.parse(imgUrl);
                            mSdvYang.setImageURI(uri);

                            String variety = lives.getVariety();
                            if (variety.equals("100")) {

                                //乌珠木漆黑头羊
                                tvVariety.setText("品种名称：乌珠木漆黑头羊");


                            } else if (variety.equals("101")) {

                                //山羊
                                tvVariety.setText("品种名称：山羊");

                            } else if (variety.equals("201")) {

                                //西门塔尔牛
                                tvVariety.setText("品种名称：西门塔尔牛");

                            } else if (variety.equals("301")) {
                                //蒙古马
                                tvVariety.setText("品种名称：蒙古马");


                            } else if (variety.equals("401")) {
                                //草原黑毛猪
                                tvVariety.setText("品种名称：草原黑毛猪");

                            }

                            String introduce = lives.getIntroduce();
                            tvDetail.setText(introduce);


                        } else {

                            ToastUtils.showShort("获取牲畜详情失败，请检查网络");
                        }


                    }
                });


    }


    @OnClick(R.id.ivBack)
    public void onViewClicked() {

        finish();

    }


}
