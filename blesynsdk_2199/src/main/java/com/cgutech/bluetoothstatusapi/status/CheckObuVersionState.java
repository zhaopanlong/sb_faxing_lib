/*     */ package com.cgutech.bluetoothstatusapi.status;
/*     */ 
/*     */ import android.bluetooth.BluetoothDevice;
/*     */ import android.bluetooth.BluetoothGatt;
/*     */ import android.bluetooth.BluetoothGattCharacteristic;
/*     */ import android.bluetooth.BluetoothGattService;
/*     */ import android.util.Log;
/*     */ import com.cgutech.bluetoothstatusapi.bean.ObuCheckBean;
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
/*     */ public class CheckObuVersionState
/*     */   extends BluetoothMessageCallback
/*     */   implements BluetoothState
/*     */ {
/*     */   private ConnectCallback connectCallback;
/*  26 */   private int reTestCount = 3;
/*  27 */   private Runnable runnable = new Runnable()
/*     */     {
/*     */       public void run() {
/*  30 */         if (CheckObuVersionState.this.reTestCount-- > 0) {
/*  31 */           CheckObuVersionState.this.getObuVersion();
/*  32 */           BluetoothStateManager.instance().getHandler().postDelayed(CheckObuVersionState.this.runnable, 1000L);
/*     */         } else {
/*  34 */           BluetoothStateManager.instance().setNeedFrameReply(false);
/*  35 */           BluetoothStateManager.instance().setmBluetoothState(new ConnectedState(CheckObuVersionState.this.connectCallback));
/*  36 */           if (CheckObuVersionState.this.connectCallback != null) {
/*  37 */             CheckObuVersionState.this.connectCallback.onConnect();
/*     */           }
/*     */         } 
/*     */       }
/*     */     };
/*     */   
/*     */   public CheckObuVersionState(ConnectCallback connectCallback) {
/*  44 */     this.connectCallback = connectCallback;
/*  45 */     init();
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStateType() {
/*  50 */     return 10;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStateName() {
/*  55 */     return "state_check_obu_version";
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  60 */     BluetoothStateManager.instance().getHandler().postDelayed(this.runnable, 10L);
/*     */   }
/*     */ 
/*     */   
/*     */   public void startScan(int timeout, ScanCallback callback) throws ErrorStateException {
/*  65 */     throw new ErrorStateException("正在连接不能扫描");
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopScan() throws ErrorStateException {
/*  70 */     throw new ErrorStateException("正在连接不能停止扫描");
/*     */   }
/*     */ 
/*     */   
/*     */   public void connect(BluetoothDevice address, int timeout, ConnectCallback callback) throws ErrorStateException {
/*  75 */     throw new ErrorStateException("正在连接不能连接");
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnect(ConnectCallback callback) throws ErrorStateException {
/*  80 */     BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
/*  81 */     BluetoothStateManager.instance().setmBluetoothState(new DisconnectIngState(callback));
/*     */   }
/*     */ 
/*     */   
/*     */   public void send(byte[] data, int delay, int reCount, int frameReCount, int timeoutTime, int frameTimeout, Receiver callback, ConnectCallback connectCallback) throws ErrorStateException {
/*  86 */     throw new ErrorStateException("正在连接不能断开连接发送数据");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGlobleReceive(Receiver receive) throws ErrorStateException {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
/*  97 */     if (newState == 0) {
/*  98 */       Log.i("CheckObuVersionState", "-->onConnectionStateChange  state: " + status + ", newState:" + newState);
/*  99 */       BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
/* 100 */       BluetoothStateManager.instance().getmBluetoothGatt().close();
/* 101 */       BluetoothStateManager.instance().setmBluetoothState(new AvailableState());
/* 102 */       if (this.connectCallback != null) {
/* 103 */         this.connectCallback.onDisconnect();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onCharacteristicChanged(String channel, byte[] data) {
/* 110 */     String sData = UtilsBt.bytesToHexString(data);
/* 111 */     LogHelperBt.LogI(getStateName(), "查询OBU版本，收到数据：" + sData);
/*     */     
/* 113 */     if (data != null) {
/* 114 */       ObuCheckBean checkBean = new ObuCheckBean(sData);
/* 115 */       if (checkBean.getChannel().equals("c2") && checkBean.getType().equals("04")) {
/* 116 */         BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
/* 117 */         if (checkBean.getSupplier() == 3) {
/* 118 */           if (checkBean.getVersion() >= 3) {
/* 119 */             LogHelperBt.LogI(getStateName(), "新协议");
/* 120 */             BluetoothStateManager.instance().setNeedFrameReply(true);
/*     */           } else {
/* 122 */             LogHelperBt.LogI(getStateName(), "旧协议");
/* 123 */             BluetoothStateManager.instance().setNeedFrameReply(false);
/*     */           } 
/* 125 */           BluetoothStateManager.instance().setmBluetoothState(new ConnectedState(this.connectCallback));
/* 126 */           if (this.connectCallback != null) {
/* 127 */             this.connectCallback.onConnect();
/*     */           }
/* 129 */         } else if (checkBean.getSupplier() == 255) {
/* 130 */           LogHelperBt.LogI(getStateName(), "旧协议");
/* 131 */           BluetoothStateManager.instance().setNeedFrameReply(false);
/* 132 */           BluetoothStateManager.instance().setmBluetoothState(new ConnectedState(this.connectCallback));
/* 133 */           if (this.connectCallback != null) {
/* 134 */             this.connectCallback.onConnect();
/*     */           }
/*     */         } else {
/* 137 */           if (this.connectCallback != null) {
/* 138 */             this.connectCallback.onError("OBU广商标识符异常");
/*     */           }
/* 140 */           BluetoothStateManager.instance().getmBluetoothGatt().close();
/* 141 */           BluetoothStateManager.instance().setmBluetoothState(new AvailableState());
/*     */         } 
/*     */       } 
/*     */     } 
/* 145 */     return true;
/*     */   }
/*     */   
/*     */   private void getObuVersion() {
/* 149 */     byte[] data = UtilsBt.hexStringTobytes("538001d2040004");
/* 150 */     BluetoothGatt gatt = BluetoothStateManager.instance().getmBluetoothGatt();
/* 151 */     if (gatt == null) {
/* 152 */       Log.e("connectedState&send", "gatt对象为空");
/*     */       
/*     */       return;
/*     */     } 
/* 156 */     BluetoothGattService service = (gatt.getService(BluetoothStateManager.UUID_RX_SERVICE) == null) ? gatt.getService(BluetoothStateManager.UUID_RX_SERVICE_WX) : gatt.getService(BluetoothStateManager.UUID_RX_SERVICE);
/* 157 */     if (service == null) {
/* 158 */       Log.e("connectState&send", "gatt service对象为空");
/*     */       
/*     */       return;
/*     */     } 
/* 162 */     BluetoothGattCharacteristic characteristic = service.getCharacteristic(BluetoothStateManager.UUID_RX_CHAR);
/* 163 */     characteristic.setValue(data);
/*     */     
/* 165 */     if (!gatt.writeCharacteristic(characteristic)) {
/* 166 */       LogHelperBt.LogI("sending", "发送失败:" + UtilsBt.bytesToHexString(data));
/*     */     } else {
/* 168 */       LogHelperBt.LogI("sending", "发送成功");
/*     */     } 
/* 170 */     Log.i("connectingState", "-->onServiceDiscovered");
/*     */   }
/*     */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/status/CheckObuVersionState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */