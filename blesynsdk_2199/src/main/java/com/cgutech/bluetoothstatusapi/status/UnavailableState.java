/*    */ package com.cgutech.bluetoothstatusapi.status;
/*    */ 
/*    */ import android.bluetooth.BluetoothDevice;
/*    */ import com.cgutech.bluetoothstatusapi.callback.BluetoothMessageCallback;
/*    */ import com.cgutech.bluetoothstatusapi.callback.ConnectCallback;
/*    */ import com.cgutech.bluetoothstatusapi.callback.Receiver;
/*    */ import com.cgutech.bluetoothstatusapi.callback.ScanCallback;
/*    */ import com.cgutech.bluetoothstatusapi.exception.ErrorStateException;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class UnavailableState
/*    */   extends BluetoothMessageCallback
/*    */   implements BluetoothState
/*    */ {
/*    */   private static final int stateTag = 0;
/*    */   
/*    */   public int getStateType() {
/* 20 */     return 0;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getStateName() {
/* 25 */     return "state_unavailable";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void init() throws ErrorStateException {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void startScan(int timeout, ScanCallback callback) throws ErrorStateException {
/* 35 */     throw new ErrorStateException("系统蓝牙关闭未打开");
/*    */   }
/*    */ 
/*    */   
/*    */   public void stopScan() throws ErrorStateException {
/* 40 */     throw new ErrorStateException("系统蓝牙关闭未打开");
/*    */   }
/*    */ 
/*    */   
/*    */   public void connect(BluetoothDevice address, int timeout, ConnectCallback callback) throws ErrorStateException {
/* 45 */     throw new ErrorStateException("系统蓝牙关闭未打开");
/*    */   }
/*    */ 
/*    */   
/*    */   public void disconnect(ConnectCallback callback) throws ErrorStateException {
/* 50 */     throw new ErrorStateException("系统蓝牙关闭未打开");
/*    */   }
/*    */ 
/*    */   
/*    */   public void send(byte[] data, int delay, int reCount, int frameReCount, int timeoutTime, int frameTimeout, Receiver callback, ConnectCallback connectCallback) throws ErrorStateException {
/* 55 */     throw new ErrorStateException("系统蓝牙关闭未打开");
/*    */   }
/*    */   
/*    */   public void addGlobleReceive(Receiver receive) throws ErrorStateException {}
/*    */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/status/UnavailableState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */