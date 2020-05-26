/*    */ package com.cgutech.bluetoothstatusapi.controller;
/*    */ 
/*    */ import android.app.Activity;
/*    */ import android.bluetooth.BluetoothAdapter;
/*    */ import android.content.Intent;
/*    */ import android.widget.Toast;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RequestForBT
/*    */ {
/*    */   private BluetoothAdapter mBluetoothAdapter;
/*    */   private static RequestForBT requestForBT;
/*    */   
/*    */   public static RequestForBT instance() {
/* 19 */     requestForBT = new RequestForBT();
/* 20 */     return requestForBT;
/*    */   }
/*    */ 
/*    */   
/*    */   public void getRequestForBT(Activity mActivity) {
/* 25 */     this.mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
/* 26 */     if (this.mBluetoothAdapter == null) {
/* 27 */       Toast.makeText(mActivity.getApplicationContext(), "本机没有找到蓝牙硬件或驱动！", 0).show();
/*    */     }
/* 29 */     if (!this.mBluetoothAdapter.isEnabled()) {
/* 30 */       Intent mIntent = new Intent("android.bluetooth.adapter.action.REQUEST_ENABLE");
/* 31 */       mActivity.startActivityForResult(mIntent, 1);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/controller/RequestForBT.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */