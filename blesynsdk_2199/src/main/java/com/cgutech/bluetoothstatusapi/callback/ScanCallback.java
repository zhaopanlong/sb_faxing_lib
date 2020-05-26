package com.cgutech.bluetoothstatusapi.callback;

import android.bluetooth.BluetoothDevice;

public interface ScanCallback {
  void onScan(BluetoothDevice paramBluetoothDevice, int paramInt);
  
  void onScanTimeout();
}


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/callback/ScanCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */