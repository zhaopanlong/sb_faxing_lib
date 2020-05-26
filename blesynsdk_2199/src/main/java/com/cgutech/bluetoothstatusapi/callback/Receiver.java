package com.cgutech.bluetoothstatusapi.callback;

public interface Receiver {
  boolean onRecv(String paramString, byte[] paramArrayOfbyte);
  
  boolean onSendTimeout(byte[] paramArrayOfbyte);
}


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/callback/Receiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */