package com.jinkun_innovation.pastureland.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.base.BaseActivity;
import com.jinkun_innovation.pastureland.utilcode.AppManager;
import com.jinkun_innovation.pastureland.utilcode.util.FileUtils;
import com.jinkun_innovation.pastureland.utilcode.util.LogUtils;
import com.jinkun_innovation.pastureland.utilcode.util.ToastUtils;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * Created by yangxing on 2018/1/15.
 */

public class UpLoadActivity extends BaseActivity {
    @BindView(R.id.pb_loading)
    ProgressBar mPbLoading;
    private File mPhotoFile;
    private int mPhase;
    private String mDeviceNo;
    private String url;

    @BindView(R.id.img_upload)
    ImageView mImgUpload;

    @Override
    protected void initToolBar() {
        setIsShowBack(true);
        switch (getIntent().getIntExtra(getString(R.string.checked_Item), 0)) {
            case 2:
                setTitle(TypeEnum.BIRTHDAY.getName());
                break;
            case 3:
                setTitle(TypeEnum.CROP.getName());
                break;
            case 0:
                setTitle(TypeEnum.LIFE.getName());
                break;
            case 4:
                setTitle(TypeEnum.PRIVATE.getName());
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
        String rootDir = "/Pastureland/crop";
        File file = new File(Environment.getExternalStorageDirectory(), rootDir);

        if (FileUtils.createOrExistsDir(file)) {
            LogUtils.e(file.getAbsolutePath());
            Luban.with(this)
                    .load(FileUtils.getFileByPath(imgUrl))                                   // 传人要压缩的图片列表
                    .ignoreBy(100)                                  // 忽略不压缩图片的大小
                    .setTargetDir(file.getAbsolutePath())
                    .setCompressListener(new OnCompressListener() { //设置回调
                        @Override
                        public void onStart() {
                            // TODO 压缩开始前调用，可以在方法内启动 loading UI
                            LogUtils.e("onStart");
                            mPbLoading.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onSuccess(File file) {
                            // TODO 压缩成功后调用，返回压缩后的图片文件
                            mPhotoFile = file;
                            LogUtils.e("onSuccess");
                            LogUtils.e(file.getAbsolutePath());
                            Glide.with(UpLoadActivity.this).load(file).into(mImgUpload);
//                            FileUtils.deleteFile(imgUrl);
                            mPbLoading.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Throwable e) {
                            // TODO 当压缩过程出现问题时调用
                            LogUtils.e(e.getMessage());
                            ToastUtils.showShort("压缩出现问题，请重新拍摄");
                            AppManager.getAppManager().finishActivity();
                            mPbLoading.setVisibility(View.GONE);
                        }
                    }).launch();
        }
    }

    @OnClick(R.id.btn_submit)
    public void onClick() {
        // TODO: 2018/1/16 上传图片

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("上传中...");
        pDialog.setCancelable(false);
        pDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pDialog.cancel();
                finish();

            }
        }, 2000);
       /* RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/jpg"), mPhotoFile);
        MultipartBody.Part photo = MultipartBody.Part.createFormData("imgUrl", mPhotoFile.getName(), photoRequestBody);
//        RequestBody photoRequestBody = RequestBody.create(MediaType.parse("image/jpg"), FileUtils.getFileByPath(url));
//        MultipartBody.Part photo = MultipartBody.Part.createFormData("imgUrl", FileUtils.getFileByPath(url).getName(), photoRequestBody);


        if (mPhase == 0) {
            RetrofitManger.getInstance().createReq(ApiCall.class)
                    .livephoto(photo, RequestBody.create(null, SpUtil.getAccount()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<ConfirmClaimBean>() {
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
                        public void onNext(ConfirmClaimBean confirmClaimBean) {
                            if (confirmClaimBean.getCode().equals("200")) {
                                AppManager.getAppManager().finishActivity();
                                ToastUtils.showShort("上传成功");
                            } else {
                                ToastUtils.showShort("上传失败，" + confirmClaimBean.getMsg());
                            }
                        }
                    });

        } else {


            RetrofitManger.getInstance().createReq(ApiCall.class)
                    .uploadPhoto(photo, RequestBody.create(null, mDeviceNo),
                            RequestBody.create(null, ""),
                            RequestBody.create(null, String.valueOf(mPhase)),
                            RequestBody.create(null, SpUtil.getToken()),
                            RequestBody.create(null, SpUtil.getUserId()),
                            RequestBody.create(null, SpUtil.getAccount()),
                            RequestBody.create(null, SpUtil.getCompanyId()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new BaseSubscriber<ConfirmClaimBean>() {
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
                        public void onNext(ConfirmClaimBean confirmClaimBean) {
                            if (confirmClaimBean.getCode().equals("200")) {
                                AppManager.getAppManager().finishActivity();
                                ToastUtils.showShort("上传成功");
                            } else {
                                ToastUtils.showShort("上传失败，" + confirmClaimBean.getMsg());
                            }
                        }
                    });
        } */

    }


    private enum TypeEnum {
        BIRTHDAY("接羔上传"), CROP("剪毛上传"), LIFE("生活上传"), PRIVATE("私人定制上传");

        private final String name;

        TypeEnum(String name) {
            this.name = name;
        }

        private String getName() {
            return name;
        }
    }

}
