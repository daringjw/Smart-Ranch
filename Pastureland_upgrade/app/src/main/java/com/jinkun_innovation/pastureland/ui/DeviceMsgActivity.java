package com.jinkun_innovation.pastureland.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.bean.DeviceMsg;
import com.jinkun_innovation.pastureland.bean.LoginSuccess;
import com.jinkun_innovation.pastureland.common.Constants;
import com.jinkun_innovation.pastureland.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

/**
 * Created by Guan on 2018/3/21.
 */

public class DeviceMsgActivity extends Activity {

    private static final String TAG1 = DeviceMsgActivity.class.getSimpleName();

    String mLogin_success;
    LoginSuccess mLoginSuccess;
    String mUsername;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_devicemsg);

        mLogin_success = PrefUtils.getString(this, "login_success", null);
        Gson gson = new Gson();
        mLoginSuccess = gson.fromJson(mLogin_success, LoginSuccess.class);
        mUsername = PrefUtils.getString(this, "username", null);

        ImageView ivBack = (ImageView) findViewById(R.id.ivBack);
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


        OkGo.<String>post(Constants.DEVICEMSG)
                .tag(this)
                .params("token", mLoginSuccess.getToken())
                .params("username", mUsername)
                .params("ranchID", mLoginSuccess.getRanchID())
                .params("current", 0)
                .params("pagesize", 3)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String s = response.body().toString();
                        Log.d(TAG1, s);

                        Gson gson1 = new Gson();
                        DeviceMsg deviceMsg = gson1.fromJson(s, DeviceMsg.class);
                        List<DeviceMsg.BatteryListBean> batteryList = deviceMsg.getBatteryList();
                        List<DeviceMsg.LivestockClaimListBean> livestockClaimList
                                = deviceMsg.getLivestockClaimList();


                    }
                });


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);
        //创建并设置Adapter
        mAdapter = new MyAdapter(getDummyDatas());
        mRecyclerView.setAdapter(mAdapter);


    }

    private String[] getDummyDatas() {

        String[] arr = {"北京", "上海", "广州", "深圳"};

        return arr;

    }

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    MyAdapter mAdapter;

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        public String[] datas = null;

        public MyAdapter(String[] datas) {
            this.datas = datas;
        }

        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device_msg, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;
        }

        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

//            viewHolder.mTextView.setText(datas[position]);

        }

        //获取数据的数量
        @Override
        public int getItemCount() {
            return 20;
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {

            //            public TextView mTextView;
            TextView tvTime1;
            TextView tvLowPower;
            TextView tvTime2;
            TextView tvQuPaizhao;
            ImageView ivQupaizhao;


            public ViewHolder(View view) {
                super(view);

//                mTextView = (TextView) view.findViewById(R.id.text);
                tvTime1 = view.findViewById(R.id.tvTime1);
                tvLowPower = view.findViewById(R.id.tvLowPower);
                tvTime2 = view.findViewById(R.id.tvTime2);
                tvQuPaizhao = view.findViewById(R.id.tvQuPaizhao);
                ivQupaizhao = view.findViewById(R.id.ivQupaizhao);






            }


        }
    }


}
