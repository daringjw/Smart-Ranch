package com.jinkun_innovation.pastureland.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.utils.PrefUtils;

/**
 * Created by Guan on 2018/4/1.
 */

public class MuchangDetailActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_muchang_detail);

        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });

        TextView tvDetail = (TextView) findViewById(R.id.tvDetail);

        String introduce = PrefUtils.getString(getApplicationContext(), "introduce", null);
        if (!TextUtils.isEmpty(introduce)) {

            tvDetail.setText(introduce);

        }


    }


}
