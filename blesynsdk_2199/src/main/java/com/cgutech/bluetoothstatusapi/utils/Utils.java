/*    */ package com.cgutech.bluetoothstatusapi.utils;
/*    */ 
/*    */ import java.util.Timer;
/*    */ import java.util.TimerTask;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class Utils
/*    */ {
/*    */   private static final int MAXLENGTH = 15;
/*    */   
/*    */   public static String intToByteString(int integer) {
/* 14 */     int byteNum = (40 - Integer.numberOfLeadingZeros((integer < 0) ? (integer ^ 0xFFFFFFFF) : integer)) / 8;
/* 15 */     byte[] byteArray = new byte[4];
/*    */     
/* 17 */     for (int n = 0; n < byteNum; n++)
/* 18 */       byteArray[3 - n] = (byte)(integer >>> n * 8); 
/* 19 */     String data = bytesToHexString(byteArray);
/* 20 */     return data.substring(data.length() - 2, data.length());
/*    */   }
/*    */   
/*    */   public static String shortToHexString(int data) {
/* 24 */     byte[] buf = new byte[2];
/* 25 */     buf[0] = (byte)(data >> 8 & 0xFF);
/* 26 */     buf[1] = (byte)(data & 0xFF);
/* 27 */     return bytesToHexString(buf);
/*    */   }
/*    */   
/*    */   public static String intToByteStringTemp(int integer) {
/* 31 */     int byteNum = (40 - Integer.numberOfLeadingZeros((integer < 0) ? (integer ^ 0xFFFFFFFF) : integer)) / 8;
/* 32 */     byte[] byteArray = new byte[4];
/*    */     
/* 34 */     for (int n = 0; n < byteNum; n++)
/* 35 */       byteArray[3 - n] = (byte)(integer >>> n * 8); 
/* 36 */     String data = bytesToHexString(byteArray);
/* 37 */     return data.substring(data.length() - 4, data.length());
/*    */   }
/*    */   
/*    */   public static String getBCC(byte[] data) {
/* 41 */     String ret = "";
/* 42 */     byte[] BCC = new byte[1];
/* 43 */     for (int i = 0; i < data.length; i++)
/*    */     {
/* 45 */       BCC[0] = (byte)(BCC[0] ^ data[i]);
/*    */     }
/* 47 */     String hex = Integer.toHexString(BCC[0] & 0xFF);
/* 48 */     if (hex.length() == 1) {
/* 49 */       hex = '0' + hex;
/*    */     }
/* 51 */     ret = ret + hex.toUpperCase();
/* 52 */     return ret;
/*    */   }
/*    */   public static Timer createJobber(TimerTask o, int time) {
/* 55 */     Timer timer = new Timer();
/* 56 */     timer.schedule(o, 0L, time);
/* 57 */     return timer;
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte[] hexStringTobytes(String inputStr) {
/* 62 */     byte[] result = new byte[inputStr.length() / 2];
/* 63 */     for (int i = 0; i < inputStr.length() / 2; i++)
/* 64 */       result[i] = (byte)(Integer.parseInt(inputStr
/* 65 */           .substring(i * 2, i * 2 + 2), 16) & 0xFF); 
/* 66 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public static String bytesToHexString(byte[] src) {
/* 71 */     StringBuilder stringBuilder = new StringBuilder();
/* 72 */     if (src == null || src.length <= 0) {
/* 73 */       return null;
/*    */     }
/* 75 */     for (int i = 0; i < src.length; i++) {
/* 76 */       int v = src[i] & 0xFF;
/* 77 */       String hv = Integer.toHexString(v);
/* 78 */       if (hv.length() < 2) {
/* 79 */         stringBuilder.append(0);
/*    */       }
/* 81 */       stringBuilder.append(hv);
/*    */     } 
/* 83 */     return stringBuilder.toString();
/*    */   }
/*    */ 
/*    */   
/*    */   public static byte getBcc(byte[] result) {
/* 88 */     byte calculate_bccActivity = 0;
/* 89 */     for (int i = 0; i < result.length - 1; i++) {
/* 90 */       calculate_bccActivity = (byte)(calculate_bccActivity ^ result[i]);
/*    */     }
/* 92 */     return calculate_bccActivity;
/*    */   }
/*    */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/utils/Utils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */