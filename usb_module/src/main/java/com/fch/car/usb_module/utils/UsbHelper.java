package com.fch.car.usb_module.utils;

import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.fch.car.usb_module.UsbActivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UsbHelper {
    private final static String Tag = UsbHelper.class.getSimpleName();

    public static void findDevices(UsbManager usbManager, List<UsbDevice> usbDevices){
        if (usbDevices == null){
            usbDevices = new ArrayList<>();
        }
        HashMap<String,UsbDevice> hashMap =  usbManager.getDeviceList();
        Collection<UsbDevice> values =  hashMap.values();
        Iterator<UsbDevice> iterator = values.iterator();
        Log.i(Tag," iterator "+ iterator);
        while (iterator.hasNext()){
            UsbDevice usbDevice = iterator.next();
            Log.i(Tag," device "+ usbDevice);
            usbDevices.add(usbDevice);
        }

    }
}
