/*     */ package com.cgutech.bluetoothstatusapi.status;
/*     */ 
/*     */ import android.bluetooth.BluetoothDevice;
/*     */ import android.bluetooth.BluetoothGatt;
/*     */ import android.bluetooth.BluetoothGattCharacteristic;
/*     */ import com.cgutech.bluetoothstatusapi.bean.SendDataBean;
/*     */ import com.cgutech.bluetoothstatusapi.callback.BluetoothGattCallbackImpl;
/*     */ import com.cgutech.bluetoothstatusapi.callback.BluetoothMessageCallback;
/*     */ import com.cgutech.bluetoothstatusapi.callback.ConnectCallback;
/*     */ import com.cgutech.bluetoothstatusapi.callback.Receiver;
/*     */ import com.cgutech.bluetoothstatusapi.callback.ScanCallback;
/*     */ import com.cgutech.bluetoothstatusapi.controller.BluetoothStateManager;
/*     */ import com.cgutech.bluetoothstatusapi.exception.ErrorStateException;
/*     */ import com.cgutech.commonBt.log.LogHelperBt;
/*     */ import com.cgutech.commonBt.util.UtilsBt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ReceivingState
/*     */   extends BluetoothMessageCallback
/*     */   implements BluetoothState
/*     */ {
/*     */   private static final int stateTag = 5;
/*     */   private Receiver receiver;
/*     */   private int reCount;
/*     */   private ConnectCallback connectCallback;
/*     */   private byte[] data;
/*     */   private SendDataBean sendData;
/*     */   
/*  34 */   private Runnable runnable = new Runnable()
/*     */     {
/*     */       public void run() {
/*  37 */         if (ReceivingState.this.reCount > 0) {
/*  38 */           BluetoothStateManager.instance().setmBluetoothState(new SendingState(ReceivingState.this
/*  39 */                 .sendData.getData(), ReceivingState.this
/*  40 */                 .sendData.getDelay(), ReceivingState.this
/*  41 */                 .sendData.getReCount(), ReceivingState.this
/*  42 */                 .sendData.getFrameReCount(), ReceivingState.this
/*  43 */                 .sendData.getTimeout(), ReceivingState.this
/*  44 */                 .sendData.getFrameTimeout(), ReceivingState.this
/*  45 */                 .sendData.getReceiver(), ReceivingState.this
/*  46 */                 .sendData.getConnectCallback()));
/*     */         
/*     */         }
/*  49 */         else if (BluetoothStateManager.instance().getmBluetoothState().getStateType() == 5) {
/*     */           
/*  51 */           BluetoothStateManager.instance().setmBluetoothState(new ConnectedState(ReceivingState.this.connectCallback));
/*  52 */           if (ReceivingState.this.receiver != null) {
/*  53 */             ReceivingState.this.receiver.onSendTimeout(ReceivingState.this.data);
/*     */           }
/*     */         } 
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public ReceivingState(SendDataBean sendData) {
/*  61 */     this.receiver = sendData.getReceiver();
/*  62 */     this.reCount = sendData.getReCount();
/*  63 */     this.connectCallback = sendData.getConnectCallback();
/*  64 */     this.data = sendData.getData();
/*  65 */     this.sendData = sendData;
/*  66 */     BluetoothGattCallbackImpl.receiveHelper.resetReceiveLsit();
/*  67 */     init();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStateType() {
/*  72 */     return 5;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStateName() {
/*  77 */     return "state_receiving";
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  82 */     BluetoothStateManager.instance().getHandler().postDelayed(this.runnable, this.sendData.getTimeout());
/*     */   }
/*     */ 
/*     */   
/*     */   public void startScan(int timeout, ScanCallback callback) throws ErrorStateException {
/*  87 */     throw new ErrorStateException("正在接收数据不能扫描");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stopScan() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(BluetoothDevice address, int timeout, ConnectCallback callback) throws ErrorStateException {
/*  97 */     throw new ErrorStateException("正在接收数据不能连接");
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnect(ConnectCallback callback) {
/* 102 */     BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
/* 103 */     BluetoothStateManager.instance().setmBluetoothState(new DisconnectIngState(callback));
/*     */   }
/*     */ 
/*     */   
/*     */   public void send(byte[] data, int delay, int reCount, int frameReCount, int timeoutTime, int frameTimeout, Receiver callback, ConnectCallback connectCallback) throws ErrorStateException {
/* 108 */     throw new ErrorStateException("正在接收数据不能发送数据");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGlobleReceive(Receiver receive) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
/* 118 */     if (newState == 0) {
/* 119 */       LogHelperBt.LogW("ReceivingState", "-->onConnectionStateChange  state: " + status + ", newState:" + newState);
/* 120 */       BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
/* 121 */       BluetoothStateManager.instance().setmBluetoothState(new AvailableState());
/* 122 */       if (this.connectCallback != null) {
/* 123 */         this.connectCallback.onDisconnect();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
/* 131 */     byte[] data = characteristic.getValue();
/* 132 */     if (status != 0) {
/* 133 */       LogHelperBt.LogW("onCharacteristicWrite", "写入数据失败     state:" + 
/* 134 */           BluetoothStateManager.instance().getmBluetoothState().getStateName() + ", " + 
/* 135 */           UtilsBt.bytesToHexString(data));
/* 136 */       BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onCharacteristicChanged(String channel, byte[] data) {
/* 142 */     boolean result = false;
/* 143 */     if (this.receiver != null) {
/* 144 */       BluetoothStateManager.instance().setmBluetoothState(new ConnectedState(this.connectCallback));
/* 145 */       BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
/* 146 */       result = this.receiver.onRecv(channel, data);
/*     */     } 
/* 148 */     return result;
/*     */   }
/*     */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/status/ReceivingState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */