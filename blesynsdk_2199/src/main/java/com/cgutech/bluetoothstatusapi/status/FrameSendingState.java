/*     */ package com.cgutech.bluetoothstatusapi.status;
/*     */ 
/*     */ import android.bluetooth.BluetoothDevice;
/*     */ import android.bluetooth.BluetoothGatt;
/*     */ import android.bluetooth.BluetoothGattCharacteristic;
/*     */ import android.bluetooth.BluetoothGattService;
/*     */ import android.util.Log;
/*     */ import com.cgutech.bluetoothstatusapi.bean.SendDataBean;
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
/*     */ public class FrameSendingState
/*     */   extends BluetoothMessageCallback
/*     */   implements BluetoothState
/*     */ {
/*     */   private SendDataBean sendData;
/*     */   
/*  29 */   private Runnable runnable = new Runnable()
/*     */     {
/*     */       public void run() {
/*  32 */         if (FrameSendingState.this.sendData.getSendCount() < 5) {
/*  33 */           LogHelperBt.LogI(FrameSendingState.class.getSimpleName(), "帧剩余重发次数:" + FrameSendingState.this
/*  34 */               .sendData.getFrameReCount() + ", delay:" + FrameSendingState.this
/*  35 */               .sendData.getDelay());
/*     */         }
/*     */         
/*  38 */         if (FrameSendingState.this.sendData.getFrameReCount() > 0) {
/*  39 */           int sendCount = FrameSendingState.this.sendData.getFrameReCount() - 1;
/*  40 */           FrameSendingState.this.sendData.setFrameReCount(sendCount);
/*  41 */           FrameSendingState.this.init();
/*     */         } else {
/*  43 */           BluetoothStateManager.instance().getHandler().removeCallbacks(FrameSendingState.this.runnable);
/*  44 */           BluetoothStateManager.instance().setmBluetoothState(new ConnectedState(FrameSendingState.this
/*  45 */                 .sendData.getConnectCallback()));
/*     */           
/*  47 */           if (FrameSendingState.this.sendData.getReceiver() != null) {
/*  48 */             FrameSendingState.this.sendData.getReceiver().onSendTimeout(FrameSendingState.this.sendData.getData());
/*     */           }
/*     */         } 
/*     */       }
/*     */     };
/*     */ 
/*     */   
/*     */   public FrameSendingState(SendDataBean sendData) {
/*  56 */     this.sendData = sendData;
/*  57 */     BluetoothStateManager.instance().getHandler().postDelayed(new Runnable()
/*     */         {
/*     */           public void run() {
/*  60 */             FrameSendingState.this.init();
/*     */           }
/*  62 */         },  sendData.getDelay());
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStateType() {
/*  67 */     return 8;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStateName() {
/*  72 */     return "state_frame_sending";
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  77 */     if (this.sendData.getPos() < this.sendData.getSendDatas().size()) {
/*  78 */       byte[] data = this.sendData.getSendDatas().get(this.sendData.getPos());
/*     */       
/*  80 */       BluetoothGatt gatt = BluetoothStateManager.instance().getmBluetoothGatt();
/*  81 */       if (gatt == null) {
/*  82 */         Log.e("connectedState&send", "gatt对象为空");
/*     */         
/*     */         return;
/*     */       } 
/*  86 */       BluetoothGattService service = (gatt.getService(BluetoothStateManager.UUID_RX_SERVICE) == null) ? gatt.getService(BluetoothStateManager.UUID_RX_SERVICE_WX) : gatt.getService(BluetoothStateManager.UUID_RX_SERVICE);
/*  87 */       if (service == null) {
/*  88 */         Log.e("connectState&send", "gatt service对象为空");
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*  93 */       BluetoothGattCharacteristic characteristic = service.getCharacteristic(BluetoothStateManager.UUID_RX_CHAR);
/*  94 */       characteristic.setValue(data);
/*     */       
/*  96 */       if (gatt.writeCharacteristic(characteristic))
/*     */       {
/*     */         
/*  99 */         LogHelperBt.LogI("sending", "发送成功     postion:" + (this.sendData
/* 100 */             .getPos() + 1) + ", data:" + 
/* 101 */             UtilsBt.bytesToHexString(data));
/*     */       }
/* 103 */       BluetoothStateManager.instance().getHandler().postDelayed(this.runnable, this.sendData.getFrameTimeout());
/*     */     } else {
/* 105 */       LogHelperBt.LogI(getStateName(), "发送数据失败");
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void startScan(int timeout, ScanCallback callback) throws ErrorStateException {
/* 111 */     throw new ErrorStateException("正在发送数据,不能扫描蓝牙");
/*     */   }
/*     */ 
/*     */   
/*     */   public void stopScan() throws ErrorStateException {
/* 116 */     throw new ErrorStateException("正在发送数据,不能停止扫描蓝牙");
/*     */   }
/*     */ 
/*     */   
/*     */   public void connect(BluetoothDevice address, int timeout, ConnectCallback callback) throws ErrorStateException {
/* 121 */     throw new ErrorStateException("正在发送数据,不能连接蓝牙");
/*     */   }
/*     */ 
/*     */   
/*     */   public void disconnect(ConnectCallback callback) throws ErrorStateException {
/* 126 */     BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
/* 127 */     BluetoothStateManager.instance().setmBluetoothState(new DisconnectIngState(this.sendData.getConnectCallback()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void send(byte[] data, int delay, int reCount, int frameReCount, int timeoutTime, int frameTimeout, Receiver callback, ConnectCallback connectCallback) throws ErrorStateException {
/* 132 */     throw new ErrorStateException("正在发送数据帧,不能发送数据");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void addGlobleReceive(Receiver receive) throws ErrorStateException {}
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
/* 144 */     if (newState == 0) {
/* 145 */       LogHelperBt.LogW("DisconnectIngState", "-->onConnectionStateChange  state: " + status + ", newState:" + newState);
/* 146 */       BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
/* 147 */       ConnectCallback callback = this.sendData.getConnectCallback();
/* 148 */       if (callback != null)
/*     */       {
/* 150 */         callback.onDisconnect();
/*     */       }
/* 152 */       BluetoothStateManager.instance().setmBluetoothState(new AvailableState());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
/* 158 */     byte[] data = characteristic.getValue();
/* 159 */     if (status != 0) {
/* 160 */       LogHelperBt.LogI("onCharacteristicWrite", "写入数据失败     state:" + 
/* 161 */           BluetoothStateManager.instance().getmBluetoothState().getStateName() + ", " + 
/* 162 */           UtilsBt.bytesToHexString(data));
/* 163 */       BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
/* 164 */       BluetoothStateManager.instance().getmBluetoothGatt().close();
/* 165 */       BluetoothStateManager.instance().setmBluetoothState(new AvailableState());
/* 166 */       if (this.sendData.getConnectCallback() != null)
/* 167 */         this.sendData.getConnectCallback().onDisconnect(); 
/*     */     } 
/*     */   }
/*     */   
/*     */   private class ReplyData
/*     */   {
/*     */     private String channel;
/*     */     private int currentCount;
/*     */     
/*     */     public ReplyData(byte[] data) {
/* 177 */       String tmpData = UtilsBt.bytesToHexString(data);
/* 178 */       if (tmpData != null && tmpData.length() > 12) {
/* 179 */         this.channel = tmpData.substring(6, 8);
/* 180 */         String tmp = tmpData.substring(10, 12);
/* 181 */         this.currentCount = Integer.parseInt(tmp, 16);
/*     */       } 
/*     */     }
/*     */     
/*     */     public String getChannel() {
/* 186 */       return this.channel;
/*     */     }
/*     */     
/*     */     public int getCurrentCount() {
/* 190 */       return this.currentCount;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onCharacteristicChanged(String channel, byte[] data) {
/* 196 */     boolean result = false;
/* 197 */     if (channel != null && !channel.equals("538001c7")) {
/* 198 */       LogHelperBt.LogI("FrameSendingState", "发送状态下收到非ACK消息   channel:" + channel + ", data:" + 
/* 199 */           UtilsBt.bytesToHexString(data));
/* 200 */       return false;
/*     */     } 
/* 202 */     ReplyData replyData = new ReplyData(data);
/* 203 */     if (replyData.getChannel().equals("c7")) {
/* 204 */       if (this.sendData.getPos() == this.sendData.getSendDatas().size() - 1) {
/* 205 */         BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
/* 206 */         BluetoothStateManager.instance().setmBluetoothState(new ReceivingState(this.sendData));
/*     */       }
/*     */       else {
/*     */         
/* 210 */         if (replyData.getCurrentCount() == this.sendData.getPos() + 1) {
/* 211 */           BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
/* 212 */           int pos = this.sendData.getPos() + 1;
/* 213 */           this.sendData.setPos(pos);
/* 214 */           int sendCount = this.sendData.getFrameRecountTotal();
/* 215 */           this.sendData.setFrameReCount(sendCount);
/*     */         } else {
/* 217 */           LogHelperBt.LogE(getStateName(), "ack错误   pos:" + (this.sendData.getPos() + 1) + ", nPos:" + replyData.getCurrentCount());
/* 218 */           int pos = this.sendData.getPos();
/* 219 */           this.sendData.setPos(pos);
/* 220 */           int sendCount = this.sendData.getFrameReCount() - 1;
/* 221 */           this.sendData.setFrameReCount(sendCount);
/*     */         } 
/* 223 */         init();
/*     */       } 
/* 225 */       result = true;
/*     */     } 
/* 227 */     return result;
/*     */   }
/*     */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/status/FrameSendingState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */