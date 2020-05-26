/*     */ package com.cgutech.common_.util;
/*     */ 
/*     */ import android.content.Context;
/*     */ import android.net.ConnectivityManager;
/*     */ import android.net.NetworkInfo;
/*     */ import com.cgutech.common_.log.LogHelper;
/*     */ import java.security.MessageDigest;
/*     */ import java.security.NoSuchAlgorithmException;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class Utils
/*     */ {
/*     */   private static final String TAG = "Utils";
/*     */   
/*     */   public static final boolean isNetworkAvailable(Context context) {
/*  22 */     ConnectivityManager _ConnectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
/*  23 */     if (_ConnectivityManager != null) {
/*  24 */       NetworkInfo[] _NetworkInfos = _ConnectivityManager.getAllNetworkInfo();
/*  25 */       if (_NetworkInfos != null) {
/*  26 */         for (int i = 0; i < _NetworkInfos.length; i++) {
/*  27 */           if (_NetworkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
/*  28 */             return true;
/*     */           }
/*     */         } 
/*     */       }
/*     */     } 
/*  33 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte[] hexStringTobytes(String inputStr) {
/*  38 */     byte[] result = new byte[inputStr.length() / 2];
/*  39 */     for (int i = 0; i < inputStr.length() / 2; i++)
/*  40 */       result[i] = (byte)(Integer.parseInt(inputStr
/*  41 */           .substring(i * 2, i * 2 + 2), 16) & 0xFF); 
/*  42 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String bytesToHexString(byte[] src) {
/*  47 */     StringBuilder stringBuilder = new StringBuilder();
/*  48 */     if (src == null || src.length <= 0) {
/*  49 */       return null;
/*     */     }
/*  51 */     for (int i = 0; i < src.length; i++) {
/*  52 */       int v = src[i] & 0xFF;
/*  53 */       String hv = Integer.toHexString(v);
/*  54 */       if (hv.length() < 2) {
/*  55 */         stringBuilder.append(0);
/*     */       }
/*  57 */       stringBuilder.append(hv);
/*     */     } 
/*  59 */     return stringBuilder.toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static byte getBcc(byte[] result) {
/*  64 */     byte calculate_bccActivity = 0;
/*  65 */     for (int i = 0; i < result.length - 1; i++) {
/*  66 */       calculate_bccActivity = (byte)(calculate_bccActivity ^ result[i]);
/*     */     }
/*  68 */     return calculate_bccActivity;
/*     */   }
/*     */ 
/*     */   
/*     */   public static double getCash(String str) {
/*  73 */     int n = 1;
/*  74 */     long result = 0L;
/*  75 */     int tmp = 0;
/*  76 */     char[] charArray = str.toCharArray();
/*  77 */     LogHelper.LogD("Utils", str);
/*  78 */     LogHelper.LogD("Utils", "char length = " + charArray.length);
/*  79 */     for (int i = 0; i < charArray.length; i++) {
/*  80 */       if (charArray[i] >= 'a') {
/*  81 */         tmp = charArray[i] - 97 + 10;
/*     */       } else {
/*  83 */         tmp = charArray[i] - 48;
/*     */       } 
/*  85 */       result = 16L * result + tmp;
/*     */     } 
/*  87 */     LogHelper.LogD("Utils", "result = " + result);
/*  88 */     return result / 100.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public static float getPower(String str) {
/*  93 */     int n = 1;
/*  94 */     long result = 0L;
/*  95 */     int tmp = 0;
/*  96 */     char[] charArray = str.toCharArray();
/*  97 */     LogHelper.LogD("Utils", str);
/*  98 */     LogHelper.LogD("Utils", "char length = " + charArray.length);
/*  99 */     for (int i = 0; i < charArray.length; i++) {
/* 100 */       if (charArray[i] >= 'a') {
/* 101 */         tmp = charArray[i] - 97 + 10;
/*     */       } else {
/* 103 */         tmp = charArray[i] - 48;
/*     */       } 
/* 105 */       result = 16L * result + tmp;
/*     */     } 
/* 107 */     LogHelper.LogD("Utils", "result = " + result);
/* 108 */     return (float)result;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static String MD532(String strSource) {
/* 114 */     String result = "";
/*     */     try {
/* 116 */       MessageDigest messageDigest = MessageDigest.getInstance("MD5");
/* 117 */       messageDigest.update(strSource.getBytes());
/* 118 */       byte[] b = messageDigest.digest();
/*     */       
/* 120 */       StringBuffer stringBuffer = new StringBuffer("");
/* 121 */       for (int offest = 0; offest < b.length; offest++) {
/* 122 */         int i = b[offest];
/* 123 */         if (i < 0) {
/* 124 */           i += 256;
/*     */         }
/* 126 */         if (i < 16) {
/* 127 */           stringBuffer.append("0");
/*     */         }
/* 129 */         stringBuffer.append(Integer.toHexString(i));
/*     */       } 
/* 131 */       result = stringBuffer.toString();
/* 132 */     } catch (NoSuchAlgorithmException e) {
/* 133 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 136 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String MD516(String strSource) {
/* 141 */     String result = "";
/*     */     try {
/* 143 */       MessageDigest messageDigest = MessageDigest.getInstance("MD5");
/* 144 */       messageDigest.update(strSource.getBytes());
/* 145 */       byte[] b = messageDigest.digest();
/*     */       
/* 147 */       StringBuffer stringBuffer = new StringBuffer("");
/* 148 */       for (int offest = 0; offest < b.length; offest++) {
/* 149 */         int i = b[offest];
/* 150 */         if (i < 0) {
/* 151 */           i += 256;
/*     */         }
/* 153 */         if (i < 16) {
/* 154 */           stringBuffer.append("0");
/*     */         }
/* 156 */         stringBuffer.append(Integer.toHexString(i));
/*     */       } 
/* 158 */       result = stringBuffer.toString().substring(8, 24);
/* 159 */     } catch (NoSuchAlgorithmException e) {
/* 160 */       e.printStackTrace();
/*     */     } 
/*     */     
/* 163 */     return result;
/*     */   }
/*     */   
/*     */   @Deprecated
/*     */   public static byte[] change(String inputStr) {
/* 168 */     byte[] result = new byte[inputStr.length() / 2];
/* 169 */     for (int i = 0; i < inputStr.length() / 2; i++)
/* 170 */       result[i] = (byte)(Integer.parseInt(inputStr
/* 171 */           .substring(i * 2, i * 2 + 2), 16) & 0xFF); 
/* 172 */     return result;
/*     */   }
/*     */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/common_/util/Utils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */