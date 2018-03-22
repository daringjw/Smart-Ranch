package com.jinkun_innovation.pastureland.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.ui.DeviceMsgActivity;
import com.jinkun_innovation.pastureland.ui.GeRenXinxiActivity;
import com.jinkun_innovation.pastureland.ui.MuchangActivity;
import com.jinkun_innovation.pastureland.ui.MuqunActivity;
import com.jinkun_innovation.pastureland.ui.RenlingActivity;
import com.jinkun_innovation.pastureland.utilcode.util.AppUtils;
import com.jinkun_innovation.pastureland.utils.PrefUtils;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Guan on 2018/3/15.
 */

public class WodeFragment extends Fragment {


    @BindView(R.id.ivIcon)
    ImageView mIvIcon;
    @BindView(R.id.tvMyPhone)
    TextView myPhone;
    @BindView(R.id.rlGeRenXinxi)
    RelativeLayout mRlGeRenXinxi;
    @BindView(R.id.ivMuchang)
    ImageView mIvMuchang;
    @BindView(R.id.ivMuqun)
    ImageView mIvMuqun;
    @BindView(R.id.ivRenling)
    ImageView mIvRenling;
    @BindView(R.id.ivShebeiXiaoXi)
    ImageView mIvShebeiXiaoXi;
    Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = View.inflate(getActivity(), R.layout.fragment_wode, null);

        unbinder = ButterKnife.bind(this, view);

        String username = PrefUtils.getString(getActivity(), "username", null);
        if (!TextUtils.isEmpty(username)) {

            myPhone.setText(username);

        }


        /**
         * 升级
         */
        Button btnVersion = (Button) view.findViewById(R.id.btnVersion);
        boolean appDebug = AppUtils.isAppDebug();
        if (appDebug) {
            btnVersion.setText("测试版本 1.0." + AppUtils.getAppVersionCode());
        } else {
            btnVersion.setText("正式版本 1.0." + AppUtils.getAppVersionCode());
        }

        btnVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Beta.checkUpgrade();
            }
        });

        return view;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.rlGeRenXinxi, R.id.ivMuchang, R.id.ivMuqun, R.id.ivRenling, R.id.ivShebeiXiaoXi})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlGeRenXinxi:


                startActivity(new Intent(getActivity(), GeRenXinxiActivity.class));

                break;
            case R.id.ivMuchang:

                startActivity(new Intent(getActivity(), MuchangActivity.class));

                break;
            case R.id.ivMuqun:

                startActivity(new Intent(getActivity(),MuqunActivity.class));

                break;

            case R.id.ivRenling:

                startActivity(new Intent(getActivity(),RenlingActivity.class));

                break;
            case R.id.ivShebeiXiaoXi:

                startActivity(new Intent(getActivity(),DeviceMsgActivity.class));

                break;
        }

    }


}
