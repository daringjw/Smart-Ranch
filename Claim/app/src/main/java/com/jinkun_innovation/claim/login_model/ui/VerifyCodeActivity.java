package com.jinkun_innovation.claim.login_model.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jinkun_innovation.claim.R;
import com.jinkun_innovation.claim.base.BaseActivity;
import com.jinkun_innovation.claim.bean.RespondBean;
import com.jinkun_innovation.claim.net.ApiCall;
import com.jinkun_innovation.claim.net.RetrofitManger;
import com.jinkun_innovation.claim.utilcode.util.ActivityUtils;
import com.jinkun_innovation.claim.utilcode.util.RegexUtils;
import com.jinkun_innovation.claim.utilcode.util.ToastUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yangxing on 2018/3/5.
 */

public class VerifyCodeActivity extends BaseActivity {


    @BindView(R.id.edt_verify_code_num)
    EditText mEdtVerifyCodeNum;
    @BindView(R.id.tv_verify_code_timer)
    TextView mTvVerifyCodeTimer;
    @BindView(R.id.edt_verify_code)
    EditText mEdtVerifyCode;
    @BindView(R.id.btn_verify_send_code)
    Button mBtnVerifySendCode;

    private CountDownTimer mCountDownTimer;

    @Override
    protected int getContentView() {
        return R.layout.activity_verify_code;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initTimer();
    }

    /**
     * 初始化倒计时器
     */
    private void initTimer() {
        mCountDownTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long l) {
                int time = ((int) l) / 1000;
                mTvVerifyCodeTimer.setText(String.format(Locale.CHINA, "%d%s", time, "s"));
            }

            @Override
            public void onFinish() {
                mBtnVerifySendCode.setText("重发");
                mBtnVerifySendCode.setVisibility(View.VISIBLE);
                mTvVerifyCodeTimer.setVisibility(View.GONE);

            }
        };
    }

    @Override
    protected void initToolBar() {

    }


    @OnClick({R.id.btn_verify_send_code, R.id.btn_verify_code_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_verify_send_code:
                calibratePhoneNumber(mEdtVerifyCodeNum.getText().toString());
                break;
            case R.id.btn_verify_code_next:
//                ActivityUtils.startActivity(LivestockDetailActivity.class);
                if (TextUtils.isEmpty(mEdtVerifyCode.getText().toString())) {
                    ToastUtils.showShort("验证码不能为空");
                    return;
                }
                Intent intent = new Intent();
                intent.setClass(VerifyCodeActivity.this, RegisterActivity.class);
                intent.putExtra("cellphone", mEdtVerifyCodeNum.getText().toString());
                intent.putExtra("verify_code", mEdtVerifyCode.getText().toString());
                ActivityUtils.startActivity(intent);
                break;
        }
    }

    private void calibratePhoneNumber(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            ToastUtils.showShort("手机号不能为空");
            return;
        }
        if (!RegexUtils.isMobileSimple(phoneNumber)) {
            ToastUtils.showShort("请输入正确手机号");
            return;
        }
        getVerifyCode(phoneNumber);
    }

    /**
     * 获取手机验证码
     */
    private void getVerifyCode(String phoneNumber) {

        mEdtVerifyCode.setVisibility(View.INVISIBLE);
        mTvVerifyCodeTimer.setVisibility(View.VISIBLE);
        mCountDownTimer.start();
        RetrofitManger.getInstance().createReq(ApiCall.class)
                .sendVerification(phoneNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<RespondBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(RespondBean bean) {

                    }
                });
    }


}
