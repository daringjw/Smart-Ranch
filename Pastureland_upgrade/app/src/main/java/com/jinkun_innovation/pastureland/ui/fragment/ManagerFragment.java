package com.jinkun_innovation.pastureland.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.zxing.client.android.CaptureActivity2;
import com.jinkun_innovation.pastureland.R;
import com.jinkun_innovation.pastureland.ui.LianJiangPasturelandActivity;
import com.jinkun_innovation.pastureland.ui.UpLoadActivity;
import com.jinkun_innovation.pastureland.ui.UploadCheckedActivity;
import com.jinkun_innovation.pastureland.utilcode.util.FileUtils;
import com.jinkun_innovation.pastureland.utilcode.util.LogUtils;

import java.io.File;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Guan on 2018/3/15.
 */

public class ManagerFragment extends Fragment {


    @BindView(R.id.btnJieGao)
    Button mBtnJieGao;
    @BindView(R.id.btnHaircuts)
    Button mBtnHaircuts;
    @BindView(R.id.btnPhoto)
    Button mBtnPhoto;
    @BindView(R.id.btnMechanicalTools)
    Button mBtnMechanicalTools;
    @BindView(R.id.btnGrass)
    Button mBtnGrass;
    @BindView(R.id.btnLianJiangPastureland)
    Button mBtnLianJiangPastureland;
    Unbinder unbinder;

    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 3;//权限请求
    private static final int SCAN_REQUEST_CODE = 100;

    private String imagePath;
    private File photoFile;
    private Uri imageUri;//原图保存地址
    private static final int REQUEST_CAPTURE = 2;  //拍照

    private int checkedItem = 0;
    private String scanMessage;

    private SliderLayout mSliderShow;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        View view = View.inflate(getActivity(), R.layout.fragment_manager, null);

        unbinder = ButterKnife.bind(this, view);



        mSliderShow =  view.findViewById(R.id.slider);
        TextSliderView textSliderView = new TextSliderView(getActivity());

        textSliderView
                .description("示例图片1")
                .setScaleType(BaseSliderView.ScaleType.Fit)//图片缩放类型
                .image("http://p2.so.qhimgs1.com/t0130237d0b387f9c1e.jpg")
        ;

        TextSliderView textSliderView1 = new TextSliderView(getActivity());
        textSliderView1
                .description("牧场2")
                .image("http://pic1.sc.chinaz.com/files/pic/pic9/201803/bpic5936.jpg");

        TextSliderView textSliderView2 = new TextSliderView(getActivity());
        textSliderView2
                .description("示例图片3")
                .image("http://pics.sc.chinaz.com/files/pic/pic9/201802/zzpic10394.jpg");


        mSliderShow.addSlider(textSliderView);
        mSliderShow.addSlider(textSliderView1);
        mSliderShow.addSlider(textSliderView2);

        mSliderShow.setPresetTransformer(SliderLayout.Transformer.RotateUp);

        return view;

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.btnJieGao, R.id.btnHaircuts, R.id.btnPhoto,
            R.id.btnMechanicalTools, R.id.btnGrass, R.id.btnLianJiangPastureland})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnJieGao:
                checkedItem = 2;

                startScanActivity();

                break;
            case R.id.btnHaircuts:
                checkedItem = 3;
                startScanActivity();
                break;
            case R.id.btnPhoto:

                checkedItem = 0;
                openCamera();

                break;
            case R.id.btnMechanicalTools:
                break;
            case R.id.btnGrass:
                break;
            case R.id.btnLianJiangPastureland:

                startActivity(new Intent(getActivity(),LianJiangPasturelandActivity.class));
                break;
        }
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
                        scanMessage = isbn;
                        Toast.makeText(getActivity(), "解析到的内容为" + isbn, Toast.LENGTH_LONG).show();
                        openCamera();
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
                            intent.setClass(getActivity(), UploadCheckedActivity.class);
                            break;
                        case 3:

                        case 0:
                            intent.setClass(getActivity(), UpLoadActivity.class);
                            break;

                    }
                    startActivity(intent);
                    break;


            }
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
