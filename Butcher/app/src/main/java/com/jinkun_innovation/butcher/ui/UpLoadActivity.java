package com.jinkun_innovation.butcher.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.jinkun_innovation.butcher.R;
import com.jinkun_innovation.butcher.base.BaseActivity;
import com.jinkun_innovation.butcher.bean.ResponseBean;
import com.jinkun_innovation.butcher.net.ApiCall;
import com.jinkun_innovation.butcher.net.BaseSubscriber;
import com.jinkun_innovation.butcher.net.ExceptionHandle;
import com.jinkun_innovation.butcher.net.RetrofitManger;
import com.jinkun_innovation.butcher.utilcode.AppManager;
import com.jinkun_innovation.butcher.utilcode.util.ActivityUtils;
import com.jinkun_innovation.butcher.utilcode.util.FileUtils;
import com.jinkun_innovation.butcher.utilcode.util.LogUtils;
import com.jinkun_innovation.butcher.utilcode.util.ToastUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by yangxing on 2018/1/15.
 */

public class UpLoadActivity extends BaseActivity {

    @BindView(R.id.pb_loading)
    ProgressBar mPbLoading;
    private File mPhotoFile;
    private String mDeviceNo;
    private int mPhase;
    private String url;


    @BindView(R.id.img_upload)
    ImageView mImgUpload;

    @Override
    protected void initToolBar() {
        setIsShowBack(true);
        switch (getIntent().getIntExtra(getString(R.string.checked_Item), 0)) {
            case 4:
                setTitle(TypeEnum.BEFORE.getName());
                break;
            case 5:
                setTitle(TypeEnum.AFTER.getName());
                break;

        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_upload;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mPhase = getIntent().getIntExtra(getString(R.string.checked_Item), 0);
        mDeviceNo = getIntent().getStringExtra(getString(R.string.scan_Message));
        cropImage(getIntent().getStringExtra(getString(R.string.img_Url)));
//        url = getIntent().getStringExtra(getString(R.string.img_Url));
//        Glide.with(UpLoadActivity.this).load(FileUtils.getFileByPath(url)).into(mImgUpload);
//        mPbLoading.setVisibility(View.GONE);
    }

    private void cropImage(final String imgUrl) {
        String rootDir = "/Butcher/crop";
        File file = new File(Environment.getExternalStorageDirectory(), rootDir);

        if (FileUtils.createOrExistsDir(file)) {
            Luban.with(this)
                    .load(FileUtils.getFileByPath(imgUrl))                                   // 传人要压缩的图片列表
                    .ignoreBy(100)                                  // 忽略不压缩图片的大小
                    .setTargetDir(file.getAbsolutePath())
                    .setCompressListener(new OnCompressListener() { //设置回调
                        @Override
                        public void onStart() {
                            // TODO 压缩开始前调用，可以在方法内启动 loading UI
                           ToastUtils.showShort("正在压缩图片");
                            mPbLoading.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onSuccess(File successFile) {
                            // TODO 压缩成功后调用，返回压缩后的图片文件
                            ToastUtils.showShort("压缩图片成功");
                            LogUtils.e("onSuccess");
                            mPhotoFile = successFile;
                            LogUtils.e(mPhotoFile.getAbsolutePath());
                            Glide.with(UpLoadActivity.this).load(successFile).into(mImgUpload);
//                            FileUtils.deleteFile(imgUrl);
                            mPbLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            // TODO 当压缩过程出现问题时调用
                            LogUtils.e(e.getMessage());
                            ToastUtils.showShort("压缩失败，请重新拍摄");
                            AppManager.getAppManager().finishActivity();
                            mPbLoading.setVisibility(View.GONE);
                        }
                    }).launch();
        }
    }

    @OnClick(R.id.btn_submit)
    public void onClick() {


        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/jpg"), mPhotoFile);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("imgUrl", mPhotoFile.getName(), photoRequestBody);


//
//        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/jpg"), FileUtils.getFileByPath(url));
//        MultipartBody.Part photo = MultipartBody.Part.createFormData("imgUrl", FileUtils.getFileByPath(url).getName(), photoRequestBody);


        RetrofitManger.getInstance().createReq(ApiCall.class)
                .uploadPhoto(photo, RequestBody.create(null, mDeviceNo),
                        RequestBody.create(null, String.valueOf(mPhase)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResponseBean>() {
                    @Override
                    public void onStart() {
                        showProgress("正在上传");
                    }

                    @Override
                    public void onCompleted() {
                        hiddenProgress();

                    }

                    @Override
                    public void onError(ExceptionHandle.ResponseThrowable throwable) {
                        hiddenProgress();
                        ToastUtils.showShort(throwable.getMessage());
                    }

                    @Override
                    public void onNext(ResponseBean responseBean) {
                        if (responseBean.getCode().equals("200")) {
                            if (mPhase == 4) {
                                AppManager.getAppManager().finishActivity();
                                ToastUtils.showShort("上传成功");
                            } else {
                                Intent intent = new Intent();
                                intent.setClass(UpLoadActivity.this, PrinterSettingActivity.class);
                                intent.putExtra("url", responseBean.getUrl());
                                LogUtils.e(responseBean.getUrl());
                                ActivityUtils.startActivity(intent);
                                AppManager.getAppManager().finishActivity();
                            }
                        } else {
                            ToastUtils.showShort("上传失败，" + responseBean.getMsg());
                        }
                    }
                });
    }


    private enum TypeEnum {
        BEFORE("宰杀前"), AFTER("宰杀后");

        private final String name;

        TypeEnum(String name) {
            this.name = name;
        }

        private String getName() {
            return name;
        }
    }
}
