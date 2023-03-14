package com.fch.car.usb_module;

import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.fch.car.usb_module.adapter.UsbListAdapter;
import com.fch.car.usb_module.utils.UsbHelper;

import java.util.ArrayList;
import java.util.List;

public class UsbActivity extends AppCompatActivity {

    private List<UsbDevice> devices;
    private RecyclerView usbRecyclerView;
    private UsbListAdapter usbListAdapter;
    private UsbManager usbManager;

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
//        UsbDevice usbDevice = getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE);
        UsbHelper.findDevices(usbManager,devices);
        usbListAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb);
        usbRecyclerView = findViewById(R.id.usbRecyclerView);

        devices = new ArrayList<>();

        usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        UsbHelper.findDevices(usbManager,devices);

        usbRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        usbListAdapter = new UsbListAdapter(devices);
        usbRecyclerView.setAdapter(usbListAdapter);

//        UsbDevice usbDevice = getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
