package com.jinkun_innovation.butcher.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.jinkun_innovation.butcher.R;
import com.jinkun_innovation.butcher.base.BaseActivity;

import java.util.Hashtable;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yangxing on 2018/1/27.
 */

public class QrActicity extends BaseActivity {

    @BindView(R.id.img_qr)
    ImageView mImgQr;
    private int QR_WIDTH = 300;
    private int QR_HEIGHT = 300;

    @Override
    protected void initToolBar() {
        setTitle("二维码");
        setIsShowBack(true);

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_qr;
    }


    public void createQRImage(String url) {
        try {
            if (url == null || "".equals(url) || url.length() < 1) {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.RGB_565);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            mImgQr.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        createQRImage(getIntent().getStringExtra("url"));
    }
}
