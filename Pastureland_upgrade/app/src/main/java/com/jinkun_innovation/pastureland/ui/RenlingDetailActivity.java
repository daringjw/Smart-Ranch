package com.jinkun_innovation.pastureland.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.bean.LiveStock;
import com.jinkun_innovation.pastureland.bean.LoginSuccess;
import com.jinkun_innovation.pastureland.common.Constants;
import com.jinkun_innovation.pastureland.utilcode.constant.TimeConstants;
import com.jinkun_innovation.pastureland.utilcode.util.TimeUtils;
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

    TextView tvVariety, tvDevcieNO, tvDetail, tvAge, tvLifeTime, tvMuchangName, tvPublishTime;

    private MapView mMapView = null;
    private BitmapDescriptor mCurrentMarker;
    BaiduMap map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_renling_detail);
        ButterKnife.bind(this);

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.bmapView);

        map = mMapView.getMap();

        tvVariety = (TextView) findViewById(R.id.tvVariety);
        tvDevcieNO = (TextView) findViewById(R.id.tvDeviceNo);
        tvDetail = (TextView) findViewById(R.id.tvDetail);

        tvAge = (TextView) findViewById(R.id.tvAge);
        tvLifeTime = (TextView) findViewById(R.id.tvLifeTime);
        tvMuchangName = (TextView) findViewById(R.id.tvMuchangName);
        tvPublishTime = (TextView) findViewById(R.id.tvPublishTime);


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
                        if (TextUtils.isEmpty(msg)) {
                            ToastUtils.showShort("抱歉，服务器的数据为空");
                        }
                        {

                            if (msg.equals("获取牲畜详情成功")) {

                                LiveStock.LivestockBean lives = liveStock.getLivestock();

                                String deviceNo = lives.getDeviceNo();
                                tvDevcieNO.setText("设备号：" + deviceNo);

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

                                } else if (variety.equals("701")) {
                                    //骆驼
                                    tvVariety.setText("品种名称：骆驼");
                                }

                                String livestockDetails = lives.livestockDetails;
                                PrefUtils.setString(getApplicationContext(), "introduce", livestockDetails);
                                tvDetail.setText(livestockDetails);

                                //homeImgUrl
                                String homeImgUrl = lives.homeImgUrl;
                                if (!TextUtils.isEmpty(homeImgUrl)) {
                                    homeImgUrl = Constants.BASE_URL + homeImgUrl;
                                    Uri uri1 = Uri.parse(homeImgUrl);
                                    mSdvYang.setImageURI(uri1);
                                }

                                String createTime = lives.getCreateTime();

                                long timeSpanByNow = TimeUtils.getTimeSpanByNow(createTime, TimeConstants.DAY);
                                int age = (int) timeSpanByNow / 30;
                                if (age == 1) {
                                    age = 2;
                                }
                                tvAge.setText("年龄：" + age + "个月");
                                tvLifeTime.setText("一般寿命：" + lives.getLifeTime() + "个月");
                                tvMuchangName.setText("牧场：" + lives.getName());
                                tvPublishTime.setText("发布时间：" + lives.getUpdateTime());


                                String lantitudeBaidu = lives.getLantitudeBaidu();
                                String longtitudeBaidu = lives.getLongtitudeBaidu();

                                BDLocation bdLocation = new BDLocation();
                                bdLocation.setLongitude(Double.parseDouble(longtitudeBaidu));
                                bdLocation.setLatitude(Double.parseDouble(lantitudeBaidu));

                                // 开启定位图层
                                map.setMyLocationEnabled(true);

                                // 构造定位数据
                                MyLocationData locData = new MyLocationData.Builder()
                                        .accuracy(bdLocation.getRadius())
                                        // 此处设置开发者获取到的方向信息，顺时针0-360
                                        //.direction(100)
                                        .latitude(bdLocation.getLatitude())
                                        .longitude(bdLocation.getLongitude()).build();

                                // 设置定位数据
                                map.setMyLocationData(locData);

                                // 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
                                mCurrentMarker = BitmapDescriptorFactory
                                        .fromResource(R.mipmap.icon_location_3);
                                MyLocationConfiguration config = new MyLocationConfiguration(
                                        MyLocationConfiguration.LocationMode.FOLLOWING,
                                        true, mCurrentMarker);

                                map.setMyLocationConfiguration(config);

                                String imgUrl = lives.getImgUrl();
                                imgUrl = Constants.BASE_URL + imgUrl;
                                SimpleDraweeView sdvAsk = (SimpleDraweeView) findViewById(R.id.sdvAsk);
                                sdvAsk.setImageURI(Uri.parse(imgUrl));

                                /*String livestock_img_url = lives.livestock_img_url;
                                if (!TextUtils.isEmpty(livestock_img_url)) {
                                    livestock_img_url = Constants.BASE_URL + livestock_img_url;
                                    Log.d(TAG1, "livestockImgUrl=" + livestock_img_url);
                                    sdvAsk.setImageURI(Uri.parse(livestock_img_url));

                                }*/

                                /*String video_url = lives.video_url;
                                if (!TextUtils.isEmpty(video_url)) {
                                    video_url = Constants.BASE_URL + video_url;
                                    Log.d(TAG1, "video_url=" + video_url);
                                    VideoView videoView = (VideoView) findViewById(R.id.videoView);

                                    videoView.setMediaController(new MediaController(getApplicationContext()));

                                    videoView.setVideoURI(Uri.parse(video_url));
                                    videoView.start();

                                }*/


                            } else {


                            }


                        }


                    }
                });


        /*OkGo.<String>get(Constants.DianziDangan)
                .tag(this)
                .params("deviceNo",getDeviceNo)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String result = response.body().toString();


                    }
                });*/

        WebView wvElectronicDangan = (WebView) findViewById(R.id.wvElectronicDangan);
        WebSettings settings = wvElectronicDangan.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        wvElectronicDangan.loadUrl(Constants.DianziDangan + "?deviceNo=" + getDeviceNo
                + "&ranchID=" + mLoginSuccess.getRanchID());

        OkGo.<String>get(Constants.DianziDangan)
                .tag(this)
                .params("deviceNo", getDeviceNo)
                .params("ranchID", mLoginSuccess.getRanchID())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {


                    }
                });


    }


    @OnClick(R.id.ivBack)
    public void onViewClicked() {

        finish();

    }


}
