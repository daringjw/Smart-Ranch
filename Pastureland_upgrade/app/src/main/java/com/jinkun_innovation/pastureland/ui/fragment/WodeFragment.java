package com.jinkun_innovation.pastureland.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.ui.GeRenXinxiActivity;

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
    @BindView(R.id.tvUserName)
    TextView mTvUserName;
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


                startActivity(new Intent(getActivity(),GeRenXinxiActivity.class));

                break;
            case R.id.ivMuchang:


                break;
            case R.id.ivMuqun:


                break;
            case R.id.ivRenling:


                break;
            case R.id.ivShebeiXiaoXi:


                break;
        }

    }


}
