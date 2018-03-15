package com.jinkun_innovation.claim.login_model.ui;

import android.os.Bundle;
import android.view.View;

import com.jinkun_innovation.claim.R;
import com.jinkun_innovation.claim.base.BaseActivity;
import com.jinkun_innovation.claim.ui.MainActivity;
import com.jinkun_innovation.claim.utilcode.util.ActivityUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangxing on 2018/3/6.
 */

public class BootLoginActivity extends BaseActivity {
    @Override
    protected void initToolBar() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_boot_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_boot_login, R.id.btn_boot_register})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_boot_login:
//                ActivityUtils.startActivity(LoginActivity.class);
                ActivityUtils.startActivity(MainActivity.class);
                break;
            case R.id.btn_boot_register:
                ActivityUtils.startActivity(VerifyCodeActivity.class);

                break;
        }
    }
}
