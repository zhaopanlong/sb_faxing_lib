/*    */ package com.cgutech.bluetoothstatusapi.Application;
/*    */ 
/*    */ import android.app.Application;
/*    */ import android.content.Context;
/*    */ 
/*    */ 
/*    */ public class MyApplication
/*    */   extends Application
/*    */ {
/*    */   private static Context context;
/*    */   
/*    */   public void onCreate() {
/* 13 */     context = getApplicationContext();
/*    */   }
/*    */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/Application/MyApplication.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */