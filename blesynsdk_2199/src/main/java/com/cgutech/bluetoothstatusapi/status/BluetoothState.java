package com.cgutech.bluetoothstatusapi.status;

import android.bluetooth.BluetoothDevice;
import com.cgutech.bluetoothstatusapi.callback.ConnectCallback;
import com.cgutech.bluetoothstatusapi.callback.Receiver;
import com.cgutech.bluetoothstatusapi.callback.ScanCallback;
import com.cgutech.bluetoothstatusapi.exception.ErrorStateException;

public interface BluetoothState {
  public static final int STATE_UNAVAILABLE = 0;
  
  public static final int STATE_AVAILABLE = 1;
  
  public static final int STATE_CONNECTED = 2;
  
  public static final int STATE_CONNECTING = 3;
  
  public static final int STATE_DISCONNECTING = 4;
  
  public static final int STATE_RECEIVE = 5;
  
  public static final int STATE_SCANING = 6;
  
  public static final int STATE_SENDING = 7;
  
  public static final int STATE_FRAME_SENDING = 8;
  
  public static final int STATE_CHECK_OBU_VERSION = 10;
  
  int getStateType();
  
  String getStateName();
  
  void init() throws ErrorStateException;
  
  void startScan(int paramInt, ScanCallback paramScanCallback) throws ErrorStateException;
  
  void stopScan() throws ErrorStateException;
  
  void connect(BluetoothDevice paramBluetoothDevice, int paramInt, ConnectCallback paramConnectCallback) throws ErrorStateException;
  
  void disconnect(ConnectCallback paramConnectCallback) throws ErrorStateException;
  
  void send(byte[] paramArrayOfbyte, int paramInt1, int paramInt2, int paramInt3, int paramInt4, int paramInt5, Receiver paramReceiver, ConnectCallback paramConnectCallback) throws ErrorStateException;
  
  void addGlobleReceive(Receiver paramReceiver) throws ErrorStateException;
}


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/status/BluetoothState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */