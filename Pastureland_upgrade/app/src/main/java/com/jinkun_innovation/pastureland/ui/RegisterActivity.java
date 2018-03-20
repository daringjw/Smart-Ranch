package com.jinkun_innovation.pastureland.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.bean.LoginSuccess;
import com.jinkun_innovation.pastureland.common.Constants;
import com.jinkun_innovation.pastureland.utilcode.util.ImageUtils;
import com.jinkun_innovation.pastureland.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Guan on 2018/3/20.
 */

public class RegisterActivity extends Activity {

    private static final String TAG1 = RegisterActivity.class.getSimpleName();
    private LoginSuccess mLoginSuccess;
    private String mUsername;
    private String mDeviceNO;
    private String mImgUrl;


    private String mType1;
    private String mVariety1;
    private String mWeight1;
    private String mAge1;
    private SweetAlertDialog mPDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_register);
        ImageView ivTakePhoto = (ImageView) findViewById(R.id.ivTakePhoto);
        Intent intent = getIntent();
        mImgUrl = intent.getStringExtra("imgUrl");
        mDeviceNO = intent.getStringExtra("scanMessage");

        File file = new File(mImgUrl);
        Bitmap bitmap = ImageUtils.getBitmap(file);
        ivTakePhoto.setImageBitmap(bitmap);

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

                mPDialog = new SweetAlertDialog(RegisterActivity.this,
                        SweetAlertDialog.PROGRESS_TYPE);
                mPDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                mPDialog.setTitleText("上传中...");
                mPDialog.setCancelable(true);
                mPDialog.show();

                Log.d(TAG1, mType1);
                Log.d(TAG1, mVariety1);
                Log.d(TAG1, mWeight1);
                Log.d(TAG1, mAge1);

                String type2 = mType1.substring(0, 1);
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
                Log.d(TAG1, "" + age3);


                OkGo.<String>post(Constants.SAVELIVESTOCK)
                        .tag(this)
                        .params("token", mLoginSuccess.getToken())
                        .params("username", mUsername)
                        .params("deviceNO", mDeviceNO)
                        .params("ranchID", mLoginSuccess.getRanchID())
                        .params("livestockType", type3)
                        .params("variety", variety3)
                        .params("weight", weight3)
                        .params("age", age3)
                        .params("imgUrl", mImgUrl)

                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                mPDialog.cancel();

                                String result = response.body().toString();
                                Log.d(TAG1, result);
                                if (result.contains("error")) {
                                    //失败
                                    Toast.makeText(getApplicationContext(),
                                            "登记失败",
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


                /*new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        pDialog.cancel();
                        Toast.makeText(getApplicationContext(),"登记成功",Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                        finish();

                    }
                },1000);*/


            }
        });

    }


}
