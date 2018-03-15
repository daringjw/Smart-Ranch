package com.jinkun_innovation.claim.fragment_livestock;

import android.content.Context;

import com.jinkun_innovation.claim.R;
import com.jinkun_innovation.claim.bean.LivestockBean;
import com.jinkun_innovation.claim.utilcode.util.LogUtils;
import com.jinkun_innovation.claim.utilcode.util.TimeUtils;
import com.jinkun_innovation.claim.weight.recyclerview.CommonAdapter;
import com.jinkun_innovation.claim.weight.recyclerview.base.ViewHolder;

import java.util.List;
import java.util.Locale;

/**
 * Created by yangxing on 2018/3/5.
 */

public class LivestockAdapter extends CommonAdapter<LivestockBean> {
    public LivestockAdapter(Context context, int layoutId, List<LivestockBean> livestockList) {
        super(context, layoutId, livestockList);
    }

    @Override
    protected void convert(ViewHolder holder, LivestockBean livestockBean, int position) {
        holder.setImg(R.id.img_livestock_image, livestockBean.getLivestockImgUrl());
        holder.setText(R.id.tv_livestock_variety, livestockBean.getLivestockName());
        holder.setText(R.id.tv_livestock_device_num, String.format(Locale.CHINA, "ID：%s", livestockBean.getDeviceNo()));
        holder.setText(R.id.tv_livestock_create_time, String.format(Locale.CANADA,"发布时间: %s",TimeUtils.millis2String(livestockBean.getCreateTime())));
        holder.setText(R.id.tv_livestock_period, String.format(Locale.CHINA, "生长周期: %s/%s月",
                TimeUtils.getFitTimeSpanMonyh(livestockBean.getBirthTime(), TimeUtils.getNowMills(), 1),
                livestockBean.getLifeTime()));
        LogUtils.e(TimeUtils.getFitTimeSpan(livestockBean.getBirthTime(), TimeUtils.getNowMills(), 1));
        holder.setText(R.id.tv_livestock_address, String.format(Locale.CHINA, "位置：%s", "假装我是地址"));
        holder.setText(R.id.tv_livestock_characteristics, String.format(Locale.CHINA, "位置：%s", livestockBean.getCharacteristics()));
        holder.setText(R.id.tv_livestock_price, String.format(Locale.CHINA, "¥%s/月", livestockBean.getPrice()));

    }
}
