package com.jinkun_innovation.pastureland.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.bean.ImgUrlBean;
import com.jinkun_innovation.pastureland.bean.LoginSuccess;
import com.jinkun_innovation.pastureland.common.Constants;
import com.jinkun_innovation.pastureland.utilcode.util.FileUtils;
import com.jinkun_innovation.pastureland.utilcode.util.LogUtils;
import com.jinkun_innovation.pastureland.utilcode.util.TimeUtils;
import com.jinkun_innovation.pastureland.utilcode.util.ToastUtils;
import com.jinkun_innovation.pastureland.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static okhttp3.MultipartBody.ALTERNATIVE;

/**
 * Created by Guan on 2018/3/16.
 */

public class PublishClaimActivity extends AppCompatActivity {

    private static final String TAG1 = PublishClaimActivity.class.getSimpleName();

    String mLogin_success;
    LoginSuccess mLoginSuccess;
    String mUsername;

    private String mType1;
    private String mVariety1;
    private String mWeight1;
    private String mAge1;

    int type;
    int variety;
    int weight;
    int age;


    private String mDeviceNo;


    private File photoFile;
    private Uri imageUri;//原图保存地址
    private static final int REQUEST_CAPTURE = 2;  //拍照
    private ImageView mIvTakePhoto;
    private String mImgUrl;
    private String mIsbn;


    private void cropImage(final String imgUrl) {
        String rootDir = "/Pastureland/crop";
        File file = new File(Environment.getExternalStorageDirectory(), rootDir);

        if (FileUtils.createOrExistsDir(file)) {
            LogUtils.e(file.getAbsolutePath());
            Luban.with(this)

                    .load(FileUtils.getFileByPath(imgUrl))                                   // 传人要压缩的图片列表
                    .ignoreBy(100)                                  // 忽略不压缩图片的大小
                    .setTargetDir(file.getAbsolutePath())
                    .setCompressListener(new OnCompressListener() { //设置回调
                        @Override
                        public void onStart() {
                            // TODO 压缩开始前调用，可以在方法内启动 loading UI
                            LogUtils.e("onStart");
//                            mPbLoading.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onSuccess(File file) {
                            // TODO 压缩成功后调用，返回压缩后的图片文件
//                            mPhotoFile = file;
                            LogUtils.e("onSuccess");
                            LogUtils.e(file.getAbsolutePath());

                            mLogin_success = PrefUtils.getString(getApplicationContext(), "login_success", null);
                            Gson gson = new Gson();
                            mLoginSuccess = gson.fromJson(mLogin_success, LoginSuccess.class);
                            mUsername = PrefUtils.getString(getApplicationContext(), "username", null);

                            Log.d(TAG1, "token=" + mLoginSuccess.getToken());
                            Log.d(TAG1, "username=" + mUsername);
                            Log.d(TAG1, "uploadFile=" + file.getAbsolutePath());

                            final SweetAlertDialog pDialog = new SweetAlertDialog(PublishClaimActivity.this,
                                    SweetAlertDialog.PROGRESS_TYPE);
                            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            pDialog.setTitleText("图片上传中...");
                            pDialog.setCancelable(true);
                            pDialog.show();


                            OkGo.<String>post(Constants.HEADIMGURL)
                                    .tag(this)
                                    .isMultipart(true)
                                    .params("token", mLoginSuccess.getToken())
                                    .params("username", mUsername)
                                    .params("uploadFile", file)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {


                                            String s = response.body().toString();
                                            Log.d(TAG1, s);
                                            Gson gson = new Gson();
                                            ImgUrlBean imgUrlBean = gson.fromJson(s, ImgUrlBean.class);
                                            mImgUrl = imgUrlBean.getImgUrl();
                                            int j = mImgUrl.indexOf("j");
                                            mImgUrl = mImgUrl.substring(j - 1, mImgUrl.length());
                                            Log.d(TAG1, mImgUrl);

                                            pDialog.cancel();


                                        }

                                        @Override
                                        public void onError(Response<String> response) {
                                            super.onError(response);

                                            pDialog.cancel();

                                            new SweetAlertDialog(PublishClaimActivity.this,
                                                    SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("抱歉...")
                                                    .setContentText("网络不稳定,上传图片失败,请重新拍摄")
                                                    .show();


                                        }
                                    });


//                            Glide.with(UpLoadActivity.this).load(file).into(mImgUpload);
//                            FileUtils.deleteFile(imgUrl);
//                            mPbLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {

                            // TODO 当压缩过程出现问题时调用
                            LogUtils.e(e.getMessage());
                            ToastUtils.showShort("压缩出现问题，请重新拍摄");

//                            AppManager.getAppManager().finishActivity();
//                            mPbLoading.setVisibility(View.GONE);
                        }
                    }).launch();
        }


    }

    private void openCamera() {

        String rootDir = "/Pastureland/photo";
        FileUtils.createOrExistsDir(new File(Environment.getExternalStorageDirectory(), rootDir));
        File file = new File(Environment.getExternalStorageDirectory(), rootDir);
        photoFile = new File(file, TimeUtils.getNowString() + ".jpg");

        Log.d(TAG1, photoFile.getAbsolutePath());
        FileUtils.createOrExistsFile(photoFile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this,
                    "com.jinkun_innovation.pastureland.fileProvider",
                    photoFile);//通过FileProvider创建一个content类型的Uri
        } else {
            imageUri = Uri.fromFile(photoFile);
        }

        mLogin_success = PrefUtils.getString(this, "login_success", null);
        Gson gson = new Gson();
        mLoginSuccess = gson.fromJson(mLogin_success, LoginSuccess.class);
        mUsername = PrefUtils.getString(this, "username", null);


        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CAPTURE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {

                case REQUEST_CAPTURE:

                    cropImage(photoFile.getAbsolutePath());

                    mIvTakePhoto.setImageURI(imageUri);

                    PrefUtils.setString(getApplicationContext(), "img_route",
                            photoFile.getAbsolutePath());

                    break;

            }


        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_publish_claim);

        Intent intent = getIntent();
        mIsbn = intent.getStringExtra("isbn");


        TextView etDeviceNo = (TextView) findViewById(R.id.etDeviceNo);
        etDeviceNo.setText(mIsbn);
        mDeviceNo = etDeviceNo.getText().toString();

        mLogin_success = PrefUtils.getString(this, "login_success", null);
        Gson gson = new Gson();
        mLoginSuccess = gson.fromJson(mLogin_success, LoginSuccess.class);
        mUsername = PrefUtils.getString(this, "username", null);


        //设备是否绑定， 没绑定弹出dialog提示，点击确定和取消 finish
        OkGo.<String>get(Constants.ISDEVICEBINDED)
                .tag(this)
                .params("token", mLoginSuccess.getToken())
                .params("username", mUsername)
                .params("ranchID", mLoginSuccess.getRanchID())
                .params("deviceNO", mIsbn)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String s = response.body().toString();
                        Log.d(TAG1, s);
                        if (s.contains("false")) {
                            //未绑定
                            ToastUtils.showShort("未登记牲畜不能发布认领");
                            finish();

                        } else if (s.contains("true")) {

                            //登记牲畜，查询发布情况
                            OkGo.<String>get(Constants.SELECT_LIVE_STOCK)
                                    .tag(this)
                                    .params("token", mLoginSuccess.getToken())
                                    .params("username", mUsername)
                                    .params("deviceNO", mIsbn)
                                    .params("ranchID", mLoginSuccess.getRanchID())
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {

                                            String s1 = response.body().toString();
                                            Log.d(TAG1,s1);


                                        }
                                    });


                        }

                    }
                });

        mIvTakePhoto = (ImageView) findViewById(R.id.ivTakePhoto);
        mIvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openCamera();

            }
        });


        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setResult(RESULT_OK);
                finish();

            }
        });


        Spinner spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {

                String[] type = getResources().getStringArray(R.array.type);
//                Log.d(TAG1, "种类" + type[pos]);
                mType1 = type[pos];

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback

            }

        });
        Spinner spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {


                String[] variety = getResources().getStringArray(R.array.variety);
//                Log.d(TAG1, "品种：" + variety[pos]);
                mVariety1 = variety[pos];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback

            }

        });

        Spinner spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {


                String[] mWeight = getResources().getStringArray(R.array.weight);
//                Log.d(TAG1, "重量：" + mWeight[pos]);
                mWeight1 = mWeight[pos];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback

            }

        });

        Spinner spinner4 = (Spinner) findViewById(R.id.spinner4);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {


                String[] mAge = getResources().getStringArray(R.array.age);
//                Log.d(TAG1, "年龄：" + mAge[pos]);
                mAge1 = mAge[pos];


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Another interface callback

            }

        });


        Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!TextUtils.isEmpty(mImgUrl)) {
                    if (!TextUtils.isEmpty(mDeviceNo)) {


                        if (mAge1.contains("2")) {
                            age = 2;
                        } else if (mAge1.contains("5")) {
                            age = 5;
                        } else if (mAge1.contains("8")) {
                            age = 8;
                        }

                        if (mWeight1.contains("100")) {
                            weight = 100;
                        } else if (mWeight1.contains("200")) {

                            weight = 200;
                        } else if (mWeight1.contains("300")) {

                            weight = 300;
                        } else if (mWeight1.contains("400")) {

                            weight = 400;
                        } else if (mWeight1.contains("500")) {

                            weight = 500;
                        }


                        if (mVariety1.equals("乌珠穆沁黑头羊")) {

                            variety = 100;
                        } else if (mVariety1.equals("山羊")) {

                            variety = 101;
                        } else if (mVariety1.equals("西门塔尔牛")) {

                            variety = 201;
                        } else if (mVariety1.equals("蒙古马")) {

                            variety = 301;
                        } else if (mVariety1.equals("草原黑毛猪")) {

                            variety = 401;
                        }


                        if (mType1.equals("羊")) {

                            type = 1;
                        } else if (mType1.equals("牛")) {

                            type = 2;
                        } else if (mType1.equals("马")) {

                            type = 3;
                        } else if (mType1.equals("猪")) {

                            type = 4;
                        } else if (mType1.equals("鸡")) {

                            type = 5;
                        } else if (mType1.equals("鹿")) {

                            type = 6;
                        }


                        Log.d(TAG1, "mImgUrl1==" + mImgUrl);
//
                        OkGo.<String>post(Constants.RELEASE)
                                .tag(this)
                                .params("token", mLoginSuccess.getToken())
                                .params("username", mUsername)
                                .params("deviceNO", mIsbn)
                                .params("ranchID", mLoginSuccess.getRanchID())
                                .params("livestockType", type)
                                .params("variety", variety)
                                .params("weight", weight)
                                .params("age", age)
                                .params("imgUrl", mImgUrl)
                                .execute(new StringCallback() {
                                    @Override
                                    public void onSuccess(Response<String> response) {

                                        Log.d(TAG1, "mImgUrl2==" + mImgUrl);

                                        final String s = response.body().toString();
                                        if (s.contains("发布牲畜到认领表成功")) {


                                            //发布认领成功
                                            Toast.makeText(getApplicationContext(), "发布牲畜到认领表成功",
                                                    Toast.LENGTH_SHORT).show();

                                            setResult(RESULT_OK);
                                            finish();


                                        } else if (s.contains("已经发布过了")) {


                                            new SweetAlertDialog(PublishClaimActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("已经发布,是否重新发布?")
                                                    .setContentText("按确定重新发布!")
                                                    .setCancelText("否")
                                                    .setConfirmText("确定")
                                                    .showCancelButton(true)
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.cancel();


                                                        }
                                                    })
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {

                                                            sweetAlertDialog.cancel();
                                                            //重新发布
                                                            OkGo.<String>post(Constants.IS_CLAIMED)
                                                                    .tag(this)
                                                                    .params("token", mLoginSuccess.getToken())
                                                                    .params("username", mUsername)
                                                                    .params("deviceNO", mIsbn)
                                                                    .params("ranchID", mLoginSuccess.getRanchID())
                                                                    .params("livestockType", type)
                                                                    .params("variety", variety)
                                                                    .params("weight", weight)
                                                                    .params("age", age)
                                                                    .params("imgUrl", mImgUrl)
                                                                    .execute(new StringCallback() {
                                                                        @Override
                                                                        public void onSuccess(Response<String> response) {

                                                                            String s1 = response.body().toString();
                                                                            Log.d(TAG1, "s1=" + s1);
                                                                            ToastUtils.showShort("重新发布成功");
                                                                            setResult(RESULT_OK);
                                                                            finish();

                                                                        }
                                                                    });

                                                        }
                                                    })
                                                    .show();


                                        } else if (s.contains("牲畜信息为空或者没有这个品种,发布不成功")) {


                                            new SweetAlertDialog(PublishClaimActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                    .setTitleText("未登记牲畜,是否直接发布认领?")
                                                    .setContentText("按确定直接发布认领")
                                                    .setCancelText("否")
                                                    .setConfirmText("确定")
                                                    .showCancelButton(true)
                                                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sDialog) {
                                                            sDialog.cancel();
                                                        }
                                                    })
                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                        @Override
                                                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                                                            sweetAlertDialog.cancel();


                                                            OkGo.<String>post(Constants.RELEASE)
                                                                    .tag(this)
                                                                    .params("token", mLoginSuccess.getToken())
                                                                    .params("username", mUsername)
                                                                    .params("deviceNO", mIsbn)
                                                                    .params("ranchID", mLoginSuccess.getRanchID())
                                                                    .params("livestockType", type)
                                                                    .params("variety", variety)
                                                                    .params("weight", weight)
                                                                    .params("age", age)
                                                                    .params("imgUrl", mImgUrl)
                                                                    .execute(new StringCallback() {
                                                                        @Override
                                                                        public void onSuccess(Response<String> response) {

                                                                            String s1 = response.body().toString();
                                                                            Log.d(TAG1, "s1=" + s1);
                                                                            ToastUtils.showShort("发布成功");
                                                                            setResult(RESULT_OK);
                                                                            finish();

                                                                        }
                                                                    });

                                                        }
                                                    })
                                                    .show();


                                        }

                                    }
                                });


                    } else {

                        Toast.makeText(getApplicationContext(), "设备号不能为空",
                                Toast.LENGTH_SHORT).show();


                    }
                } else {

                    Toast.makeText(getApplicationContext(), "图片上传失败，请重新拍照",
                            Toast.LENGTH_SHORT).show();
                }


            }
        });


    }


    private void multeUpload(String url, File file) {

        RequestBody multipartBody = new MultipartBody.Builder()
                .setType(ALTERNATIVE)//一样的效果
                .addFormDataPart("token", mLoginSuccess.getToken())
                .addFormDataPart("username", mUsername)
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))
                .build();
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url)
//                .cacheControl(new CacheControl.Builder().noCache().noStore().build())
                .addHeader("User-Agent", "android")
                .header("Content-Type", "text/html; charset=utf-8;")
                .header("Cache-Control", "public, max-age=" + 0)
                .post(multipartBody)//传参数、文件或者混合，改一下就行请求体就行
                .build();

        client.newBuilder().connectTimeout(30, TimeUnit.SECONDS).readTimeout(30, TimeUnit.SECONDS);
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtils.d("onFailure>>" + e.toString());
            }

            @Override
            public void onResponse(Call call, okhttp3.Response response) throws IOException {

                LogUtils.i("xxx", "请求返回结果1>>>" + response.body().toString() + ">>>" + response.toString());
                if (response.isSuccessful()) {

                    LogUtils.i("xxx", "请求返回结果2>>>" + response.body().string());

                }
            }
        });

    }
}
