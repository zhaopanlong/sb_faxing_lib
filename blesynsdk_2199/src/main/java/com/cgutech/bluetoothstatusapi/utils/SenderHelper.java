/*    */ package com.cgutech.bluetoothstatusapi.utils;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class SenderHelper
/*    */ {
/*    */   private static final int MAXLENGTH = 15;
/*    */   
/*    */   public static byte getBcc(byte[] result) {
/* 13 */     byte calculate_bccActivity = 0;
/* 14 */     for (int i = 0; i < result.length - 1; i++) {
/* 15 */       calculate_bccActivity = (byte)(calculate_bccActivity ^ result[i]);
/*    */     }
/* 17 */     return calculate_bccActivity;
/*    */   }
/*    */   public static List<byte[]> getPackageList(byte[] data) {
/*    */     int len;
/* 21 */     List<byte[]> datas = (List)new ArrayList<>();
/*    */ 
/*    */     
/* 24 */     if (data.length % 15 == 0) {
/* 25 */       len = data.length / 15;
/*    */     } else {
/* 27 */       len = data.length / 15 + 1;
/*    */     } 
/* 29 */     if (len == 1) {
/* 30 */       datas.add(createPackage(0, data, 1));
/*    */     } else {
/* 32 */       for (int i = 0; i < len; i++) {
/* 33 */         datas.add(createPackage(i, data, len));
/*    */       }
/*    */     } 
/* 36 */     return datas;
/*    */   }
/*    */   private static byte[] createPackage(int num, byte[] data, int len) {
/*    */     byte[] ctl;
/*    */     int currentLength;
/* 41 */     if (num == 0) {
/* 42 */       ctl = getCtlFirst(len);
/*    */     } else {
/* 44 */       ctl = getCtlNotFirst(num + 1);
/*    */     } 
/*    */     
/* 47 */     if (data.length - num * 15 > 15) {
/* 48 */       currentLength = 15;
/*    */     } else {
/* 50 */       currentLength = data.length - num * 15;
/*    */     } 
/* 52 */     byte st = (byte)(0x50 | currentLength);
/* 53 */     byte[] packageData = new byte[4 + currentLength];
/* 54 */     packageData[0] = st;
/* 55 */     System.arraycopy(ctl, 0, packageData, 1, 2);
/* 56 */     System.arraycopy(data, num * 15, packageData, 3, currentLength);
/* 57 */     packageData[packageData.length - 1] = getBcc(packageData);
/*    */     
/* 59 */     return packageData;
/*    */   }
/*    */   
/*    */   private static byte[] getCtlNotFirst(int num) {
/* 63 */     return new byte[] { (byte)(0x7F & num >> 8), (byte)num };
/*    */   }
/*    */   
/*    */   private static byte[] getCtlFirst(int len) {
/* 67 */     return new byte[] { (byte)(len >> 8 | 0x80), (byte)len };
/*    */   }
/*    */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/utils/SenderHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */