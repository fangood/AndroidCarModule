package com.fch.car.usb_module.adapter;

import android.hardware.usb.UsbDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.fch.car.usb_module.R;

import java.util.List;

public class UsbListAdapter extends RecyclerView.Adapter<UsbListAdapter.MyViewHolder> {

    private List<UsbDevice> mList;
    public UsbListAdapter( List<UsbDevice> list) {
        this.mList = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rv_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        UsbDevice usbDevice = mList.get(position);
        if (usbDevice != null){
            holder.vendorIdTextView.setText(String.format("verdorId: %d", usbDevice.getVendorId()));
            holder.productIdTextView.setText(String.format("productId: %d", usbDevice.getProductId()));
            holder.nameTextView.setText(String.format("deviceName: %s", usbDevice.getDeviceName()));
            holder.manufactureTextView.setText(String.format("制造商: %s", usbDevice.getManufacturerName()));
            holder.productNameTextView.setText(String.format("产品名: %s", usbDevice.getProductName()));
        }

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
         TextView vendorIdTextView;
         TextView productIdTextView;
         TextView nameTextView;
         TextView manufactureTextView;
         TextView productNameTextView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            vendorIdTextView = itemView.findViewById(R.id.vendorIdTextView);
            productIdTextView = itemView.findViewById(R.id.productIdTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            manufactureTextView = itemView.findViewById(R.id.manufactureTextView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }
    private static OnItemClickListener mOnItemClickListener;

    // 6.设置点击事件对象
    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
