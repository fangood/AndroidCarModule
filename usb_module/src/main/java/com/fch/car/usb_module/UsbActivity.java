package com.fch.car.usb_module;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fch.car.usb_module.adapter.UsbListAdapter;
import com.fch.car.usb_module.utils.UsbHelper;
import com.fch.car.usb_module.vo.UsbConnection;

import java.util.ArrayList;
import java.util.List;

public class UsbActivity extends AppCompatActivity implements UsbListAdapter.OnItemClickListener {

    private final String TAG = UsbActivity.class.getSimpleName();
    private List<UsbDevice> devices;
    private RecyclerView usbRecyclerView;
    private UsbListAdapter usbListAdapter;
    private UsbManager usbManager;
    private UsbReceiver usbReceiver;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        UsbDevice usbDevice = getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE);
        UsbHelper.findDevices(usbManager, devices);
        usbListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb);
        usbRecyclerView = findViewById(R.id.usbRecyclerView);
        registerReceiver();

        devices = new ArrayList<>();

        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        UsbHelper.findDevices(usbManager, devices);

        usbRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usbListAdapter = new UsbListAdapter(devices);
        usbRecyclerView.setAdapter(usbListAdapter);
        usbListAdapter.setOnItemClickListener(this);

//        UsbDevice usbDevice = getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceiver();

    }


    @Override
    public void onItemClick(View view, int position) {
        UsbDevice usbDevice = devices.get(position);
        Log.i(TAG, "onItemClick " + position);
        if (!usbManager.hasPermission(usbDevice)) {//是否有权限
            //申请权限，系统弹出对话框，用户选择后发出广播，触发注册的广播接收者
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 0,
                    new Intent(UsbReceiver.ACTION_USB_PERMISSION), 0);
            usbManager.requestPermission(usbDevice, pendingIntent);
        } else {
            intentDetailActivity(usbDevice);
            showUsbDevice(usbDevice);
        }

    }

    private void registerReceiver() {
        usbReceiver = new UsbReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UsbReceiver.ACTION_USB_PERMISSION);
        registerReceiver(usbReceiver, intentFilter);
    }

    private void unRegisterReceiver() {
        unregisterReceiver(usbReceiver);
    }

    private void intentDetailActivity(UsbDevice usbDevice) {
        Intent intent = new Intent(this, UsbDetailActivity.class);
        intent.putExtra("usbDevice", usbDevice);
        startActivity(intent);
    }

    private class UsbReceiver extends BroadcastReceiver {

        public static final String ACTION_USB_PERMISSION = "com.fch.car.USB_PERMISSION";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.i(TAG, "UsbReceiver " + action);
            if (TextUtils.equals(action, ACTION_USB_PERMISSION)) {
                UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    Log.i(TAG, "获得usb权限");
                    intentDetailActivity(usbDevice);
                } else {
                    Log.i(TAG, "拒绝usb权限");
                }
            } else if (TextUtils.equals(action, UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                //Usb设备断开连接
                UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (usbDevice != null) {// && 当前通信设备
                    UsbConnection usbConnection = UsbHelper.isUsbMassStorage(usbDevice);
                    if (usbConnection != null) {
                        usbConnection.close();
                        Log.i(TAG, "usbDevice close");
                    }
                }

            }

        }
    }

    private void showUsbDevice(UsbDevice usbDevice) {
        UsbConnection usbConnection = UsbHelper.isUsbMassStorage(usbDevice);
        if (usbConnection != null) {
            usbConnection.connect(usbManager);
            int maxLun = usbConnection.getMaxLun();
            Log.i(TAG, "showUsbDevice maxLun " + maxLun);
        }
    }
}
