package com.jinkun_innovation.claim.login_model.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.jinkun_innovation.claim.R;
import com.jinkun_innovation.claim.base.BaseActivity;
import com.jinkun_innovation.claim.bean.RespondBean;
import com.jinkun_innovation.claim.login_model.RegisterPresenter;
import com.jinkun_innovation.claim.login_model.RegisterView;
import com.jinkun_innovation.claim.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangxing on 2018/3/6.
 */

public class RegisterActivity extends BaseActivity implements RegisterView {
    @BindView(R.id.edt_register_username)
    EditText mEdtRegisterUsername;
    @BindView(R.id.edt_register_secret)
    EditText mEdtRegisterSecret;
    @BindView(R.id.edt_register_secret_confirm)
    EditText mEdtRegisterSecretConfirm;
    @BindView(R.id.edt_register_verification)
    EditText mEdtRegisterVerification;

    private String cellPhone;
    private String verifyCode;

    @Override
    protected void initToolBar() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_set_secret;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        cellPhone = getIntent().getStringExtra("cellphone");
        verifyCode = getIntent().getStringExtra("verify_code");
        mEdtRegisterVerification.setText(verifyCode);

    }

    @OnClick(R.id.btn_register_submit)
    public void onClick() {
        String userName = mEdtRegisterUsername.getText().toString();
        String secret = mEdtRegisterSecret.getText().toString();
        String secretConfirm = mEdtRegisterSecretConfirm.getText().toString();
        if (TextUtils.isEmpty(userName)) {
            ToastUtils.showShort("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(secret)) {
            ToastUtils.showShort("密码不能为空");
            return;
        }
        if (secret.equals(secretConfirm)) {
            ToastUtils.showShort("两次密码不一致");
            return;
        }
        toSubmit(userName, secret);
    }

    private void toSubmit(String userName, String secret) {
        RegisterPresenter setSecretPresenter = new RegisterPresenter();
        setSecretPresenter.attachView(this);
        setSecretPresenter.register(cellPhone, verifyCode, userName, secret, "性别");

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
        ToastUtils.showShort(msg);
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
    public void success(RespondBean respondBean) {


    }
}
