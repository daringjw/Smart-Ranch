package com.jinkun_innovation.pastureland.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.bean.LoginSuccess;
import com.jinkun_innovation.pastureland.bean.MuqunSum;
import com.jinkun_innovation.pastureland.common.Constants;
import com.jinkun_innovation.pastureland.ui.YangListActivity;
import com.jinkun_innovation.pastureland.ui.activity.CamelListActivity;
import com.jinkun_innovation.pastureland.ui.activity.MaListActivity;
import com.jinkun_innovation.pastureland.ui.activity.NiuListActivity;
import com.jinkun_innovation.pastureland.ui.activity.PigListActivity;
import com.jinkun_innovation.pastureland.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Guan on 2018/3/15.
 */

public class MuqunFragment extends Fragment {


    private static final String TAG1 = MuqunFragment.class.getSimpleName();
    @BindView(R.id.ivYang)
    ImageView mIvYang;
    @BindView(R.id.ivNiu)
    ImageView mIvNiu;
    @BindView(R.id.ivMa)
    ImageView mIvMa;
    @BindView(R.id.ivPig)
    ImageView mIvPig;
    @BindView(R.id.ivCamel)
    ImageView ivCamel;

    @BindView(R.id.tvYangNo)
    TextView tvYangNo;

    Unbinder unbinder;

    String mLogin_success;
    LoginSuccess mLoginSuccess;
    String mUsername;

    private TextView mTvNiuNo;
    private TextView mTvMaNo;
    private TextView mTvDeerNo, tvCamelNo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = View.inflate(getActivity(), R.layout.fragment_muqun, null);
        unbinder = ButterKnife.bind(this, view);

        mTvNiuNo = view.findViewById(R.id.tvNiuNo);
        mTvMaNo = view.findViewById(R.id.tvMaNo);
        mTvDeerNo = view.findViewById(R.id.tvDeerNo);
        tvCamelNo = view.findViewById(R.id.tvCamelNo);


        mLogin_success = PrefUtils.getString(getActivity(), "login_success", null);
        Gson gson = new Gson();
        mLoginSuccess = gson.fromJson(mLogin_success, LoginSuccess.class);
        mUsername = PrefUtils.getString(getActivity(), "username", null);

        OkGo.<String>get(Constants.QUERYTYPEANDSUM)
                .tag(this)
                .params("token", mLoginSuccess.getToken())
                .params("username", mUsername)
                .params("ranchID", mLoginSuccess.getRanchID())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String s = response.body().toString();
                        Log.d(TAG1, s);
                        if (s.contains("牧场无牲畜")) {
                            tvYangNo.setText(0 + "头");

                        } else if (s.contains("获取牲畜类型和数量成功")) {

                            Gson gson1 = new Gson();
                            MuqunSum muqunSum = gson1.fromJson(s, MuqunSum.class);
                            MuqunSum.TypeMapBean typeMap = muqunSum.getTypeMap();

                            if (typeMap != null) {

                                tvYangNo.setText(typeMap.get_$1() + "只");
                                mTvNiuNo.setText(typeMap.get_$2() + "头");
                                mTvMaNo.setText(typeMap.get_$3() + "匹");
                                mTvDeerNo.setText(typeMap.get_$4() + "头");
                                tvCamelNo.setText(typeMap.get_$7() + "匹");

                            }


                        } else {


                        }


                    }
                });

        return view;


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.ivYang, R.id.ivNiu, R.id.ivMa, R.id.ivPig, R.id.ivCamel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ivYang:

                startActivity(new Intent(getActivity(), YangListActivity.class));

                break;

            case R.id.ivNiu:

                startActivity(new Intent(getActivity(), NiuListActivity.class));

                break;

            case R.id.ivMa:

                startActivity(new Intent(getActivity(), MaListActivity.class));

                break;

            case R.id.ivPig:

                startActivity(new Intent(getActivity(), PigListActivity.class));

                break;

            case R.id.ivCamel:

                startActivity(new Intent(getActivity(), CamelListActivity.class));

                break;


        }
    }

}
