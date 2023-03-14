package com.fch.car.android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fch.car.usb_module.UsbActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void usbTestBtn(View view){
        Intent intent = new Intent(MainActivity.this, UsbActivity.class);
        startActivity(intent);
    }
}