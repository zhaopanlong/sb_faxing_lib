/*     */ package com.cgutech.bluetoothstatusapi.bean;
/*     */ 
/*     */ import com.cgutech.bluetoothstatusapi.callback.ConnectCallback;
/*     */ import com.cgutech.bluetoothstatusapi.callback.Receiver;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ public class SendDataBean
/*     */ {
/*     */   private int pos;
/*     */   private List<byte[]> sendDatas;
/*     */   private int timeout;
/*     */   private int reCount;
/*     */   private Receiver receiver;
/*     */   private ConnectCallback connectCallback;
/*     */   private byte[] data;
/*     */   private int sendCount;
/*     */   private int frameReCount;
/*     */   private int frameRecountTotal;
/*     */   private int frameTimeout;
/*     */   private int delay;
/*     */   
/*     */   public int getFrameReCount() {
/*  24 */     return this.frameReCount;
/*     */   }
/*     */   
/*     */   public void setFrameReCount(int frameReCount) {
/*  28 */     this.frameReCount = frameReCount;
/*     */   }
/*     */   
/*     */   public int getFrameRecountTotal() {
/*  32 */     return this.frameRecountTotal;
/*     */   }
/*     */   
/*     */   public void setFrameRecountTotal(int frameRecountTotal) {
/*  36 */     this.frameRecountTotal = frameRecountTotal;
/*     */   }
/*     */   
/*     */   public int getFrameTimeout() {
/*  40 */     return this.frameTimeout;
/*     */   }
/*     */   
/*     */   public void setFrameTimeout(int frameTimeout) {
/*  44 */     this.frameTimeout = frameTimeout;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getDelay() {
/*  50 */     return this.delay;
/*     */   }
/*     */   
/*     */   public void setDelay(int delay) {
/*  54 */     this.delay = delay;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getSendCount() {
/*  60 */     return this.sendCount;
/*     */   }
/*     */   
/*     */   public void setSendCount(int sendCount) {
/*  64 */     this.sendCount = sendCount;
/*     */   }
/*     */   
/*     */   public int getPos() {
/*  68 */     return this.pos;
/*     */   }
/*     */   
/*     */   public void setPos(int pos) {
/*  72 */     this.pos = pos;
/*     */   }
/*     */   
/*     */   public List<byte[]> getSendDatas() {
/*  76 */     return this.sendDatas;
/*     */   }
/*     */   
/*     */   public void setSendDatas(List<byte[]> sendDatas) {
/*  80 */     this.sendDatas = sendDatas;
/*     */   }
/*     */   
/*     */   public int getTimeout() {
/*  84 */     return this.timeout;
/*     */   }
/*     */   
/*     */   public void setTimeout(int timeout) {
/*  88 */     this.timeout = timeout;
/*     */   }
/*     */   
/*     */   public int getReCount() {
/*  92 */     return this.reCount;
/*     */   }
/*     */   
/*     */   public void setReCount(int reCount) {
/*  96 */     this.reCount = reCount;
/*     */   }
/*     */   
/*     */   public Receiver getReceiver() {
/* 100 */     return this.receiver;
/*     */   }
/*     */   
/*     */   public void setReceiver(Receiver receiver) {
/* 104 */     this.receiver = receiver;
/*     */   }
/*     */   
/*     */   public ConnectCallback getConnectCallback() {
/* 108 */     return this.connectCallback;
/*     */   }
/*     */   
/*     */   public void setConnectCallback(ConnectCallback connectCallback) {
/* 112 */     this.connectCallback = connectCallback;
/*     */   }
/*     */   
/*     */   public byte[] getData() {
/* 116 */     return this.data;
/*     */   }
/*     */   
/*     */   public void setData(byte[] data) {
/* 120 */     this.data = data;
/*     */   }
/*     */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/bean/SendDataBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */