/*     */ package com.cgutech.bluetoothstatusapi.status;
/*     */ 
/*     */ import android.bluetooth.BluetoothDevice;
/*     */ import android.bluetooth.BluetoothGatt;
/*     */ import android.bluetooth.BluetoothGattCharacteristic;
/*     */ import android.bluetooth.BluetoothGattService;
/*     */ import com.cgutech.bluetoothstatusapi.bean.SendDataBean;
/*     */ import com.cgutech.bluetoothstatusapi.callback.BluetoothMessageCallback;
/*     */ import com.cgutech.bluetoothstatusapi.callback.ConnectCallback;
/*     */ import com.cgutech.bluetoothstatusapi.callback.Receiver;
/*     */ import com.cgutech.bluetoothstatusapi.callback.ScanCallback;
/*     */ import com.cgutech.bluetoothstatusapi.callback.Sender;
/*     */ import com.cgutech.bluetoothstatusapi.controller.BluetoothStateManager;
/*     */ import com.cgutech.bluetoothstatusapi.exception.ErrorStateException;
/*     */ import com.cgutech.bluetoothstatusapi.utils.SendCmdHelper;
/*     */ import com.cgutech.bluetoothstatusapi.utils.SenderHelper;
/*     */ import com.cgutech.commonBt.log.LogHelperBt;
/*     */ import com.cgutech.commonBt.util.UtilsBt;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SendingState
/*     */   extends BluetoothMessageCallback
/*     */   implements BluetoothState, Sender
/*     */ {
/*     */   private static final int stateTag = 7;
/*     */   private SendDataBean sendData;
/*     */   SendCmdHelper sendCmdHelper;
/*     */   
/*  37 */   Runnable noReplyRunnable = new Runnable()
/*     */     {
/*     */       public void run()
/*     */       {
/*  41 */         byte[] data = SendingState.this.sendData.getData();
/*     */         
/*  43 */         int outTime = 300 * data.length / 15;
/*  44 */         if (outTime < SendingState.this.sendData.getTimeout()) {
/*  45 */           outTime = SendingState.this.sendData.getTimeout();
/*     */         }
/*  47 */         SendingState.this.sendData.setTimeout(outTime);
/*  48 */         BluetoothStateManager.instance().setmBluetoothState(new ReceivingState(SendingState.this
/*  49 */               .sendData));
/*  50 */         SendingState.this.sendCmdHelper.send(data);
/*     */       }
/*     */     };
/*     */   
/*  54 */   Runnable replyRunnable = new Runnable()
/*     */     {
/*     */       public void run() {
/*  57 */         BluetoothStateManager.instance().setmBluetoothState(new FrameSendingState(SendingState.this.sendData));
/*     */       }
/*     */     };
/*     */   
/*     */   public SendingState(byte[] data, int delay, int reCount, int frameReCount, int timeoutTime, int frameTimeout, Receiver receiver, ConnectCallback connectCallback) {
/*  62 */     List<byte[]> datas = SenderHelper.getPackageList(data);
/*     */     
/*  64 */     this.sendData = new SendDataBean();
/*  65 */     this.sendData.setPos(0);
/*  66 */     this.sendData.setConnectCallback(connectCallback);
/*  67 */     this.sendData.setData(data);
/*  68 */     this.sendData.setSendDatas(datas);
/*  69 */     this.sendData.setReceiver(receiver);
/*  70 */     this.sendData.setReCount(reCount);
/*  71 */     this.sendData.setTimeout(timeoutTime);
/*  72 */     this.sendData.setSendCount(frameReCount + 1);
/*  73 */     this.sendData.setDelay(delay);
/*  74 */     this.sendData.setFrameTimeout(frameTimeout);
/*  75 */     this.sendData.setFrameReCount(frameReCount);
/*  76 */     this.sendData.setFrameRecountTotal(frameReCount);
/*     */     
/*  78 */     this.sendCmdHelper = new SendCmdHelper(null, this);
/*     */     
/*  80 */     init();
/*     */   }
/*     */ 
/*     */   
/*     */   public void init() {
/*  85 */     int reCount = this.sendData.getReCount();
/*  86 */     if (this.sendData.getReCount() < 5) {
/*  87 */       LogHelperBt.LogI(SendingState.class.getSimpleName(), "数据剩余重发次数:" + this.sendData.getReCount());
/*     */     }
/*     */     
/*  90 */     if (reCount < 0) {
/*  91 */       LogHelperBt.LogW("connectedState&send", "重发次数小于0");
/*  92 */       if (this.sendData.getReceiver() != null) {
/*  93 */         this.sendData.getReceiver().onSendTimeout(this.sendData.getData());
/*     */       }
/*  95 */       BluetoothStateManager.instance().setmBluetoothState(new ConnectedState(this.sendData.getConnectCallback()));
/*     */       
/*     */       return;
/*     */     } 
/*  99 */     reCount--;
/* 100 */     this.sendData.setReCount(reCount);
/* 101 */     if (BluetoothStateManager.instance().isNeedFrameReply()) {
/* 102 */       LogHelperBt.LogI(getStateName(), "新协议");
/* 103 */       BluetoothStateManager.instance().getHandler().postDelayed(this.replyRunnable, this.sendData.getDelay());
/*     */     } else {
/* 105 */       LogHelperBt.LogI(getStateName(), "旧协议");
/* 106 */       BluetoothStateManager.instance().getHandler().postDelayed(this.noReplyRunnable, this.sendData.getDelay());
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void startScan(int timeout, ScanCallback callback) throws ErrorStateException {
/* 112 */     throw new ErrorStateException("正在发送，数据不能扫描");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void stopScan() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void connect(BluetoothDevice address, int timeout, ConnectCallback callback) throws ErrorStateException {
/* 122 */     throw new ErrorStateException("正在发送，数据不能连接");
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void disconnect(ConnectCallback callback) {
/* 128 */     BluetoothStateManager.instance().setmBluetoothState(new DisconnectIngState(callback));
/*     */   }
/*     */ 
/*     */   
/*     */   public void send(byte[] data, int delay, int reCount, int frameReCount, int timeoutTime, int frameTimeout, Receiver callback, ConnectCallback connectCallback) throws ErrorStateException {
/* 133 */     throw new ErrorStateException("正在发送，数据不能发送数据");
/*     */   }
/*     */ 
/*     */   
/*     */   public void addGlobleReceive(Receiver receive) {
/* 138 */     BluetoothStateManager.instance().setGloableReceiver(receive);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
/* 143 */     if (newState == 0) {
/* 144 */       LogHelperBt.LogW("SendingState", "蓝牙连接断开");
/* 145 */       BluetoothStateManager.instance().getHandler().removeCallbacks(this.noReplyRunnable);
/* 146 */       BluetoothStateManager.instance().setmBluetoothState(new AvailableState());
/* 147 */       ConnectCallback connectCallback = this.sendData.getConnectCallback();
/* 148 */       if (connectCallback != null) {
/* 149 */         connectCallback.onDisconnect();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
/* 156 */     byte[] data = characteristic.getValue();
/* 157 */     if (status != 0) {
/* 158 */       LogHelperBt.LogI("onCharacteristicWrite", "写入数据失败     state:" + 
/* 159 */           BluetoothStateManager.instance().getmBluetoothState().getStateName() + ", " + 
/* 160 */           UtilsBt.bytesToHexString(data));
/* 161 */       BluetoothStateManager.instance().getHandler().removeCallbacks(this.noReplyRunnable);
/* 162 */       BluetoothStateManager.instance().getmBluetoothGatt().close();
/* 163 */       BluetoothStateManager.instance().setmBluetoothState(new AvailableState());
/* 164 */       if (this.sendData.getConnectCallback() != null) {
/* 165 */         this.sendData.getConnectCallback().onDisconnect();
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean onCharacteristicChanged(String channel, byte[] data) {
/* 172 */     LogHelperBt.LogW("Sending", "收到消息：" + UtilsBt.bytesToHexString(data));
/* 173 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean send(byte[] data) {
/* 178 */     BluetoothGatt gatt = BluetoothStateManager.instance().getmBluetoothGatt();
/* 179 */     if (gatt == null) {
/* 180 */       LogHelperBt.LogE("connectedState&send", "gatt对象为空");
/* 181 */       return false;
/*     */     } 
/*     */     
/* 184 */     BluetoothGattService service = (gatt.getService(BluetoothStateManager.UUID_RX_SERVICE) == null) ? gatt.getService(BluetoothStateManager.UUID_RX_SERVICE_WX) : gatt.getService(BluetoothStateManager.UUID_RX_SERVICE);
/* 185 */     if (service == null) {
/* 186 */       LogHelperBt.LogE("connectState&send", "gatt service对象为空");
/* 187 */       return false;
/*     */     } 
/*     */ 
/*     */     
/* 191 */     BluetoothGattCharacteristic characteristic = service.getCharacteristic(BluetoothStateManager.UUID_RX_CHAR);
/* 192 */     characteristic.setValue(data);
/*     */     
/* 194 */     if (!gatt.writeCharacteristic(characteristic))
/*     */     {
/* 196 */       return false;
/*     */     }
/* 198 */     LogHelperBt.LogI("sending", "发送成功:" + UtilsBt.bytesToHexString(data));
/*     */     
/* 200 */     return true;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getStateType() {
/* 205 */     return 7;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getStateName() {
/* 210 */     return "state_sending";
/*     */   }
/*     */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/status/SendingState.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */