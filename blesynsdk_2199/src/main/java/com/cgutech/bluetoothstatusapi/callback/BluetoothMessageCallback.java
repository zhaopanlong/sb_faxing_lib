/*    */ package com.cgutech.bluetoothstatusapi.callback;
/*    */ 
/*    */ import android.bluetooth.BluetoothGatt;
/*    */ import android.bluetooth.BluetoothGattCharacteristic;
/*    */ import android.bluetooth.BluetoothGattDescriptor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BluetoothMessageCallback
/*    */ {
/*    */   public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {}
/*    */   
/*    */   public void onServicesDiscovered(BluetoothGatt gatt, int status) {}
/*    */   
/*    */   public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {}
/*    */   
/*    */   public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {}
/*    */   
/*    */   public boolean onCharacteristicChanged(String channel, byte[] data) {
/* 37 */     return false;
/*    */   }
/*    */   
/*    */   public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {}
/*    */   
/*    */   public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {}
/*    */   
/*    */   public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {}
/*    */   
/*    */   public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {}
/*    */   
/*    */   public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {}
/*    */ }


/* Location:              /Users/zhaopanlong/Desktop/BleSynSdk_2199.jar!/com/cgutech/bluetoothstatusapi/callback/BluetoothMessageCallback.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */