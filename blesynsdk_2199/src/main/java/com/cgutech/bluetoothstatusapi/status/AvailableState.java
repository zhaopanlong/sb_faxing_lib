/*    */ package com.cgutech.bluetoothstatusapi.status;
/*    */ 
/*    */ import android.bluetooth.BluetoothDevice;
/*    */ import android.content.Intent;
/*    */ import com.cgutech.bluetoothstatusapi.callback.BluetoothMessageCallback;
/*    */ import com.cgutech.bluetoothstatusapi.callback.ConnectCallback;
/*    */ import com.cgutech.bluetoothstatusapi.callback.Receiver;
/*    */ import com.cgutech.bluetoothstatusapi.callback.ScanCallback;
/*    */ import com.cgutech.bluetoothstatusapi.controller.BluetoothStateManager;
/*    */ import com.cgutech.bluetoothstatusapi.exception.ErrorStateException;
/*    */ import com.cgutech.bluetoothstatusapi.service.BluetoothStateListener;
/*    */ import com.cgutech.commonBt.log.LogHelperBt;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AvailableState
/*    */   extends BluetoothMessageCallback
/*    */   implements BluetoothState
/*    */ {
/*    */   public int getStateType() {
/* 24 */     return 1;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getStateName() {
/* 29 */     return "state_available";
/*    */   }
/*    */ 
/*    */   
/*    */   public void init() throws ErrorStateException {
/* 34 */     Intent intent = new Intent(BluetoothStateManager.instance().getContext(), BluetoothStateListener.class);
/* 35 */     BluetoothStateManager.instance().getContext().startService(intent);
/* 36 */     if (!BluetoothStateManager.instance().getContext().getPackageManager().hasSystemFeature("android.hardware.bluetooth_le")) {
/* 37 */       throw new ErrorStateException("蓝牙不支持BLE，无法初始化");
/*    */     }
/*    */     
/* 40 */     if (!BluetoothStateManager.instance().getmBluetoothAdapter().isEnabled()) {
/* 41 */       throw new ErrorStateException("蓝牙未打开，无法初始化");
/*    */     }
/*    */   }
/*    */ 
/*    */   
/*    */   public void startScan(int timeout, ScanCallback callback) {
/* 47 */     BluetoothStateManager.instance().setNeedFrameReply(true);
/* 48 */     BluetoothStateManager.instance().setmBluetoothState(new ScaningState(timeout, callback));
/*    */   }
/*    */ 
/*    */   
/*    */   public void stopScan() throws ErrorStateException {
/* 53 */     throw new ErrorStateException("未扫描");
/*    */   }
/*    */ 
/*    */   
/*    */   public void connect(BluetoothDevice address, int timeout, ConnectCallback callback) throws ErrorStateException {
/* 58 */     if (address != null) {
/* 59 */       LogHelperBt.LogI(getStateName(), "开始连接蓝牙");
/* 60 */       BluetoothStateManager.instance().setmBluetoothDevice(address);
/* 61 */       BluetoothStateManager.instance().setmBluetoothState(new ConnectingState(callback, timeout));
/*    */     } else {
/* 63 */       LogHelperBt.LogE(getStateName(), "连接蓝牙设备时，device参数为null");
/* 64 */       throw new NullPointerException("连接地址为空");
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void disconnect(ConnectCallback callback) throws ErrorStateException {
/* 70 */     throw new ErrorStateException("蓝牙未扫描");
/*    */   }
/*    */ 
/*    */   
/*    */   public void send(byte[] data, int delay, int reCount, int frameReCount, int timeoutTime, int frameTimeout, Receiver callback, ConnectCallback connectCallback) throws ErrorStateException {
/* 75 */     throw new ErrorStateException("蓝牙未连接");
/*    */   }
/*    */ 
/*    */   
/*    */   public void addGlobleReceive(Receiver receive) throws ErrorStateException {
/* 80 */     throw new ErrorStateException("蓝牙未扫描");
/*    */   }
/*    */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/status/AvailableState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */