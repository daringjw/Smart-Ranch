package com.jinkun_innovation.butcher.ui;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.gprinter.io.PortParameters;
import com.gprinter.service.GpPrintService;
import com.jinkun_innovation.butcher.R;
import com.jinkun_innovation.butcher.base.BaseActivity;
import com.jinkun_innovation.butcher.utilcode.AppManager;
import com.jinkun_innovation.butcher.utilcode.util.LogUtils;
import com.jinkun_innovation.butcher.view.recyclerview.CommonAdapter;
import com.jinkun_innovation.butcher.view.recyclerview.MultiItemTypeAdapter;
import com.jinkun_innovation.butcher.view.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by yangxing on 2018/1/22.
 */

public class ScanBluetoothActivity extends BaseActivity {
    public static final int REQUEST_ENABLE_BT = 2;
    @BindView(R.id.tv_scan)
    TextView mTvScan;
    @BindView(R.id.pb_scan)
    ProgressBar mPbScan;
    private BluetoothAdapter mBluetoothAdapter;

    private List<BluetoothDevice> mBluetoothDevices = new ArrayList<>();
    private CommonAdapter<BluetoothDevice> mBluetoothDeviceCommonAdapter;

    @BindView(R.id.recycle_scan_bluetooth)
    RecyclerView mRecycleScanBluetooth;

    @Override
    protected void initToolBar() {
        setIsShowBack(true);
        setTitle("扫描蓝牙设备");

    }

    @Override
    protected int getContentView() {
        return R.layout.activity_scan_bluetooth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        registerReceiver();
        getBluetoothDevice();
        initRecycle();
    }

    private void initRecycle() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecycleScanBluetooth.setLayoutManager(linearLayoutManager);

        mBluetoothDeviceCommonAdapter = new CommonAdapter<BluetoothDevice>(this, R.layout.item_bluetooth, mBluetoothDevices) {
            @Override
            protected void convert(ViewHolder holder, BluetoothDevice bluetoothDevice, int position) {
                holder.setText(R.id.tv_ble_name, bluetoothDevice.getName());
                holder.setText(R.id.tv_ble_adress, bluetoothDevice.getAddress());
            }
        };
        mRecycleScanBluetooth.setAdapter(mBluetoothDeviceCommonAdapter);


        mBluetoothDeviceCommonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                Intent intent = new Intent(ScanBluetoothActivity.this, PrinterSettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", mBluetoothDevices.get(position).getName());
                bundle.putInt(GpPrintService.PORT_TYPE, PortParameters.BLUETOOTH);
                bundle.putString(GpPrintService.BLUETOOT_ADDR, mBluetoothDevices.get(position).getAddress());
                intent.putExtras(bundle);
                setResult(Activity.RESULT_OK, intent);
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                }
                AppManager.getAppManager().finishActivity();
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    private void registerReceiver() {
        // Register for broadcasts when a device is discovered
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        this.registerReceiver(mFindBlueToothReceiver, filter);
        // Register for broadcasts when discovery has finished
        filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        this.registerReceiver(mFindBlueToothReceiver, filter);
    }


    public void getBluetoothDevice() {
        mPbScan.setVisibility(View.VISIBLE);
        mTvScan.setText("正在扫描");
        // Get local Bluetooth adapter
        mBluetoothAdapter = BluetoothAdapter
                .getDefaultAdapter();
        // If the adapter is null, then Bluetooth is not supported
        if (mBluetoothAdapter == null) {
            LogUtils.e("Bluetooth is not supported by the device");
        } else {
            // If BT is not on, request that it be enabled.
            // setupChat() will then be called during onActivityResult
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableIntent,
                        REQUEST_ENABLE_BT);
            } else {
                mBluetoothAdapter.startDiscovery();
            }
        }
    }

    private final BroadcastReceiver mFindBlueToothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // If it's already paired, skip it, because it's been listed
                // already
                LogUtils.e(device.getName(), device.getAddress());
                if (device.getName() != null && device.getName().contains("Printer")) {
                    mBluetoothDevices.add(device);
                }
                mBluetoothDeviceCommonAdapter.notifyItemChanged(mBluetoothDevices.size() - 1);
                // When discovery is finished, change the Activity title
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                LogUtils.e("扫描完毕");
                mPbScan.setVisibility(View.GONE);
                mTvScan.setText("扫描完成");
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
//    Log.d(DEBUG_TAG, "requestCode" + requestCode + "=>" + "resultCode"
//        + resultCode);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                // bluetooth is opened
                // select bluetooth device fome list
                mBluetoothAdapter.startDiscovery();
            } else {
                LogUtils.e("ble not open");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mFindBlueToothReceiver);
    }
}
