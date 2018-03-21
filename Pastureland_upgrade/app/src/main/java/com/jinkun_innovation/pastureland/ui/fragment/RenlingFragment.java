package com.jinkun_innovation.pastureland.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.zxing.client.android.CaptureActivity2;
import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.bean.LoginSuccess;
import com.jinkun_innovation.pastureland.bean.RenLing;
import com.jinkun_innovation.pastureland.common.Constants;
import com.jinkun_innovation.pastureland.ui.PublishClaimActivity;
import com.jinkun_innovation.pastureland.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.header.FunGameHitBlockHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.SpinnerStyle;
import com.scwang.smartrefresh.layout.footer.BallPulseFooter;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

/**
 * Created by Guan on 2018/3/15.
 */

public class RenlingFragment extends Fragment {

    private static final int SCAN_REQUEST_CODE = 100;
    private static final String TAG1 = RenlingFragment.class.getSimpleName();

    private List<RenLing.LivestockListBean> mLivestockList;
    final int pageSize = 10; // 固定大小
    int startIndex = -1;  // 起始页（从0开始）
    private RefreshLayout mRefreshLayout;
    private int mTempStartIndex;


    private void startScanActivity() {
        Intent intent = new Intent(getActivity(), CaptureActivity2.class);
        intent.putExtra(CaptureActivity2.USE_DEFUALT_ISBN_ACTIVITY, true);
        intent.putExtra("inputUnable", 0);
        intent.putExtra("lightUnable", 1);
        intent.putExtra("albumUnable", 1);
        intent.putExtra("cancleUnable", 1);
        startActivityForResult(intent, SCAN_REQUEST_CODE);

    }

    int count = 1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = View.inflate(getActivity(), R.layout.fragment_claim, null);


        ImageView ivAdd = view.findViewById(R.id.ivAdd);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                startActivity(new Intent(getActivity(), PublishClaimActivity.class));

            }
        });


        mRefreshLayout = view.findViewById(R.id.refreshLayout);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败

            }
        });


        mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {


                Log.d(TAG1, "count=" + count);

                OkGo.<String>post(Constants.LIVE_STOCK_CLAIM_LIST)
                        .tag(this)
                        .params("token", mLoginSuccess.getToken())
                        .params("username", mUsername)
                        .params("ranchID", mLoginSuccess.getRanchID())
//                .params("isClaimed",)
                        .params("current", 10 * count)
                        .params("pagesize", 10)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                count++;

                                String s = response.body().toString();
                                Log.d(TAG1, s);
                                Gson gson1 = new Gson();
                                RenLing renLing = gson1.fromJson(s, RenLing.class);
                                List<RenLing.LivestockListBean> livestockList1 = renLing.getLivestockList();
                                if (livestockList1.size()==0){

                                    Toast.makeText(getActivity(),
                                            "没有更多数据了",
                                            Toast.LENGTH_SHORT).show();
                                    refreshLayout.finishLoadMore();

                                    return;
                                }else {

                                    for (int i = 0; i < livestockList1.size(); i++) {
                                        mLivestockList.add(livestockList1.get(i));
                                    }
                                    //创建并设置Adapter
                                    mAdapter = new MyAdapter(mLivestockList);
                                    mRecyclerView.setAdapter(mAdapter);
                                    refreshLayout.finishLoadMore();
                                }




                            }
                        });


            }
        });

        FunGameHitBlockHeader funGameHitBlockHeader = new FunGameHitBlockHeader(getActivity());
        //设置 Header 为 Material样式
        mRefreshLayout.setRefreshHeader(funGameHitBlockHeader);
        //设置 Footer 为 球脉冲
        mRefreshLayout.setRefreshFooter(new BallPulseFooter(getActivity())
                .setSpinnerStyle(SpinnerStyle.Scale));


        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);

        initData();


        return view;

    }

    String mLogin_success;
    LoginSuccess mLoginSuccess;
    String mUsername;

    private void initData() {

        mLogin_success = PrefUtils.getString(getActivity(), "login_success", null);
        Gson gson = new Gson();
        mLoginSuccess = gson.fromJson(mLogin_success, LoginSuccess.class);
        mUsername = PrefUtils.getString(getActivity(), "username", null);

        OkGo.<String>post(Constants.LIVE_STOCK_CLAIM_LIST)
                .tag(this)
                .params("token", mLoginSuccess.getToken())
                .params("username", mUsername)
                .params("ranchID", mLoginSuccess.getRanchID())
//                .params("isClaimed",)
                .params("current", 0)
                .params("pagesize", 15)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        startIndex = 0;

                        String s = response.body().toString();
                        Log.d(TAG1, s);
                        Gson gson1 = new Gson();
                        RenLing renLing = gson1.fromJson(s, RenLing.class);
                        mLivestockList = renLing.getLivestockList();

                        //创建并设置Adapter
                        mAdapter = new MyAdapter(mLivestockList);
                        mRecyclerView.setAdapter(mAdapter);


                    }
                });


    }

    private String[] getDummyDatas() {

        String[] arr = {"北京", "上海", "广州", "深圳"};

        return arr;

    }

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;

    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view, String[] data);


    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.mOnItemClickListener = listener;


    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener {
        public List<RenLing.LivestockListBean> datas = null;

        public MyAdapter(List<RenLing.LivestockListBean> datas) {

            this.datas = datas;
        }

        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_claim, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);

            //将创建的View注册点击事件
            view.setOnClickListener(this);

            return vh;
        }


        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

//            viewHolder.mTextView.setText(datas[position]);
            //将数据保存在itemView的Tag中，以便点击时进行获取
//            viewHolder.itemView.setTag(datas[position]);

            String imgUrl = datas.get(position).getImgUrl();
            if (!TextUtils.isEmpty(imgUrl)) {
                Uri uri = Uri.parse(datas.get(position).getImgUrl());
                viewHolder.ivGhoat.setImageURI(uri);
            } else {
                //网络无图片

            }


            String livestockName = datas.get(position).getLivestockName();
            if (livestockName.equals("100")) {
                viewHolder.tvAnimalName.setText("乌珠穆泣黑头羊");
            } else {
                viewHolder.tvAnimalName.setText(livestockName);
            }
            viewHolder.tvId.setText("设备号：" + datas.get(position).getDeviceNo());
            viewHolder.tvAnimalAge.setText("出生日期：" + datas.get(position).getBirthTime());
            viewHolder.tvMuChang.setText("特点：" + datas.get(position).getCharacteristics());
            viewHolder.tvClaimTime.setText("认领时间：" + datas.get(position).getClaimTime());
            String isClaimed = datas.get(position).getIsClaimed();
            if (isClaimed.equals("0")) {
                viewHolder.tvClaimOrNot.setText("未认领");
            } else if (isClaimed.equals("1")) {
                viewHolder.tvClaimOrNot.setText("已认领");
            }

            viewHolder.tvPrice.setText("价格：" + datas.get(position).getPrice());


        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取数据
//                mOnItemClickListener.onItemClick(v,(String[])v.getTag());

            }
        }

        //获取数据的数量
        @Override
        public int getItemCount() {
            return datas.size();
        }


        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {

            SimpleDraweeView ivGhoat;
            TextView tvAnimalName;
            TextView tvId;
            TextView tvAnimalAge;
            TextView tvMuChang;
            TextView tvClaimTime;
            TextView tvClaimOrNot;
            TextView tvPrice;


            public ViewHolder(View view) {
                super(view);
//                mTextView = view.findViewById(R.id.tvClaim);
                ivGhoat = view.findViewById(R.id.ivGhoat);
                tvAnimalName = view.findViewById(R.id.tvAnimalName);
                tvId = view.findViewById(R.id.tvId);
                tvAnimalAge = view.findViewById(R.id.tvAnimalAge);
                tvMuChang = view.findViewById(R.id.tvMuChang);
                tvClaimTime = view.findViewById(R.id.tvClaimTime);
                tvClaimOrNot = view.findViewById(R.id.tvClaimOrNot);
                tvPrice = view.findViewById(R.id.tvPrice);


            }

        }

    }


}
