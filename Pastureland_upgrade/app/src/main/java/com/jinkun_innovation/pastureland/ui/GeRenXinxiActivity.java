package com.jinkun_innovation.pastureland.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.utilcode.AppManager;

import static com.jinkun_innovation.pastureland.utilcode.AppManager.getAppManager;

/**
 * Created by Guan on 2018/3/16.
 */

public class GeRenXinxiActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_gerenxinxi);

        getAppManager().addActivity(this);

        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        Button btnExit = (Button) findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                AppManager.getAppManager().finishAllActivity();

            }
        });


    }


}
