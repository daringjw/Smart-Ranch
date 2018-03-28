package com.jinkun_innovation.pastureland.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.bean.AdminInfo1;
import com.jinkun_innovation.pastureland.bean.LoginSuccess;
import com.jinkun_innovation.pastureland.common.Constants;
import com.jinkun_innovation.pastureland.utilcode.AppManager;
import com.jinkun_innovation.pastureland.utilcode.SpUtil;
import com.jinkun_innovation.pastureland.utilcode.util.ToastUtils;
import com.jinkun_innovation.pastureland.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.jinkun_innovation.pastureland.utilcode.AppManager.getAppManager;

/**
 * Created by Guan on 2018/3/16.
 */

public class GeRenXinxiActivity extends AppCompatActivity {

    private static final String TAG1 = GeRenXinxiActivity.class.getSimpleName();
    @BindView(R.id.ivBack)
    ImageView mIvBack;
    @BindView(R.id.ivTouxiang)
    ImageView mIvTouxiang;
    @BindView(R.id.tvAdminName)
    TextView mTvAdminName;
    @BindView(R.id.tvSex)
    TextView mTvSex;
    @BindView(R.id.tvPhone)
    TextView mTvPhone;
    @BindView(R.id.btnExit)
    Button mBtnExit;
    private SweetAlertDialog mPDialog;

    String mLogin_success;
    LoginSuccess mLoginSuccess;
    String mUsername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gerenxinxi);
        ButterKnife.bind(this);

        getAppManager().addActivity(this);

        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        mLogin_success = PrefUtils.getString(this, "login_success", null);
        Gson gson = new Gson();
        mLoginSuccess = gson.fromJson(mLogin_success, LoginSuccess.class);
        mUsername = PrefUtils.getString(this, "username", null);

        OkGo.<String>post(Constants.ADMINLIST)
                .tag(this)
                .params("token", mLoginSuccess.getToken())
                .params("username", mUsername)
                .params("ranchID", mLoginSuccess.getRanchID())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String s = response.body().toString();
                        Log.d(TAG1, s);

                        if (s.contains("获取个人信息异常")) {

                            ToastUtils.showShort("获取个人信息异常");

                        } else {
                            Gson gson1 = new Gson();
                            AdminInfo1 adminInfo = gson1.fromJson(s, AdminInfo1.class);
                            String sex = adminInfo.getAdminInfo().getSex();
                            if (sex.equals("1")) {
                                mTvSex.setText("性别：男");
                            } else {
                                mTvSex.setText("性别：女");
                            }
                            mTvAdminName.setText("姓名：" + adminInfo.getAdminInfo().getUsername());
                            mTvPhone.setText("电话号码：" + adminInfo.getAdminInfo().getUsername());
                        }


                    }
                });


        Button btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mPDialog = new SweetAlertDialog(GeRenXinxiActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                mPDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                mPDialog.setTitleText("正在退出...");
                mPDialog.setCancelable(false);
                mPDialog.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        mPDialog.cancel();
                        SpUtil.saveLoginState(false);
                        startActivity(new Intent(getApplicationContext(), LoginActivity1.class));
                        AppManager.getAppManager().finishAllActivity();

                    }
                }, 2000);


            }
        });


        ImageView ivTouxiang = (ImageView) findViewById(R.id.ivTouxiang);
        ivTouxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //点击头像


            }
        });


    }


}
