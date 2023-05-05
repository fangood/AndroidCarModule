package com.fch.car.usb_module;

import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.fch.car.usb_module.utils.UsbHelper;
import com.fch.car.usb_module.vo.UsbConnection;

public class UsbDetailActivity extends AppCompatActivity {

    private TextView usbDetailInfoText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usb_detail);

        usbDetailInfoText = findViewById(R.id.usbDetailInfo);

        showUsbDeviceInfo();
    }

    private void showUsbDeviceInfo(){
        Intent intent = getIntent();
        if (intent != null){
            UsbDevice usbDevice = intent.getParcelableExtra("usbDevice");
            if (usbDevice != null){
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(" ProductId: ").append(usbDevice.getProductId());
                stringBuilder.append("\n ProductName:  ").append(usbDevice.getProductName());
                stringBuilder.append("\n VendorId:  ").append(usbDevice.getVendorId());
                stringBuilder.append("\n DeviceId:  ").append(usbDevice.getDeviceId());
                stringBuilder.append("\n DeviceProtocol:  ").append( usbDevice.getDeviceProtocol());
                stringBuilder.append("\n Version:  ").append(usbDevice.getVersion());
                usbDetailInfoText.setText(stringBuilder);
            }
        }
    }
}
