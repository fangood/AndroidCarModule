package com.fch.car.usb_module.utils;

import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.fch.car.usb_module.UsbActivity;
import com.fch.car.usb_module.vo.UsbConnection;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UsbHelper {
    private final static String Tag = UsbHelper.class.getSimpleName();

    /**
     * 查找设备
     * @param usbManager
     * @param usbDevices
     */
    public static void findDevices(UsbManager usbManager, List<UsbDevice> usbDevices) {
        if (usbDevices == null) {
            usbDevices = new ArrayList<>();
        }
        HashMap<String, UsbDevice> hashMap = usbManager.getDeviceList();
        Collection<UsbDevice> values = hashMap.values();
        Iterator<UsbDevice> iterator = values.iterator();
        Log.i(Tag, " iterator " + iterator);
        while (iterator.hasNext()) {
            UsbDevice usbDevice = iterator.next();
            Log.i(Tag, " device " + usbDevice);
            usbDevices.add(usbDevice);
        }

    }

    /**
     * 判断是否是大容量存储设备
     * @param usbDevice
     *  1、类为：USB_CLASS_MASS_STORAGE 0x08
     *  2、子类为：0x06 （大部分U盘使用）
     *  3、协议为：0x50 (Bulk-Only协议 批量传输)
     */
    public static UsbConnection isUsbMassStorage(UsbDevice usbDevice){
        for (int i = 0;i< usbDevice.getInterfaceCount();i++){
            UsbInterface usbInterface = usbDevice.getInterface(i);
            if (usbInterface.getInterfaceClass() == UsbConstants.USB_CLASS_MASS_STORAGE
                    && usbInterface.getInterfaceSubclass() == 0x06
                    && usbInterface.getInterfaceProtocol() == 0x50
            ){
                UsbEndpoint outEndpoint = null,inEndpoint = null;
                for (int j = 0; j<usbInterface.getEndpointCount();j++){
                    UsbEndpoint endpoint = usbInterface.getEndpoint(j);
                    if (endpoint.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK){//USB块传输模式通道
                        if (endpoint.getDirection() == UsbConstants.USB_DIR_OUT){
                            outEndpoint = endpoint;
                        }else{
                            inEndpoint = endpoint;
                        }
                       return new UsbConnection(usbDevice,usbInterface,inEndpoint,outEndpoint);
                    }
                }

            }
        }
        return null;
    }

}
