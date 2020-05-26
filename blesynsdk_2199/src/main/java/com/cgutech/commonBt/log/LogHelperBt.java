/*    */ package com.cgutech.commonBt.log;
/*    */ 
/*    */ import android.content.Context;
/*    */ import android.util.Log;
/*    */ 
/*    */ 
/*    */ public class LogHelperBt
/*    */ {
/*    */   public static void LogI(Context context, String str) {
/* 10 */     Log.i(context.getClass().getName(), str);
/* 11 */     String data = String.format("[%s] [%s] %s", new Object[] { context.getClass().getName(), "INFO", str });
/*    */   }
/*    */   
/*    */   public static void LogI(String TAG, String str) {
/* 15 */     Log.i(TAG, str);
/* 16 */     String data = String.format("[%s] [%s] %s", new Object[] { TAG, "INFO", str });
/*    */   }
/*    */   
/*    */   public static void LogE(String TAG, String str, Exception e) {
/* 20 */     Log.e(TAG, str, e);
/* 21 */     String data = String.format("[%s] [%s] %s", new Object[] { TAG, "ERROR", str });
/*    */   }
/*    */   
/*    */   public static void LogE(String TAG, String str) {
/* 25 */     Log.e(TAG, str);
/* 26 */     String data = String.format("[%s] [%s] %s", new Object[] { TAG, "ERROR", str });
/*    */   }
/*    */   
/*    */   public static void LogW(String TAG, String str) {
/* 30 */     Log.w(TAG, str);
/* 31 */     String data = String.format("[%s] [%s] %s", new Object[] { TAG, "WARN", str });
/*    */   }
/*    */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/commonBt/log/LogHelperBt.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */