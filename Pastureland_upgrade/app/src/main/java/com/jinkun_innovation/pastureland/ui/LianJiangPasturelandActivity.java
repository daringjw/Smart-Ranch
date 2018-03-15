package com.jinkun_innovation.pastureland.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.jinkun_innovation.pastureland.R;

/**
 * Created by Guan on 2018/3/15.
 */

public class LianJiangPasturelandActivity extends AppCompatActivity {

    private SliderLayout mSliderShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lianjiangpastureland);

        mSliderShow = (SliderLayout) findViewById(R.id.slider);
        TextSliderView textSliderView = new TextSliderView(this);
        textSliderView
                .description("示例图片1")
                .image("http://pic2.sc.chinaz.com/files/pic/pic9/201803/bpic6100.jpg");

        TextSliderView textSliderView1 = new TextSliderView(this);
        textSliderView1
                .description("牧场2")
                .image("http://pic1.sc.chinaz.com/files/pic/pic9/201803/bpic5936.jpg");

        TextSliderView textSliderView2 = new TextSliderView(this);
        textSliderView2
                .description("示例图片3")
                .image("http://pics.sc.chinaz.com/files/pic/pic9/201802/zzpic10394.jpg");


        mSliderShow.addSlider(textSliderView);
        mSliderShow.addSlider(textSliderView1);
        mSliderShow.addSlider(textSliderView2);




    }

    @Override
    protected void onStop() {

        mSliderShow.stopAutoCycle();
        super.onStop();

    }


}
