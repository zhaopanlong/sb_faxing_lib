/*    */ package com.cgutech.bluetoothstatusapi.status;
/*    */ 
/*    */ import android.bluetooth.BluetoothDevice;
/*    */ import android.bluetooth.BluetoothGatt;
/*    */ import com.cgutech.bluetoothstatusapi.callback.BluetoothMessageCallback;
/*    */ import com.cgutech.bluetoothstatusapi.callback.ConnectCallback;
/*    */ import com.cgutech.bluetoothstatusapi.callback.Receiver;
/*    */ import com.cgutech.bluetoothstatusapi.callback.ScanCallback;
/*    */ import com.cgutech.bluetoothstatusapi.controller.BluetoothStateManager;
/*    */ import com.cgutech.bluetoothstatusapi.exception.ErrorStateException;
/*    */ import com.cgutech.commonBt.log.LogHelperBt;
/*    */ import com.cgutech.commonBt.util.UtilsBt;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class ConnectedState
/*    */   extends BluetoothMessageCallback
/*    */   implements BluetoothState
/*    */ {
/*    */   private ConnectCallback connectCallback;
/*    */   
/*    */   public ConnectedState(ConnectCallback connectCallback) {
/* 26 */     this.connectCallback = connectCallback;
/* 27 */     init();
/*    */   }
/*    */ 
/*    */   
/*    */   public int getStateType() {
/* 32 */     return 2;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getStateName() {
/* 37 */     return "state_connected";
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void init() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void startScan(int timeout, ScanCallback callback) throws ErrorStateException {
/* 47 */     throw new ErrorStateException("");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void stopScan() {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void connect(BluetoothDevice address, int timeout, ConnectCallback callback) throws ErrorStateException {
/* 57 */     throw new ErrorStateException("");
/*    */   }
/*    */ 
/*    */   
/*    */   public void disconnect(ConnectCallback callback) throws ErrorStateException {
/* 62 */     BluetoothStateManager.instance().setmBluetoothState(new DisconnectIngState(callback));
/*    */   }
/*    */ 
/*    */   
/*    */   public void send(byte[] data, int delay, int reCount, int frameReCount, int timeoutTime, int frameTimeout, Receiver callback, ConnectCallback connectCallback) {
/* 67 */     LogHelperBt.LogI(ConnectedState.class.getName(), "send   data:" + UtilsBt.bytesToHexString(data) + ", delay:" + delay + ", reCount:" + reCount + ", frameReCount:" + frameReCount + ", timeoutTime:" + timeoutTime + ", frameTimeout:" + frameTimeout);
/*    */ 
/*    */     
/* 70 */     BluetoothState state = new SendingState(data, delay, reCount, frameReCount, timeoutTime, frameTimeout, callback, connectCallback);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 80 */     BluetoothStateManager.instance().setmBluetoothState(state);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void addGlobleReceive(Receiver receive) {}
/*    */ 
/*    */ 
/*    */   
/*    */   public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
/* 90 */     if (newState == 0) {
/* 91 */       BluetoothStateManager.instance().setmBluetoothState(new AvailableState());
/* 92 */       if (this.connectCallback != null)
/* 93 */         this.connectCallback.onDisconnect(); 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/status/ConnectedState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */