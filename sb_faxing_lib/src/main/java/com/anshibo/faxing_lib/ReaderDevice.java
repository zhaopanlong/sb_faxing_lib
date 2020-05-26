package com.anshibo.faxing_lib;

import android.bluetooth.BluetoothDevice;

/**
 * @author zhaopanlong
 * @createtime：2020/5/26 下午4:50
 */
public class ReaderDevice {
    private BluetoothDevice bluetoothDevice;
    private NFCDevice nfcDevice;
    private String deviceName;

    public ReaderDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
        deviceName = bluetoothDevice.getName();
    }

    public ReaderDevice(NFCDevice nfcDevice) {
        this.nfcDevice = nfcDevice;
        deviceName = nfcDevice.getDeviceName();
    }

    public BluetoothDevice getBluetoothDevice() {
        return bluetoothDevice;
    }

    public void setBluetoothDevice(BluetoothDevice bluetoothDevice) {
        this.bluetoothDevice = bluetoothDevice;
    }

    public NFCDevice getNfcDevice() {
        return nfcDevice;
    }

    public void setNfcDevice(NFCDevice nfcDevice) {
        this.nfcDevice = nfcDevice;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
