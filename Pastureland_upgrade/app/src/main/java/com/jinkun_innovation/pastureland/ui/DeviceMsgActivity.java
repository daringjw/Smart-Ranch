package com.jinkun_innovation.pastureland.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.bean.DeviceMsg;
import com.jinkun_innovation.pastureland.bean.ImgUrlBean;
import com.jinkun_innovation.pastureland.bean.LoginSuccess;
import com.jinkun_innovation.pastureland.common.Constants;
import com.jinkun_innovation.pastureland.ui.activity.QuPaizhaoActivity;
import com.jinkun_innovation.pastureland.utilcode.util.ToastUtils;
import com.jinkun_innovation.pastureland.utils.PrefUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static com.jinkun_innovation.pastureland.R.id.ivQupaizhao;


/**
 * Created by Guan on 2018/3/21.
 */

public class DeviceMsgActivity extends Activity {

    private static final String TAG1 = DeviceMsgActivity.class.getSimpleName();

    String mLogin_success;
    LoginSuccess mLoginSuccess;
    String mUsername;

    File file;
    private static final int REQUEST_CAMERA = 1001;
    private String mDeviceNo;
    private ImageView mIvQupaizhao;
    private ImageView mIvQuluxiang;

    /**
     * 使用相机
     */
    private void useCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/test/" + System.currentTimeMillis() + ".jpg");
        file.getParentFile().mkdirs();

        //改变Uri  com.xykj.customview.fileprovider注意和xml中的一致
        Uri uri = FileProvider.getUriForFile(this, "com.jinkun_innovation.pastureland.fileProvider", file);
        //添加权限
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

//            Log.e("TAG", "---------" + FileProvider.getUriForFile(this, "com.xykj.customview.fileprovider", file));
//            imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));

            switch (requestCode) {

                case REQUEST_CAMERA:

                    Intent intent = new Intent(this, QuPaizhaoActivity.class);
                    intent.putExtra("qupaizhao", file.getAbsolutePath());
                    intent.putExtra("mDeviceNo", mDeviceNo);

                    startActivityForResult(intent, 1002);

                    break;

                case 1002:


                    mIvQupaizhao.setImageResource(R.mipmap.done);
                    mIvQupaizhao.setClickable(false);

                    break;


                case CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE:

                    Toast.makeText(this, "Video saved to:\n" +
                            data.getData(), Toast.LENGTH_LONG).show();

                    Log.d(TAG1, "data.getDATA=" + data.getData());
                    filename = data.getData().toString();


                    File file = new File(filename);
                    Log.d(TAG1, file.exists() + "xxxxxxx");

                    /*filename = filename.substring(7, filename.length() - 1);
                    Log.d(TAG1, "filename=" + filename);*/

                    OkGo.<String>post(Constants.HEADIMGURL)
                            .tag(this)
                            .params("token", mLoginSuccess.getToken())
                            .params("username", mUsername)
                            .params("uploadFile", new File(filename))
                            .execute(new StringCallback() {
                                @Override
                                public void onSuccess(Response<String> response) {

                                    String result = response.body().toString();
                                    Gson gson = new Gson();
                                    ImgUrlBean imgUrlBean = gson.fromJson(result, ImgUrlBean.class);
                                    String videoUrl = imgUrlBean.getImgUrl();

                                    OkGo.<String>get(Constants.updLivestockClaim)
                                            .tag(this)
                                            .params("token", mLoginSuccess.getToken())
                                            .params("username", mUsername)
                                            .params("deviceNO", mDeviceNo)
                                            .params("businessType", 2)
//                                            .params("livestockImgUrl",)
                                            .params("videoUrl", videoUrl)
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {

                                                    String result = response.body().toString();
                                                    if (result.contains("success")) {

                                                        ToastUtils.showShort("视频上传成功");

                                                    }

                                                }
                                            });


                                }
                            });

                    mIvQuluxiang.setImageResource(R.mipmap.done);
                    mIvQuluxiang.setClickable(false);


                    break;


            }


        }

    }

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

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        //创建默认的线性LayoutManager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        //如果可以确定每个item的高度是固定的，设置这个选项可以提高性能
        mRecyclerView.setHasFixedSize(true);

        OkGo.<String>post(Constants.DEVICEMSG)
                .tag(this)
                .params("token", mLoginSuccess.getToken())
                .params("username", mUsername)
                .params("ranchID", mLoginSuccess.getRanchID())
                .params("current", 0)
                .params("pagesize", 15)
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

                        if (batteryList.size() != 0) {
                            //创建并设置Adapter
                            mAdapter = new MyAdapter(batteryList);
                            mRecyclerView.setAdapter(mAdapter);
                        }

                        if (livestockClaimList.size() != 0) {
                            TextView tvTime2 = (TextView) findViewById(R.id.tvTime2);
                            TextView tvQuPaizhao = (TextView) findViewById(R.id.tvQuPaizhao);
                            mIvQupaizhao = (ImageView) findViewById(ivQupaizhao);

                            TextView tvTime3 = (TextView) findViewById(R.id.tvTime3);
                            TextView tvQuLuxiang = (TextView) findViewById(R.id.tvQuLuxiang);
                            mIvQuluxiang = (ImageView) findViewById(R.id.ivQuluxiang);

                            mIvQupaizhao.setVisibility(View.VISIBLE);
                            mIvQuluxiang.setVisibility(View.VISIBLE);

                            tvTime2.setText(livestockClaimList.get(0).getPhotographicTime());
                            tvQuPaizhao.setText("用户 " + livestockClaimList.get(0).getCellphone() +
                                    " 请求 牲畜（设备号" + livestockClaimList.get(0).getDeviceNo() + "）拍照" +
                                    "，请及时处理");
                            mDeviceNo = livestockClaimList.get(0).getDeviceNo();


                            mIvQupaizhao.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //去拍照
                                    useCamera();


                                }
                            });


                            tvTime3.setText(livestockClaimList.get(0).getVideoTime());
                            tvQuLuxiang.setText("用户 " + livestockClaimList.get(0).getCellphone() +
                                    " 请求 牲畜（设备号" + livestockClaimList.get(0).getDeviceNo() + "）摄像" +
                                    "，请及时处理");
                            mIvQuluxiang.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //去录像

                                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                                    StrictMode.setVmPolicy(builder.build());

                                    Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);//Intent action type for requesting a video from an existing camera application.
                                    fileUri = getOutputMediaFileUri();  // create a file to save the video
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);  // set the image file name
                                    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1); // set the video image quality to high
                                    // 开始视频录制Intent
                                    startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);


                                }
                            });
                        }


                    }
                });


        RefreshLayout refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                refreshlayout.finishRefresh(2000/*,false*/);//传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {

                refreshlayout.finishLoadMore(500/*,false*/);//传入false表示加载失败


            }
        });


    }

    private Uri fileUri;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    Button btn1;
    Button btn2;
    String filename = "";

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri() {
        return Uri.fromFile(getOutputMediaFile());
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        @SuppressLint("SimpleDateFormat") String timeStamp = new
                SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "VID_" + timeStamp + ".mp4");


        return mediaFile;
    }


    private String[] getDummyDatas() {

        String[] arr = {"北京", "上海", "广州", "深圳"};

        return arr;

    }

    RecyclerView mRecyclerView;
    LinearLayoutManager mLayoutManager;
    MyAdapter mAdapter;

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        public List<DeviceMsg.BatteryListBean> datas = null;

        public MyAdapter(List<DeviceMsg.BatteryListBean> datas) {
            this.datas = datas;
        }

        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

            View view = LayoutInflater.from(viewGroup.getContext()).
                    inflate(R.layout.item_device_msg, viewGroup, false);
            ViewHolder vh = new ViewHolder(view);
            return vh;

        }

        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {

//            viewHolder.mTextView.setText(datas[position]);
            viewHolder.tvTime1.setText(datas.get(position).getCreateTime());
            viewHolder.tvLowPower.setText("设备" + datas.get(position).getDeviceNo() + "于" +
                    datas.get(position).getCreateTime() + "发出了低电报警，请尽快去确认，及时充电");


        }

        //获取数据的数量
        @Override
        public int getItemCount() {
            return datas.size();
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public class ViewHolder extends RecyclerView.ViewHolder {

            //            public TextView mTextView;
            TextView tvTime1;
            TextView tvLowPower;


            public ViewHolder(View view) {
                super(view);

//                mTextView = (TextView) view.findViewById(R.id.text);
                tvTime1 = view.findViewById(R.id.tvTime1);
                tvLowPower = view.findViewById(R.id.tvLowPower);


            }


        }
    }


}
