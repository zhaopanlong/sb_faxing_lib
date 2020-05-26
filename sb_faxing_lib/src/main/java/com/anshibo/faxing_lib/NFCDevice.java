package com.anshibo.faxing_lib;

import android.os.Parcelable;

/**
 * @author zhaopanlong
 * @createtime：2020/5/26 下午4:51
 */
public class NFCDevice {
    Parcelable p;
    private String deviceName = "nfc";

    public NFCDevice(Parcelable p) {
        this.p = p;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
