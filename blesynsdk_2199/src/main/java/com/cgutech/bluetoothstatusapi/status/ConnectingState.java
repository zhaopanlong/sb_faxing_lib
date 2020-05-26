//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cgutech.bluetoothstatusapi.status;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.util.Log;
import com.cgutech.bluetoothstatusapi.callback.BluetoothMessageCallback;
import com.cgutech.bluetoothstatusapi.callback.ConnectCallback;
import com.cgutech.bluetoothstatusapi.callback.Receiver;
import com.cgutech.bluetoothstatusapi.callback.ScanCallback;
import com.cgutech.bluetoothstatusapi.controller.BluetoothStateManager;
import com.cgutech.bluetoothstatusapi.exception.ErrorStateException;
import com.cgutech.commonBt.log.LogHelperBt;

public class ConnectingState extends BluetoothMessageCallback implements BluetoothState {
    private static final int stateTag = 3;
    private ConnectCallback callback;
    private int timeout;
    private int reTestCount = 2;
    BluetoothGatt gatt;
    private Runnable runnable = new Runnable() {
        public void run() {
            if (ConnectingState.this.reTestCount-- > 0) {
                if (ConnectingState.this.gatt != null) {
                    Log.i(this.toString(), "重连gatt.disconnect()  gatt.close()");
                    ConnectingState.this.gatt.disconnect();
                    ConnectingState.this.gatt.close();
                    ConnectingState.this.gatt = null;
                    BluetoothStateManager.instance().setmControllerNull();
                    BluetoothStateManager.instance().getHandler().postDelayed(new Runnable() {
                        public void run() {
                            Log.i(this.toString(), "重连次数" + ConnectingState.this.reTestCount);
                            ConnectingState.this.gatt = BluetoothStateManager.instance().getmBluetoothDevice().connectGatt(BluetoothStateManager.instance().getContext(), false, BluetoothStateManager.instance().getmBluetoothGattCallback());
                            BluetoothStateManager.instance().setmBluetoothGatt(ConnectingState.this.gatt);
                            BluetoothStateManager.instance().getHandler().postDelayed(ConnectingState.this.runnable, 3000L);
                        }
                    }, 500L);
                } else {
                    Log.i(this.toString(), "重连次数" + ConnectingState.this.reTestCount);
                    ConnectingState.this.gatt = BluetoothStateManager.instance().getmBluetoothDevice().connectGatt(BluetoothStateManager.instance().getContext(), false, BluetoothStateManager.instance().getmBluetoothGattCallback());
                    BluetoothStateManager.instance().setmBluetoothGatt(ConnectingState.this.gatt);
                    BluetoothStateManager.instance().getHandler().postDelayed(ConnectingState.this.runnable, 3000L);
                }
            } else {
                LogHelperBt.LogE(ConnectingState.this.getStateName(), "连接蓝牙超时");
                BluetoothStateManager.instance().setmBluetoothState(new AvailableState());
                if (ConnectingState.this.callback != null) {
                    ConnectingState.this.callback.onTimeout();
                }
            }

        }
    };

    public ConnectingState(ConnectCallback callback, int timeout) {
        this.callback = callback;
        this.timeout = timeout;
        this.init();
    }

    public int getStateType() {
        return 3;
    }

    public String getStateName() {
        return "state_connecting";
    }

    public void init() {
        BluetoothStateManager.instance().getHandler().postDelayed(this.runnable, 3000L);
        if (BluetoothStateManager.instance().getmBluetoothGatt() != null) {
            BluetoothStateManager.instance().setmControllerNull();
        }

        this.gatt = BluetoothStateManager.instance().getmBluetoothDevice().connectGatt(BluetoothStateManager.instance().getContext(), false, BluetoothStateManager.instance().getmBluetoothGattCallback());
        BluetoothStateManager.instance().setmBluetoothGatt(this.gatt);
    }

    public void startScan(int timeout, ScanCallback callback) throws ErrorStateException {
        throw new ErrorStateException("正在连接不能扫描");
    }

    public void stopScan() {
    }

    public void connect(BluetoothDevice address, int timeout, ConnectCallback callback) throws ErrorStateException {
        throw new ErrorStateException("正在连接不能连接");
    }

    public void disconnect(ConnectCallback callback) throws ErrorStateException {
        BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
        BluetoothStateManager.instance().getmBluetoothGatt().disconnect();
        BluetoothStateManager.instance().getmBluetoothGatt().close();
        BluetoothStateManager.instance().setmBluetoothState(new AvailableState());
        callback.onDisconnect();
    }

    public void send(byte[] data, int delay, int reCount, int frameReCount, int timeoutTime, int frameTimeout, Receiver callback, ConnectCallback connectCallback) throws ErrorStateException {
    }

    public void addGlobleReceive(Receiver receive) throws ErrorStateException {
    }

    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (newState == 2) {
            BluetoothStateManager.instance().setmBluetoothGatt(gatt);
            gatt.discoverServices();
        } else if (newState == 0) {
            LogHelperBt.LogW("ConnectingState", "-->onConnectionStateChange  state: " + status + ", newState:" + newState);
        } else {
            LogHelperBt.LogI("onConnectionStateChange", "address:" + gatt.getDevice().getAddress() + ", status:" + status + ", newStatus:" + newState);
        }

    }

    public void onServicesDiscovered(BluetoothGatt gatt, int status) {
        if (BluetoothStateManager.instance().enableTXNotification()) {
            BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
            BluetoothStateManager.instance().setmBluetoothState(new CheckObuVersionState(this.callback));
        } else {
            BluetoothStateManager.instance().setmBluetoothState(new AvailableState());
            if (this.callback != null) {
                this.callback.onError("写入特征值失败");
            }
        }

    }
}
