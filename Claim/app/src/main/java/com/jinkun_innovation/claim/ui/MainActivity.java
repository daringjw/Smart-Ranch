package com.jinkun_innovation.claim.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.jinkun_innovation.claim.R;
import com.jinkun_innovation.claim.bean.tabEntity.TabEntity;
import com.jinkun_innovation.claim.categroy_model.CategroyFragment;
import com.jinkun_innovation.claim.fragment_claim.ClaimFragment;
import com.jinkun_innovation.claim.fragment_livestock.LiveStockFragment;
import com.jinkun_innovation.claim.fragment_mine.MineFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_ctl_bottom_tab)
    CommonTabLayout mMainCtlBottomTab;
    @BindView(R.id.main_fl_change)
    FrameLayout mMainFlChange;


    private String[] mTitles = {"首页", "关注", "通知", "我的"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] mIconUnselectIds = {
            R.drawable.ic_tab_strip_icon_feed, R.drawable.ic_tab_strip_icon_follow,
            R.drawable.ic_tab_strip_icon_category, R.drawable.ic_tab_strip_icon_profile};
    private int[] mIconSelectIds = {
            R.drawable.ic_tab_strip_icon_feed_selected, R.drawable.ic_tab_strip_icon_follow_selected,
            R.drawable.ic_tab_strip_icon_category_selected, R.drawable.ic_tab_strip_icon_profile_selected};
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initBottomTab();
    }

    private void initBottomTab() {

        for (int i = 0; i < mTitles.length; i++) {
            mTabEntities.add(new TabEntity(mTitles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }
        mFragments.add(new LiveStockFragment());
        mFragments.add(new CategroyFragment());
        mFragments.add(new ClaimFragment());
        mFragments.add(new MineFragment());

        mMainCtlBottomTab.setTabData(mTabEntities, this, R.id.main_fl_change, mFragments);
        mMainCtlBottomTab.setCurrentTab(0);

    }
}
