package com.jinkun_innovation.butcher.ui;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.gprinter.aidl.GpService;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.command.GpUtils;
import com.gprinter.command.LabelCommand;
import com.gprinter.io.PortParameters;
import com.gprinter.service.GpPrintService;
import com.jinkun_innovation.butcher.R;
import com.jinkun_innovation.butcher.base.BaseActivity;
import com.jinkun_innovation.butcher.utilcode.SpUtil;
import com.jinkun_innovation.butcher.utilcode.util.ActivityUtils;
import com.jinkun_innovation.butcher.utilcode.util.LogUtils;

import java.util.Vector;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by yangxing on 2018/1/20.
 */

public class PrinterSettingActivity extends BaseActivity {
    private static final int MAIN_QUERY_PRINTER_STATUS = 0xfe;
    @BindView(R.id.tv_printer_ble_name)
    TextView mTvPrinterBleName;
    @BindView(R.id.tv_printer_ble_address)
    TextView mTvPrinterBleAddress;
    @BindView(R.id.tv_printer_num)
    TextView mTvPrinterNum;
    @BindView(R.id.btn_open_port)
    ImageButton mBtnOpenPort;
    private GpService mGpService = null;
    private PrinterServiceConnection conn = null;
    private static final int SCAN_BLUETOOTH = 100;
    private String mUrl;


    @Override
    protected void initToolBar() {
        setIsShowBack(true);
        setTitle("打印");
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_printer_setting;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
        registerReceiver(mBroadcastReceiver, new IntentFilter(GpCom.ACTION_DEVICE_REAL_STATUS));
        connection();

        mUrl =getIntent().getStringExtra("url");
//        mUrl ="http://222.249.165.94:10100//login.do?deviceNo=4A4B2A000A7E&flag=_2018_5";
    }

    private void initView() {
        if (TextUtils.isEmpty(SpUtil.getBleName())) {
            mTvPrinterBleName.setText("未连接过设备");
        } else {
            mTvPrinterBleName.setText("型号：" + SpUtil.getBleName());
            mTvPrinterBleAddress.setText("MAC地址：" + SpUtil.getBleAddress());
        }
    }

    private void connection() {
        conn = new PrinterServiceConnection();
        Intent intent = new Intent(this, GpPrintService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE); // bindService
    }


    private class PrinterServiceConnection implements ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "onServiceDisconnected() called");
            mGpService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mGpService = GpService.Stub.asInterface(service);
            getPrinterStatusClicked();
        }
    }

    /**
     * 获取打印机状态
     */
    public void getPrinterStatusClicked() {
        try {
            mGpService.queryPrinterStatus(0, 2000, MAIN_QUERY_PRINTER_STATUS);
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }


    /**
     * 打印信息
     */
    private void sendLabel() {
        LabelCommand tsc = new LabelCommand();
        tsc.addSize(40, 50); // 设置标签尺寸，按照实际尺寸设置
        tsc.addGap(2); // 设置标签间隙，按照实际尺寸设置，如果为无间隙纸则设置为0
        tsc.addDirection(LabelCommand.DIRECTION.FORWARD, LabelCommand.MIRROR.NORMAL);// 设置打印方向
        tsc.addReference(0, 20);// 设置原点坐标
        tsc.addTear(EscCommand.ENABLE.ON); // 撕纸模式开启
        tsc.addCls();// 清除打印缓冲区
        // 绘制简体中文
        tsc.addText(50, 10, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "扫一扫，品质就知道");

        tsc.addQRCode(50, 70, LabelCommand.EEC.LEVEL_H, 6, LabelCommand.ROTATION.ROTATION_0, mUrl);
        tsc.addText(110, 310, LabelCommand.FONTTYPE.SIMPLIFIED_CHINESE, LabelCommand.ROTATION.ROTATION_0, LabelCommand.FONTMUL.MUL_1, LabelCommand.FONTMUL.MUL_1,
                "金坤科创");
        tsc.addPrint(Integer.valueOf(mTvPrinterNum.getText().toString()), 1); // 打印标签
        tsc.addSound(2, 100); // 打印标签后 蜂鸣器响
        tsc.addCashdrwer(LabelCommand.FOOT.F5, 255, 255);
        Vector<Byte> datas = tsc.getCommand(); // 发送数据
        byte[] bytes = GpUtils.ByteTo_byte(datas);
        String str = Base64.encodeToString(bytes, Base64.DEFAULT);
        int rel;
        try {
            rel = mGpService.sendLabelCommand(0, str);
            GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
            if (r != GpCom.ERROR_CODE.SUCCESS) {
                Toast.makeText(getApplicationContext(), GpCom.getErrorText(r), Toast.LENGTH_SHORT).show();
            }
        } catch (RemoteException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            // GpCom.ACTION_DEVICE_REAL_STATUS 为广播的IntentFilter
            if (action.equals(GpCom.ACTION_DEVICE_REAL_STATUS)) {

                // 业务逻辑的请求码，对应哪里查询做什么操作
                int requestCode = intent.getIntExtra(GpCom.EXTRA_PRINTER_REQUEST_CODE, -1);
                // 判断请求码，是则进行业务操作
                if (requestCode == MAIN_QUERY_PRINTER_STATUS) {

                    int status = intent.getIntExtra(GpCom.EXTRA_PRINTER_REAL_STATUS, 16);
                    String str;
                    if (status == GpCom.STATE_NO_ERR) {
                        str = "打印机正常";
                        mBtnOpenPort.setBackground(getContext().getResources().getDrawable(R.drawable.printer_linked, null));
                    } else {
                        str = "打印机 ";
                        if ((byte) (status & GpCom.STATE_OFFLINE) > 0) {
                            str += "脱机";
                        }
                        if ((byte) (status & GpCom.STATE_PAPER_ERR) > 0) {
                            str += "缺纸";
                        }
                        if ((byte) (status & GpCom.STATE_COVER_OPEN) > 0) {
                            str += "打印机开盖";
                        }
                        if ((byte) (status & GpCom.STATE_ERR_OCCURS) > 0) {
                            str += "打印机出错";
                        }
                        if ((byte) (status & GpCom.STATE_TIMES_OUT) > 0) {
                            str += "查询超时";
                        }
                    }

                    Toast.makeText(getApplicationContext(), " 状态：" + str, Toast.LENGTH_SHORT)
                            .show();
                }
            }
        }
    };


    @OnClick({R.id.btn_printer_scan, R.id.btn_open_port, R.id.tv_printer_num, R.id.btn_printer,R.id.btn_show_qr})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_printer_scan:
                Intent intent = new Intent(PrinterSettingActivity.this, ScanBluetoothActivity.class);
                startActivityForResult(intent, SCAN_BLUETOOTH);
                break;
            case R.id.btn_open_port:
                connectOrDisConnectToDevice(PortParameters.BLUETOOTH, SpUtil.getBleAddress());
                break;
            case R.id.tv_printer_num:

                new MaterialDialog.Builder(PrinterSettingActivity.this)
                        .title("打印数量")
                        .items(new String[]{"1", "2", "3", "4", "5", "6", "7", "8"})
                        .itemsCallback(new MaterialDialog.ListCallback() {//选中监听，同时dialog消失
                            @Override
                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
                                mTvPrinterNum.setText(text);
                                Toast.makeText(PrinterSettingActivity.this, text, Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
                break;
            case R.id.btn_printer:
                sendLabel();
                break;
            case R.id.btn_show_qr:
                Intent intent1 = new Intent();
                intent1.setClass(PrinterSettingActivity.this,QrActicity.class);
                intent1.putExtra("url",mUrl);
                ActivityUtils.startActivity(intent1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SCAN_BLUETOOTH) {
            if (data!=null) {
                Bundle bundle = data.getExtras();
                int param = bundle.getInt(GpPrintService.PORT_TYPE);
                String address = bundle.getString(GpPrintService.BLUETOOT_ADDR);
                String name = bundle.getString("name");
                LogUtils.e(name, param, address);
                mTvPrinterBleName.setText(name);
                mTvPrinterBleAddress.setText(address);
                SpUtil.saveBleName(name);
                SpUtil.saveBleAddress(address);
                connectOrDisConnectToDevice(param, address);
            }


        }
    }

    /**
     * 打开通信接口
     *
     * @param param
     * @param address
     */
    private void connectOrDisConnectToDevice(int param, String address) {
        int rel = 0;
        try {
            rel = mGpService.openPort(0, param, address, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
            LogUtils.e(e.getMessage());
        }
        GpCom.ERROR_CODE r = GpCom.ERROR_CODE.values()[rel];
        LogUtils.e("result :" + String.valueOf(r));
        if (String.valueOf(r).equals("SUCCESS")) {
            mBtnOpenPort.setBackground(getContext().getResources().getDrawable(R.drawable.printer_linked, null));
        } else {
            getPrinterStatusClicked();
        }
        if (r != GpCom.ERROR_CODE.SUCCESS) {
            if (r == GpCom.ERROR_CODE.DEVICE_ALREADY_OPEN) {
                LogUtils.e(GpCom.getErrorText(r));
            } else {
                LogUtils.e(GpCom.getErrorText(r));
            }
        } else {
            LogUtils.e("连接成功");
//            try {
//                Thread.sleep(500);
//                getPrinterStatusClicked();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        if (conn != null) {
            unbindService(conn); // unBindService
        }

    }
}
