package com.jinkun_innovation.pastureland.ui;

import android.os.Bundle;
import android.os.CountDownTimer;

import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.base.BaseActivity;
import com.jinkun_innovation.pastureland.utilcode.AppManager;
import com.jinkun_innovation.pastureland.utilcode.SpUtil;
import com.jinkun_innovation.pastureland.utilcode.util.ActivityUtils;

/**
 * Created by yangxing on 2018/1/21.
 */

public class GuideActivity extends BaseActivity {
    @Override
    protected void initToolBar() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_guide;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new CountDownTimer(1000, 3000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                if (SpUtil.getLoginState()) {
                    ActivityUtils.startActivity(MainActivity.class);
                } else {
                    ActivityUtils.startActivity(LoginActivity.class);
                }
                AppManager.getAppManager().finishActivity();
            }
        }.start();
    }
}
