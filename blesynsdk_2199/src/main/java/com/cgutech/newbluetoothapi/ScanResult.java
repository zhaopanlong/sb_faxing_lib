/*    */ package com.cgutech.newbluetoothapi;
/*    */ 
/*    */ import android.bluetooth.BluetoothDevice;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ScanResult
/*    */ {
/*    */   private BluetoothDevice device;
/*    */   private int rssi;
/*    */   
/*    */   public BluetoothDevice getDevice() {
/* 15 */     return this.device;
/*    */   }
/*    */   
/*    */   public void setDevice(BluetoothDevice device) {
/* 19 */     this.device = device;
/*    */   }
/*    */   
/*    */   public int getRssi() {
/* 23 */     return this.rssi;
/*    */   }
/*    */   
/*    */   public void setRssi(int rssi) {
/* 27 */     this.rssi = rssi;
/*    */   }
/*    */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/newbluetoothapi/ScanResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */