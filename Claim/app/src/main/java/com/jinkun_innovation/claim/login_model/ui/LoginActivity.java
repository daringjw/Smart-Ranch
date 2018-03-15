package com.jinkun_innovation.claim.login_model.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.jinkun_innovation.claim.R;
import com.jinkun_innovation.claim.base.BaseActivity;
import com.jinkun_innovation.claim.bean.LoginBean;
import com.jinkun_innovation.claim.login_model.LoginPresenter;
import com.jinkun_innovation.claim.login_model.LoginView;
import com.jinkun_innovation.claim.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangxing on 2018/3/6.
 */

public class LoginActivity extends BaseActivity implements LoginView {
    @BindView(R.id.edt_login_username)
    EditText mEdtLoginUsername;
    @BindView(R.id.edt_login_secret)
    EditText mEdtLoginSecret;

    private LoginPresenter mLoginPresenter;

    @Override
    protected void initToolBar() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_login;
    }

    /**
     * 显示正在加载view
     */
    @Override
    public void showLoading() {

    }

    /**
     * 关闭正在加载view
     */
    @Override
    public void hideLoading() {

    }

    /**
     * 显示提示
     *
     * @param msg
     */
    @Override
    public void showToast(String msg) {

    }

    /**
     * 显示请求错误提示
     *
     * @param e
     */
    @Override
    public void showErr(Throwable e) {

    }

    @Override
    public void toLogin(LoginBean bean) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mLoginPresenter = new LoginPresenter();
        mLoginPresenter.attachView(this);
    }

    @OnClick({R.id.btn_forgot_secret, R.id.btn_login_submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_forgot_secret:
                break;
            case R.id.btn_login_submit:
                if (TextUtils.isEmpty(mEdtLoginUsername.getText().toString())) {
                    ToastUtils.showShort("用户名不能为空");
                    return;
                }
                if (TextUtils.isEmpty(mEdtLoginSecret.getText().toString())) {
                    ToastUtils.showShort("密码不能为空");
                    return;
                }
                mLoginPresenter.toLogin(mEdtLoginUsername.getText().toString(),mEdtLoginSecret.getText().toString());
                break;
        }
    }

    
}
