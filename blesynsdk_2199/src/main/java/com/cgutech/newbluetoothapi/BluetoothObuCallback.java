package com.cgutech.newbluetoothapi;

import android.bluetooth.BluetoothDevice;

public interface BluetoothObuCallback {
  void onConnect();
  
  void onConnectTimeout();
  
  void onConnectError(String paramString);
  
  void onDisconnect();
  
  void onReceiveObuCmd(String paramString1, String paramString2);
  
  void onScanSuccess(BluetoothDevice paramBluetoothDevice, int paramInt);
  
  void onScanTimeout();
  
  void onSendTimeout(String paramString1, String paramString2);
  
  void onSendError(String paramString);
}


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/newbluetoothapi/BluetoothObuCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */