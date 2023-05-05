package com.fch.car.usb_module.vo;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;

public class UsbConnection {
    private UsbDevice usbDevice;
    private UsbInterface usbInterface;
    private UsbEndpoint intEndpoint;
    private UsbEndpoint outEndpoint;
    private UsbDeviceConnection usbDeviceConnection;

    public UsbConnection(UsbDevice usbDevice, UsbInterface usbInterface, UsbEndpoint intEndpoint, UsbEndpoint outEndpoint) {
        this.usbDevice = usbDevice;
        this.usbInterface = usbInterface;
        this.intEndpoint = intEndpoint;
        this.outEndpoint = outEndpoint;
    }

    public void connect(UsbManager usbManager) {
        usbDeviceConnection = usbManager.openDevice(usbDevice);
        //锁定此接口,同时只能一处使用
        usbDeviceConnection.claimInterface(usbInterface, true);
    }

    public void close() {
        usbDeviceConnection.releaseInterface(usbInterface);
        usbDeviceConnection.close();
    }

    public int getMaxLun() {
        //接收响应数据
        byte[] buffer = new byte[1];
        usbDeviceConnection.controlTransfer(0xA1, 0xFE, 0, usbInterface.getId(), buffer, 1, 5000);
        //转成int
        return (buffer[0] & 0xFF) + 1;
    }

}
