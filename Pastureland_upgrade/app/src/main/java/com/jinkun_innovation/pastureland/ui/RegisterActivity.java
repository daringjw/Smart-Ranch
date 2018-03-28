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

import cn.pedant.SweetAlert.SweetAlertDialog;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static com.jinkun_innovation.pastureland.R.id.ivTakePhoto;

/**
 * Created by Guan on 2018/3/20.
 */

public class RegisterActivity extends Activity {

    private static final String TAG1 = RegisterActivity.class.getSimpleName();
    private LoginSuccess mLoginSuccess;
    private String mUsername;
    private String mDeviceNO;
//    private String mImgUrl;


    private String mType1;
    private String mVariety1;
    private String mWeight1;
    private String mAge1;


    private File photoFile;
    private Uri imageUri;//原图保存地址
    private static final int REQUEST_CAPTURE = 2;  //拍照
    private ImageView mIvTakePhoto;

    SweetAlertDialog mDialog;


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
                            mDialog = new SweetAlertDialog(RegisterActivity.this,
                                    SweetAlertDialog.PROGRESS_TYPE);
                            mDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                            mDialog.setTitleText("图片正在上传...");
                            mDialog.setCancelable(false);
                            mDialog.show();


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

                            Log.d(TAG1, mLoginSuccess.getToken());
                            Log.d(TAG1, mUsername);
                            Log.d(TAG1, file.getAbsolutePath());


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


                                        }

                                        @Override
                                        public void onError(Response<String> response) {
                                            super.onError(response);

                                            ToastUtils.showShort("图片上传失败,请重新拍摄");


                                        }
                                    });

                            mDialog.cancel();

//                            Glide.with(UpLoadActivity.this).load(file).into(mImgUpload);
//                            FileUtils.deleteFile(imgUrl);
//                            mPbLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            // TODO 当压缩过程出现问题时调用
                            LogUtils.e(e.getMessage());

                            mDialog.cancel();
                            ToastUtils.showShort("压缩出现问题，请重新拍摄");


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

     /*   OkGo.<String>post(Constants.HEADIMGURL)
                .tag(this)
                .isMultipart(true)
                .params("token", mLoginSuccess.getToken())
                .params("username", mUsername)
                .params("uploadFile", photoFile)
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


                    }
                });*/

        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CAPTURE);

    }

    String mImgUrl;
    String mLogin_success;


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

        setContentView(R.layout.activity_register);
        mIvTakePhoto = (ImageView) findViewById(ivTakePhoto);

        mIvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openCamera();

            }
        });


        Intent intent = getIntent();
//        mImgUrl = intent.getStringExtra("imgUrl");
        mDeviceNO = intent.getStringExtra("scanMessage");

        TextView tvDeviceNo = (TextView) findViewById(R.id.tvDeviceNo);
        tvDeviceNo.setText(mDeviceNO);

        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View view) {

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


        String login_success = PrefUtils.getString(getApplicationContext(), "login_success", null);
        Gson gson = new Gson();
        mLoginSuccess = gson.fromJson(login_success, LoginSuccess.class);

        mUsername = PrefUtils.getString(this, "username", null);

        if (mLoginSuccess != null) {

            Log.d(TAG1, mLoginSuccess.getToken());
            Log.d(TAG1, mUsername);
            Log.d(TAG1, mDeviceNO);
            Log.d(TAG1, mLoginSuccess.getRanchID() + "");


        }


        Button btnConfirm = (Button) findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Log.d(TAG1, mType1);
                Log.d(TAG1, mVariety1);
                Log.d(TAG1, mWeight1);
                Log.d(TAG1, mAge1);

                int type = 1;
                if (mType1.equals("羊")) {
                    type = 1;

                } else if (mType1.equals("")) {

                }

             /*   String type2 = mType1.substring(0, 1);
                int type3 = Integer.parseInt(type2);
                String variety2 = mVariety1.substring(0, 3);
                int variety3 = Integer.parseInt(variety2);
                String weight2 = mWeight1.substring(0, 2);
                int weight3 = Integer.parseInt(weight2);
                String age2 = mAge1.substring(0, 1);
                int age3 = Integer.parseInt(age2);
                Log.d(TAG1, type3 + "");
                Log.d(TAG1, "" + variety3);
                Log.d(TAG1, weight3 + "");
                Log.d(TAG1, "" + age3);*/

                if (photoFile != null) {
                    OkGo.<String>post(Constants.SAVELIVESTOCK)
                            .tag(this)
                            .params("token", mLoginSuccess.getToken())
                            .params("username", mUsername)
                            .params("deviceNO", mDeviceNO)
                            .params("ranchID", mLoginSuccess.getRanchID())
                            .params("livestockType", 1)
                            .params("variety", 100)
                            .params("weight", 20)
                            .params("age", 2)
                            .params("imgUrl", mImgUrl)

                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {


                                    String result = response.body().toString();
                                    Log.d(TAG1, result);
                                    if (result.contains("error")) {
                                        //失败
                                        Toast.makeText(getApplicationContext(),
                                                "上传图片失败，登记失败，请重试",
                                                Toast.LENGTH_SHORT)
                                                .show();

                                    } else {
                                        //成功
                                        Toast.makeText(getApplicationContext(),
                                                "登记成功",
                                                Toast.LENGTH_SHORT)
                                                .show();

                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        finish();


                                    }

                                }
                            });
                } else {


                    Toast.makeText(getApplicationContext(), "请先拍照", Toast.LENGTH_SHORT).show();

                }


            }
        });

    }


}
