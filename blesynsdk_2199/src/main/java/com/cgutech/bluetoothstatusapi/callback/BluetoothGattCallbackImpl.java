//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cgutech.bluetoothstatusapi.callback;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.util.Log;
import com.cgutech.bluetoothstatusapi.controller.BluetoothStateManager;
import com.cgutech.bluetoothstatusapi.utils.ReceiverHelper;
import com.cgutech.commonBt.log.LogHelperBt;
import com.cgutech.commonBt.util.UtilsBt;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BluetoothGattCallbackImpl extends BluetoothGattCallback {
    private static final ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    public static ReceiverHelper receiveHelper = new ReceiverHelper(new ReceiverFinish() {
        public void onFinish(final String channel, final byte[] data) {
            BluetoothStateManager.instance().getHandler().post(new Runnable() {
                public void run() {
                    BluetoothMessageCallback bluetoothMessageCallback = (BluetoothMessageCallback)BluetoothStateManager.instance().getmBluetoothState();
                    Receiver receiver = BluetoothStateManager.instance().getGloableReceiver();
                    boolean result = false;
                    if (receiver != null) {
                        result = receiver.onRecv(channel, data);
                    }

                    if (!result) {
                        LogHelperBt.LogI("BluetoothGatt", "全局接收器未处理  channel:" + channel + ", data:" + UtilsBt.bytesToHexString(data));
                        bluetoothMessageCallback.onCharacteristicChanged(channel, data);
                    } else {
                        LogHelperBt.LogI("BluetoothGatt", "全局接收器已处理  channel:" + channel + ", data:" + UtilsBt.bytesToHexString(data));
                    }

                }
            });
        }
    });

    public BluetoothGattCallbackImpl() {
    }

    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        LogHelperBt.LogI("bluetoothcallback", "onConnectionStateChange state: " + BluetoothStateManager.instance().getmBluetoothState().getStateName());
        if (newState == 0) {
            LogHelperBt.LogI("bluetoothcallback", "蓝牙断开");
            if (BluetoothStateManager.instance().getmBluetoothGatt() != null) {
                BluetoothStateManager.instance().getmBluetoothGatt().disconnect();
                BluetoothStateManager.instance().getmBluetoothGatt().close();
                Log.i(this.toString(), "callbackimplgatt.disconnect()  gatt.close");
            }
        }

        BluetoothMessageCallback bluetoothMessageCallback = (BluetoothMessageCallback)BluetoothStateManager.instance().getmBluetoothState();
        bluetoothMessageCallback.onConnectionStateChange(gatt, status, newState);
    }

    public void onServicesDiscovered(final BluetoothGatt gatt, final int status) {
        LogHelperBt.LogI("bluetoothcallback", "onServicesDiscovered state: " + BluetoothStateManager.instance().getmBluetoothState().getStateName());
        BluetoothStateManager.instance().getHandler().postDelayed(new Runnable() {
            public void run() {
                BluetoothMessageCallback bluetoothMessageCallback = (BluetoothMessageCallback)BluetoothStateManager.instance().getmBluetoothState();
                bluetoothMessageCallback.onServicesDiscovered(gatt, status);
            }
        }, 0L);
    }

    public void onCharacteristicRead(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
        LogHelperBt.LogI("bluetoothcallback", "onCharacteristicRead state: " + BluetoothStateManager.instance().getmBluetoothState().getStateName());
        BluetoothStateManager.instance().getHandler().postDelayed(new Runnable() {
            public void run() {
                BluetoothMessageCallback bluetoothMessageCallback = (BluetoothMessageCallback)BluetoothStateManager.instance().getmBluetoothState();
                bluetoothMessageCallback.onCharacteristicRead(gatt, characteristic, status);
            }
        }, 0L);
    }

    public void onCharacteristicWrite(final BluetoothGatt gatt, final BluetoothGattCharacteristic characteristic, final int status) {
        byte[] data = characteristic.getValue();
        if (status == 0) {
            LogHelperBt.LogI("onCharacteristicWrite", "写入数据成功     state:" + BluetoothStateManager.instance().getmBluetoothState().getStateName() + ", " + UtilsBt.bytesToHexString(data));
        }

        BluetoothStateManager.instance().getHandler().postDelayed(new Runnable() {
            public void run() {
                BluetoothMessageCallback bluetoothMessageCallback = (BluetoothMessageCallback)BluetoothStateManager.instance().getmBluetoothState();
                bluetoothMessageCallback.onCharacteristicWrite(gatt, characteristic, status);
            }
        }, 0L);
    }

    public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
        final byte[] data = characteristic.getValue();
        LogHelperBt.LogI("bluetoothcallback", "蓝牙系统数据: " + BluetoothStateManager.instance().getmBluetoothState().getStateName() + ", data:" + UtilsBt.bytesToHexString(data));
        singleThreadExecutor.execute(new Runnable() {
            public void run() {
                BluetoothGattCallbackImpl.this.onRecv(data);
            }
        });

        try {
            Thread.sleep(10L);
        } catch (InterruptedException var5) {
            var5.printStackTrace();
        }

    }

    private void onRecv(final byte[] data) {
        String sData = UtilsBt.bytesToHexString(data);
        if (sData != null && sData.length() >= 8) {
            final String channel = sData.substring(0, 8);
            if (BluetoothStateManager.instance().getmBluetoothState().getStateType() != 10 && !channel.equals("538001c7") && !channel.equals("548001c2")) {
                receiveHelper.receive(data);
            } else {
                BluetoothStateManager.instance().getHandler().post(new Runnable() {
                    public void run() {
                        BluetoothMessageCallback bluetoothMessageCallback = (BluetoothMessageCallback)BluetoothStateManager.instance().getmBluetoothState();
                        bluetoothMessageCallback.onCharacteristicChanged(channel, data);
                    }
                });
            }
        } else {
            LogHelperBt.LogW("BluetoothCallback", "未处理的蓝牙消息：长度过智短");
        }

    }

    public void onDescriptorRead(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
        LogHelperBt.LogI("bluetoothcallback", "onDescriptorRead state: " + BluetoothStateManager.instance().getmBluetoothState().getStateName());
        BluetoothStateManager.instance().getHandler().postDelayed(new Runnable() {
            public void run() {
                BluetoothMessageCallback bluetoothMessageCallback = (BluetoothMessageCallback)BluetoothStateManager.instance().getmBluetoothState();
                bluetoothMessageCallback.onDescriptorRead(gatt, descriptor, status);
            }
        }, 0L);
    }

    public void onDescriptorWrite(final BluetoothGatt gatt, final BluetoothGattDescriptor descriptor, final int status) {
        LogHelperBt.LogI("bluetoothcallback", "onDescriptorWrite state: " + BluetoothStateManager.instance().getmBluetoothState().getStateName());
        BluetoothStateManager.instance().getHandler().postDelayed(new Runnable() {
            public void run() {
                BluetoothMessageCallback bluetoothMessageCallback = (BluetoothMessageCallback)BluetoothStateManager.instance().getmBluetoothState();
                bluetoothMessageCallback.onDescriptorWrite(gatt, descriptor, status);
            }
        }, 0L);
    }

    public void onReliableWriteCompleted(final BluetoothGatt gatt, final int status) {
        LogHelperBt.LogI("bluetoothcallback", "onReliableWriteCompleted state: " + BluetoothStateManager.instance().getmBluetoothState().getStateName());
        BluetoothStateManager.instance().getHandler().postDelayed(new Runnable() {
            public void run() {
                BluetoothMessageCallback bluetoothMessageCallback = (BluetoothMessageCallback)BluetoothStateManager.instance().getmBluetoothState();
                bluetoothMessageCallback.onReliableWriteCompleted(gatt, status);
            }
        }, 0L);
    }

    public void onReadRemoteRssi(final BluetoothGatt gatt, final int rssi, final int status) {
        LogHelperBt.LogI("bluetoothcallback", "onReadRemoteRssi state: " + BluetoothStateManager.instance().getmBluetoothState().getStateName());
        BluetoothStateManager.instance().getHandler().postDelayed(new Runnable() {
            public void run() {
                BluetoothMessageCallback bluetoothMessageCallback = (BluetoothMessageCallback)BluetoothStateManager.instance().getmBluetoothState();
                bluetoothMessageCallback.onReadRemoteRssi(gatt, rssi, status);
            }
        }, 0L);
    }

    public void onMtuChanged(final BluetoothGatt gatt, final int mtu, final int status) {
        LogHelperBt.LogI("bluetoothcallback", "onMtuChanged state: " + BluetoothStateManager.instance().getmBluetoothState().getStateName());
        BluetoothStateManager.instance().getHandler().postDelayed(new Runnable() {
            public void run() {
                BluetoothMessageCallback bluetoothMessageCallback = (BluetoothMessageCallback)BluetoothStateManager.instance().getmBluetoothState();
                bluetoothMessageCallback.onMtuChanged(gatt, mtu, status);
            }
        }, 0L);
    }
}
