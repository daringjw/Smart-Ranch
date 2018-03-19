package com.jinkun_innovation.pastureland.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.common.Constants;
import com.jinkun_innovation.pastureland.utilcode.SpUtil;
import com.jinkun_innovation.pastureland.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by Guan on 2018/3/19.
 */

public class LoginActivity1 extends AppCompatActivity {

    private static final String TAG1 = LoginActivity1.class.getSimpleName();

    @BindView(R.id.tieAccount)
    TextInputEditText mTieAccount;
    @BindView(R.id.tiePwd)
    TextInputEditText mTiePwd;
    @BindView(R.id.tvForgetPwd)
    TextView mTvForgetPwd;
    @BindView(R.id.btnLogin)
    Button mBtnLogin;


    private SweetAlertDialog mPDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login1);
        ButterKnife.bind(this);


    }


    @OnClick({R.id.tvForgetPwd, R.id.btnLogin})
    public void onViewClicked(View view) {
        switch (view.getId()) {


            case R.id.tvForgetPwd:

                startActivity(new Intent(getApplicationContext(), FindPwdActivity.class));

                break;
            case R.id.btnLogin:

                String account = mTieAccount.getText().toString().trim();
                String pwd = mTiePwd.getText().toString();

                if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd)) {

                    // if (PhoneFormatCheckUtils.isChinaPhoneLegal(account)) {

                    //大陆号码，可以登录
                    mPDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
                    mPDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
                    mPDialog.setTitleText("正在登录...");
                    mPDialog.setCancelable(false);
                    mPDialog.show();

                    OkGo.<String>post(Constants.LOGIN)
                            .tag(this)
                            .params("username", account)
                            .params("password", pwd)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {

                                    String result = response.body().toString();
                                    Log.d(TAG1, result);
                                    if (result.contains("error")) {
                                        Toast.makeText(getApplicationContext(),
                                                "账号或者密码错误",
                                                Toast.LENGTH_SHORT).show();
                                        mPDialog.cancel();
                                    } else {
                                        //登陆成功
                                        PrefUtils.setString(getApplicationContext(), "login_success", result);
                                        mPDialog.cancel();
                                        SpUtil.saveLoginState(true);
                                        Toast.makeText(getApplicationContext(), "登录成功",
                                                Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                                        finish();


                                    }


                                }
                            });


                    /*} else {

                        //非法电话号码
                        Toast.makeText(getApplicationContext(),
                                "请输入正确的电话号码",
                                Toast.LENGTH_SHORT).show();

                    }*/

                } else {

                    Toast.makeText(getApplicationContext(),
                            "账号和密码不能为空",
                            Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }


}
