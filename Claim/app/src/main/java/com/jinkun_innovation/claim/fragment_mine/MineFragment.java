package com.jinkun_innovation.claim.fragment_mine;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jinkun_innovation.claim.R;
import com.jinkun_innovation.claim.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by yangxing on 2018/3/3.
 */

public class MineFragment extends BaseFragment {
    @BindView(R.id.tv_mine_name)
    TextView mTvMineName;
    @BindView(R.id.tv_mine_phone)
    TextView mTvMinePhone;
    Unbinder unbinder;

    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_mine;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.cc_mine_order, R.id.cc_mine_address, R.id.cc_mine_claim, R.id.img_mine_edit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cc_mine_order:
                break;
            case R.id.cc_mine_address:
                break;
            case R.id.cc_mine_claim:
                break;
            case R.id.img_mine_edit:
                break;
        }
    }
}
