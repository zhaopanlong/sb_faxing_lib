/*    */ package com.cgutech.bluetoothstatusapi.service;
/*    */ 
/*    */ import android.app.Service;
/*    */ import android.content.Intent;
/*    */ import android.os.IBinder;
/*    */ import android.util.Log;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BluetoothStateListener
/*    */   extends Service
/*    */ {
/* 20 */   private String tag = "MyService";
/*    */   
/*    */   public IBinder onBind(Intent intent) {
/* 23 */     return null;
/*    */   }
/*    */   public void onCreate() {
/*    */     try {
/* 27 */       Log.i(this.tag, "MyService onCreate().....");
/* 28 */     } catch (Exception e) {
/* 29 */       e.printStackTrace();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/service/BluetoothStateListener.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */