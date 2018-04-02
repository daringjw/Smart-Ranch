package com.jinkun_innovation.pastureland.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.google.zxing.client.android.CaptureActivity2;
import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.bean.LoginSuccess;
import com.jinkun_innovation.pastureland.common.Constants;
import com.jinkun_innovation.pastureland.ui.GrassActivity;
import com.jinkun_innovation.pastureland.ui.LianJiangPasturelandActivity;
import com.jinkun_innovation.pastureland.ui.RegisterActivity;
import com.jinkun_innovation.pastureland.ui.ToolsActivity;
import com.jinkun_innovation.pastureland.ui.UpLoadActivity;
import com.jinkun_innovation.pastureland.utilcode.util.FileUtils;
import com.jinkun_innovation.pastureland.utilcode.util.LogUtils;
import com.jinkun_innovation.pastureland.utilcode.util.TimeUtils;
import com.jinkun_innovation.pastureland.utilcode.util.ToastUtils;
import com.jinkun_innovation.pastureland.utils.PrefUtils;
import com.jinkun_innovation.pastureland.utils.StrLengthUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;


/**
 * Created by Guan on 2018/3/15.
 */

public class ManagerFragment extends Fragment {


    private static final String TAG1 = ManagerFragment.class.getSimpleName();

    @BindView(R.id.llRegister)
    LinearLayout llRegister;
    @BindView(R.id.llJianMao)
    LinearLayout llJianMao;
    @BindView(R.id.llLifePhoto)
    LinearLayout llLifePhoto;
    @BindView(R.id.llTools)
    LinearLayout llTools;
    @BindView(R.id.llGrass)
    LinearLayout llGrass;
    @BindView(R.id.llMyMuChang)
    LinearLayout llMyMuChang;


    Unbinder unbinder;

    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 3;//权限请求
    private static final int SCAN_REQUEST_CODE = 100;

    private static final int CAMERA_ACTIVITY = 6;  //视频

    private String imagePath;
    private File photoFile;
    private Uri imageUri;//原图保存地址
    private static final int REQUEST_CAPTURE = 2;  //拍照

    private int checkedItem = 0;
    private String scanMessage;

    private SliderLayout mSliderShow;
    private LoginSuccess mLoginSuccess;
    private TextView mTvMuchangName;
    private String mLogin_success;
    private String mUsername;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = View.inflate(getActivity(), R.layout.fragment_manager, null);

        unbinder = ButterKnife.bind(this, view);


        mLogin_success = PrefUtils.getString(getActivity(), "login_success", null);
        Gson gson = new Gson();
        mLoginSuccess = gson.fromJson(mLogin_success, LoginSuccess.class);
        mUsername = PrefUtils.getString(getActivity(), "username", null);


        //牧场名
        mTvMuchangName = view.findViewById(R.id.tvMuchangName);
        if (!TextUtils.isEmpty(mLogin_success)) {
            mTvMuchangName.setText(mLoginSuccess.getName());
        }

        mSliderShow = view.findViewById(R.id.slider);
        TextSliderView textSliderView = new TextSliderView(getActivity());

        textSliderView
                .description("智慧牧场")
                .setScaleType(BaseSliderView.ScaleType.Fit)//图片缩放类型
                .image("http://imgsrc.baidu.com/image/c0%3Dshijue1%2C0%2C0%2C294%2C40/sign=8cc5e60fb91bb0519b29bb6b5e13b0c1/f9198618367adab46f5ff16e81d4b31c8701e414.jpg");

        TextSliderView textSliderView1 = new TextSliderView(getActivity());
        textSliderView1
                .description("科技点亮牧场")
                .image("https://goss2.vcg.com/creative/vcg/800/version23/VCG41544531487.jpg");

        TextSliderView textSliderView2 = new TextSliderView(getActivity());
        textSliderView2
                .description("金坤技术")
                .image("https://goss1.vcg.com/creative/vcg/800/version23/VCG41544521221.jpg");


        mSliderShow.addSlider(textSliderView);
        mSliderShow.addSlider(textSliderView1);
        mSliderShow.addSlider(textSliderView2);

        mSliderShow.setPresetTransformer(SliderLayout.Transformer.RotateUp);

        LinearLayout llMyMuChang = (LinearLayout) view.findViewById(R.id.llMyMuChang);
        llMyMuChang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getActivity(), LianJiangPasturelandActivity.class));

            }
        });


        return view;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.llRegister, R.id.llJianMao, R.id.llLifePhoto,
            R.id.llTools, R.id.llGrass, R.id.llMyMuChang})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llRegister:
                checkedItem = 2;

                startScanActivity();

                break;
            case R.id.llJianMao:
                checkedItem = 3;
                startScanActivity();
                break;
            case R.id.llLifePhoto:

                checkedItem = 0;
                openCamera();

                break;
            case R.id.llTools:

                startActivity(new Intent(getActivity(), ToolsActivity.class));


                break;
            case R.id.llGrass:

                startActivity(new Intent(getActivity(), GrassActivity.class));

                break;
            case R.id.llMyMuChang:

//                startActivity(new Intent(getActivity(), LianJiangPasturelandActivity.class));
                break;

        }
    }


    //  a为原字符串，b为要插入的字符串，t为插入位置
    public String Stringinsert(String a, String b, int t) {
        return a.substring(0, t) + b + a.substring(t, a.length());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case SCAN_REQUEST_CODE:
                    String isbn = data.getStringExtra("CaptureIsbn");
                    if (!TextUtils.isEmpty(isbn)) {
                        LogUtils.e(isbn);

                        Toast.makeText(getActivity(), "解析到的内容为" + isbn, Toast.LENGTH_LONG).show();

                        if (StrLengthUtil.length(isbn) == 16) {
                            scanMessage = isbn;
                            register(isbn);
                        } else if (StrLengthUtil.length(isbn) == 15) {
                            String str = Stringinsert(isbn, "1", 7);
                            Log.d(TAG1, "15位isbn=" + str);
                            Log.d(TAG1, "新的长度" + StrLengthUtil.length(str));
                            scanMessage = str;
                            register(str);

                        } else {
                            ToastUtils.showShort("设备号必须是15位或者16位");
                        }


                    }


                    break;

                case CAMERA_ACTIVITY:

                    try {
                        AssetFileDescriptor videoAsset = getActivity().getContentResolver()
                                .openAssetFileDescriptor(data.getData(), "r");
                        FileInputStream fis = videoAsset.createInputStream();
                        File tmpFile = new File(Environment.getExternalStorageDirectory(),
                                TimeUtils.getNowString() + ".mp4");

                        FileOutputStream fos = new FileOutputStream(tmpFile);
                        byte[] buf = new byte[1024];
                        int len;
                        while ((len = fis.read(buf)) > 0) {
                            fos.write(buf, 0, len);
                        }
                        fis.close();
                        fos.close();

                        Log.d("ManagerFragment", tmpFile.getAbsolutePath());
                        PrefUtils.setString(getActivity(), "v1", tmpFile.getAbsolutePath());


                    } catch (IOException io_e) {
                        // TODO: handle error

                    }

                    break;

                case REQUEST_CAPTURE://拍照
                    LogUtils.e(imageUri);
                    Intent intent = new Intent();
                    intent.putExtra(getString(R.string.checked_Item), checkedItem);
                    intent.putExtra(getString(R.string.img_Url), photoFile.getAbsolutePath());
                    intent.putExtra(getString(R.string.scan_Message), scanMessage);

                    switch (checkedItem) {
                        case 2:
                            intent.setClass(getActivity(), RegisterActivity.class);
                            break;
                        case 3:
                            //剪毛
                            intent.setClass(getActivity(), UpLoadActivity.class);

                        case 0:

                            intent.setClass(getActivity(), UpLoadActivity.class);
                            break;

                    }
                    startActivity(intent);
                    break;


            }
        }
    }

    private void register(String isbn) {
        switch (checkedItem) {

            case 2:
                //接羔
                //判断设备是否被绑定
                OkGo.<String>post(Constants.ISDEVICEBINDED)
                        .tag(this)
                        .params("token", mLoginSuccess.getToken())
                        .params("username", mUsername) //用户手机号
                        .params("deviceNO", isbn)
                        .params("ranchID", mLoginSuccess.getRanchID())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String result = response.body().toString();
                                Log.d(TAG1, result);
                                if (result.contains("true")) {

                                    //已绑定
                                    new SweetAlertDialog(getActivity())
                                            .setTitleText("已经登记过该设备!")
                                            .show();

                                } else {
                                    //未绑定
//                                                    openCamera();
                                    Intent intent = new Intent(getActivity()
                                            , RegisterActivity.class);

                                    intent.putExtra(getString(R.string.scan_Message),
                                            scanMessage);

                                    startActivity(intent);


                                }


                            }
                        });

                break;

            case 3:
                //剪毛
                //判断设备是否被绑定
                OkGo.<String>post(Constants.ISDEVICEBINDED)
                        .tag(this)
                        .params("token", mLoginSuccess.getToken())
                        .params("username", mUsername) //用户手机号
                        .params("deviceNO", isbn)
                        .params("ranchID", mLoginSuccess.getRanchID())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {

                                String result = response.body().toString();
                                Log.d(TAG1, result);
                                if (result.contains("true")) {

                                    //已绑定
                                    openCamera();

                                } else {
                                    //未绑定
                                    new SweetAlertDialog(getActivity())
                                            .setTitleText("未登记设备，不能剪毛!")
                                            .show();

                                }


                            }
                        });





                break;

            case 0:
                //拍照


                break;

        }
    }


    private void openCamera() {

        String rootDir = "/Pastureland/photo";
        FileUtils.createOrExistsDir(new File(Environment.getExternalStorageDirectory(), rootDir));
        File file = new File(Environment.getExternalStorageDirectory(), rootDir);
        photoFile = new File(file, UUID.randomUUID().toString() + ".jpg");
        FileUtils.createOrExistsFile(photoFile);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(getActivity(),
                    "com.jinkun_innovation.pastureland.fileProvider",
                    photoFile);//通过FileProvider创建一个content类型的Uri
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
        Intent intent = new Intent(getActivity(), CaptureActivity2.class);
        intent.putExtra(CaptureActivity2.USE_DEFUALT_ISBN_ACTIVITY, true);
        intent.putExtra("inputUnable", 0);
        intent.putExtra("lightUnable", 1);
        intent.putExtra("albumUnable", 1);
        intent.putExtra("cancleUnable", 1);
        startActivityForResult(intent, SCAN_REQUEST_CODE);
    }


}
