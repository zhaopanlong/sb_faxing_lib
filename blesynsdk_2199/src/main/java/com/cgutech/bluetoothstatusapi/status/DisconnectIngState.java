//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cgutech.bluetoothstatusapi.status;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.util.Log;
import com.cgutech.bluetoothstatusapi.callback.BluetoothMessageCallback;
import com.cgutech.bluetoothstatusapi.callback.ConnectCallback;
import com.cgutech.bluetoothstatusapi.callback.Receiver;
import com.cgutech.bluetoothstatusapi.callback.ScanCallback;
import com.cgutech.bluetoothstatusapi.controller.BluetoothStateManager;
import com.cgutech.bluetoothstatusapi.exception.ErrorStateException;
import com.cgutech.commonBt.log.LogHelperBt;
import com.cgutech.commonBt.util.UtilsBt;

public class DisconnectIngState extends BluetoothMessageCallback implements BluetoothState {
    private static final int stateTag = 4;
    private ConnectCallback callback;
    Runnable runnable;

    public DisconnectIngState(ConnectCallback callback) {
        this.callback = callback;
        BluetoothStateManager.instance().getHandler().postDelayed(new Runnable() {
            public void run() {
                DisconnectIngState.this.init();
            }
        }, 100L);
    }

    public int getStateType() {
        return 4;
    }

    public String getStateName() {
        return "state_disconnecting";
    }

    public void init() {
        this.sleep();
        BluetoothStateManager.instance().getHandler().postDelayed(new Runnable() {
            public void run() {
                BluetoothStateManager.instance().getmBluetoothGatt().disconnect();
            }
        }, 50L);
        BluetoothStateManager.instance().getHandler().postDelayed(this.runnable = new Runnable() {
            public void run() {
                if (BluetoothStateManager.instance().getmBluetoothState().getStateType() == 4) {
                    BluetoothStateManager.instance().getmBluetoothGatt().close();
                    BluetoothStateManager.instance().setmBluetoothState(new AvailableState());
                    LogHelperBt.LogE("未正常断开", "超时状态转换启");
                }

            }
        }, 800L);
    }

    public void startScan(int timeout, ScanCallback callback) throws ErrorStateException {
        throw new ErrorStateException("正在断开连接不能搜索");
    }

    public void stopScan() throws ErrorStateException {
        throw new ErrorStateException("正在断开连接,不能停止搜索");
    }

    public void connect(BluetoothDevice address, int timeout, ConnectCallback callback) throws ErrorStateException {
        throw new ErrorStateException("正在断开连接不能连接");
    }

    public void disconnect(ConnectCallback callback) throws ErrorStateException {
        throw new ErrorStateException("正在断开连接不能断开连接");
    }

    public void send(byte[] data, int delay, int reCount, int frameReCount, int timeoutTime, int frameTimeout, Receiver callback, ConnectCallback connectCallback) throws ErrorStateException {
        throw new ErrorStateException("正在断开连接不能发送数据");
    }

    public void addGlobleReceive(Receiver receive) {
    }

    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        if (newState == 0) {
            LogHelperBt.LogW("DisconnectIngState", "-->onConnectionStateChange  state: " + status + ", newState:" + newState);
            BluetoothStateManager.instance().getHandler().removeCallbacks(this.runnable);
            if (this.callback != null) {
                this.callback.onDisconnect();
            }

            BluetoothStateManager.instance().setmBluetoothState(new AvailableState());
        }

    }

    public boolean onCharacteristicChanged(String channel, byte[] data) {
        String sData = UtilsBt.bytesToHexString(data);
        if (data != null && "00".equalsIgnoreCase(sData) && "C1".equalsIgnoreCase(channel)) {
            LogHelperBt.LogI(this.getStateName(), "休眠指令，收到数据：" + sData);
        }

        return true;
    }

    private void sleep() {
        byte[] data = UtilsBt.hexStringTobytes("528001D10002");
        BluetoothGatt gatt = BluetoothStateManager.instance().getmBluetoothGatt();
        if (gatt == null) {
            Log.e("connectedState&send", "gatt对象为空");
        } else {
            BluetoothGattService service = gatt.getService(BluetoothStateManager.UUID_RX_SERVICE) == null ? gatt.getService(BluetoothStateManager.UUID_RX_SERVICE_WX) : gatt.getService(BluetoothStateManager.UUID_RX_SERVICE);
            if (service == null) {
                Log.e("connectState&send", "gatt service对象为空");
            } else {
                BluetoothGattCharacteristic characteristic = service.getCharacteristic(BluetoothStateManager.UUID_RX_CHAR);
                characteristic.setValue(data);
                if (!gatt.writeCharacteristic(characteristic)) {
                    LogHelperBt.LogI("sending", "发送失败:" + UtilsBt.bytesToHexString(data));
                } else {
                    LogHelperBt.LogI("sending", "发送成功");
                }

            }
        }
    }
}
