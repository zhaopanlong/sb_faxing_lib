package com.cgutech.bluetoothstatusapi.callback;

public interface ConnectCallback {
  void onConnect();
  
  void onDisconnect();
  
  void onTimeout();
  
  void onError(String paramString);
}


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/callback/ConnectCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */