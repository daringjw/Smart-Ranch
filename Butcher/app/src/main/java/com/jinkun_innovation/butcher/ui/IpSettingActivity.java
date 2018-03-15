package com.jinkun_innovation.butcher.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.jinkun_innovation.butcher.R;
import com.jinkun_innovation.butcher.base.BaseActivity;
import com.jinkun_innovation.butcher.net.RetrofitManger;
import com.jinkun_innovation.butcher.utilcode.AppManager;
import com.jinkun_innovation.butcher.utilcode.SpUtil;
import com.jinkun_innovation.butcher.utilcode.util.RegexUtils;
import com.jinkun_innovation.butcher.utilcode.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangxing on 2018/1/24.
 */

public class IpSettingActivity extends BaseActivity {
    @BindView(R.id.edt_set_ip)
    EditText mEdtSetIp;
    @BindView(R.id.edt_set_port)
    EditText mEdtSetPort;
    @BindView(R.id.btn_set_confirm)
    Button mBtnSetConfirm;

    @Override
    protected void initToolBar() {

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_setting_ip;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mEdtSetIp.setText(SpUtil.getIP());
        mEdtSetPort.setText(SpUtil.getPort());
    }

    private void settingIp() {
        if (!RegexUtils.isIP(mEdtSetIp.getText().toString())) {
            ToastUtils.showShort("请输入正确ip");
            return;
        }
        if (TextUtils.isEmpty(mEdtSetPort.getText().toString())) {
            ToastUtils.showShort("端口不能为空");
            return;
        }
        SpUtil.saveIP(mEdtSetIp.getText().toString());
        SpUtil.savePort(mEdtSetPort.getText().toString());
        RetrofitManger.mRetrofitManger=null;
        AppManager.getAppManager().finishActivity();
    }

    @OnClick(R.id.btn_set_confirm)
    public void onClick() {
        settingIp();
    }
}
