package com.jinkun_innovation.pastureland.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.bean.LoginSuccess;
import com.jinkun_innovation.pastureland.bean.MuqunDetail;
import com.jinkun_innovation.pastureland.common.Constants;
import com.jinkun_innovation.pastureland.utilcode.util.ToastUtils;
import com.jinkun_innovation.pastureland.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jinkun_innovation.pastureland.R.id.tvDetail;

/**
 * Created by Guan on 2018/4/4.
 */

public class Muchang2Activity extends Activity {

    @BindView(R.id.ivBack)
    ImageView mIvBack;
    @BindView(R.id.ivEdit)
    ImageView mIvEdit;
    @BindView(R.id.sdvYang)
    SimpleDraweeView mSdvYang;
    @BindView(R.id.tvRancher)
    TextView mTvRancher;
    @BindView(R.id.tvPhone)
    TextView mTvPhone;
    @BindView(R.id.tvMianji)
    TextView mTvMianji;
    @BindView(R.id.tvRiverNearby)
    TextView mTvRiverNearby;
    @BindView(R.id.tvNum)
    TextView mTvNum;
    @BindView(R.id.tvVariety)
    TextView mTvVariety;

    @BindView(tvDetail)
    TextView mTvDetail;

    private LoginSuccess mLoginSuccess;
    private String mLogin_success;
    private String mUsername;


    WebView webView;

    public void webmap() {//地图定位


        webView = (WebView) findViewById(R.id.wvMap);

        webView.getSettings().setDatabaseEnabled(true);//开启数据库

        webView.setFocusable(true);//获取焦点

        webView.requestFocus();

        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();//设置数据库路径

        webView.getSettings().setCacheMode(webView.getSettings().LOAD_CACHE_ELSE_NETWORK);//本地缓存

        webView.getSettings().setBlockNetworkImage(false);//显示网络图像

        webView.getSettings().setLoadsImagesAutomatically(true);//显示网络图像

        webView.getSettings().setPluginState(WebSettings.PluginState.ON);//插件支持

        webView.getSettings().setSupportZoom(false);//设置是否支持变焦

        webView.getSettings().setJavaScriptEnabled(true);//支持JavaScriptEnabled

        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);//支持JavaScriptEnabled

        webView.getSettings().setGeolocationEnabled(true);//定位

        webView.getSettings().setGeolocationDatabasePath(dir);//数据库

        webView.getSettings().setDomStorageEnabled(true);//缓存 （ 远程web数据的本地化存储）

        WebViewClient myWebViewClient = new WebViewClient();//建立对象

        webView.setWebViewClient(myWebViewClient);//调用

        webView.loadUrl("https://map.baidu.com/");//百度地图地址

        webView.setWebChromeClient(new WebChromeClient() {

//重写WebChromeClient的onGeolocationPermissionsShowPrompt

            public void onGeolocationPermissionsShowPrompt(String origin,

                                                           GeolocationPermissions.Callback callback) {

                callback.invoke(origin, true, false);

                super.onGeolocationPermissionsShowPrompt(origin, callback);

            }

        });


    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_muchang2);
        ButterKnife.bind(this);

        webmap();

        mLogin_success = PrefUtils.getString(this, "login_success", null);
        Gson gson = new Gson();
        mLoginSuccess = gson.fromJson(mLogin_success, LoginSuccess.class);
        mUsername = PrefUtils.getString(this, "username", null);

        OkGo.<String>get(Constants.RANCH)
                .tag(this)
                .params("token", mLoginSuccess.getToken())
                .params("username", mUsername)
                .params("ranchID", mLoginSuccess.getRanchID())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String result = response.body().toString();
                        Gson gson1 = new Gson();
                        MuqunDetail muqunDetail = gson1.fromJson(result, MuqunDetail.class);
                        String msg = muqunDetail.getMsg();
                        if (msg.equals("获取牧场详情成功")) {

                            String imgUrl = muqunDetail.getRanch().getImgUrl();
                            imgUrl = Constants.BASE_URL + imgUrl;
                            mSdvYang.setImageURI(Uri.parse(imgUrl));

                            mTvRancher.setText("牧场：" + muqunDetail.getRanch().getName());
                            mTvPhone.setText("联系电话：" + muqunDetail.getRanch().getRancherAccount());

                            mTvRiverNearby.setText("附近河流：" + muqunDetail.getRanch().getIntroduceRiver());
                            mTvMianji.setText("面积：" + muqunDetail.getRanch().getAcreage() + "平方公里");

                            mTvNum.setText("数量：" + muqunDetail.getRanch().getIntroduceAnimalCount() + "头");
                            mTvVariety.setText("种类：" + muqunDetail.getRanch().getIntroduceAnimalType());

                            mTvDetail.setText(muqunDetail.getRanch().getIntroduce());

                        } else {

                            ToastUtils.showShort("获取牧场详情失败");

                        }


                    }
                });

    }


    @OnClick({R.id.ivBack, R.id.ivEdit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivBack:

                finish();

                break;
            case R.id.ivEdit:

                startActivity(new Intent(getApplicationContext(), EditMuchangActivity.class));

                break;

        }


    }
}
