/*     */ package com.cgutech.bluetoothstatusapi.status;
/*     */ 
/*     */ import android.bluetooth.BluetoothAdapter;
/*     */ import android.bluetooth.BluetoothDevice;
/*     */ import android.util.Log;
/*     */ import com.cgutech.bluetoothstatusapi.callback.BluetoothMessageCallback;
/*     */ import com.cgutech.bluetoothstatusapi.callback.ConnectCallback;
/*     */ import com.cgutech.bluetoothstatusapi.callback.Receiver;
/*     */ import com.cgutech.bluetoothstatusapi.callback.ScanCallback;
/*     */ import com.cgutech.bluetoothstatusapi.controller.BluetoothStateManager;
/*     */ import com.cgutech.bluetoothstatusapi.exception.ErrorStateException;
/*     */ import com.cgutech.commonBt.log.LogHelperBt;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ScaningState
/*     */   extends BluetoothMessageCallback
/*     */   implements BluetoothState
/*     */ {
/*     */   private static final int stateTag = 6;
/*     */   private ScanCallback callback;
/*     */   private int timeout;
/*     */   private Runnable runnable;
/*     */   private BluetoothAdapter.LeScanCallback mLeScanCallback;
/*     */   
/*     */   public ScaningState(int timeout, ScanCallback callback) {
/*  32 */     this.runnable = new Runnable()
/*     */       {
/*     */         public void run() {
/*  35 */           BluetoothStateManager.instance().setmBluetoothState(new AvailableState());
/*  36 */           BluetoothStateManager.instance().getmBluetoothAdapter().stopLeScan(ScaningState.this.mLeScanCallback);
/*  37 */           if (ScaningState.this.callback != null) {
/*  38 */             ScaningState.this.callback.onScanTimeout();
/*     */           }
/*     */         }
/*     */       };
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 115 */     this.mLeScanCallback = new BluetoothAdapter.LeScanCallback()
/*     */       {
/*     */         public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord)
/*     */         {
/* 119 */           if (ScaningState.this.callback != null && device != null && device.getName() != null) {
/* 120 */             ScaningState.this.callback.onScan(device, rssi);
/* 121 */             Log.i("scaningState", "device: " + device.getName());
/*     */           } 
/*     */         }
/*     */       };
/*     */     this.callback = callback;
/*     */     this.timeout = timeout;
/*     */     init();
/*     */   }
/*     */   
/*     */   public int getStateType() {
/*     */     return 6;
/*     */   }
/*     */   
/*     */   public String getStateName() {
/*     */     return "state_scaning";
/*     */   }
/*     */   
/*     */   public void init() {
/*     */     LogHelperBt.LogI("ScaningState", "-->扫描操作");
/*     */     BluetoothStateManager.instance().getmBluetoothAdapter().startLeScan(this.mLeScanCallback);
/*     */     BluetoothStateManager.instance().getHandler().postDelayed(this.runnable, this.timeout);
/*     */   }
/*     */   
/*     */   public void startScan(int timeout, ScanCallback callback) throws ErrorStateException {
/*     */     stopScan();
/*     */     BluetoothState state = new AvailableState();
/*     */     BluetoothStateManager.instance().setmBluetoothState(state);
/*     */     state.startScan(timeout, callback);
/*     */   }
/*     */   
/*     */   public void stopScan() {
/*     */     BluetoothStateManager.instance().getmBluetoothAdapter().stopLeScan(this.mLeScanCallback);
/*     */     BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
/*     */     BluetoothStateManager.instance().setmBluetoothState(new AvailableState());
/*     */   }
/*     */   
/*     */   public void connect(BluetoothDevice address, int timeout, ConnectCallback callback) {
/*     */     if (address != null) {
/*     */       BluetoothStateManager.instance().getmBluetoothAdapter().stopLeScan(this.mLeScanCallback);
/*     */       this.callback = null;
/*     */       LogHelperBt.LogI(getStateName(), "stopScan");
/*     */       BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
/*     */       BluetoothStateManager.instance().setmBluetoothDevice(address);
/*     */       BluetoothStateManager.instance().setmBluetoothState(new ConnectingState(callback, timeout));
/*     */     } else {
/*     */       LogHelperBt.LogE(getStateName(), "连接蓝牙设备时，device参数为null");
/*     */       throw new NullPointerException("连接地址为空");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void disconnect(final ConnectCallback callback) throws ErrorStateException {
/*     */     BluetoothStateManager.instance().getmBluetoothAdapter().stopLeScan(this.mLeScanCallback);
/*     */     BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
/*     */     BluetoothStateManager.instance().setmBluetoothState(new AvailableState());
/*     */     if (callback != null)
/*     */       BluetoothStateManager.instance().getHandler().postDelayed(new Runnable() {
/*     */             public void run() {
/*     */               callback.onDisconnect();
/*     */             }
/*     */           },  20L); 
/*     */   }
/*     */   
/*     */   public void send(byte[] data, int delay, int reCount, int frameReCount, int timeoutTime, int frameTimeout, Receiver callback, ConnectCallback connectCallback) throws ErrorStateException {
/*     */     throw new ErrorStateException("蓝牙正在扫描，不能发送数据");
/*     */   }
/*     */   
/*     */   public void addGlobleReceive(Receiver receive) throws ErrorStateException {
/*     */     throw new ErrorStateException("蓝牙正在扫描，不能设置全局接收据");
/*     */   }
/*     */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/status/ScaningState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */