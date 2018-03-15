package com.jinkun_innovation.butcher.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.client.android.CaptureActivity2;
import com.jinkun_innovation.butcher.R;
import com.jinkun_innovation.butcher.base.BaseActivity;
import com.jinkun_innovation.butcher.bean.ResponseBean;
import com.jinkun_innovation.butcher.net.ApiCall;
import com.jinkun_innovation.butcher.net.BaseSubscriber;
import com.jinkun_innovation.butcher.net.ExceptionHandle;
import com.jinkun_innovation.butcher.net.RetrofitManger;
import com.jinkun_innovation.butcher.utilcode.util.ActivityUtils;
import com.jinkun_innovation.butcher.utilcode.util.FileUtils;
import com.jinkun_innovation.butcher.utilcode.util.LogUtils;
import com.jinkun_innovation.butcher.utilcode.util.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 3;//权限请求
    private static final int SCAN_REQUEST_CODE = 100;
    @BindView(R.id.toolbar_title)
    TextView mToolbarTitle;

    private String imagePath;
    private File photoFile;
    private Uri imageUri;//原图保存地址
    private static final int REQUEST_CAPTURE = 2;  //拍照

    private int checkedItem = 0;
    private String scanMessage;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        checkRuntimePermissions();
        mToolbarTitle.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ActivityUtils.startActivity(IpSettingActivity.class);
                return true;
            }
        });
    }

    @Override
    protected void initToolBar() {
        setTitle("智慧屠宰场");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_main;
    }


    private void openCamera() {

        String rootDir = "/Butcher/photo";
        FileUtils.createOrExistsDir(new File(Environment.getExternalStorageDirectory(), rootDir));
        File file = new File(Environment.getExternalStorageDirectory(), rootDir);
        photoFile = new File(file, UUID.randomUUID().toString() + ".jpg");
        FileUtils.createOrExistsFile(photoFile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(MainActivity.this, "com.jinkun_innovation.butcher.fileProvider", photoFile);//通过FileProvider创建一个content类型的Uri
        } else {
            imageUri = Uri.fromFile(photoFile);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CAPTURE);
    }


    private void startScanActivity() {
        Intent intent = new Intent(MainActivity.this, CaptureActivity2.class);
        intent.putExtra(CaptureActivity2.USE_DEFUALT_ISBN_ACTIVITY, true);
        intent.putExtra("inputUnable", 0);
        intent.putExtra("lightUnable", 1);
        intent.putExtra("albumUnable", 1);
        intent.putExtra("cancleUnable", 1);
        startActivityForResult(intent, SCAN_REQUEST_CODE);
    }


    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkRuntimePermissions() {
        List<String> permissions = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.CAMERA);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissions.size() > 0) {
            ActivityCompat.requestPermissions(MainActivity.this, permissions.toArray(new String[permissions.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
        } else {
//            startScanActivity();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SCAN_REQUEST_CODE:
                    String isbn = data.getStringExtra("CaptureIsbn");
                    if (!TextUtils.isEmpty(isbn)) {
                        LogUtils.e(isbn);
                        scanMessage = isbn;
                        Toast.makeText(this, "解析到的内容为" + isbn, Toast.LENGTH_LONG).show();
                        if (checkedItem == 0) {
                            toRequestQrUrl();
                        } else {
                            openCamera();
                        }
                    }
                    break;

                case REQUEST_CAPTURE://拍照
                    Intent intent = new Intent();
                    intent.putExtra(getString(R.string.checked_Item), checkedItem);
                    intent.putExtra(getString(R.string.img_Url), photoFile.getAbsolutePath());
                    intent.putExtra(getString(R.string.scan_Message), scanMessage);
                    intent.setClass(MainActivity.this, UpLoadActivity.class);
                    startActivity(intent);
                    break;


            }
        }
    }

    private void toRequestQrUrl() {
        // TODO: 2018/1/24 请求qr二维码
        RetrofitManger.getInstance().createReq(ApiCall.class)
                .toQueryRecord(scanMessage)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<ResponseBean>() {
                    @Override
                    public void onStart() {
                        showProgress("正在处理");
                    }

                    @Override
                    public void onCompleted() {
                        hiddenProgress();

                    }

                    @Override
                    public void onError(ExceptionHandle.ResponseThrowable throwable) {
                        hiddenProgress();
                        ToastUtils.showShort(throwable.message);
                    }

                    @Override
                    public void onNext(ResponseBean responseBean) {
                        if (responseBean.getCode().equals("200")) {
                            Intent intent = new Intent();
                            intent.setClass(MainActivity.this, PrinterSettingActivity.class);
                            intent.putExtra("url", responseBean.getUrl());
                            ActivityUtils.startActivity(intent);
                        } else {
                            ToastUtils.showShort(responseBean.getMsg());
                        }
                    }
                });
    }

    @OnClick({R.id.btn_before, R.id.btn_after, R.id.btn_print})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_before:
                checkedItem = 4;
                startScanActivity();
                break;
            case R.id.btn_after:
                checkedItem = 5;
                startScanActivity();
                break;
            case R.id.btn_print:
                checkedItem =0;
                startScanActivity();
                break;
        }
    }
}
