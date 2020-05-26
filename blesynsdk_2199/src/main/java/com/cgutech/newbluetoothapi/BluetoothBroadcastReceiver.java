/*    */ package com.cgutech.newbluetoothapi;
/*    */ 
/*    */ import android.content.BroadcastReceiver;
/*    */ import android.content.Context;
/*    */ import android.content.Intent;
/*    */ import android.util.Log;
/*    */ import com.cgutech.bluetoothstatusapi.controller.BluetoothStateManager;
/*    */ import com.cgutech.bluetoothstatusapi.status.AvailableState;
/*    */ import com.cgutech.bluetoothstatusapi.status.BluetoothState;
/*    */ import com.cgutech.bluetoothstatusapi.status.UnavailableState;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BluetoothBroadcastReceiver
/*    */   extends BroadcastReceiver
/*    */ {
/* 20 */   private String tag = "收到广播";
/*    */ 
/*    */   
/*    */   public void onReceive(Context context, Intent intent) {
/* 24 */     int state = intent.getIntExtra("android.bluetooth.adapter.extra.STATE", -1);
/*    */     
/* 26 */     if (intent.getAction().endsWith("android.bluetooth.adapter.action.STATE_CHANGED")) {
/* 27 */       Log.v(this.tag, state + "");
/* 28 */       if (state == 12) {
/* 29 */         if (BluetoothStateManager.instance().getmBluetoothState().getStateType() == 0) {
/*    */           
/* 31 */           BluetoothStateManager.instance().setmBluetoothState((BluetoothState)new AvailableState());
/* 32 */           Log.i(this.tag, "蓝牙已打开");
/*    */         } else {
/* 34 */           Log.w(this.tag, "蓝牙已打开, 但状态未处理");
/*    */         } 
/* 36 */         BluetoothObuHandler.getInstance().setmIsConnect(false);
/* 37 */       } else if (state == 10) {
/* 38 */         BluetoothStateManager.instance().setmBluetoothState((BluetoothState)new UnavailableState());
/* 39 */         Log.v(this.tag, "蓝牙已关闭");
/*    */       } else {
/* 41 */         Log.v(this.tag, "收到广播: " + state);
/*    */       } 
/*    */     } 
/*    */   }
/*    */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/newbluetoothapi/BluetoothBroadcastReceiver.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */