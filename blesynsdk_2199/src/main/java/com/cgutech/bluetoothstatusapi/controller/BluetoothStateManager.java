/*     */ package com.cgutech.bluetoothstatusapi.controller;
/*     */ 
/*     */ import android.bluetooth.BluetoothAdapter;
/*     */ import android.bluetooth.BluetoothDevice;
/*     */ import android.bluetooth.BluetoothGatt;
/*     */ import android.bluetooth.BluetoothGattCallback;
/*     */ import android.bluetooth.BluetoothGattCharacteristic;
/*     */ import android.bluetooth.BluetoothGattService;
/*     */ import android.content.Context;
/*     */ import android.os.Handler;
/*     */ import android.util.Log;
/*     */ import com.cgutech.bluetoothstatusapi.callback.BluetoothGattCallbackImpl;
/*     */ import com.cgutech.bluetoothstatusapi.callback.Receiver;
/*     */ import com.cgutech.bluetoothstatusapi.status.AvailableState;
/*     */ import com.cgutech.bluetoothstatusapi.status.BluetoothState;
/*     */ import com.cgutech.commonBt.log.LogHelperBt;
/*     */ import java.util.UUID;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class BluetoothStateManager
/*     */ {
/*  24 */   public static final UUID UUID_RX_SERVICE = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
/*     */ 
/*     */   
/*  27 */   public static final UUID UUID_RX_SERVICE_WX = UUID.fromString("0000fee7-0000-1000-8000-00805f9b34fb");
/*     */ 
/*     */   
/*  30 */   public static final UUID UUID_RX_CHAR = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
/*     */ 
/*     */   
/*  33 */   public static final UUID UUID_TX_CHAR = UUID.fromString("0000ffe4-0000-1000-8000-00805f9b34fb");
/*     */   
/*     */   private BluetoothAdapter mBluetoothAdapter;
/*     */   
/*     */   private BluetoothState mBluetoothState;
/*     */   
/*     */   private BluetoothGatt mBluetoothGatt;
/*     */   
/*     */   private BluetoothDevice mBluetoothDevice;
/*     */   private BluetoothGattCallback mBluetoothGattCallback;
/*     */   private static BluetoothStateManager mController;
/*     */   private Receiver gloableReceiver;
/*     */   private Context context;
/*     */   private Handler handler;
/*     */   private boolean needFrameReply = true;
/*     */   
/*     */   public boolean isNeedFrameReply() {
/*  50 */     return this.needFrameReply;
/*     */   }
/*     */   
/*     */   public void setNeedFrameReply(boolean needFrameReply) {
/*  54 */     this.needFrameReply = needFrameReply;
/*     */   }
/*     */   
/*     */   public RequestForBT getRequestForBT() {
/*  58 */     return new RequestForBT();
/*     */   }
/*     */   
/*     */   public Handler getHandler() {
/*  62 */     return this.handler;
/*     */   }
/*     */ 
/*     */   
/*     */   public Context getContext() {
/*  67 */     return this.context;
/*     */   }
/*     */   
/*     */   public void setContext(Context context) {
/*  71 */     this.context = context;
/*     */   }
/*     */   
/*     */   public Receiver getGloableReceiver() {
/*  75 */     return this.gloableReceiver;
/*     */   }
/*     */   
/*     */   public void setGloableReceiver(Receiver gloableReceiver) {
/*  79 */     this.gloableReceiver = gloableReceiver;
/*     */   }
/*     */   
/*     */   public static BluetoothStateManager instance() {
/*  83 */     if (mController == null) {
/*  84 */       mController = new BluetoothStateManager();
/*  85 */       mController.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
/*  86 */       mController.setmBluetoothState((BluetoothState)new AvailableState());
/*  87 */       mController.handler = new Handler();
/*     */     } 
/*  89 */     return mController;
/*     */   }
/*     */   
/*     */   public BluetoothState getmBluetoothState() {
/*  93 */     return this.mBluetoothState;
/*     */   }
/*     */   
/*     */   public void setmBluetoothState(BluetoothState mBluetoothState) {
/*  97 */     if (this.mBluetoothState != null) {
/*  98 */       LogHelperBt.LogI("state", this.mBluetoothState.getStateName() + "-->" + mBluetoothState.getStateName());
/*     */     }
/* 100 */     this.mBluetoothState = mBluetoothState;
/*     */   }
/*     */   
/*     */   public BluetoothAdapter getmBluetoothAdapter() {
/* 104 */     return this.mBluetoothAdapter;
/*     */   }
/*     */   
/*     */   public BluetoothGatt getmBluetoothGatt() {
/* 108 */     return this.mBluetoothGatt;
/*     */   }
/*     */   
/*     */   public void setmBluetoothGatt(BluetoothGatt mBluetoothGatt) {
/* 112 */     this.mBluetoothGatt = mBluetoothGatt;
/*     */   }
/*     */   
/*     */   public BluetoothDevice getmBluetoothDevice() {
/* 116 */     return this.mBluetoothDevice;
/*     */   }
/*     */   
/*     */   public void setmBluetoothDevice(BluetoothDevice mBluetoothDevice) {
/* 120 */     this.mBluetoothDevice = mBluetoothDevice;
/*     */   }
/*     */   
/*     */   public boolean enableTXNotification() {
/* 124 */     if (this.mBluetoothGatt == null) {
/* 125 */       return false;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 130 */     BluetoothGattService _BluetoothGattService = (this.mBluetoothGatt.getService(UUID_RX_SERVICE) == null) ? this.mBluetoothGatt.getService(UUID_RX_SERVICE_WX) : this.mBluetoothGatt.getService(UUID_RX_SERVICE);
/*     */     
/* 132 */     if (_BluetoothGattService == null) {
/* 133 */       return false;
/*     */     }
/* 135 */     Log.i("this", _BluetoothGattService.getUuid().toString());
/*     */     
/* 137 */     BluetoothGattCharacteristic _BluetoothGattCharacteristic = _BluetoothGattService.getCharacteristic(UUID_TX_CHAR);
/*     */     
/* 139 */     return (_BluetoothGattCharacteristic != null && this.mBluetoothGatt
/* 140 */       .setCharacteristicNotification(_BluetoothGattCharacteristic, true));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BluetoothGattCallback getmBluetoothGattCallback() {
/* 146 */     if (this.mBluetoothGattCallback == null) {
/* 147 */       this.mBluetoothGattCallback = (BluetoothGattCallback)new BluetoothGattCallbackImpl();
/*     */     }
/* 149 */     return this.mBluetoothGattCallback;
/*     */   }
/*     */   
/*     */   public boolean isBluetoothOpen() {
/* 153 */     boolean result = false;
/* 154 */     if (instance().getmBluetoothAdapter() != null && 
/* 155 */       instance().getmBluetoothAdapter().isEnabled()) {
/* 156 */       result = true;
/*     */     }
/* 158 */     return result;
/*     */   }
/*     */   
/*     */   public void setmControllerNull() {
/* 162 */     this.mBluetoothGatt = null;
/* 163 */     this.mBluetoothGattCallback = null;
/*     */   }
/*     */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/controller/BluetoothStateManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */