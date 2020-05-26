/*    */ package com.cgutech.common_.log;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.util.Log;
/*    */ 
/*    */ public class LogHelper
/*    */ {
/*    */   private static final boolean debug = true;
/*    */   
/*    */   public static void LogI(String TAG, String str) {
/* 11 */     Log.i(TAG, str);
/* 12 */     String str1 = String.format("[%s] [%s] %s", new Object[] { TAG, "INFO", str });
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void LogI(Context context, String str) {
/* 19 */     Log.i(context.getClass().getName(), str);
/* 20 */     String str1 = String.format("[%s] [%s] %s", new Object[] { context.getClass().getName(), "INFO", str });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void LogD(Context context, String str) {
/* 26 */     Log.d(context.getClass().getName(), str);
/* 27 */     String str1 = String.format("[%s] [%s] %s", new Object[] { context.getClass().getName(), "DEBUG", str });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void LogD(String TAG, String str) {
/* 33 */     Log.d(TAG, str);
/* 34 */     String str1 = String.format("[%s] [%s] %s", new Object[] { TAG, "DEBUG", str });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void LogE(String TAG, String str, Exception e) {
/* 40 */     Log.e(TAG, str, e);
/* 41 */     String str1 = String.format("[%s] [%s] %s", new Object[] { TAG, "ERROR", str });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void LogE(String TAG, String str) {
/* 47 */     Log.e(TAG, str);
/* 48 */     String str1 = String.format("[%s] [%s] %s", new Object[] { TAG, "ERROR", str });
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static void LogW(String TAG, String str) {
/* 54 */     Log.w(TAG, str);
/* 55 */     String str1 = String.format("[%s] [%s] %s", new Object[] { TAG, "WARN", str });
/*    */   }
/*    */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/common_/log/LogHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */