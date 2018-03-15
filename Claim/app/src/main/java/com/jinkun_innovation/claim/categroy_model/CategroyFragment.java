package com.jinkun_innovation.claim.categroy_model;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.jinkun_innovation.claim.R;
import com.jinkun_innovation.claim.base.BaseFragment;
import com.jinkun_innovation.claim.bean.tabEntity.TabEntity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by yangxing on 2018/3/3.
 */

public class CategroyFragment extends BaseFragment {
    @BindView(R.id.ctl_category_bottom_tab)
    CommonTabLayout mCtlCategoryBottomTab;
    @BindView(R.id.ctl_category_change)
    FrameLayout mCtlCategoryChange;
    Unbinder unbinder;


    private String[] mTitles = {"种类", "牧场"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] mIconUnselectIds = {
            R.drawable.ic_tab_strip_icon_feed, R.drawable.ic_tab_strip_icon_follow,
            R.drawable.ic_tab_strip_icon_category, R.drawable.ic_tab_strip_icon_profile};
    private int[] mIconSelectIds = {
            R.drawable.ic_tab_strip_icon_feed_selected, R.drawable.ic_tab_strip_icon_follow_selected,
            R.drawable.ic_tab_strip_icon_category_selected, R.drawable.ic_tab_strip_icon_profile_selected};
    private ArrayList<Fragment> mFragments = new ArrayList<>();


    @Override
    protected int getContentViewLayoutID() {
        return R.layout.fragment_categroy;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initBottomTab();
    }

    private void initBottomTab() {

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mFragments.add(new CategoryLiveStockFragment());
        mFragments.add(new CateGoryRanchFragment());

        mCtlCategoryBottomTab.setTabData(mTabEntities, getActivity(), R.id.ctl_category_change, mFragments);
        mCtlCategoryBottomTab.setCurrentTab(0);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
