/*    */ package com.cgutech.bluetoothstatusapi.bean;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ObuCheckBean
/*    */ {
/*    */   private String channel;
/*    */   private String type;
/*    */   private String data;
/*    */   private int supplier;
/*    */   private int version;
/*    */   
/*    */   public ObuCheckBean(String bluetoothData) {
/* 14 */     if (bluetoothData != null) {
/* 15 */       if (bluetoothData.length() > 10) {
/* 16 */         this.channel = bluetoothData.substring(6, 8);
/*    */       }
/* 18 */       this.type = bluetoothData.substring(8, 10);
/* 19 */       if (this.type.equals("04")) {
/*    */         
/* 21 */         if (bluetoothData.length() > 12) {
/* 22 */           String tmp = bluetoothData.substring(10, 12);
/* 23 */           this.supplier = Integer.valueOf(tmp, 16).intValue();
/*    */         } 
/* 25 */         if (bluetoothData.length() > 14) {
/* 26 */           String tmp = bluetoothData.substring(12, 14);
/* 27 */           this.version = Integer.valueOf(tmp, 16).intValue();
/*    */         } 
/*    */       } 
/* 30 */       if (bluetoothData.length() > 14) {
/* 31 */         this.data = bluetoothData.substring(10, bluetoothData.length() - 3);
/*    */       }
/*    */     } 
/*    */   }
/*    */   
/*    */   public String getChannel() {
/* 37 */     return this.channel;
/*    */   }
/*    */   
/*    */   public String getType() {
/* 41 */     return this.type;
/*    */   }
/*    */   
/*    */   public String getData() {
/* 45 */     return this.data;
/*    */   }
/*    */   
/*    */   public int getSupplier() {
/* 49 */     return this.supplier;
/*    */   }
/*    */   
/*    */   public int getVersion() {
/* 53 */     return this.version;
/*    */   }
/*    */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/bean/ObuCheckBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */