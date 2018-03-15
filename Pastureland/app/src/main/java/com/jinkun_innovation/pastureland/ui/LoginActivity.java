package com.jinkun_innovation.pastureland.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.base.BaseActivity;
import com.jinkun_innovation.pastureland.bean.LoginBean;
import com.jinkun_innovation.pastureland.net.ApiCall;
import com.jinkun_innovation.pastureland.net.RetrofitManger;
import com.jinkun_innovation.pastureland.utilcode.AppManager;
import com.jinkun_innovation.pastureland.utilcode.MD5;
import com.jinkun_innovation.pastureland.utilcode.SpUtil;
import com.jinkun_innovation.pastureland.utilcode.util.ActivityUtils;
import com.jinkun_innovation.pastureland.utilcode.util.LogUtils;
import com.jinkun_innovation.pastureland.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yangxing on 2018/1/16.
 */

public class LoginActivity extends BaseActivity {

    private Subscription mSubscription;
    @BindView(R.id.edt_name)
    EditText mEdtName;
    @BindView(R.id.edt_pwd)
    EditText mEdtPwd;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.btn_setting_ip)
    Button mBtnSettingIp;

    @Override
    protected void initToolBar() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mBtnSettingIp.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ActivityUtils.startActivity(IpSettingActivity.class);
                return true;
            }
        });
    }

    @OnClick({R.id.btn_login, R.id.btn_setting_ip})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                if (TextUtils.isEmpty(mEdtName.getText().toString())) {
                    ToastUtils.showShort("用户名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(mEdtPwd.getText().toString())) {
                    ToastUtils.showShort("密码不能为空");
                    return;
                }
                toLogin();
                break;


        }

    }

    private void toLogin() {
        mSubscription = RetrofitManger.getInstance().createReq(ApiCall.class)
                .login(mEdtName.getText().toString(), MD5.md5Encode(mEdtName.getText().toString(), mEdtPwd.getText().toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<LoginBean>() {
                    @Override
                    public void onStart() {
                        showProgress("正在登录");
                    }

                    @Override
                    public void onCompleted() {
                        hiddenProgress();
                    }

                    @Override
                    public void onError(Throwable e) {
                        hiddenProgress();
                        LogUtils.e(e.getMessage());
                        ToastUtils.showShort(e.getMessage());
                    }

                    @Override
                    public void onNext(LoginBean loginBean) {
                        if (loginBean.getCode().equals("200")) {
                            SpUtil.saveLoginState(true);
                            SpUtil.saveAccount(mEdtName.getText().toString());
                            SpUtil.saveToken(loginBean.getData().getIdTooken());
                            SpUtil.saveUserId(loginBean.getData().getId());
                            SpUtil.saveCompanyId(String.valueOf(loginBean.getData().getCompanyId()));
                            ActivityUtils.startActivity(MainActivity.class);
                            AppManager.getAppManager().finishActivity();
                        } else {
                            ToastUtils.showShort(loginBean.getMsg());
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mSubscription!=null&&mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
    }
}
