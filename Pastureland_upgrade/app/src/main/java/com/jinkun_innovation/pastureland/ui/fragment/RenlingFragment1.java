package com.jinkun_innovation.pastureland.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
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

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.google.zxing.client.android.CaptureActivity2;
import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.bean.LoginSuccess;
import com.jinkun_innovation.pastureland.bean.RenLing;
import com.jinkun_innovation.pastureland.common.Constants;
import com.jinkun_innovation.pastureland.ui.PublishClaimActivity;
import com.jinkun_innovation.pastureland.ui.RenlingDetailActivity;
import com.jinkun_innovation.pastureland.utilcode.util.ToastUtils;
import com.jinkun_innovation.pastureland.utils.PrefUtils;
import com.jinkun_innovation.pastureland.utils.StrLengthUtil;
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
 * Created by Guan on 2018/3/23.
 */

public class RenlingFragment1 extends Fragment {

    private static final String TAG1 = RenlingFragment1.class.getSimpleName();

    String mLogin_success;
    LoginSuccess mLoginSuccess;
    String mUsername;

    private static final int SCAN_REQUEST_CODE = 100;
    private int checkedItem = 0;
    private String scanMessage;

    int index = 2;

    private List<RenLing.LivestockListBean> mLivestockList;


    private void startScanActivity() {
        Intent intent = new Intent(getActivity(), CaptureActivity2.class);
        intent.putExtra(CaptureActivity2.USE_DEFUALT_ISBN_ACTIVITY, true);
        intent.putExtra("inputUnable", 0);
        intent.putExtra("lightUnable", 1);
        intent.putExtra("albumUnable", 1);
        intent.putExtra("cancleUnable", 1);
        startActivityForResult(intent, SCAN_REQUEST_CODE);

    }

    //  a为原字符串，b为要插入的字符串，t为插入位置
    public String Stringinsert(String a, String b, int t) {
        return a.substring(0, t) + b + a.substring(t, a.length());
    }

    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SCAN_REQUEST_CODE:

                    String isbn = data.getStringExtra("CaptureIsbn");

                    if (!TextUtils.isEmpty(isbn)) {
                        if (StrLengthUtil.length(isbn) == 16) {

                            //设备是否绑定
                            OkGo.<String>get(Constants.ISDEVICEBINDED)
                                    .tag(this)
                                    .params("token", mLoginSuccess.getToken())
                                    .params("username", mLoginSuccess.getName())
                                    .params("ranchID", mLoginSuccess.getRanchID())
                                    .params("deviceNO", isbn)
                                    .execute(new StringCallback() {
                                        @Override
                                        public void onSuccess(Response<String> response) {

                                            String s = response.body().toString();
                                            if (s.contains("true")) {
                                                //已登记


                                            } else {
                                                //未登记


                                            }


                                        }
                                    });

                            Intent intent = new Intent(getActivity(), PublishClaimActivity.class);
                            intent.putExtra("isbn", isbn);
                            startActivityForResult(intent, 1001);

                        } else if (StrLengthUtil.length(isbn) == 15) {

                            String str = Stringinsert(isbn, "1", 7);
                            Log.d(TAG1, "15位isbn=" + str);
                            Log.d(TAG1, "新的长度" + StrLengthUtil.length(str));
                            Intent intent = new Intent(getActivity(), PublishClaimActivity.class);
                            intent.putExtra("isbn", str);
                            startActivityForResult(intent, 1001);

                        } else {

                            ToastUtils.showShort("设备号必须是15位或者16位");

                        }

                    }

                case 1001:

                    //刷新数据
                    OkGo.<String>post(Constants.LIVE_STOCK_CLAIM_LIST)
                            .tag(this)
                            .params("token", mLoginSuccess.getToken())
                            .params("username", mUsername)
                            .params("ranchID", mLoginSuccess.getRanchID())
//                .params("isClaimed",)
                            .params("current", 0)
                            .params("pagesize", 5)
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {


                                    String s = response.body().toString();
                                    Log.d(TAG1, s);
                                    if (s.contains("livestockId")) {
                                        Gson gson1 = new Gson();
                                        RenLing renLing = gson1.fromJson(s, RenLing.class);
                                        mLivestockList = renLing.getLivestockList();

                                        //创建并设置Adapter
                                        mAdapter = new MyAdapter(mLivestockList);
                                        mRecyclerView.setAdapter(mAdapter);

                                        MoveToPosition(mLayoutManager, 0);


                                        mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                                            @Override
                                            public void onItemClick(View view, int position) {

                                                Log.d(TAG1, position + "被点击了");
                                                RenLing.LivestockListBean livestockListBean = mLivestockList.get(position);

                                                Intent intent = new Intent(getActivity(), RenlingDetailActivity.class);
                                                intent.putExtra("getImgUrl", livestockListBean.getImgUrl());
                                                intent.putExtra("getLivestockName", livestockListBean.getLivestockName());
                                                intent.putExtra("getDeviceNo", livestockListBean.getDeviceNo());
                                                intent.putExtra("getCharacteristics", livestockListBean.getCharacteristics());
                                                intent.putExtra("getCellphone", livestockListBean.getCellphone());
                                                intent.putExtra("getCreateTime", livestockListBean.getCreateTime());
                                                intent.putExtra("getPrice", livestockListBean.getPrice());
                                                intent.putExtra("getIsClaimed", livestockListBean.getIsClaimed());
                                                intent.putExtra("getLifeTime", livestockListBean.getLifeTime());
                                                intent.putExtra("getBirthTime", livestockListBean.getBirthTime());
                                                intent.putExtra("getClaimTime", livestockListBean.getClaimTime());

                                                startActivity(intent);

                                            }
                                        });


                                    } else {


                                    }


                                }
                            });


                    break;

            }

        }
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = View.inflate(getActivity(), R.layout.fragment_claim, null);

        ImageView ivAdd = view.findViewById(R.id.ivAdd);
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //扫描
                checkedItem = 2;

                startScanActivity();


//                startActivity(new Intent(getActivity(), PublishClaimActivity.class));


            }
        });


        RefreshLayout refreshLayout = view.findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {


                OkGo.<String>post(Constants.LIVE_STOCK_CLAIM_LIST)
                        .tag(this)
                        .params("token", mLoginSuccess.getToken())
                        .params("username", mUsername)
                        .params("ranchID", mLoginSuccess.getRanchID())
//                .params("isClaimed",)
                        .params("current", 0)
                        .params("pagesize", 5)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {


                                String s = response.body().toString();
                                Log.d(TAG1, s);
                                if (s.contains("livestockId")) {

                                    index = 1;
                                    Gson gson1 = new Gson();
                                    RenLing renLing = gson1.fromJson(s, RenLing.class);
                                    mLivestockList = renLing.getLivestockList();

                                    //创建并设置Adapter
                                    mAdapter = new MyAdapter(mLivestockList);
                                    mRecyclerView.setAdapter(mAdapter);



                                    mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {

                                            Log.d(TAG1, position + "被点击了");
                                            RenLing.LivestockListBean livestockListBean = mLivestockList.get(position);

                                            Intent intent = new Intent(getActivity(), RenlingDetailActivity.class);
                                            intent.putExtra("getImgUrl", livestockListBean.getImgUrl());
                                            intent.putExtra("getLivestockName", livestockListBean.getLivestockName());
                                            intent.putExtra("getDeviceNo", livestockListBean.getDeviceNo());
                                            intent.putExtra("getCharacteristics", livestockListBean.getCharacteristics());
                                            intent.putExtra("getCellphone", livestockListBean.getCellphone());
                                            intent.putExtra("getCreateTime", livestockListBean.getCreateTime());
                                            intent.putExtra("getPrice", livestockListBean.getPrice());
                                            intent.putExtra("getIsClaimed", livestockListBean.getIsClaimed());
                                            intent.putExtra("getLifeTime", livestockListBean.getLifeTime());
                                            intent.putExtra("getBirthTime", livestockListBean.getBirthTime());
                                            intent.putExtra("getClaimTime", livestockListBean.getClaimTime());

                                            startActivity(intent);

                                        }
                                    });


                                } else {


                                }


                            }
                        });

                refreshlayout.finishRefresh(2000);//传入false表示刷新失败


            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {

                OkGo.<String>post(Constants.LIVE_STOCK_CLAIM_LIST)
                        .tag(this)
                        .params("token", mLoginSuccess.getToken())
                        .params("username", mUsername)
                        .params("ranchID", mLoginSuccess.getRanchID())
                        .params("current", index)
                        .params("pagesize", 3)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String s = response.body().toString();
                                Log.d(TAG1, s);
                                if (s.contains("livestockId")) {

                                    index++;

                                    Gson gson1 = new Gson();
                                    RenLing renLing = gson1.fromJson(s, RenLing.class);
                                    List<RenLing.LivestockListBean> mylist =
                                            renLing.getLivestockList();

                                    if (mylist.size() != 0) {
                                        for (int i = 0; i < mylist.size(); i++) {
                                            mLivestockList.add(mylist.get(i));
                                        }
                                        MoveToPosition(mLayoutManager, 3 * (index - 1));


                                    }

                                    //创建并设置Adapter
                                    mAdapter = new MyAdapter(mLivestockList);
                                    mRecyclerView.setAdapter(mAdapter);


                                    mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {

                                            Log.d(TAG1, position + "被点击了");
                                            RenLing.LivestockListBean livestockListBean = mLivestockList.get(position);

                                            Intent intent = new Intent(getActivity(), RenlingDetailActivity.class);
                                            intent.putExtra("getDeviceNo", livestockListBean.getDeviceNo());
                                            intent.putExtra("getImgUrl", livestockListBean.getImgUrl());
                                            intent.putExtra("getLivestockName", livestockListBean.getLivestockName());
                                            intent.putExtra("getCharacteristics", livestockListBean.getCharacteristics());
                                            intent.putExtra("getCellphone", livestockListBean.getCellphone());
                                            intent.putExtra("getCreateTime", livestockListBean.getCreateTime());
                                            intent.putExtra("getPrice", livestockListBean.getPrice());
                                            intent.putExtra("getIsClaimed", livestockListBean.getIsClaimed());
                                            intent.putExtra("getLifeTime", livestockListBean.getLifeTime());
                                            intent.putExtra("getBirthTime", livestockListBean.getBirthTime());
                                            intent.putExtra("getClaimTime", livestockListBean.getClaimTime());
                                            startActivity(intent);


                                        }
                                    });


                                } else {

                                    ToastUtils.showShort("没有更多数据了");

                                }


                            }
                        });


                refreshLayout.finishLoadMore();


            }

        });


        FunGameHitBlockHeader funGameHitBlockHeader = new FunGameHitBlockHeader(getActivity());
        //设置 Header 为 Material样式
        refreshLayout.setRefreshHeader(funGameHitBlockHeader);
        //设置 Footer 为 球脉冲
        refreshLayout.setRefreshFooter(new BallPulseFooter(getActivity()).setSpinnerStyle(SpinnerStyle.Scale));

        mRecyclerView = view.findViewById(R.id.my_recycler_view);
        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);

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
                .params("pagesize", 5)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        String s = response.body().toString();
                        Log.d(TAG1, s);
                        if (s.contains("livestockId")) {

                            Gson gson1 = new Gson();
                            RenLing renLing = gson1.fromJson(s, RenLing.class);
                            mLivestockList = renLing.getLivestockList();

                            //创建并设置Adapter
                            mAdapter = new MyAdapter(mLivestockList);
                            mRecyclerView.setAdapter(mAdapter);

                            mAdapter.setOnItemClickListener(new OnRecyclerViewItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {

                                    Log.d(TAG1, position + "被点击了");
                                    RenLing.LivestockListBean livestockListBean = mLivestockList.get(position);

                                    Intent intent = new Intent(getActivity(), RenlingDetailActivity.class);
                                    intent.putExtra("getDeviceNo", livestockListBean.getDeviceNo());
                                    intent.putExtra("getImgUrl", livestockListBean.getImgUrl());
                                    intent.putExtra("getLivestockName", livestockListBean.getLivestockName());
                                    intent.putExtra("getCharacteristics", livestockListBean.getCharacteristics());
                                    intent.putExtra("getCellphone", livestockListBean.getCellphone());
                                    intent.putExtra("getCreateTime", livestockListBean.getCreateTime());
                                    intent.putExtra("getPrice", livestockListBean.getPrice());
                                    intent.putExtra("getIsClaimed", livestockListBean.getIsClaimed());
                                    intent.putExtra("getLifeTime", livestockListBean.getLifeTime());
                                    intent.putExtra("getBirthTime", livestockListBean.getBirthTime());
                                    intent.putExtra("getClaimTime", livestockListBean.getClaimTime());
                                    startActivity(intent);

                                }
                            });

                        } else {

                        }

                    }
                });


        return view;


    }


    private String[] getDummyDatas() {

        String[] arr = {"北京", "上海", "广州", "深圳"};

        return arr;

    }

    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private MyAdapter mAdapter;

    public static interface OnRecyclerViewItemClickListener {

        void onItemClick(View view, int data);

    }

    /**
     * RecyclerView 移动到当前位置，
     *
     * @param manager 设置RecyclerView对应的manager
     * @param n       要跳转的位置
     */

    public static void MoveToPosition(LinearLayoutManager manager, int n) {
        manager.scrollToPositionWithOffset(n, 0);
        manager.setStackFromEnd(true);
    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener {


        private OnRecyclerViewItemClickListener mOnItemClickListener = null;


        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.mOnItemClickListener = listener;

        }

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

//            viewHolder.tvId.setText(datas.get(position).getDeviceNo());
            //将数据保存在itemView的Tag中，以便点击时进行获取
            viewHolder.itemView.setTag(position);

            String imgUrl = datas.get(position).getImgUrl();
            imgUrl = Constants.BASE_URL + imgUrl;
            Log.d(TAG1, imgUrl);
            Uri uri = Uri.parse(imgUrl);
            viewHolder.ivGhoat.setImageURI(uri);


            String livestockName = datas.get(position).getLivestockName();
            Log.d(TAG1, "livestockName=" + livestockName);
            viewHolder.tvAnimalName.setText(livestockName);


            viewHolder.tvId.setText("设备号：" + datas.get(position).getDeviceNo());
            viewHolder.tvAnimalAge.setText("发布日期：" + datas.get(position).getBirthTime());
            viewHolder.tvMuChang.setText("特点：" + datas.get(position).getCharacteristics());

            String claimTime = datas.get(position).getClaimTime();
            if (TextUtils.isEmpty(claimTime)) {
                viewHolder.tvClaimTime.setVisibility(View.INVISIBLE);
            } else {
                viewHolder.tvClaimTime.setVisibility(View.VISIBLE);
                viewHolder.tvClaimTime.setText("认领时间：" + claimTime);
            }

            String isClaimed = datas.get(position).getIsClaimed();
            if (isClaimed.equals("0")) {

                viewHolder.tvPriceAndClaim.setTextColor(Color.GRAY);
                viewHolder.tvPriceAndClaim.setText(" 未认领");

            } else if (isClaimed.equals("1")) {

                viewHolder.tvPriceAndClaim.setText("价格：" +
                        datas.get(position).getPrice()
                        + "元     已认领");

                viewHolder.tvPriceAndClaim.setTextColor(Color.GREEN);


            }

            String cellphone = datas.get(position).getCellphone();
            if (TextUtils.isEmpty(cellphone)) {

                viewHolder.tvPhone.setVisibility(View.INVISIBLE);

            } else {

                viewHolder.tvPhone.setText("手机号：" + cellphone);
                viewHolder.tvPhone.setVisibility(View.VISIBLE);

            }


        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                mOnItemClickListener.onItemClick(v, (int) v.getTag());

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

            TextView tvPriceAndClaim;
            TextView tvClaimer;
            TextView tvPhone;


            public ViewHolder(View view) {
                super(view);
//                mTextView = view.findViewById(R.id.tvClaim);
                ivGhoat = view.findViewById(R.id.ivGhoat);
                tvAnimalName = view.findViewById(R.id.tvAnimalName);
                tvId = view.findViewById(R.id.tvId);
                tvAnimalAge = view.findViewById(R.id.tvAnimalAge);
                tvMuChang = view.findViewById(R.id.tvMuChang);
                tvClaimTime = view.findViewById(R.id.tvClaimTime);

                tvPriceAndClaim = view.findViewById(R.id.tvPriceAndClaim);
                tvClaimer = view.findViewById(R.id.tvClaimer);
                tvPhone = view.findViewById(R.id.tvPhone);


            }

        }

    }


}
